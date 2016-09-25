package experiment.pe.import2db.cities;

import lombok.Data;

/**
 * POJO of a row in the us_postal_codes.csv
 */
@Data
public class UsPostalCode {

	protected int postalCode = 0;
	protected String cityName = null;
	protected String stateName = null;
	protected String stateAbbreviation = null;
	protected String county = null;
	protected double latitude;
	protected double longitude;

}
