package experiment.pe.generator;

import java.io.IOException;
import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.urelay.announce.entities.BrokerLoad;

import biz.daich.common.tools.jpa.JpaEntityDependencyResolver;

public class TestJavaDependencyResolver
{
	static final Logger l = LogManager.getLogger(TestJavaDependencyResolver.class.getName());

	public static void main(String[] args) throws IOException
	{
		final String className = BrokerLoad.class.getCanonicalName();

		l.debug("FOR className = " + className);
		//		final Set<Class<?>> classesUsedBy = ClassCollector.getClassesUsedBy(className, prefix);
		//		for (Class<?> z : classesUsedBy)
		//		{
		//
		//			if (z.isAnnotationPresent(Entity.class))
		//			{
		//				l.error(z.getCanonicalName());
		//			}
		//			else
		//			{
		//				l.debug(z.getCanonicalName());
		//			}
		//
		//		}

		Collection<String> jpaAnnotatedClassNamesDependedOn = JpaEntityDependencyResolver.getJpaAnnotatedClassNamesDependedOn(BrokerLoad.class);
		l.debug(jpaAnnotatedClassNamesDependedOn);

	}
}
