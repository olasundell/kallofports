package kop.map.routecalculator;

import kop.game.Game;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertNotNull;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;
import static org.testng.internal.junit.ArrayAsserts.assertArrayEquals;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 5/17/11
 * Time: 8:11 AM
 * To change this template use File | Settings | File Templates.
 */
public class PointTest {
	private NewWorld world;

	@BeforeMethod
	public void setUp() throws Exception {
		world = Util.getBlankWorld();
	}

	@Test
	public void getY() {
		assertEquals(world.lats[0].longitudes.length - 1, world.lats[0].longitudes[0].getY(-1, world));
	}

	@Test
	public void equalsShouldWork() {
		NewWorld world = Game.getInstance().getWorld();
		Point point = null;
		Point oldPoint;
		for (int i=0;i<world.lats.length;i++) {
			for (int j=0;j<world.lats[i].longitudes.length;j++) {
				if (world.lats[i].longitudes[j] != null) {
					oldPoint = point;
					point = world.lats[i].longitudes[j];
					assertEquals("Point doesn't equal itself!",point, point);
					assertEquals(point.hashCode(), point.hashCode());
					if (oldPoint != null) {
						assertTrue("Old point equals point! ARGH!",!oldPoint.equals(point));
					}
				}
			}
		}
	}

	@Test
	public void getNeighbours() {
		Point p = world.lats[2].longitudes[2];
		Point[] arr = {
				world.lats[1].longitudes[1], world.lats[1].longitudes[2], world.lats[1].longitudes[3],
				world.lats[2].longitudes[1],			  world.lats[2].longitudes[3],
				world.lats[3].longitudes[1], world.lats[3].longitudes[2], world.lats[3].longitudes[3]
		};

		List<Point> result = p.getNeighbours(world);
		assertNotNull(result);
		assertArrayEquals(arr, result.toArray());

		p = world.lats[0].longitudes[0];
		Point[] arr2 = {
				world.lats[0].longitudes[world.lats[0].longitudes.length-1],			  world.lats[0].longitudes[1],
				world.lats[1].longitudes[world.lats[0].longitudes.length-1], world.lats[1].longitudes[0], world.lats[1].longitudes[1]
		};

		result = p.getNeighbours(world);
		assertNotNull(result);
		assertArrayEquals(arr2, result.toArray());
	}

	@Test
	public void totalCost() {
		Point previous = null;
		double dist=0;
		for (int i=0;i<10;i++) {
			Point p = new Point(i,i);
			if (previous != null) {
				p.setParent(previous);
				dist+=p.distance(previous);
			}
			previous = p;
		}

		assertEquals(dist, previous.getTotalCost());

	}
}
