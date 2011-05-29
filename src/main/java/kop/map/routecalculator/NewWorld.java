package kop.map.routecalculator;

import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Contains real arrays with lats and lons (no Collections here), originally used for XML persisting
 * purposes but has outgrown original purpose.
 */
@Root
public class NewWorld {
	public static final int DEFAULT_SCALE = 2;
	public static final int DEFAULT_SOUTH_OFFSET = 20;
	public static final int DEFAULT_NORTH_OFFSET = 20;

	/**
	 * Scale of lats and lons.
	 * If the scale is 1, then every lat and lon is represented by an array point.
	 * If the scale is 2, then every .5 lat and lon is represented (70.5, 70, 69.5 and so on)
	 * I leave it to the reader to figure out what happens when the scale is below 1.
	 * A setting of 0 or less isn't recommended at all.
	 */
	private float scale = DEFAULT_SCALE;
	private int southOffset = DEFAULT_SOUTH_OFFSET;
	private int northOffset = DEFAULT_NORTH_OFFSET;

	@ElementArray(empty = true)
	LatitudeArr[] lats;

	public NewWorld() {}

	public NewWorld(Point[][] points) {
		lats = new LatitudeArr[points.length];

		for (int i=0;i<lats.length;i++) {
			lats[i] = new LatitudeArr(points[i]);
		}
	}

	public NewWorld(int latitudeSize, int longitudeSize) {
		lats = new LatitudeArr[latitudeSize];
		for (int i=0;i<lats.length;i++) {
			lats[i] = new LatitudeArr(longitudeSize);
		}
	}

	public static NewWorld getWorld(float scale) {
		RouteCalculator calculator = new RouteCalculator();
		NewWorld newWorld = new NewWorld(Math.round(180*scale),Math.round(360*scale));
		newWorld.setScale(scale);
		newWorld.setNorthOffset(0);
		newWorld.setSouthOffset(0);
		return calculator.calculateWorld(newWorld);
	}

	protected int reverseLat(double lat) {
		return (int) Math.round((lat - 90+northOffset)*scale*-1);
	}

	protected int reverseLon(double lon) {
		return (int) (Math.round((180+lon)*scale));
	}

	protected float calcLon(int j) {
		return j / scale - 180;
	}

	protected float calcLat(int i) {
		return 90 - northOffset - i/scale;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float sc) {
		if (sc <= 0) {
			throw new IllegalArgumentException("Trying to set scale to zero or below will have funky divide by zero side effects. Is this wanted?");
		}

		scale = sc;
	}

	public int getSouthOffset() {
		return southOffset;
	}

	public void setSouthOffset(int offset) {
		southOffset = offset;
	}

	public int getNorthOffset() {
		return northOffset;
	}

	public void setNorthOffset(int offset) {
		northOffset = offset;
	}

	public Point[][] getPointsAsArray() {
		Point[][] ret = new Point[lats.length][lats[0].longitudes.length];
		for (int i=0;i<ret.length;i++){
			ret[i] = lats[i].longitudes;
		}

		return ret;
	}

	public static int getDefaultLatitudeSize() {
		return (int) ((180 - DEFAULT_NORTH_OFFSET - DEFAULT_SOUTH_OFFSET)* DEFAULT_SCALE);
	}

	public static int getDefaultLongitudeSize() {
		return (int) (360* DEFAULT_SCALE);
	}

	public int getLatitudeSize() {
		return (int) ((180 - getNorthOffset() - getSouthOffset())* getScale());
	}

	public int getLongitudeSize() {
		return (int) (360* getScale());
	}

	/**
	 * Returns a neat string with the world and route represented by:
	 * . (sea)
	 * X (land)
	 * S (start point)
	 * E (end point)
	 * O (route between start and end)
	 * TODO rewrite it so we use inner class toStrings with parameters.
	 * @param route
	 * @return
	 */
	public String toString(ASRoute route) {
		String s="";

		List<Point> travelRoute = null;
		Point end = route.points.get(0);
		Point start = route.points.get(route.points.size()-1);

		if (route.points.size() > 2) {
			travelRoute = route.points.subList(1,route.points.size()-1);
		}

		for (int i=0;i<lats.length;i++) {
			for (int j=0;j<lats[i].longitudes.length;j++) {
				Point point = lats[i].longitudes[j];
				if (point == null) {
					s +="X";
				} else if (start.equals(point)) {
					s+="S";
				} else if (end.equals(point)) {
					s+="E";
				} else if (travelRoute!=null && travelRoute.contains(point)) {
					s+="O";
				} else {
					s+=".";
				}
			}
			s+="\n";
		}

		return s;
	}

	public String toString() {
		String s="";

		for (int i=0;i<lats.length;i++) {
			s+=lats[i].toString() + "\n";
		}

		return s;
	}

	/**
	 * Contains an array of Points which represent longitudes. Mainly used for persistence purposes.
	 */
	public static class LatitudeArr {
		@ElementArray(empty = true)
		public Point[] longitudes;

		public LatitudeArr(Point[] points) {
			longitudes = points;
		}

		public LatitudeArr() {}

		public LatitudeArr(int longitudeSize) {
			longitudes = new Point[longitudeSize];
		}

		public String toString() {
			String s="";

			for (Point p: longitudes) {
				if (p==null) {
					s+="x";
				} else {
					s+=".";
				}
			}

			return s;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			LatitudeArr that = (LatitudeArr) o;

			if (!Arrays.equals(longitudes, that.longitudes)) return false;

			return true;
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		NewWorld newWorld = (NewWorld) o;

		if (!Arrays.equals(lats, newWorld.lats)) return false;

		return true;
	}
}
