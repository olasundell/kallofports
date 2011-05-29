package kop.map.routecalculator;

import kop.game.Game;
import kop.ports.NoSuchPortException;
import kop.ports.Port;
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
	private static NewWorld smallWorld;

	@BeforeClass
	public static void init() {
		smallWorld = NewWorld.getWorld((float) 0.5);
	}

	@Before
	public void setup() {
		aStarUtil = new AStarUtil();
//		world = RouteCalculatorTest.getSmallWorld();
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
	public void aStar() {
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
	public void findClosestPoint() throws NoSuchPortException {
		Point p = aStarUtil.findClosestPointForPort(Game.getInstance().getPortByName("Aberdeen").getProxy(),
				smallWorld);
		assertNotNull(p);

		p = aStarUtil.findClosestPointForPort(Game.getInstance().getPortByName("Gothenburg").getProxy(),
				smallWorld);
		assertNotNull(p);

		p = aStarUtil.findClosestPoint(AStarUtil.panamaPacific, smallWorld);
		assertNotNull(p);
	}

	@Test
	public void findRouteThroughCanal() throws NoSuchPortException {
		Point start = aStarUtil.findClosestPointForPort(Game.getInstance().getPortByName("Barcelona").getProxy(),
				smallWorld);
		Point goal = aStarUtil.findClosestPointForPort(Game.getInstance().getPortByName("Durban").getProxy(),
				smallWorld);

		ASRoute route = aStarUtil.findRouteThroughSuezCanal(goal, start, smallWorld);
		assertNotNull(route);
	}

	// this test is broken, needs to be fixed.
//	@Test
	public void findRouteBetweenTwoPorts() throws NoSuchPortException {
		PortProxy start = Game.getInstance().getPortByName("Gothenburg").getProxy();
		PortProxy goal = Game.getInstance().getPortByName("Rio de Janeiro").getProxy();

		ASDistance distance = aStarUtil.aStar(start, goal, smallWorld);

		assertNotNull(distance);
		assertEquals((float) goal.getLatitude(), distance.shortestRoute().points.get(0).getCoord().getLatitude());
	}
}
