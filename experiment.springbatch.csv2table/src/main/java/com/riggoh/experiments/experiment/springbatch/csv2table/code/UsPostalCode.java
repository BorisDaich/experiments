package com.riggoh.experiments.experiment.springbatch.csv2table.code;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.apache.commons.lang3.RandomStringUtils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * POJO of a row in the us_postal_codes.csv
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsPostalCode
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long		id;
	protected int		postalCode			= 0;
	protected String	cityName			= null;
	protected String	stateName			= null;
	protected String	stateAbbreviation	= null;
	protected String	county				= null;
	protected double	latitude;
	protected double	longitude;

	public UsPostalCode(int postalCode, String cityName, String stateAbbreviation)
	{
		super();
		this.postalCode = postalCode;
		this.cityName = cityName;
		this.stateAbbreviation = stateAbbreviation;
		latitude = Math.random();
		longitude = Math.random();
	}

	public static UsPostalCode random()
	{
		UsPostalCode x = new UsPostalCode();
		x.latitude = Math.random();
		x.longitude = Math.random();
		x.stateAbbreviation = RandomStringUtils.randomAlphabetic(2);
		x.cityName = RandomStringUtils.randomAlphabetic(10);
		x.postalCode = (int) Math.round(Math.random() * 10000);
		x.stateName = RandomStringUtils.randomAlphabetic(5);
		return x;
	}
}
