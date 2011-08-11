package kop.map.routecalculator;

import com.vividsolutions.jts.geom.prep.PreparedPoint;
import kop.ports.NoRouteFoundException;
import kop.ports.Port;

import java.lang.annotation.RetentionPolicy;
import java.util.*;

import kop.ports.PortProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Used to calculate routes with the A* algorithm.
 * TODO this class is currently very optimisable.
 * TODO this class returns routes which are goal-first, start-last. Not quite what you'd expect.
 */
public class AStarUtil {
	public final static Point suezMediterranean = new Point(31.28,32.34);
	public final static Point suezIndianOcean = new Point(29.93,32.56);
	public final static Point panamaAtlantic = new Point(9.31,-79.92);
	public final static Point panamaPacific = new Point(8.93,-79.56);
	public static HashMap<CompoundPortKey, ASDistance> cachedDistances = new HashMap<CompoundPortKey, ASDistance>();
	private static final int MAX_ASTAR_ITERATIONS = 1000000;

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
	protected ASRoute aStar(int latStart, int lonStart, int latGoal, int lonGoal, NewWorld world) throws NoRouteFoundException, CouldNotFindPointException {
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
	public ASDistance aStar(PortProxy start, PortProxy goal, NewWorld world) throws NoRouteFoundException, CouldNotFindPointException {
		ASDistance cachedDistance = getCachedDistance(start, goal);
		if (cachedDistance != null) {
			return cachedDistance;
		}

		// these are approximations
		Point closestStartPoint;
		Point closestGoalPoint = null;
		try {
			closestStartPoint = findClosestPointForPort(start, world);
			closestGoalPoint = findClosestPointForPort(goal, world);
		} catch (CouldNotFindPointException e) {
			throw new NoRouteFoundException(e);
		}
		Point startPoint = start.getPosition();
		Point goalPoint = goal.getPosition();


		if (closestStartPoint == null) {
			throw new CouldNotFindPointException(String.format("Cannot find starting point for port %s", closestStartPoint));
		}

		if (closestGoalPoint == null) {
			throw new CouldNotFindPointException(String.format("Cannot find end point for port %s", goal));
		}


		// first, the direct route.
		ASRoute route = aStar(closestStartPoint, closestGoalPoint, world);
		addStartAndGoalToRoute(startPoint, goalPoint, route);

		ASDistance distance = new ASDistance(start, goal);
		distance.addRoute(route);

		// then suez
		// TODO disabled. Canal routing obviously needs more work.
		try {
			route = findRouteThroughSuezCanal(closestStartPoint, closestGoalPoint, world);
			addStartAndGoalToRoute(startPoint, goalPoint, route);

			distance.addRoute(route);
		} catch (NoRouteFoundException e) {
			logger.debug("Failed to find route through Suez", e);
		}

//		// then panama
		try {
			route = findRouteThroughPanamaCanal(closestStartPoint, closestGoalPoint, world);
			addStartAndGoalToRoute(startPoint, goalPoint, route);

			distance.addRoute(route);
		} catch (NoRouteFoundException e) {
			logger.debug("Failed to find route through Panama", e);
		}

		cachedDistances.put(new CompoundPortKey(start, goal), distance);

		return distance;
	}

	/**
	 * Finds route through the suez canal. Hopefully.
	 * @param closestStartPoint
	 * @param closestGoalPoint
	 * @param world
	 * @return
	 */

	protected ASRoute findRouteThroughSuezCanal(Point closestStartPoint, Point closestGoalPoint, NewWorld world) throws NoRouteFoundException {
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

	protected ASRoute findRouteThroughPanamaCanal(Point closestStartPoint, Point closestGoalPoint, NewWorld world) throws NoRouteFoundException {
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
										  NewWorld world) throws NoRouteFoundException {
		ASRoute route = null;
		// TODO these points could be static and predefined, thus saving us a bit of time.
		Point closestFirstEntrance = null;
		Point closestSecondEntrance = null;
		try {
			closestFirstEntrance = findClosestPoint(firstCanalEntrance, world);
			closestSecondEntrance = findClosestPoint(secondCanalEntrance, world);
		} catch (CouldNotFindPointException e) {
			throw new NoRouteFoundException(e);
		}

		try {
			// TODO this could be refactored to more generalised code.
			if (closestStartPoint.distance(closestSecondEntrance) < closestStartPoint.distance(closestFirstEntrance)) {
				route = aStar(closestStartPoint, closestSecondEntrance, world);

//				route.addPointFirst(secondCanalEntrance);
//				route.addPointFirst(firstCanalEntrance);

				route.addRoute(aStar(closestFirstEntrance, closestGoalPoint, world));
			} else {
				route = aStar(closestStartPoint, closestFirstEntrance, world);

//				route.addPointFirst(firstCanalEntrance);
//				route.addPointFirst(secondCanalEntrance);

				route.addRoute(aStar(closestSecondEntrance, closestGoalPoint, world));
			}
		} catch (CouldNotFindPointException e) {
			logger.error("Could not find start/goal point when trying to find a route through a canal. This should not happen!",e);
		}

		return route;
	}

	/**
	 * Finds the closest Point approximation for a Port.
	 * @param port
	 * @param world
	 * @return
	 */
	protected Point findClosestPointForPort(PortProxy port, NewWorld world) throws CouldNotFindPointException {
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
	protected Point findClosestPoint(Point p, NewWorld world) throws CouldNotFindPointException {
		int i = world.reverseLat(p.getLat());
		int j = world.reverseLon(p.getLon());

		Point point = world.lats[i].longitudes[j];

		if (point == null) {
			Point point1 = new Point(i, j,
					p.getLat(),
					p.getLon());
			List<Point> neighbours = point1.getNeighbours(world);
			if (neighbours.size() == 0) {
				throw new IndexOutOfBoundsException("Could not find neighbours for "+p.toString());
			}
//			point = getLowestF(neighbours, point1, null);
			Point nearest=neighbours.get(0);
			for (Point n: neighbours) {
				if (nearest==null) {
					nearest = n;
				}

				if (n!=null && n.distance(p) > nearest.distance(p)) {
					nearest = n;
				}
			}
			point = nearest;
		}

		if (point == null) {
			throw new CouldNotFindPointException("Couldn't find closest point for "+p.toString());
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

//	protected Point getLowestF(List<Point> openset, Point current, Point goal) {
	protected Point getLowestF(Set<Point> openset, Point current, Point goal) {
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
	 * @param origStart starting point
	 * @param origGoal goal point
	 * @param world world to do some SPFing in.
	 * @return the shortest route in the world given start and goal - hopefully...
	 * @throws kop.ports.NoRouteFoundException whenever a route isn't found.
	 * TODO optimise, optimise, optimise!
	 */
	public ASRoute aStar(Point origStart, Point origGoal, NewWorld world) throws NoRouteFoundException, CouldNotFindPointException {
		HashSet<Point> closedset = new HashSet<Point>();

		// something befouls our world! Probably a test.
		world.clean();

		Point start = findClosestPoint(origStart, world);
		Point goal = findClosestPoint(origGoal, world);

		OpenSet openset = new OpenSet(goal);

		openset.add(start);
		long tries = 0;


		while (openset.size() != 0) {
			if (tries == 2430) {
				int afaa=0;
			}

			Point currentPoint = openset.poll();

			// TODO this shouldn't be commented out, fix log4j settings.
//			logger.debug(world.toString(reconstructPath(currentPoint)));

			if (currentPoint.equals(goal)) {
				// TODO world cleaning is necessary
//				ASRoute route = reconstructPath(start, goal, origGoal);
				ASRoute route = reconstructPath(goal);
				world.clean();
				return route;
			}

//			openset.remove(currentPoint);
			closedset.add(currentPoint);

			List<Point> neighbours = currentPoint.getNeighbours(world);

			for (Point p: neighbours) {
				if (p == null || closedset.contains(p)) {
					continue;
				}

				if (!openset.contains(p)) {
					p.setParent(currentPoint);
					openset.add(p);
				} else if ((currentPoint.getTotalCost() + currentPoint.distance(p)) < p.getTotalCost()) {
					openset.remove(p);
					p.setParent(currentPoint);
				}
			}

			tries++;
			if (tries % 5000 == 0) {
				logger.debug(String.format("Number of tries: %d",tries));
			}

			if (tries > MAX_ASTAR_ITERATIONS) {
				// something went wrong. Horribly wrong.
				throw new NoRouteFoundException(
						String.format("Maximum number of attempts (%d) exceeded, no route could be found between %s and %s",
								MAX_ASTAR_ITERATIONS, start, goal));
			}
		}

		throw new NoRouteFoundException(String.format("No route could be found between %s and %s", start, origGoal));
	}

	/**
	 * Constructs an ASRoute from the points.
	 * @param cameFrom
	 * @return an ASRoute which is mirrored (ie goal first)
	 * TODO reverse the ASRoute
	 * TODO add start and goal here
	 */
//	protected ASRoute reconstructPath(Point start, Point cameFrom, Point origGoal) {
	protected ASRoute reconstructPath(Point cameFrom) {
		ASRoute route = new ASRoute();

		Point current = cameFrom;
		Point parent;

		while ((parent = current.getParent())!=null) {
			route.addPoint((Point) current.clone());
			current = parent;
		}

		route.addPoint((Point) current.clone());

		for (int i=0;i<route.getNumberOfPoints()-1;i++) {
			route.getPoints().get(i).setParent(route.getPoints().get(i+1));
		}

		route.getPoints().get(route.getNumberOfPoints()-1).resetParent();

		return route;
	}

	public ASDistance getCachedDistance(PortProxy start, PortProxy goal) {
		CompoundPortKey key = new CompoundPortKey(start, goal);
		if (cachedDistances.containsKey(key)) {
			return cachedDistances.get(key);
		}

		return null;
	}

	private static class CompoundPortKey {
		private PortProxy start;
		private PortProxy goal;

		CompoundPortKey(PortProxy start, PortProxy goal) {
			this.start = start;
			this.goal = goal;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}

			CompoundPortKey that = (CompoundPortKey) o;

			if (goal != null ? !goal.equals(that.goal) : that.goal != null) {
				return false;
			}

			if (start != null ? !start.equals(that.start) : that.start != null) {
				return false;
			}

			return true;
		}

		@Override
		public int hashCode() {
			int result = start != null ? start.hashCode() : 0;
			result = 31 * result + (goal != null ? goal.hashCode() : 0);
			return result;
		}
	}

	/**
	 * This class uses a HashSet to keep track of contains() and a PriorityQueue to keep track of ordering.
	 * The best of two worlds. :-)
	 * It contains quite a bit of test code and assertions to check if we run into problems with the A* algorithm.
	 */

	private static class OpenSet {
		HashSet<Point> hashSet;
		PriorityQueue<Point> queue;
		Point goal;
		List<Point> removedFromQueue;
		List<Point> removedFromSet;

		public OpenSet(Point goal) {
			removedFromQueue = new ArrayList<Point>();
			removedFromSet = new ArrayList<Point>();

			hashSet = new HashSet<Point>();
			queue = new PriorityQueue<Point>(1, new Comparator<Point>() {
				@Override
				public int compare(Point o1, Point o2) {
					return (int) (o1.getTotalCostIncludingDistance() * 100 - o2.getTotalCostIncludingDistance() * 100);
				}
			});
			this.goal = goal;
		}

		public void add(Point p) {
			p.setGoalDistance(goal);
			boolean s = hashSet.add(p);
			boolean q = queue.add(p);

			if (s ^ q) {
				throw new AssertionError(String.format("Could not add %s",p));
			}
		}

		public int size() {
			if (queue.size() != hashSet.size()) {
				// test code used if we get problems with the A* algorithm.
//				ArrayList<Point> diff1 = new ArrayList<Point>();
//				ArrayList<Point> diff2 = new ArrayList<Point>();
//				Iterator<Point> iter = queue.iterator();
//				while (iter.hasNext()) {
//					Point p = iter.next();
//					if (!hashSet.contains(p)) {
//						diff1.add(p);
//					}
//				}
//
//				iter = hashSet.iterator();
//
//				while (iter.hasNext()) {
//					Point p = iter.next();
//					if (!queue.contains(p)) {
//						diff2.add(p);
//					}
//				}
				throw new AssertionError("Priority queue and hash set aren't the same size! Yikes!");
			}
			return hashSet.size();
		}

		public boolean contains(Point p) {
			return hashSet.contains(p);
		}

		public void remove(Point p) {
			boolean q = queue.remove(p);
			boolean s = hashSet.remove(p);
			if (s ^ q) {
				throw new AssertionError(String.format("Could not remove %s",p));
			}
			removedFromQueue.add(p);
			removedFromSet.add(p);
		}

		public Point poll() {
			Point p = queue.poll();
			if (p!=null && !hashSet.contains(p)) {
				throw new AssertionError(String.format("Could not remove %s",p));
			}

			hashSet.remove(p);

			return p;
		}
	}
}
