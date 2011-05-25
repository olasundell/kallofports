package kop.map.routecalculator;

import kop.ports.Port;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Used to calculate routes with the A* algorithm.
 * TODO this class is currently very optimisable.
 * TODO this class returns routes which are goal-first, start-last. Not quite what you'd expect.
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

	/**
	 * Used for testing purposes.
	 * @param latStart
	 * @param lonStart
	 * @param latGoal
	 * @param lonGoal
	 * @param world
	 * @return
	 */
	protected ASRoute aStar(int latStart, int lonStart, int latGoal, int lonGoal, NewWorld world) {
		Point start = world.lats[latStart].longitudes[lonStart];
		Point goal = world.lats[latGoal].longitudes[lonGoal];
		return aStar(start, goal, world);
	}

	/**
	 * Calculates an ASDistance between two ports, given a world.
	 * TODO optimise it by checking whether the ShipModel instance can use the canals at all
	 * TODO if the direct ASRoute is shorter than the haversine distance to either canals we don't need to do canal routes.
	 * TODO insert smarter logical checks to avoid unnecessary calculations here.
	 * @param start Starting port
	 * @param goal Goal port
	 * @param world The world to route in.
	 * @return an ASDistance with routes via both canals and a direct route.
	 */
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

	/**
	 * Finds route through the suez canal. Hopefully.
	 * @param closestStartPoint
	 * @param closestGoalPoint
	 * @param world
	 * @return
	 */

	protected ASRoute findRouteThroughSuezCanal(Point closestStartPoint, Point closestGoalPoint, NewWorld world) {
		ASRoute route = findRouteThroughCanal(closestStartPoint, closestGoalPoint, suezMediterranean, suezIndianOcean, world);

		route.setPassesSuez(true);

		return route;
	}

	/**
	 * Finds route through the panama canal. Hopefully.
	 * @param closestStartPoint
	 * @param closestGoalPoint
	 * @param world
	 * @return
	 */

	protected ASRoute findRouteThroughPanamaCanal(Point closestStartPoint, Point closestGoalPoint, NewWorld world) {
		ASRoute route = findRouteThroughCanal(closestStartPoint, closestGoalPoint, panamaPacific, panamaAtlantic, world);

		route.setPassesPanama(true);

		return route;
	}

	/**
	 * The A* implementation doesn't add the starting and ending points to the route, we have to do it manually.
	 * @param startPoint
	 * @param goalPoint
	 * @param route
	 * TODO fix the above!
	 */

	private void addStartAndGoalToRoute(Point startPoint, Point goalPoint, ASRoute route) {
		route.addPointFirst(goalPoint);

		route.addPoint(startPoint);
	}

	/**
	 * Checks for the route through a generic canal and adds the canal points to the route.
	 * It will result in a haversine-straight canal, which (of course) isn't the case.
	 * @param closestStartPoint
	 * @param closestGoalPoint
	 * @param firstCanalEntrance
	 * @param secondCanalEntrance
	 * @param world
	 * @return
	 */
	private ASRoute findRouteThroughCanal(Point closestStartPoint,
										  Point closestGoalPoint,
										  Point firstCanalEntrance,
										  Point secondCanalEntrance,
										  NewWorld world) {
		ASRoute route;
		// TODO these points could be static and predefined, thus saving us a bit of time.
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

	/**
	 * Finds the closest Point approximation for a Port.
	 * @param port
	 * @param world
	 * @return
	 */
	protected Point findClosestPointForPort(Port port, NewWorld world) {
		return findClosestPoint(new Point(port), world);
	}

	/**
	 * Finds the closest Point approxiamtion which is in the provided world for a Point which isn't in the world.
	 * If the obvious choice (just round the given Point's lat/lon off and check if there's a Point in the world)
	 * doesn't succeed, then get the closest (ie lowest F) neighbour.
	 * @param p
	 * @param world
	 * @return
	 */
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

	/**
	 * Finds the lowest F (that is, G+H) Point in the open set and returns it.
	 * @param openset
	 * @param current
	 * @param goal
	 * @return
	 * TODO obviously we're not using Point current.
	 */

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

	/**
	 * This is what this class is about, ie the implementation of the shortest path first A* algorithm.
	 * Should NOT go through canals, but it SHOULD go through Gibraltar and the Malacca sound.
	 * @param start starting point
	 * @param goal goal point
	 * @param world world to do some SPFing in.
	 * @return the shortest route in the world given start and goal - hopefully...
	 * TODO optimise, optimise, optimise!
	 * FIXME contains a bug which isn't the fault of the implementation, apparently the world isn't fine-grained enough so the Panama strip of land connecting NA and SA isn't there.
	 */
	public ASRoute aStar(Point start, Point goal, NewWorld world) {
		ArrayList<Point> closedset = new ArrayList<Point>();
		ArrayList<Point> openset = new ArrayList<Point>();
		openset.add(start);

		while (openset.size() != 0) {
			Point currentPoint = getLowestF(openset, start, goal);

			// TODO this shouldn't be commented out, fix log4j settings.
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

	/**
	 * Constructs an ASRoute from the points.
	 * @param start obviously not used.
	 * @param cameFrom
	 * @return an ASRoute which is mirrored (ie goal first)
	 * TODO reverse the ASRoute
	 * TODO add start and goal here
	 */
	protected ASRoute reconstructPath(Point start, Point cameFrom) {
		ASRoute route = new ASRoute();

		Point current = cameFrom;
		Point parent;

		while ((parent = current.getParent())!=null) {
			route.addPoint((Point) current.clone());
			current = parent;
			parent.resetParent();
		}

		// it isn't as easy as this, mate!
//		route.addPoint(start);
		route.addPoint(current);
		current.resetParent();

		return route;
	}

}
