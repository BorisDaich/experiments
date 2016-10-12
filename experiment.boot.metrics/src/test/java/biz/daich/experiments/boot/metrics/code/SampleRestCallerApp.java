package biz.daich.experiments.boot.metrics.code;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import biz.daich.experiments.boot.metrics.test.SampleRestCaller;

public class SampleRestCallerApp extends SampleRestCaller implements Runnable
{
	private static final Logger	l					= LogManager.getLogger(SampleRestCallerApp.class.getName());
	protected boolean runIt = true;

	public SampleRestCallerApp()
	{
		super();
	}

	public void run()
	{
		while (runIt)
		{
			callRest();

			try
			{
				Thread.sleep(DELAY_TIME_MILLI);
			}
			catch (InterruptedException e)
			{
				l.warn(e.getMessage()); //$NON-NLS-1$
			}
		}
	}

	public static void main(String[] args)
	{
		new SampleRestCallerApp().run();

	}

}
