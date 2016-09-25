package com.riggoh.experiments.experiment.h2.inmemory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.riggoh.experiments.experiment.h2.inmemory.code.Customer;

/**
 * the H2 Web console accessible on http://localhost:8080/h2-console/
 *
 *
 * when none of the following lines is not uncommented the name of the database will be "testdb"
 * and the jdbc connection string for the DB console will be
 * <b> jdbc:h2:mem:testdb </b>
 *
 * for both the HACK WAY and the RIGHT WAY the JDBC connection URL is
 * <b>jdbc:h2:mem:PostEverywhereLoads</b>
 *
 * when the HACK WAY
 */
@EnableWebMvc // <== this is required to have the webserver and use h2 db console
@SpringBootApplication
public class AppH2InMemory
{
	private static final Logger l = LogManager.getLogger(AppH2InMemory.class.getName());

	public static void main(String[] args)
	{
		SpringApplication springApplication = new SpringApplication(AppH2InMemory.class);

		// when none of the following lines is not uncomented the name of the database will be testdb
		// and the jdbc connection string for the DB console will be  jdbc:h2:mem:testdb

		// THE HACK WAY profile
		// springApplication.setAdditionalProfiles("HackWay");

		// THE RIGHT WAY profile
		//  springApplication.setAdditionalProfiles("RightWay");

		springApplication.run(args);

	}
}
