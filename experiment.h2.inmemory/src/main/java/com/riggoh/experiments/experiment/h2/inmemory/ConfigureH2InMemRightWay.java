package com.riggoh.experiments.experiment.h2.inmemory;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.sql.Driver;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.embedded.ConnectionProperties;
import org.springframework.jdbc.datasource.embedded.DataSourceFactory;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;

@Configuration
@Profile("RightWay")
public class ConfigureH2InMemRightWay
{
	private static final Logger l = LogManager.getLogger(ConfigureH2InMemRightWay.class.getName());

	class MyH2DataSourceFactory implements DataSourceFactory
	{
		private final Joiner.MapJoiner			mapJoiner				= Joiner.on(';').withKeyValueSeparator("=");
		private final SimpleDriverDataSource	dataSource				= new SimpleDriverDataSource();

		private final Map<String, String>		additionalSettings		= new HashMap<>();

		private final ConnectionProperties		connectionProperties	= new ConnectionProperties()
		//static class MyConnectionProperties implements ConnectionProperties
		{

																			//			private final SimpleDriverDataSource	dataSource;

																			//			public MyConnectionProperties(SimpleDriverDataSource dataSource)
																			//			{
																			//				this.dataSource = dataSource;
																			//				assertTrue(dataSource != null);
																			//			}

																			@Override
																			public void setDriverClass(Class<? extends Driver> driverClass)
																			{
																				dataSource.setDriverClass(driverClass);
																			}

																			@Override
																			public void setUrl(String url)
																			{
																				dataSource.setUrl(updateUrl(url));
																			}

																			protected String updateUrl(String s)
																			{
																				if (Strings.isNullOrEmpty(s)) return s;

																				String additionalSettingsStr = mapJoiner.join(additionalSettings);
																				if (!Strings.isNullOrEmpty(additionalSettingsStr))
																				{
																					s = s + ";" + additionalSettingsStr;
																				}
																				l.debug("the H2 JDBC URL is  " + s);
																				return s;
																			}

																			@Override
																			public void setUsername(String username)
																			{
																				dataSource.setUsername(username);
																			}

																			@Override
																			public void setPassword(String password)
																			{
																				dataSource.setPassword(password);
																			}

																		};

		@Override
		public ConnectionProperties getConnectionProperties()
		{
			return connectionProperties;
		}

		@Override
		public DataSource getDataSource()
		{
			return this.dataSource;
		}

		public Map<String, String> getAdditionalConnectionSettingsMap()
		{
			return additionalSettings;
		}
	}

	@Bean
	public DataSource getDataSource() throws SQLException
	{
		MyH2DataSourceFactory dataSourceFactory = new MyH2DataSourceFactory();
		dataSourceFactory.getAdditionalConnectionSettingsMap().put("DATABASE_TO_UPPER", "false");
		EmbeddedDatabase db = new EmbeddedDatabaseBuilder()//
															.setName("PostEverywhereLoads")//
																.setType(EmbeddedDatabaseType.H2)//
																.setScriptEncoding("UTF-8")//
																.ignoreFailedDrops(true)//
																.setDataSourceFactory(dataSourceFactory)
																.build();

		return db;
	}
}
