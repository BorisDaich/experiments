/**
 *
 */
package experiment.pe.generator.code;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.urelay.posteverywhere.bean.load.PostLoad;

import uk.co.jemos.podam.api.AbstractRandomDataProviderStrategy;
import uk.co.jemos.podam.api.AttributeMetadata;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * @author boris
 *
 */

public class GenLoadWithPadam implements IObjGen<PostLoad>
{
	private static final Logger				l			= LogManager.getLogger(GenLoadWithPadam.class.getName());

	protected static final GenLoadWithPadam	instance	= new GenLoadWithPadam();
	protected static final Random			R			= new Random();
	protected PodamFactory					podamFactory;

	public GenLoadWithPadam()
	{
		podamFactory = getPodamFactory();
	}

	public static final IObjGen<PostLoad> getInstance()
	{
		return instance;
	}

	/**
	 * random integer in the range
	 */
	public static int randBetween(int start, int end)
	{
		if (end < start)
		{
			l.warn("end in less than start");
		}
		return start + R.nextInt(end - start);
	}

	public class CustomProviderStrategy extends AbstractRandomDataProviderStrategy
	{

		@Override
		public Integer getInteger(AttributeMetadata am)
		{
			if ("loadCount".equals(am.getAttributeName()))
			{
				return super.getIntegerInRange(0, 10, am);
			}
			else
			{
				return super.getInteger(am);
			}
		}

	}

	protected PodamFactory getPodamFactory()
	{
		PodamFactory factory = new PodamFactoryImpl(new CustomProviderStrategy());
		//factory.
		return factory;
	}

	public static PostLoad get()
	{
		return instance.get(1);
	}

	@Override
	public PostLoad get(Object... objects)
	{
		return podamFactory.manufacturePojo(PostLoad.class);
	}

}
