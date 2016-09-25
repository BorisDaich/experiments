package com.riggoh.experiments.experiment.h2.inmemory.code;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigurationTestDbWorks
{
	private static final Logger l = LogManager.getLogger(ConfigurationTestDbWorks.class.getName());

	@Bean
	public CommandLineRunner testJPA(CustomerRepository repository)
	{
		return (args) ->
			{
				// save a couple of customers
				repository.save(new Customer("Jack", "Bauer"));
				repository.save(new Customer("Chloe", "O'Brian"));
				repository.save(new Customer("Kim", "Bauer"));
				repository.save(new Customer("David", "Palmer"));
				repository.save(new Customer("Michelle", "Dessler"));

				// fetch all customers
				l.info("Customers found with findAll():");
				l.info("-------------------------------");
				for (Customer customer : repository.findAll())
				{
					l.info(customer.toString());
				}
				l.info("");

				// fetch an individual customer by ID
				Customer customer = repository.findOne(1L);
				l.info("Customer found with findOne(1L):");
				l.info("--------------------------------");
				l.info(customer.toString());
				l.info("");

				// fetch customers by last name
				l.info("Customer found with findByLastName('Bauer'):");
				l.info("--------------------------------------------");
				for (Customer bauer : repository.findByLastName("Bauer"))
				{
					l.info(bauer.toString());
				}
				l.info("");
			};
	}
}
