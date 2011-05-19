package kop.map.routecalculator;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertArrayEquals;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 5/17/11
 * Time: 8:11 AM
 * To change this template use File | Settings | File Templates.
 */
public class PointTest {
	private Point[][] world;

	@Before
	public void setUp() throws Exception {
		world = WorldTest.createWorld();
	}

	@Test
	public void getY() {
		assertEquals(world.length - 1, world[0][0].getY(-1, world));
	}

	@Test
	public void getNeighbours() {
		Point p = world[2][2];
		Point[] arr = {
				world[1][1], world[1][2], world[1][3],
				world[2][1],			  world[2][3],
				world[3][1], world[3][2], world[3][3]
		};

		List<Point> result = p.getNeighbours(world);
		assertNotNull(result);
		assertArrayEquals(arr, result.toArray());

		p = world[0][0];
		Point[] arr2 = {
				world[0][9],			  world[0][1],
				world[1][9], world[1][0], world[1][1]
		};

		result = p.getNeighbours(world);
		assertNotNull(result);
		assertArrayEquals(arr2, result.toArray());
	}
}
