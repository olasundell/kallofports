/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kop.ports;

/**
 *
 * @author ola
 */
public class Port {
	private String name;
	private String bunkerOil;
	private String dieselOil;
	private String countryCode;
	private String harbourSize;
	private String harbourType;
	private String drydock;
	private String country;
	private String unlocode;
	// TODO add resource production stuff

	public void setName(String name) {
		this.name=name;
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Port port = (Port) o;

		return !(name != null ? !name.equals(port.name) : port.name != null);
	}

	public void setBunkerOil(String bunkerOil) {
		this.bunkerOil = bunkerOil;
	}

	public void setDieselOil(String dieselOil) {
		this.dieselOil = dieselOil;
	}


	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public void setLatitude(String deg, String min, String hemisphere) {
	}

	public void setLongitude(String deg, String min, String hemisphere) {
	}

	public void setHarbourSize(String harbourSize) {
		this.harbourSize = harbourSize;
	}

	public void setHarbourType(String harbourType) {
		this.harbourType = harbourType;
	}

	public void setDrydock(String drydock) {
		this.drydock = drydock;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountry(String country) {
		this.country = country;
	}
}
