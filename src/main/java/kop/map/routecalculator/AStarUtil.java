package kop.map.routecalculator;

import kop.ports.Port;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 5/16/11
 * Time: 3:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class AStarUtil {
	public static Point suezMediterranean = new Point(31.28,32.34);
	public static Point suezIndianOcean = new Point(29.93,32.56);
	public static Point panamaAtlantic = new Point(9.31,-79.92);
	public static Point panamaPacific = new Point(8.93,-79.56);

	Logger logger;
	public AStarUtil() {
		logger = LoggerFactory.getLogger(this.getClass());
	}

	public ASRoute aStar(int latStart, int lonStart, int latGoal, int lonGoal, NewWorld world) {
		Point start = world.lats[latStart].longitudes[lonStart];
		Point goal = world.lats[latGoal].longitudes[lonGoal];
		return aStar(start, goal, world);
	}

	public ASDistance aStar(Port start, Port goal, NewWorld world) {
		// these are approximations
		Point closestStartPoint = findClosestPointForPort(start, world);
		Point closestGoalPoint = findClosestPointForPort(goal, world);
		Point startPoint = new Point(start);
		Point goalPoint = new Point(goal);

		ASDistance distance = new ASDistance(start, goal);

		if (closestStartPoint == null) {
			throw new NullPointerException("Cannot find starting point for port " + closestStartPoint);
		}

		if (closestGoalPoint == null) {
			throw new NullPointerException("Cannot find end point for port " + goal);
		}

		// TODO refactor this, the code is too lengthy and full of duplicates.

		// first, the direct route.
		ASRoute route = aStar(closestStartPoint, closestGoalPoint, world);
		addStartAndGoalToRoute(startPoint, goalPoint, route);

		distance.addRoute(route);

		// then suez
		route = findRouteThroughSuezCanal(closestStartPoint, closestGoalPoint, world);
		addStartAndGoalToRoute(startPoint, goalPoint, route);

		distance.addRoute(route);

		// then panama
		route = findRouteThroughPanamaCanal(closestStartPoint, closestGoalPoint, world);
		addStartAndGoalToRoute(startPoint, goalPoint, route);

		distance.addRoute(route);

		return distance;
	}

	protected ASRoute findRouteThroughSuezCanal(Point closestStartPoint, Point closestGoalPoint, NewWorld world) {
		ASRoute route = findRouteThroughCanal(closestStartPoint, closestGoalPoint, suezMediterranean, suezIndianOcean, world);

		route.setPassesSuez(true);

		return route;
	}

	protected ASRoute findRouteThroughPanamaCanal(Point closestStartPoint, Point closestGoalPoint, NewWorld world) {
		ASRoute route = findRouteThroughCanal(closestStartPoint, closestGoalPoint, panamaPacific, panamaAtlantic, world);

		route.setPassesPanama(true);

		return route;
	}

	private void addStartAndGoalToRoute(Point startPoint, Point goalPoint, ASRoute route) {
		route.addPointFirst(goalPoint);

		route.addPoint(startPoint);
	}

	private ASRoute findRouteThroughCanal(Point closestStartPoint,
										  Point closestGoalPoint,
										  Point firstCanalEntrance,
										  Point secondCanalEntrance,
										  NewWorld world) {
		ASRoute route;
		Point closestFirstEntrance = findClosestPoint(firstCanalEntrance, world);
		Point closestSecondEntrance = findClosestPoint(secondCanalEntrance, world);

		if (closestStartPoint.distance(closestSecondEntrance) < closestStartPoint.distance(closestFirstEntrance)) {
			route = aStar(closestStartPoint, closestSecondEntrance, world);

			route.addPointFirst(secondCanalEntrance);
			route.addPointFirst(firstCanalEntrance);

			route.addRoute(aStar(closestFirstEntrance, closestGoalPoint, world));
		} else {
			route = aStar(closestStartPoint, closestFirstEntrance, world);

			route.addPointFirst(firstCanalEntrance);
			route.addPointFirst(secondCanalEntrance);

			route.addRoute(aStar(closestSecondEntrance, closestGoalPoint, world));
		}

		return route;
	}

	protected Point findClosestPointForPort(Port port, NewWorld world) {
		return findClosestPoint(new Point(port), world);
	}

	protected Point findClosestPoint(Point p, NewWorld world) {
		int i = world.reverseLat(p.getLat());
		int j = world.reverseLon(p.getLon());

		Point point = world.lats[i].longitudes[j];

		if (point == null) {
			Point point1 = new Point(i, j,
					p.getLat(),
					p.getLon());
			List<Point> neighbours = point1.getNeighbours(world);
			point = getLowestF(neighbours, point1, null);
		}


		return point;
	}

	protected Point getLowestF(List<Point> openset, Point current, Point goal) {
		double lowestF=-1;
		double currentF;
		Point retp=null;

		for (Point p: openset) {
			if (p==null) {
				continue;
			}

//			currentF = p.distance(current);
			currentF = p.getTotalCost();
			if (goal!=null) {
				currentF+=p.distance(goal);
			}
			if (retp == null || currentF < lowestF) {
				lowestF = currentF;
				retp = p;
			}
		}
		return retp;
	}

	public ASRoute aStar(Point start, Point goal, NewWorld world) {
		ArrayList<Point> closedset = new ArrayList<Point>();
		ArrayList<Point> openset = new ArrayList<Point>();
		openset.add(start);

		while (openset.size() != 0) {
			Point currentPoint = getLowestF(openset, start, goal);

//			if (logger.isDebugEnabled()) {
//				logger.debug(world.toString(reconstructPath(start, currentPoint)));
//				if (world.getScale() < 1) {
//					try {
//						Thread.sleep(150);
//					} catch (InterruptedException e) {
//						e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//					}
//				}
//			}

			if (currentPoint.equals(goal)) {
				return reconstructPath(start, goal);
			}

			openset.remove(currentPoint);
			closedset.add(currentPoint);

			List<Point> neighbours = currentPoint.getNeighbours(world);

			for (Point p: neighbours) {
				if (p == null || closedset.contains(p)) {
					continue;
				}

				if (!openset.contains(p)) {
					openset.add(p);
					p.setParent(currentPoint);
				} else if (currentPoint.getTotalCost() + currentPoint.distance(p) < p.getTotalCost()) {
					openset.remove(p);
					p.setParent(currentPoint);
				}
			}
		}

		return null;
	}

	protected ASRoute reconstructPath(Point start, Point cameFrom) {
		ASRoute route = new ASRoute();

		Point current = cameFrom;
		Point parent;

		while ((parent = current.getParent())!=null) {
			route.addPoint((Point) current.clone());
			current = parent;
			parent.resetParent();
		}

//		route.addPoint(start);
		route.addPoint(current);
		current.resetParent();

		return route;
	}

}
