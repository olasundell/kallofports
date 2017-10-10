package kop.ports;

import kop.cargo.CargoType;

/**
* @author Ola Sundell
*/

class Continent {
	private final String[] arr = {
			"North America",
			"South America",
			"Oceania",
			"Africa",
			"Middle East",
			"Asia",
			"Europe"
	};

	public boolean isContinent(String name) {
		for (String continent: arr) {
			if (name.equalsIgnoreCase(continent)) {
				return true;
			}
		}

		return false;
	}

	public String getPortForCargoType(String name, CargoType type) {
		return "";
	}
}
