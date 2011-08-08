package kop.ports;

import kop.cargo.CargoType;
import kop.game.Game;
import kop.serialization.ModelSerializer;
import kop.serialization.SerializationException;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author Ola Sundell
*/
@Root
public class Countries {
	@ElementMap
	private Map<String, String> countries;

	private static volatile Countries instance = null;

	public static Countries getInstance() throws SerializationException {
		if (instance == null) {
			instance = (Countries) ModelSerializer.readFromFile("kop/ports/countries.xml", Countries.class);
		}

		return instance;
	}

	public Countries() {
		countries = new HashMap<String, String>();
	}

	void createTestCountries() {
		countries.put("JP","Japan");
		countries.put("SE","Sweden");
	}

	public boolean isCountry(String name) {
		return countries.containsValue(name) || countries.containsKey(name);
	}


	public String getCountryCode(String country) throws NoSuchCountryException {
		if (countries.containsKey(country)) {
			// supplied argument is already a country code!
			return country;
		}

		for (Map.Entry<String, String> e: countries.entrySet()) {
			if (e.getValue().equals(country)) {
				return e.getKey();
			}
		}
		throw new NoSuchCountryException(String.format("Could not find country code for %s",country));
	}

	public static class NoSuchCountryException extends Exception {
		public NoSuchCountryException(String s) {
			super(s);
		}
	}
}
