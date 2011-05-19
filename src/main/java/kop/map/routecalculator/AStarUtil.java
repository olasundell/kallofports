package kop.map.routecalculator;

import com.bbn.openmap.LatLonPoint;
import kop.map.Route;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 5/16/11
 * Time: 3:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class AStarUtil {
	public ASRoute aStar(int latStart, int lonStart, int latGoal, int lonGoal, Point[][] world) {
		Point start = world[latStart][lonStart];
		Point goal = world[latGoal][lonGoal];
		return aStar(start, goal, world);
	}

	public ASRoute aStar(Point start, Point goal, Point[][] world) {
		ArrayList<Point> closedset = new ArrayList<Point>();
		ArrayList<Point> openset = new ArrayList<Point>();
		openset.add(start);

		while (openset.size() != 0) {
			Point currentPoint = getLowestF(openset, start, goal);
			if (currentPoint.equals(goal)) {
				return reconstructPath(goal);
			}

			openset.remove(currentPoint);
			closedset.add(currentPoint);

			for (Point p: currentPoint.getNeighbours(world)) {
				if (p == null || closedset.contains(p)) {
					continue;
				}

				if (openset.contains(p)) {
//					if (currentPoint.getTotalCost() + currentPoint.distance(p) < p.getParentCost()) {
//					if (currentPoint.getTotalCost() < p.getTotalCost()) {
					if (currentPoint.getTotalCost() + currentPoint.distance(p) < p.getTotalCost()) {
						p.setParent(currentPoint);
					}
				} else {
					openset.add(p);
					p.setParent(currentPoint);
				}
			}
		}

		return null;
	}

	protected Point getLowestF(ArrayList<Point> openset, Point current, Point goal) {
		double lowestF=-1;
		double currentF;
		Point retp=null;

		for (Point p: openset) {
			currentF = p.distance(current) + p.distance(goal);
			if (retp == null || currentF < lowestF) {
				lowestF = currentF;
				retp = p;
			}
		}
		return retp;
	}

	protected ASRoute reconstructPath(Point cameFrom) {
		ASRoute route = new ASRoute();

		Point current = cameFrom;
		Point parent;

		while ((parent = current.getParent())!=null) {
			route.addPoint(current.getCoord());
			current = parent;
		}
		return route;
	}

	public static class ASRoute {
		ArrayList<LatLonPoint> points;
		public ASRoute() {
			points = new ArrayList<LatLonPoint>();
		}

		public void addPoint(LatLonPoint p) {
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
	}
}
