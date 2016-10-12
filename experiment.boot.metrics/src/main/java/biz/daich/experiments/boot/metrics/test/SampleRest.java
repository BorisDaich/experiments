package biz.daich.experiments.boot.metrics.test;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import biz.daich.common.gson.GsonTools;
import biz.daich.experiments.boot.metrics.code.DistinctInTimeWindowCounter;

@RestController
public class SampleRest
{
	private static final Logger				l	= LogManager.getLogger(SampleRest.class.getName());

	@Autowired
	protected DistinctInTimeWindowCounter<String, DataObj>	metrica;
	@Autowired
	protected IObjGen<DataObj>				generator;

	public SampleRest()
	{
		super();
		l.debug("SampleRest created");
	}

	@RequestMapping(value = { "/get" }, produces = { "application/json" })
	@ResponseBody
	public DataObj get()
	{
		DataObj dataObj = generator.get();
		metrica.count(dataObj.getType(), dataObj);
		if (l.isTraceEnabled()) //
			l.trace("returning a data " + GsonTools.print(dataObj));
		return dataObj;
	}

}
