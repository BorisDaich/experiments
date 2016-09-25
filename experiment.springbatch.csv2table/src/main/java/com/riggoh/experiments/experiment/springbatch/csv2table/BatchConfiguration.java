package com.riggoh.experiments.experiment.springbatch.csv2table;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;

import com.riggoh.experiments.experiment.springbatch.csv2table.code.UsPostalCode;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration
{

	@Autowired
	public JobBuilderFactory	jobBuilderFactory;

	@Autowired
	public StepBuilderFactory	stepBuilderFactory;

	@Autowired
	public DataSource			dataSource;

	static class UsPostalCodeDelimitedLineTokenizer extends DelimitedLineTokenizer
	{
		public UsPostalCodeDelimitedLineTokenizer()
		{
			setStrict(false); // because we have spare comma at the end of each line
			setNames(new String[] { "postalCode", "cityName", "stateName", "stateAbbreviation", "county", "latitude", "longitude" });
		}

	}

	static class UsPostalCodeBeanWrapperFieldSetMapper extends BeanWrapperFieldSetMapper<UsPostalCode>
	{
		public UsPostalCodeBeanWrapperFieldSetMapper()
		{
			setTargetType(UsPostalCode.class);
		}
	}

	static class UsPostalCodeLineMapper extends DefaultLineMapper<UsPostalCode>
	{

		public UsPostalCodeLineMapper()
		{
			setLineTokenizer(new UsPostalCodeDelimitedLineTokenizer());
			setFieldSetMapper(new UsPostalCodeBeanWrapperFieldSetMapper());
		}
	}

	// tag::readerwriterprocessor[]
	@Bean
	public FlatFileItemReader<UsPostalCode> usPostalCodeCsvFileReader()
	{
		FlatFileItemReader<UsPostalCode> reader = new FlatFileItemReader<UsPostalCode>();
		reader.setResource(new ClassPathResource("us_postal_codes_short.csv"));
		UsPostalCodeLineMapper usPostalCodeLineMapper = new UsPostalCodeLineMapper();
		reader.setLineMapper(usPostalCodeLineMapper);
		reader.setLinesToSkip(1);
		return reader;
	}

	@Bean
	public LineItemProcessor usPostalCodeCsvLineProcessor()
	{
		return new LineItemProcessor();
	}

	@Bean
	public JdbcBatchItemWriter<UsPostalCode> UsPostalCodeJdbcWriter()
	{
		JdbcBatchItemWriter<UsPostalCode> writer = new JdbcBatchItemWriter<UsPostalCode>();
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<UsPostalCode>());
		//		writer.setSql("INSERT INTO people (first_name, last_name) VALUES (:firstName, :lastName)");
		writer.setSql(//
						"insert into us_postal_code " +//
								"(id,   city_name,  county,  latitude,  longitude, postal_code,  state_abbreviation, state_name) " + //
								"values " + //
								"(null, :cityName, :county, :latitude, :longitude,:postalCode,  :stateAbbreviation, :stateName)"//
		);
		writer.setDataSource(dataSource);
		return writer;
	}
	// end::readerwriterprocessor[]

	// tag::listener[]

	@Bean
	public JobExecutionListener jobCompletionNotificationListener()
	{
		return new JobCompletionNotificationListener(new JdbcTemplate(dataSource));
	}

	// end::listener[]

	// tag::jobstep[]
	@Bean
	public Job importUsPostalCodeJob()
	{
		return jobBuilderFactory.get("importUsPostalCodeJob").incrementer(new RunIdIncrementer()).listener(jobCompletionNotificationListener()).flow(step1()).end().build();
	}

	@Bean
	public Step step1()
	{
		return stepBuilderFactory.get("step1").<UsPostalCode, UsPostalCode> chunk(10).reader(usPostalCodeCsvFileReader()).processor(usPostalCodeCsvLineProcessor()).writer(UsPostalCodeJdbcWriter()).build();
	}
	// end::jobstep[]
}
