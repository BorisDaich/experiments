package experiment.pe.generator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;

import com.google.gson.Gson;
import com.urelay.posteverywhere.bean.load.PostLoad;

import biz.daich.common.gson.GsonTools;
import experiment.pe.generator.code.GenLoadWithPadam;
import experiment.pe.generator.code.IObjGen;

@SpringBootApplication
//@EnableIntegration
//@Configuration
public class MainPEGen
{
	private static final Logger l = LogManager.getLogger(MainPEGen.class.getName());

	static
	{
		System.setProperty("jansi.passthrough", "true");
	}

	public static void main(String[] args)
	{
		SpringApplication springApplication = new SpringApplication(MainPEGen.class);
	//	springApplication.setAdditionalProfiles("p1");
		ConfigurableApplicationContext configurableApplicationContext = springApplication.run(args);
		l.debug("------------------PROFILE-------------------------------------");
		l.debug(GsonTools.print(configurableApplicationContext));
		l.debug("------------------RUNNING-------------------------------------");

	}

	@Configuration
	public static class ConfigureProfile_all
	{
		private static final Logger l = LogManager.getLogger(ConfigureProfile_all.class.getName());

		@Bean
		public Gson getGsonSingleton()
		{
			l.debug("gson bean created");
			return GsonTools.gson;
		}
	}

	@Configuration
	@Profile("p1")
	@ImportResource({ "pe.spring.integration.p1.xml" })
	public static class ConfigureProfile_p1
	{

		@Bean
		IObjGen<PostLoad> loadsGenerator()
		{
			return GenLoadWithPadam.getInstance();
		}
	}
}
