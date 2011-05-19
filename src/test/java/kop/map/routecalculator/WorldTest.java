package kop.map.routecalculator;

import junit.framework.TestCase;
import kop.ships.ModelSerializer;
import org.junit.Test;

import java.io.File;
import java.net.URL;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 5/18/11
 * Time: 2:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class WorldTest {
	public static Point[][] createWorld() {
		Point[][] world = new Point[10][10];
		for (int i=0;i<world.length;i++) {
			for (int j=0;j<world[i].length;j++) {
				// why +20? Because 0,0 is the north pole, and there be bears.
				// also, distances up there, say between 0,0 and 0,1, are kind of funny.
				world[i][j] = new Point(i,j,i+20,j+20);
			}
		}

		return world;
	}

	@Test
	public void serializeDeserialize() throws Exception {
		World world = new World(createWorld());

		ModelSerializer.saveToFile("worldtest.xml", World.class, world);
		World ret = (World) ModelSerializer.readFromFile(new File("worldtest.xml").toURI().toURL(), World.class);
		assertNotNull(ret);
		assertTrue(world.equals(ret));
//		assertEquals(world,ret);
	}
}
