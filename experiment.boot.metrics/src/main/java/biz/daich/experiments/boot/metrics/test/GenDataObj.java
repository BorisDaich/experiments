package biz.daich.experiments.boot.metrics.test;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.co.jemos.podam.api.AbstractRandomDataProviderStrategy;
import uk.co.jemos.podam.api.AttributeMetadata;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

public class GenDataObj implements IObjGen<DataObj>
{
	private static final Logger			l				= LogManager.getLogger(GenDataObj.class.getName());

	protected static final Random		R				= new Random();
	protected static final PodamFactory	podamFactory	= getPodamFactory();
	protected static final int			NUM_OF_TYPES	= 111;

	protected static PodamFactory getPodamFactory()
	{
		PodamFactory factory = new PodamFactoryImpl(new CustomProviderStrategy());
		//factory.
		return factory;
	}

	public static class CustomProviderStrategy extends AbstractRandomDataProviderStrategy
	{
		public final String[] types;
		{
			types = new String[NUM_OF_TYPES];
			for (int i = 0; i < NUM_OF_TYPES; i++)
				types[i] = "type" + i;
		}

		@Override
		public String getStringValue(AttributeMetadata attributeMetadata)
		{
			if ("type".equals(attributeMetadata.getAttributeName()))
			{
				return types[R.nextInt(NUM_OF_TYPES)];

			}
			else
			{
				return super.getStringValue(attributeMetadata);
			}
		}

	}

	public DataObj get(Object... o)
	{
		return podamFactory.manufacturePojo(DataObj.class);
	}

	//	public static void main(String[] args)
	//	{
	//		l.debug(new GenDataObj().get());
	//
	//	}

}
