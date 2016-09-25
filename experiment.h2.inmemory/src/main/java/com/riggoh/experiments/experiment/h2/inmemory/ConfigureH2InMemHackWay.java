package com.riggoh.experiments.experiment.h2.inmemory;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

@Configuration
@Profile("HackWay")
public class ConfigureH2InMemHackWay
{
	@Bean
	public DataSource getDataSource() throws SQLException
	{
		return new EmbeddedDatabaseBuilder()//
											.setName("PostEverywhereLoads;DATABASE_TO_UPPER=false")// <-- this is THE HACK!!!

												// built on the fact that the in the H2EmbeddedDatabaseConfigurer at least in Spring JDBC 4.3.2 RELEASE
												// JDBC string "jdbc:h2:mem:%s;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE"
												// and there is a line
												// properties.setUrl(String.format("jdbc:h2:mem:%s;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=false", databaseName));
												// so it works

												.setType(EmbeddedDatabaseType.H2)//
												.setScriptEncoding("UTF-8")//
												.ignoreFailedDrops(true)//
												.build();
	}

}
