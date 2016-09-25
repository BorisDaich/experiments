package com.riggoh.experiments.experiment.springbatch.csv2table;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.riggoh.experiments.experiment.springbatch.csv2table.code.UsPostalCode;

public class JobCompletionNotificationListener extends JobExecutionListenerSupport
{
	private static final Logger	l	= LogManager.getLogger(JobCompletionNotificationListener.class.getName());

	private final JdbcTemplate	jdbcTemplate;

	@Autowired
	public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate)
	{
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void afterJob(JobExecution jobExecution)
	{
		if (jobExecution.getStatus() == BatchStatus.COMPLETED)
		{
			l.info("!!! JOB FINISHED! Time to verify the results");

			List<UsPostalCode> results = jdbcTemplate.query("SELECT id, postal_code,  city_name, state_name, state_abbreviation, county,  latitude,  longitude  FROM us_postal_code",
															new RowMapper<UsPostalCode>()
			{
																@Override
																public UsPostalCode mapRow(ResultSet rs, int row) throws SQLException
																{
																	return new UsPostalCode(//
																							rs.getLong(1),//
																							rs.getInt(2), //
																							rs.getString(3),//
																							rs.getString(4), //
																							rs.getString(5), //
																							rs.getString(6), //
																							rs.getDouble(7), //
																							rs.getDouble(8)//
																	);
																}
															});

			for (UsPostalCode person : results)
			{
				l.info("Found <" + person + "> in the database.");
			}

		}
	}
}
