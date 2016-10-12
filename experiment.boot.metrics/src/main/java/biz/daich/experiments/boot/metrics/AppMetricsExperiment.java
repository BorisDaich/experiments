package biz.daich.experiments.boot.metrics;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppMetricsExperiment
{
	private static final Logger l = LogManager.getLogger(AppMetricsExperiment.class.getName());

	public static void main(String[] args)
	{
		l.debug("Start");
		SpringApplication.run(AppMetricsExperiment.class, args);
		l.debug("end");
	}

}
