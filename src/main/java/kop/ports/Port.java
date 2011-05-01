/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kop.ports;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 *
 * @author ola
 */
@Root
public class Port {
	@Attribute
	private String name;
	@Element
	private String unlocode;
	private String bunkerOil;
	private String dieselOil;
	private String countryCode;
	private String harbourSize;
	private String harbourType;
	private String drydock;
	private String country;
	@Element(required = false)
	private LatLong latitude;
	@Element(required = false)
	private LatLong longitude;
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
		latitude = new LatLong(deg, min, hemisphere);
	}

	public void setLongitude(String deg, String min, String hemisphere) {
		longitude = new LatLong(deg, min, hemisphere);
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

	public void setUnlocode(String unlocode) {
		this.unlocode = unlocode;
	}

	public String getUnlocode() {
		return unlocode;  //To change body of created methods use File | Settings | File Templates.
	}

	public String toString() {
		return getUnlocode() + " " + getName();
	}

	public LatLong getLatitude() {
		return latitude;
	}

	public LatLong getLongitude() {
		return longitude;
	}

	public void setLatitude(LatLong latitude) {
		this.latitude = latitude.clone();
	}

	public void setLongitude(LatLong longitude) {
		this.longitude = longitude.clone();
	}

	@Root
	public static class LatLong {
		private static final String WEST = "W";
		private static final String SOUTH = "S";
		@Element
		String deg;
		@Element
		String min;
		@Element
		String hemisphere;

		public LatLong() {

		}

		public LatLong(String deg, String min, String hemisphere) {
			this.deg = deg;
			this.min = min;
			this.hemisphere = hemisphere;
		}

		public LatLong clone() {
			return new LatLong(deg, min, hemisphere);
		}

		public double getCoordinate() {
			double v = Double.parseDouble(deg);
			v+=Double.parseDouble(min) / 60.0;
			if (hemisphere.equals(WEST) || hemisphere.equals(SOUTH)) {
				v = (double) (v * -1.0);
			}

			return (double)Math.round(v*1000)/1000;
		}

		public String getDeg() {
			return deg;
		}

		public void setDeg(String deg) {
			this.deg = deg;
		}

		public String getMin() {
			return min;
		}

		public void setMin(String min) {
			this.min = min;
		}

		public String getHemisphere() {
			return hemisphere;
		}

		public void setHemisphere(String hemisphere) {
			this.hemisphere = hemisphere;
		}
	}
}
