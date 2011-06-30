package kop.map.routecalculator;

import kop.ports.Port;

import java.util.ArrayList;
import java.util.List;

/**
 *  Contains the route Points between two destinations, keeps track of whether the ship passes either major canal.
 *  The points are ordered so that the first element is the parent of the second, etc.
 *  Parentage is used in the A* algorithm.
 */
public class ASRoute {
	private boolean suez;
	private boolean panama;

	private List<Point> points;

	public ASRoute() {
		points = new ArrayList<Point>();
	}

	/**
	 * Adds point to the route and sets the point's parent accordingly.
	 * @param p
	 */
	public void addPoint(Point p) {
		if (points.contains(p)) {
			throw new IllegalArgumentException("The point which you are trying to add is already in the list!");
		}
		if (points.size() > 0) {
			points.get(points.size()-1).setParent(p);
		}
		points.add(p);
	}

	public int getNumberOfPoints() {
		return points.size();
	}

	public float getTotalDistance() {
		float d=0;
		for (int i=0;i<points.size()-1;i++) {
			d+=points.get(i).distance(points.get(i+1));
		}
		return d;
	}

	public boolean passesPanama() {
		return panama;  //To change body of created methods use File | Settings | File Templates.
	}

	public boolean passesSuez() {
		return suez;  //To change body of created methods use File | Settings | File Templates.
	}

	public void addRoute(ASRoute asRoute) {
		points.addAll(asRoute.points);
	}

	public void setPassesPanama(boolean b) {
		panama = true;
	}

	public void setPassesSuez(boolean b) {
		suez = true;
	}

	/**
	 * Adds point to the beginning of the list, adjusts parents accordingly.
	 * @param point
	 */
	public void addPointFirst(Point point) {
		if (points.contains(point)) {
			throw new IllegalArgumentException("The point which you are trying to add is already in the list!");
		}

		point.setParent(points.get(0));
		points.add(0,point);
	}

	public List<Point> getPoints() {
		return points;
	}
}
