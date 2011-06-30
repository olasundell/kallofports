package kop.map.routecalculator;

import kop.game.Game;
import kop.ports.NoRouteFoundException;
import kop.ports.NoSuchPortException;
import kop.ports.PortProxy;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.*;

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

	@Before
	public void setup() {
		aStarUtil = new AStarUtil();
		world = Util.getBlankWorld();
	}

	@Test
	public void getLowestFShouldWork() {
		ArrayList<Point> list = new ArrayList<Point>();

		for (int i=0;i<9;i++) {
			list.add(world.lats[((int) Math.floor(i / 3))].longitudes[i%3]);
		}

		list.remove(4);

		Point ret = aStarUtil.getLowestF(list, world.lats[1].longitudes[1], world.lats[3].longitudes[3]);
		assertNotNull(ret);
		assertEquals(list.get(7), ret);

		ret = aStarUtil.getLowestF(list, world.lats[1].longitudes[1], world.lats[3].longitudes[0]);
		assertNotNull(ret);
		// why not list.get(5)? Because the differences in distance between latitudes and longitudes!
		assertEquals(list.get(5), ret);
	}


	@Test
	public void aStar() throws NoRouteFoundException {
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

		for (int i=5;i<world.lats.length-1;i++) {
			world.lats[i].longitudes[world.lats[i].longitudes.length-1] = null;
		}

		Point start = world.lats[15].longitudes[30];
//		Point goal = world.lats[1].longitudes[1];
		Point goal = world.lats[10].longitudes[5];

		ASRoute route = aStarUtil.aStar(start,goal,world);
		assertNotNull(route);
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
			findClosestPointForWorld(getSmallWorld());
			findClosestPointForWorld(getRealWorld());
		} catch (CouldNotFindPointException e) {
			System.err.println(getSmallWorld().toString());
			throw e;
		}

	}

	private void findClosestPointForWorld(NewWorld world) throws CouldNotFindPointException, NoSuchPortException {
		Point p;
		p = aStarUtil.findClosestPointForPort(Game.getInstance().getPortByName("Aberdeen").getProxy(),
				world);
		assertNotNull(p);

		p = aStarUtil.findClosestPointForPort(Game.getInstance().getPortByName("Gothenburg").getProxy(),
				world);
		assertNotNull(p);

		p = aStarUtil.findClosestPoint(AStarUtil.panamaPacific, world);
		assertNotNull(p);
	}

//	@Test
	// TODO this test fails miserably and needs to be fixed urgently,
	// it indicates that something is horribly wrong in the routing code.
	public void findRouteThroughCanal() throws NoSuchPortException, CouldNotFindPointException, NoRouteFoundException {
		Point start = aStarUtil.findClosestPointForPort(Game.getInstance().getPortByName("Barcelona").getProxy(),
				getRealWorld());
		Point goal = aStarUtil.findClosestPointForPort(Game.getInstance().getPortByName("Durban").getProxy(),
				getRealWorld());

		ASRoute route = aStarUtil.findRouteThroughSuezCanal(goal, start, getRealWorld());
		assertNotNull(route);
	}

	// this test is broken, needs to be fixed.
//	@Test
	public void findRouteBetweenTwoPorts() throws NoSuchPortException, NoRouteFoundException, CouldNotFindPointException {
		PortProxy start = Game.getInstance().getPortByName("Gothenburg").getProxy();
		PortProxy goal = Game.getInstance().getPortByName("Rio de Janeiro").getProxy();

		ASDistance distance = aStarUtil.aStar(start, goal, getSmallWorld());

		assertNotNull(distance);
		assertEquals((float) goal.getLatitude(), distance.shortestRoute().getPoints().get(0).getCoord().getLatitude());
	}
}
