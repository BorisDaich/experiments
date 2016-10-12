package biz.daich.experiments.boot.metrics.test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableScheduling
@Component
public class SampleRestCaller
{

	private static final Logger	l					= LogManager.getLogger(SampleRestCaller.class.getName());
	public static final long		DELAY_TIME_MILLI	= 100L;

	public SampleRestCaller()
	{
		super();
		l.debug(SampleRestCaller.class.getSimpleName() + " created");
	}

	@Scheduled(fixedDelay = DELAY_TIME_MILLI)
	public void callRest()
	{
		try
		{
			final URI uri = new URIBuilder().setHost("localhost").setPort(8080).setPath("/get").setScheme("http").build();

			String response = Request.Get(uri).connectTimeout(3000).socketTimeout(3000).execute().returnContent().asString();
			if (l.isTraceEnabled())//
				l.trace("RESPONSE: " + response);
		}
		catch (URISyntaxException | IOException e)
		{
			l.warn(e.getMessage()); //$NON-NLS-1$
		}
	}

}
