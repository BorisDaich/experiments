package com.riggoh.experiments.experiment.springbatch.csv2table;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.batch.item.ItemProcessor;

import com.riggoh.experiments.experiment.springbatch.csv2table.code.UsPostalCode;

public class LineItemProcessor implements ItemProcessor<UsPostalCode, UsPostalCode>
{
	private static final Logger l = LogManager.getLogger(LineItemProcessor.class.getName());

	@Override
	public UsPostalCode process(final UsPostalCode x) throws Exception
	{
		final String cityName = x.getCityName().toUpperCase();
		final String stateAbr = x.getStateAbbreviation().toUpperCase();

		final UsPostalCode transformed = new UsPostalCode(1111, cityName, stateAbr);

		l.info("Converting (" + x + ") into (" + transformed + ")");

		return x;
	}

}
