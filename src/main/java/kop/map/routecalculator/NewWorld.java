package kop.map.routecalculator;

import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
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
	public static final String SHAPE_FILENAME = "data/shape/10m_ocean.shp";

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

	private Logger logger;

	@ElementArray(empty = true)
	LatitudeArr[] lats;
	private WaterVerifier waterVerifier;

	public NewWorld() {
		logger = LoggerFactory.getLogger(this.getClass());
	}

	public NewWorld(Point[][] points) {
		this();

		lats = new LatitudeArr[points.length];

		for (int i=0;i<lats.length;i++) {
			lats[i] = new LatitudeArr(points[i]);
		}
	}

	public NewWorld(int latitudeSize, int longitudeSize, WaterVerifier waterVerifier) {
		this();
		setScale((float) (longitudeSize/360.0));
		setWaterVerifier(waterVerifier);

		lats = new LatitudeArr[latitudeSize];

		for (int i=0;i<lats.length;i++) {
			lats[i] = new LatitudeArr(longitudeSize);
		}
	}

	public NewWorld(int latitudeSize, int longitudeSize) {
		this(latitudeSize, longitudeSize, getDefaultWaterVerifier());
	}

	private void setWaterVerifier(WaterVerifier newWaterVerifier) {
		waterVerifier = newWaterVerifier;
		waterVerifier.setupInstance(SHAPE_FILENAME);
	}

	public static NewWorld getWorld(float scale, WaterVerifier waterVerifier) {
		RouteCalculator calculator = new RouteCalculator();
		NewWorld newWorld = new NewWorld(Math.round(180*scale),Math.round(360*scale), waterVerifier);
		newWorld.setScale(scale);
		newWorld.setNorthOffset(0);
		newWorld.setSouthOffset(0);
		return calculator.calculateWorld(newWorld);
	}

	public static NewWorld getWorld(float scale) {
		return getWorld(scale, getDefaultWaterVerifier());
	}

	public static OpenMapWaterVerifier getDefaultWaterVerifier() {
		return new OpenMapWaterVerifier();
	}

	protected int reverseLat(double lat) {
		return (int) Math.round((lat - 90+northOffset)*scale*-1);
	}

	protected int reverseLon(double lon) {
		return (int) (Math.round((180+lon)*scale));
	}

	protected float calcLon(int j) {
		return (float) ((j / scale) - 180);
	}

	protected float calcLat(int i) {
		return (float) (90 - northOffset - (i / scale));
	}

	public double getScale() {
		return scale;
	}

	public final void setScale(double sc) {
		if (sc <= 0) {
			throw new IllegalArgumentException("Trying to set scale to zero or below will have funky divide by zero side effects. Is this wanted?");
		}

		scale = (float) sc;
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
		StringBuffer s = new StringBuffer();

		List<Point> travelRoute = null;

		List<Point> points = route.getPoints();
		Point end = points.get(0);
		Point start = points.get(points.size()-1);

		if (points.size() > 2) {
			travelRoute = points.subList(1,points.size()-1);
		}

		for (int i=0;i<lats.length;i++) {
			for (int j=0;j<lats[i].longitudes.length;j++) {
				Point point = lats[i].longitudes[j];
				if (point == null) {
					s.append('X');
				} else if (start.equals(point)) {
					s.append('S');
				} else if (end.equals(point)) {
					s.append('E');
				} else if (travelRoute!=null && travelRoute.contains(point)) {
					s.append('O');
				} else {
					s.append('.');
				}
			}
			s.append('\n');
		}

		return s.toString();
	}

	public String toString() {
		StringBuilder s = new StringBuilder();

		for (int i=0;i<lats.length;i++) {
			s.append(String.format("%s\n", lats[i].toString()));
		}

		return s.toString();
	}

	public WaterVerifier getWaterVerifier() {
		if (waterVerifier == null) {
			setWaterVerifier(getDefaultWaterVerifier());
		}
		return waterVerifier;
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
			this(new Point[longitudeSize]);
		}

		public String toString() {
			StringBuffer s = new StringBuffer();

			for (Point p: longitudes) {
				if (p==null) {
					s.append('x');
				} else {
					s.append('.');
				}
			}

			return s.toString();
		}

		@Override
		public int hashCode() {
			return longitudes != null ? Arrays.hashCode(longitudes) : 0;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) { return true; }
			if (o == null || getClass() != o.getClass()) { return false; }

			LatitudeArr that = (LatitudeArr) o;

			if (!Arrays.equals(longitudes, that.longitudes)) { return false; }

			return true;
		}
	}

	public static NewWorld readFromFile(String filename) {
		File file = new File(filename);
		NewWorld world = null;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			ArrayList<String> lines = new ArrayList<String>();
			String line;

			while ((line = reader.readLine())!=null) {
				lines.add(line);
			}

			reader.close();

			world = new NewWorld(lines.size(), lines.get(0).length());
			world.setScale(lines.size() / 180d);
			for (int i=0;i<lines.size();i++) {
				for (int j=0;j<lines.get(i).length();j++) {
					if (lines.get(i).charAt(j) == '.') {
						world.lats[i].longitudes[j] = new Point(i,j,world.calcLat(i),world.calcLon(j));
					} else {
						world.lats[i].longitudes[j] = null;
					}
				}
			}

		} catch (FileNotFoundException e) {
			Logger logger = LoggerFactory.getLogger(NewWorld.class);
			logger.error(String.format("File %s could not be found.", filename), e);
		} catch (IOException e) {
			Logger logger = LoggerFactory.getLogger(NewWorld.class);
			logger.error(String.format("Error reading from file %s", filename),e);
		}

		return world;
	}

	@Override
	public int hashCode() {
		int result = (scale != +0.0f ? Float.floatToIntBits((float) scale) : 0);
		result = 31 * result + southOffset;
		result = 31 * result + northOffset;
		result = 31 * result + (logger != null ? logger.hashCode() : 0);
		result = 31 * result + (lats != null ? Arrays.hashCode(lats) : 0);
		result = 31 * result + (waterVerifier != null ? waterVerifier.hashCode() : 0);
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) { return true; }
		if (o == null || getClass() != o.getClass()) { return false; }

		NewWorld newWorld = (NewWorld) o;

		if (!Arrays.equals(lats, newWorld.lats)) { return false; }

		return true;
	}
}
