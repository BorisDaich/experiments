package experiment.pe.generator.code;

import static org.junit.Assert.assertTrue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.springframework.boot.test.context.TestComponent;

import com.urelay.posteverywhere.bean.load.PostLoad;

import biz.daich.common.gson.GsonTools;

@TestComponent
public class TestGenLoadWithPadam
{
	private static final Logger l = LogManager.getLogger(TestGenLoadWithPadam.class.getName());

	public static void main(String[] args)
	{
		PostLoad p = GenLoadWithPadam.get();

		l.debug(GsonTools.getDefaultGson().toJson(p));

	}

	@Test
	public void test_PostLoadProperties()
	{
		PostLoad p = GenLoadWithPadam.get();
		l.debug(GsonTools.print(p));
		assertTrue(p.getLoadCount() > 0);
	}
}
