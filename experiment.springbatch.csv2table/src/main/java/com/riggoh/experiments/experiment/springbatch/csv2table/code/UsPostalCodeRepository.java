package com.riggoh.experiments.experiment.springbatch.csv2table.code;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface UsPostalCodeRepository extends CrudRepository<UsPostalCode, Long>
{
	List<UsPostalCode> findByCityName(String cityName);

	List<UsPostalCode> findByCityNameAndStateAbbreviation(String cityName, String stateAbbreviation);
}
