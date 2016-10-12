package biz.daich.experiments.boot.metrics.code;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.actuate.autoconfigure.ExportMetricWriter;
import org.springframework.boot.actuate.metrics.jmx.JmxMetricWriter;
import org.springframework.boot.actuate.metrics.writer.MetricWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jmx.export.MBeanExporter;

import com.google.common.eventbus.EventBus;

import biz.daich.experiments.boot.metrics.test.DataObj;
import biz.daich.experiments.boot.metrics.test.GenDataObj;
import biz.daich.experiments.boot.metrics.test.IObjGen;
import de.codecentric.boot.admin.config.EnableAdminServer;

@EnableAdminServer
@Configuration
public class AppMetricsExperimentConfiguration
{
	private static final Logger l = LogManager.getLogger(AppMetricsExperimentConfiguration.class.getName());

	@Bean
	public IObjGen<DataObj> getGenerator()
	{
		l.debug("DataObj generator created");
		return new GenDataObj();
	}

	@Bean
	public EventBus getEventBus()
	{
		l.debug("EventBus created");
		return new EventBus();
	}

	@Bean
	public DistinctInTimeWindowCounter<String, DataObj> getTypeCounter()
	{
		return new DistinctInTimeWindowCounter<String, DataObj>();
	}

	@Bean
	@ExportMetricWriter
	public MetricWriter metricWriter(MBeanExporter exporter)
	{
		l.debug("JmxMetricWriter created");
		return new JmxMetricWriter(exporter);
	}

}
