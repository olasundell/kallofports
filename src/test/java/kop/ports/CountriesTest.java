package kop.ports;

import kop.serialization.ModelSerializer;
import kop.serialization.SerializationException;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * @author Ola Sundell
 */
public class CountriesTest {
//	@Test
// this test was originally needed to create the first countries.xml file and is left should it be needed again.
//	public void serialize() throws SerializationException {
//		Countries country = new Countries();
//
//		country.createTestCountries();
//
//		ModelSerializer.saveToFile("country.xml",Countries.class, country);
//	}

	@Test
	public void deserialize() throws SerializationException {
		Countries countries;
		countries = (Countries) ModelSerializer.readFromFile("kop/ports/countries.xml", Countries.class);
		assertNotNull(countries);
		assertTrue(countries.isCountry("Japan"), "Japan should be a countries but isn't.");
		assertFalse(countries.isCountry("japan"), "japan shouldn't be a countries but is (case sensitivity, damnit).");
	}

	@Test
	public void getCountryCodeForCountry() throws SerializationException, Countries.NoSuchCountryException {
		Countries countries;
		countries = (Countries) ModelSerializer.readFromFile("kop/ports/countries.xml", Countries.class);
		String code = countries.getCountryCode("Italy");
		assertNotNull(code);
		assertEquals(code,"IT");

		boolean exceptionThrown = false;
		try {
			code = countries.getCountryCode("Blah");
		} catch (Countries.NoSuchCountryException e) {
			exceptionThrown = true;
		}

		assertTrue(exceptionThrown, "No exception was thrown when searching for a non-existant country.");
	}
}
