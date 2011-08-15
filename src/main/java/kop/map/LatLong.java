package kop.map;

import kop.ports.Port;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Old implementation to keep track of Latitude and Longitude points.
 * @deprecated use the OpenMap LatLonPoint instead.
 * @see com.bbn.openmap.LatLonPoint
*/
@Root
public final class LatLong implements Cloneable {
	/**
	 * Used for deserialization.
	 */
	public enum Direction {
		latitude,
		longitude
	}
	private static final String EAST = "E";
	private static final String WEST = "W";
	private static final String NORTH = "N";
	private static final String SOUTH = "S";

	@Element
	String deg;
	@Element
	String min;
	@Element
	String hemisphere;
	@Element(required = false)
	Double decimal=null;

	public LatLong() {

	}

	public LatLong(String coord, Direction direction) {
		if (direction == LatLong.Direction.latitude) {
			if (coord.charAt(0) == '-') {
				setHemisphere(SOUTH);
			} else {
				setHemisphere(NORTH);
			}
		} else if (direction == LatLong.Direction.longitude) {
			if (coord.charAt(0) == '-') {
				setHemisphere(WEST);
			} else {
				setHemisphere(EAST);
			}
		}

		decimal = Double.parseDouble(coord);
		String[] split = coord.split("\\.");
		deg = split[0].replaceAll("-","");
		min = String.valueOf(Math.round(Double.parseDouble(split[1])*0.6));
	}

	public LatLong(String deg, String min, String hemisphere) {
		this.deg = deg;
		this.min = min;
		this.hemisphere = hemisphere;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
//		return new LatLong(deg, min, hemisphere);
	}

	public double getCoordinate() {
		if (decimal == null) {
			double v = Double.parseDouble(deg);
			v+=Double.parseDouble(min) / 60.0;
			if (hemisphere.equals(WEST) || hemisphere.equals(SOUTH)) {
				v = (double) (v * -1.0);
			}

			decimal = (double)Math.round(v*1000)/1000;
		}

		return decimal;
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

	public final void setHemisphere(String hemisphere) {
		this.hemisphere = hemisphere;
	}
}
