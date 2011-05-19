package kop.map.routecalculator;

import org.junit.Before;
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

	private Point[][] world;

	@Before
	public void setup() {
		aStarUtil = new AStarUtil();
		world = WorldTest.createWorld();
	}

	@Test
	public void getLowestFShouldWork() {
		ArrayList<Point> list = new ArrayList<Point>();

		for (int i=0;i<9;i++) {
			list.add(world[((int) Math.floor(i / 3))][i%3]);
		}

		list.remove(4);

		Point ret = aStarUtil.getLowestF(list, world[1][1], world[3][3]);
		assertNotNull(ret);
		assertEquals(list.get(7), ret);

		ret = aStarUtil.getLowestF(list, world[1][1], world[3][0]);
		assertNotNull(ret);
		// why not list.get(5)? Because the differences in distance between latitudes and longitudes!
		assertEquals(list.get(6), ret);
	}

	@Test
	public void aStar() {
		world[3][3] = null;
		world[3][4] = null;
		world[3][5] = null;
		AStarUtil.ASRoute route = aStarUtil.aStar(1,1,5,8,world);
		assertNotNull(route);
		assertNotSame(0, route.getNumberOfPoints());
		float totalDistance = route.getTotalDistance();
		assertTrue(totalDistance > 0);
	}

//	@Test
	public void reconstructPath() {

	}
}
