package kop.map.routecalculator;

import kop.game.Game;
import kop.ports.NoRouteFoundException;
import kop.ports.NoSuchPortException;
import kop.ports.PortProxy;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.*;

import static org.testng.AssertJUnit.*;


/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 5/17/11
 * Time: 7:50 AM
 * To change this template use File | Settings | File Templates.
 */
public class AStarUtilTest {
	private AStarUtil aStarUtil;

	private NewWorld world;
	private static transient NewWorld smallWorld;
	private static transient NewWorld realWorld;

	@BeforeClass
	public static void init() {
	}

	private static NewWorld getSmallWorld() {
		if (smallWorld == null) {
			smallWorld = NewWorld.getWorld((float) 0.5);
		}

		return smallWorld;
	}

	private static NewWorld getRealWorld() {
		if (realWorld == null) {
			realWorld = Game.getInstance().getWorld();
		}

		return realWorld;
	}

	@BeforeMethod
	public void setup() {
		aStarUtil = new AStarUtil();
		world = Util.getBlankWorld();
	}

	@Test
	public void getLowestFShouldWork() {
		ArrayList<Point> list = new ArrayList<Point>();
//		HashSet<Point> list = new HashSet<Point>();
		Point first = null, second = null;

		for (int i=0;i<9;i++) {
			Point point = world.lats[((int) Math.floor(i / 3))].longitudes[i % 3];
			list.add(point);
			if (i == 8) {
				first = point;
			} else  if (i == 6) {
				second = point;
			}
		}

		// starting point.
		Point current = world.lats[1].longitudes[1];

		list.remove(current);

		HashSet<Point> set = new HashSet<Point>();
		set.addAll(list);

		Point ret = aStarUtil.getLowestF(set, current, world.lats[3].longitudes[3]);
		assertNotNull(ret);
		assertEquals(first, ret);

		ret = aStarUtil.getLowestF(set, current, world.lats[3].longitudes[0]);
		assertNotNull(ret);
		// why not list.get(5)? Because the differences in distance between latitudes and longitudes!
		assertEquals(second, ret);
	}


	@Test
	public void aStar() throws NoRouteFoundException, CouldNotFindPointException {
		NewWorld world = Util.getBlankWorld(0.1,0,0);
		for (int i=0;i<world.lats[2].longitudes.length;i++) {
			if (i==1) {
				continue;
			}
			world.lats[2].longitudes[i] = null;
		}

		for (int i=0;i<world.lats[4].longitudes.length;i++) {
			if (i==7) {
				continue;
			}
			world.lats[4].longitudes[i] = null;
		}

		for (int j=5;j<13;j++) {
			for (int i=8;i<world.lats[j].longitudes.length - (j*2 - 5);i++) {
				world.lats[j].longitudes[i] = null;
			}
		}

		for (int i=5;i<world.lats.length;i++) {
			world.lats[i].longitudes[world.lats[i].longitudes.length-1] = null;
		}

		Point start = world.lats[15].longitudes[30];
//		Point goal = world.lats[1].longitudes[1];
//		Point goal = world.lats[10].longitudes[5];
		Point goal = world.lats[14].longitudes[18];

		ASRoute route = aStarUtil.aStar(start,goal,world);
		assertRoute(start, goal, route);
		assertNotSame(0, route.getNumberOfPoints());
		float totalDistance = route.getTotalDistance();
		assertTrue(totalDistance > 0);
		String s = world.toString(route);
		System.out.println(s);

	}

	@Test
	public void findClosestPoint() throws NoSuchPortException, CouldNotFindPointException {
		Point p = null;
		try {
//			findClosestPointForWorld(getSmallWorld());
			findClosestPointForWorld(getRealWorld());
		} catch (CouldNotFindPointException e) {
			System.err.println(getSmallWorld().toString());
			throw e;
		}
	}

	private void findClosestPointForWorld(NewWorld world) throws CouldNotFindPointException, NoSuchPortException {
		Point p;
		p = aStarUtil.findClosestPointForPort(Game.getInstance().getPortByName("New York").getProxy(),
				world);
		assertNotNull(p);

		p = aStarUtil.findClosestPointForPort(Game.getInstance().getPortByName("Singapore").getProxy(),
				world);
		assertNotNull(p);

		p = aStarUtil.findClosestPoint(AStarUtil.panamaPacific, world);
		assertNotNull(p);
		Point p2 = aStarUtil.findClosestPoint(AStarUtil.panamaAtlantic, world);
		assertNotNull(p2);
		assertNotSame("Points for Panama channel entrances are the same",p,p2);

		p = aStarUtil.findClosestPoint(AStarUtil.suezIndianOcean, world);
		assertNotNull(p);
		p2 = aStarUtil.findClosestPoint(AStarUtil.suezMediterranean, world);
		assertNotNull(p2);
		assertNotSame("Points for Suez channel entrances are the same",p,p2);
	}

