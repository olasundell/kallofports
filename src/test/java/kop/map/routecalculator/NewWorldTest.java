package kop.map.routecalculator;

import junit.framework.TestCase;
import kop.ships.ModelSerializer;
import org.junit.Test;

import java.io.File;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 5/20/11
 * Time: 5:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class NewWorldTest {
	@Test
	public void serializeDeserialize() throws Exception {
		NewWorld world = new NewWorld();
		world.lats = new NewWorld.LatitudeArr[18];
		for (int i=0;i<world.lats.length;i++) {
			world.lats[i] = new NewWorld.LatitudeArr();
			world.lats[i].longitudes = new Point[36];
			for (int j=0;j<world.lats[i].longitudes.length;j++) {
				// let's create some nulls and see what happens
				if (j % 7 == 0 || i % 11 == 0) {
					// yes, I know this is redundant. Explicit is good at times.
					world.lats[i].longitudes[j] = null;
					continue;
				}
				world.lats[i].longitudes[j] = new Point(i,j,i,j);
			}
		}
		ModelSerializer.saveToFile("newworld.xml",NewWorld.class, world);
		NewWorld readWorld = (NewWorld) ModelSerializer.readFromFile(new File("newworld.xml").toURI().toURL(), NewWorld.class);
		assertNotNull(readWorld);
		assertEquals(world,readWorld);
	}

	@Test
	public void reverseLat() {
		NewWorld blankWorld = Util.getBlankWorld(1,0,0);

		assertEquals(0, blankWorld.reverseLat(90));
		assertEquals(Math.round(20 * blankWorld.getScale()), blankWorld.reverseLat(70));
		assertEquals(Math.round(90 * blankWorld.getScale()), blankWorld.reverseLat(0));
		assertEquals(Math.round(105* blankWorld.getScale()), blankWorld.reverseLat(-15));
		assertEquals(Math.round(180* blankWorld.getScale()), blankWorld.reverseLat(-90));
		blankWorld.setNorthOffset(20);
		assertEquals(0, blankWorld.reverseLat(70));
		assertEquals(Math.round(-20* blankWorld.getScale()), blankWorld.reverseLat(90));

	}
	@Test
	public void reverseLon() {
		NewWorld blankWorld = Util.getBlankWorld(1,0,0);
		assertEquals(Math.round(blankWorld.getScale()*180),blankWorld.reverseLon(0));
		assertEquals(Math.round(blankWorld.getScale()*(180+75)),blankWorld.reverseLon(75));
		assertEquals(Math.round(blankWorld.getScale()*(180-75)),blankWorld.reverseLon(-75));
	}

	@Test
	public void calcLatLong() {
		NewWorld blankWorld = Util.getBlankWorld(2,0,0);

		assertEquals(-175.0,(double) blankWorld.calcLon(10));
		assertEquals(85.0 - blankWorld.getNorthOffset(), (double) blankWorld.calcLat(10));
		assertEquals(45.0 - blankWorld.getNorthOffset(), (double) blankWorld.calcLat(90));
		assertEquals(-15.0 - blankWorld.getNorthOffset(), (double) blankWorld.calcLat(210));
	}

}
