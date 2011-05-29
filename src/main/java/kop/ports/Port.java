package kop.ports;

import com.bbn.openmap.proj.coords.DMSLatLonPoint;
import kop.cargo.CargoType;
import kop.map.routecalculator.Point;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Port model.
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
	Point position;
	@Element(required = false)
	LatLong longitude;
	@Element(required = false)
	LatLong latitude;
	@Element(required = false)
	private PortCargoTypeList portCargoTypes;

	// TODO this constructor should most probably be private, anyone who wants a port should use the factory methods.
	public Port() {
		position = new Point();
		position.setLat(-1);
		position.setLon(-1);
		portCargoTypes = new PortCargoTypeList();
	}
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
		setLatitude(Integer.valueOf(deg), Integer.valueOf(min), hemisphere);
	}

	public void setLatitude(int deg, int min, String hemisphere) {
		DMSLatLonPoint p = new DMSLatLonPoint(hemisphere.equals("S"),
				deg,
				min,
				0,
				false,
				0,
				0,
				(float) 0.0);
		position.setLat(p.getDecimalLatitude());
	}

	public void setLongitude(String deg, String min, String hemisphere) {
		setLongitude(Integer.valueOf(deg), Integer.valueOf(min), hemisphere);
	}
	public void setLongitude(int deg, int min, String hemisphere) {
		DMSLatLonPoint p = new DMSLatLonPoint(false,
				0,
				0,
				0,
				hemisphere.equals("E"),
				deg,
				min,
				0);
		position.setLon(p.getDecimalLongitude());
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

	private void initializePosition() {
		position = new Point();
		setLatitude(latitude.deg, latitude.min, latitude.hemisphere);
		setLongitude(longitude.deg, longitude.min, longitude.hemisphere);
	}

	public double getLatitude() {
		if (position.getLat() == -1) {
			initializePosition();
		}
		return position.getLat();
	}

	public double getLongitude() {
		if (position==null || position.getLon() == -1.0) {
			initializePosition();
		}
		return position.getLon();
	}

	public void setLatitude(double latitude) {
		position.setLat((float) latitude);
	}

	public void setLongitude(double longitude) {
		position.setLon((float) longitude);
	}

	public Point getPosition() {
		if (position == null || position.getLat() == -1.0) {
			initializePosition();
		}
		return position;
	}

	public void addCargoType(CargoType cargoType) {
		portCargoTypes.add(new PortCargoType(cargoType));
	}

	public void addAllCargoTypes(List<CargoType> cargoTypeList) {
		for (CargoType t: cargoTypeList) {
			addCargoType(t);
		}
	}

	public PortProxy getProxy() {
		return new PortProxy(this);
	}

	@Root
	public static class LatLong {
		@Element
		int deg;
		@Element
		int min;
		@Element
		String hemisphere;

		public LatLong() {
		}
	}
}