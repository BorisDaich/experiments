package biz.daich.experiments.boot.metrics.code;

import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.apache.commons.collections4.map.LRUMap;
import org.apache.commons.collections4.map.ListenableExpiringMap;
import org.apache.commons.collections4.map.PassiveExpiringMap.ConstantTimeToLiveExpirationPolicy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.google.common.eventbus.EventBus;

import lombok.Data;
import lombok.Getter;

@Component
public class DistinctInTimeWindowCounter<IDTYPE, DATATYPE>
{
	private static final Logger								l											= LogManager.getLogger(DistinctInTimeWindowCounter.class.getName());

	private static final int								DEFAULT_MIN_METRICS_UPDATE_INTERVAL_MILI	= 1000;
	private static final int								MAX_SEEN_SIZE								= 100;
	private static final int								MAX_TTL_MILI								= 10000;
	private static final String								METRIC_TYPE_COUNTER_SEEN_NAME				= "distinct.items.in.time.window";
	private static final String								METRIC_TYPE_COUNTER_SEEN_PERIOD_NAME		= "distinct.items.in.time.window.size.mili";

	@Autowired
	protected GaugeService									gaugeService;

	@Autowired
	protected EventBus										eventBus;

	protected final ListenableExpiringMap<IDTYPE, DATATYPE>	distinctItems;

	/**
	 * Instant when the resetMetrics() was called last time. If never reseted - will be Instant of class instance creation
	 */
	@Getter
	protected Instant										lastReset									= Instant.now();

	/**
	 * when was the last time the metrics where updated to GaugeService service. If where not yet - will be Instant of class instance creation
	 */
	@Getter
	protected Instant										lastMetricsUpdate							= Instant.now();
	/**
	 * How often the GaugeService updated with values.
	 */
	@Getter
	protected final int										metricUpdateIntervalMili;
	@Getter
	protected final int										maxDistictItems;
	@Getter
	protected final int										timeWindowSizeMili;
	@Getter
	protected final String									distinctItemsDuringPeriodCounterMetricName;
	@Getter
	protected final String									timeWindowSizeGaugeMetricName;

	@Data
	public static class EventLongTimeNotSeen<IDTYPE, DATATYPE>
	{
		public final IDTYPE		id;
		public final DATATYPE	data;
	}

	@Data
	public static class EventDistinctInTimeWindowCounterSaturated<IDTYPE, DATATYPE>
	{
		public final String	metricName;
		public final int	itemCount;
	}

	public DistinctInTimeWindowCounter()
	{
		this(DEFAULT_MIN_METRICS_UPDATE_INTERVAL_MILI, MAX_SEEN_SIZE, MAX_TTL_MILI, METRIC_TYPE_COUNTER_SEEN_NAME, METRIC_TYPE_COUNTER_SEEN_PERIOD_NAME);
	}

	/**
	 * @param metricUpdateIntervalMili
	 * @param maxDistictItems
	 * @param timeWindowSizeMili
	 * @param distinctItemsDuringPeriodCounterMetricName
	 * @param timeWindowSizeGaugeMetricName
	 */
	public DistinctInTimeWindowCounter(int metricUpdateIntervalMili, //
			int maxDistictItems, //
			int timeWindowSizeMili, //
			String distinctItemsDuringPeriodCounterMetricName,//
			String timeWindowSizeGaugeMetricName//
	)
	{
		super();
		this.metricUpdateIntervalMili = metricUpdateIntervalMili;
		this.maxDistictItems = maxDistictItems;
		this.timeWindowSizeMili = timeWindowSizeMili;
		this.distinctItemsDuringPeriodCounterMetricName = distinctItemsDuringPeriodCounterMetricName;
		this.timeWindowSizeGaugeMetricName = timeWindowSizeGaugeMetricName;

		final LRUMap<IDTYPE, DATATYPE> lruMap = new LRUMap<>(maxDistictItems);
		distinctItems = new ListenableExpiringMap<>(new ConstantTimeToLiveExpirationPolicy<IDTYPE, DATATYPE>(timeWindowSizeMili), lruMap);
		distinctItems.addExpirationListener((string, data) -> eventBus.post(new EventLongTimeNotSeen<IDTYPE, DATATYPE>(string, data)));

		l.debug(DistinctInTimeWindowCounter.class.getSimpleName() + " created");

	}

	public synchronized void count(final IDTYPE id, final DATATYPE x)
	{
		distinctItems.put(id, x);
		updateMetrics();
	}

	public void updateMetrics()
	{
		Instant now = Instant.now();
		Duration between = Duration.between(lastMetricsUpdate, now);

		if (between.toMillis() > metricUpdateIntervalMili)
		{
			updateMetricsNow();
		}
		else
		{
			if (l.isTraceEnabled()) //
				l.trace("only " + between + " passed since the last metrics update - doing nothing ");
		}
	}

	private void updateMetricsNow()
	{
		int sizeVal = distinctItems.size();
		gaugeService.submit(distinctItemsDuringPeriodCounterMetricName, sizeVal);
		gaugeService.submit(timeWindowSizeGaugeMetricName, timeWindowSizeMili);
		lastMetricsUpdate = Instant.now();
		if (sizeVal >= maxDistictItems - 1)
		{
			if (eventBus != null)
			{
				eventBus.post(new EventDistinctInTimeWindowCounterSaturated<>(distinctItemsDuringPeriodCounterMetricName, sizeVal));
			}
			l.warn("Saturation of DistinctInTimeWindowCounter " + distinctItemsDuringPeriodCounterMetricName);
		}
		if (l.isInfoEnabled())
		{
			l.info("metrics updated at " + DateTimeFormatter.ISO_INSTANT.format(lastMetricsUpdate));
		}
	}

	public synchronized void resetMetrics()
	{
		lastReset = Instant.now();
		distinctItems.clear();
		updateMetrics();
		l.debug("Metrics reseted at " + DateTimeFormatter.ISO_INSTANT.format(lastReset));
	}

	/**
	 * @return a shallow copy (the Map is different but the values are same) of the Map with all the items currently stored there
	 */
	public synchronized Map<IDTYPE, DATATYPE> getDistinctItems()
	{
		return Maps.newHashMap(distinctItems);
	}
}
