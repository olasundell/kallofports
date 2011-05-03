package kop.map;

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
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 5/2/11
 * Time: 5:01 PM
 * To change this template use File | Settings | File Templates.
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

	protected static class Point {
		LatLong latitude;
		LatLong longitude;

		public Point(LatLong latitude, LatLong longitude) {
			this.latitude = latitude;
			this.longitude = longitude;
		}
	}

	public static Route getRoute(String from, String to, boolean panama, boolean suez) throws URISyntaxException, IOException {
		Route route = new Route();
		String fileName = from+"-"+to+"-suez_" + suez + "-panama_"+panama+".json";
		InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("routes/" + from.substring(0, 2) + "/" + fileName);
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		String jsonstr = reader.readLine();

		JSONObject obj = (JSONObject) JSONValue.parse(jsonstr);

		route.setFrom(from);
		route.setTo(to);
		route.setNm(Double.parseDouble((String) obj.get("totalmiles")));
		List<Point> points = new ArrayList<Point>();
		JSONArray positions = (JSONArray) obj.get("positions");

		for (int i=0; i < positions.size();i++) {
			JSONArray array = (JSONArray) positions.get(i);
			LatLong latitude = new LatLong((String) array.get(0), LatLong.Direction.latitude);
			LatLong longitude = new LatLong((String) array.get(1), LatLong.Direction.longitude);
			points.add(new Point(latitude, longitude));
		}

		route.setPoints(points);

		return route;
	}
}
