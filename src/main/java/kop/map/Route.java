package kop.map;

import com.bbn.openmap.geo.GeoPoint;
import kop.ports.NoRouteFoundException;
import kop.ports.NoSuchPortException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @deprecated old route implementation, use ASRoute instead.
 * @see kop.map.routecalculator.ASRoute
 */
public class Route {
	private String from;
	private String to;
	private List<Point> points;
	private double nm;
	private boolean panama;
	private boolean suez;

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public List<Point> getPoints() {
		return points;
	}

	public void setPoints(List<Point> points) {
		this.points = points;
	}

	public double getNm() {
		return nm;
	}

	public void setNm(double nm) {
		this.nm = nm;
	}

	public boolean isPanama() {
		return panama;
	}

	public void setPanama(boolean panama) {
		this.panama = panama;
	}

	public boolean isSuez() {
		return suez;
	}

	public void setSuez(boolean suez) {
		this.suez = suez;
	}

	public double getDistanceBetweenPoints(int first, int second) {
		double radius = 6371.009;
		Point p1 = points.get(first);
		Point p2 = points.get(second);

		double lat1 = p1.getLatitude().getCoordinate();
		double lat2 = p2.getLatitude().getCoordinate();
		double lon1 = p1.getLongitude().getCoordinate();
		double lon2 = p2.getLongitude().getCoordinate();
		double dLat = Math.toRadians(lat2-lat1);
		double dLon = Math.toRadians(lon2-lon1);
		double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
				Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
						Math.sin(dLon/2) * Math.sin(dLon/2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

		return radius * c / 1.852;
	}

	public static class Point {
		private LatLong latitude;
		private LatLong longitude;

		public Point(LatLong latitude, LatLong longitude) {
			this.latitude = latitude;
			this.longitude = longitude;
		}

		public LatLong getLatitude() {
			return latitude;
		}

		public LatLong getLongitude() {
			return longitude;
		}
	}

	public static Route getRoute(String from, String to, boolean panama, boolean suez) throws NoRouteFoundException {
		Route route = new Route();

		// those pesky unlocodes contain a ' '
		String unlocodes = from.replaceAll(" ", "") + "-" + to.replaceAll(" ", "");

		String fileName = unlocodes +"-suez_" + suez + "-panama_"+panama+".json";
		String resourceName = "routes/" + from.substring(0, 2) + "/" + fileName;
		InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream(resourceName);

		if (stream == null) {
			throw new NoRouteFoundException(String.format("Resource %s wasn't found, hence no route information could be loaded.", resourceName));
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		String jsonstr = null;

		try {
			jsonstr = reader.readLine();
		} catch (IOException e) {
			throw new NoRouteFoundException("Could not retrieve route from file");
		}

		JSONObject obj = (JSONObject) JSONValue.parse(jsonstr);

		route.setFrom(from);
		route.setTo(to);
		route.setNm(Double.parseDouble((String) obj.get("totalmiles")));
		List<Point> points = new ArrayList<Point>();
		JSONArray positions = (JSONArray) obj.get("positions");

		for (int i=0; i < positions.size();i++) {
			JSONArray array = (JSONArray) positions.get(i);
			// mixed up, apparently.
			LatLong latitude = new LatLong((String) array.get(1), LatLong.Direction.latitude);
			LatLong longitude = new LatLong((String) array.get(0), LatLong.Direction.longitude);
			points.add(new Point(latitude, longitude));
		}

		route.setPoints(points);

		return route;
	}
}
