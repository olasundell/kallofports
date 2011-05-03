package kop.map;

import kop.ports.Port;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
* Created by IntelliJ IDEA.
* User: ola
* Date: 5/2/11
* Time: 4:46 PM
* To change this template use File | Settings | File Templates.
*/
@Root
public class LatLong {
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

		String[] split = coord.split("\\.");
		deg = split[0].replaceAll("-","");
		min = String.valueOf(Math.round(Double.parseDouble(split[1])*0.6));
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