	@Test
	public void findRouteBetweenTwoPorts() throws NoSuchPortException, NoRouteFoundException, CouldNotFindPointException {
		PortProxy start = Game.getInstance().getPortByName("Durban").getProxy();
		PortProxy goal = Game.getInstance().getPortByName("Jeddah").getProxy();

		ASDistance distance = aStarUtil.aStar(start, goal, getRealWorld());

		assertDistance(start.getPosition(),  goal.getPosition(), distance);

		ASDistance cachedDistance = aStarUtil.getCachedDistance(start, goal);

		assertNotNull(cachedDistance);
		assertEquals(distance, cachedDistance);
	}

	@Test
	public void findRouteBetweenPointAndCanalEntrance() throws NoSuchPortException, NoRouteFoundException, CouldNotFindPointException {
//		PortProxy start = Game.getInstance().getPortByName("Jeddah").getProxy();
		Point goal = AStarUtil.suezIndianOcean;

//		Point p = new Point(24.5, 36.0);
		Point p = new Point(25.0, 35.5);
		Point closestStartPoint = aStarUtil.findClosestPoint(p, getRealWorld());
		Point closestGoalPoint = aStarUtil.findClosestPoint(goal, getRealWorld());
		ASRoute route = aStarUtil.aStar(closestStartPoint, closestGoalPoint, getRealWorld());
//		assertRoute(start.getPosition(), goal, route);
		assertRoute(closestStartPoint, closestGoalPoint, route);
	}

	// TODO one of these days we will fix the bugs in the A* algorithm. We'll use this test to verify when the day comes.
//	@Test
	public void previouslyFailingRouteTests() throws CouldNotFindPointException, NoRouteFoundException {
//		No route could be found between lat 9,500000 lon -79,000000 x 121 y 202 and lat 41,000000 lon 1,500000 x 58 y 363
		NewWorld world = getRealWorld();
		ASRoute route = aStarUtil.aStar(world.lats[121].longitudes[202], world.lats[58].longitudes[363], world);
		assertNotNull(route);
	}

	@Test
	public void findRouteBetweenTwoPortsThroughCanal() throws NoSuchPortException, CouldNotFindPointException, NoRouteFoundException {
		Point start = aStarUtil.findClosestPointForPort(Game.getInstance().getPortByName("Barcelona").getProxy(),
				getRealWorld());
		Point goal = aStarUtil.findClosestPointForPort(Game.getInstance().getPortByName("Durban").getProxy(),
				getRealWorld());

		ASRoute suezRoute = aStarUtil.findRouteThroughSuezCanal(start, goal, getRealWorld());
		assertRoute(start, goal, suezRoute);

		ASRoute panamaRoute = aStarUtil.findRouteThroughPanamaCanal(start, goal, getRealWorld());
		assertRoute(start, goal, panamaRoute);
	}

	@Test
	public void bayWorldRoute() throws CouldNotFindPointException, NoRouteFoundException {
		NewWorld world = Util.getBayWorld();
		ASRoute route = aStarUtil.aStar(world.lats[20].longitudes[11], world.lats[1].longitudes[11],world);
		assertNotNull(route);
		System.out.println(world.toString(route));
		route = aStarUtil.aStar(world.lats[21].longitudes[11], world.lats[1].longitudes[11],world);
		assertNotNull(route);
		System.out.println(world.toString(route));
	}

	@Test
	public void priorityQueue() {
		PriorityQueue<Point> queue = new PriorityQueue<Point>(1, new Comparator<Point>() {
			@Override
			public int compare(Point o1, Point o2) {
				return (int) (o1.getTotalCostIncludingDistance() - o2.getTotalCostIncludingDistance());
			}
		});
		queue.add(new Point());
	}

	private void assertRoute(Point start, Point goal, ASRoute route) {
		assertNotNull(route);
		List<Point> points = route.getPoints();
		assertEquals(goal.getLat(), points.get(0).getLat());
		assertEquals(goal.getLon(), points.get(0).getLon());

		assertEquals(start.getLat(), points.get(points.size() - 1).getLat());
		assertEquals(start.getLon(), points.get(points.size() - 1).getLon());
	}

	private void assertDistance(Point start, Point goal, ASDistance distance) {
		assertNotNull(distance);
		assertRoute(start, goal, distance.shortestRoute());
	}
}
