package com.riggoh.experiments.experiment.springbatch.csv2table;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

//@EnableWebMvc
@EnableBatchProcessing
@SpringBootApplication
public class AppSpringBatchCsv2H2
{
	private static final Logger l = LogManager.getLogger(AppSpringBatchCsv2H2.class.getName());

	public static void main(String[] args)
	{
		SpringApplication.run(AppSpringBatchCsv2H2.class);
		l.debug("----------Ended----------");
	}

}
