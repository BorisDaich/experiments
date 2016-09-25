package com.riggoh.experiments.experiment.springbatch.csv2table.code;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigurationTestDbWorks
{
	private static final Logger	l		= LogManager.getLogger(ConfigurationTestDbWorks.class.getName());
	static final UsPostalCode[]	data	= new UsPostalCode[] { UsPostalCode.random(), UsPostalCode.random(), UsPostalCode.random(), UsPostalCode.random(), UsPostalCode.random() };

	@Bean
	public CommandLineRunner testJPA(UsPostalCodeRepository repository)
	{
		return (args) ->
			{
				// save a couple of customers
				for (UsPostalCode x : data)
					repository.save(x);

				// fetch all
				l.info("Customers found with findAll():");
				l.info("-------------------------------");
				for (UsPostalCode customer : repository.findAll())
				{
					l.info(customer.toString());
				}
				l.info("");

				// fetch an individual customer by ID
				UsPostalCode customer = repository.findOne(1L);
				l.info("Customer found with findOne(1L):");
				l.info("--------------------------------");
				l.info(customer.toString());
				l.info("");

				// fetch customers by last name
				l.info("Customer found with findByLastName('Bauer'):");
				l.info("--------------------------------------------");
				for (UsPostalCode z : repository.findByCityName(data[0].getCityName()))
				{
					l.info(z.toString());
				}
				l.info("");
			};
	}
}
