package kop.map.routecalculator;

import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.Root;

import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 5/20/11
 * Time: 5:34 PM
 * To change this template use File | Settings | File Templates.
 */
@Root
public class NewWorld {
	public static final int DEFAULT_SCALE = 2;
	public static final int DEFAULT_SOUTH_OFFSET = 20;
	public static final int DEFAULT_NORTH_OFFSET = 20;

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
