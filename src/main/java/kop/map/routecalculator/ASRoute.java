package kop.map.routecalculator;

import kop.ports.Port;

import java.util.ArrayList;

/**
* Created by IntelliJ IDEA.
* User: ola
* Date: 5/22/11
* Time: 3:47 PM
* To change this template use File | Settings | File Templates.
*/
public class ASRoute {
	private boolean suez;
	private boolean panama;

	ArrayList<Point> points;
	public ASRoute() {
		points = new ArrayList<Point>();
	}

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

	public void addPointFirst(Point point) {
		if (points.contains(point)) {
			throw new IllegalArgumentException("The point which you are trying to add is already in the list!");
		}

		point.setParent(points.get(0));
		points.add(0,point);
	}
}
