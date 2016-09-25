/**
 *
 */
package experiment.pe.generator.code;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
	protected PodamFactory					podamFactory;

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

	public GenLoadWithPadam()
	{
		podamFactory = getPodamFactory();
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

	public static void main(String[] args)
	{
		PostLoad p = GenLoadWithPadam.get();
		l.debug(ReflectionToStringBuilder.toString(p, ToStringStyle.MULTI_LINE_STYLE));

	}
}
