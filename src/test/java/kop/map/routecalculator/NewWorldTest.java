package kop.map.routecalculator;

import kop.game.Game;
import kop.serialization.ModelSerializer;
import kop.serialization.SerializationException;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNotSame;


/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 5/20/11
 * Time: 5:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class NewWorldTest {
	@Test
	public void equalsShouldWork() {
		NewWorld world1 = new NewWorld(2,2);
		world1.lats[0].longitudes[0] = null;
		world1.lats[0].longitudes[1] = new Point(0,1,0,1);
		world1.lats[1].longitudes[0] = new Point(1,0,1,0);
		world1.lats[1].longitudes[1] = new Point(1,1,1,1);

		NewWorld world2 = new NewWorld(2,2);

		world2.lats[0].longitudes[0] = null;
		world2.lats[0].longitudes[1] = new Point(0,1,0,1);
		world2.lats[1].longitudes[0] = new Point(1,0,1,0);
		world2.lats[1].longitudes[1] = new Point(1,1,1,1);

		assertEquals(world2, world1);

		world2.lats[1].longitudes[0] = null;

		assertNotSame(world2, world1);
	}

	@Test
	public void correctLatLon() {
		NewWorld world = Game.getInstance().getWorld();
		assertEquals(world.getScale(), 2.0);
		for (int i=0;i<world.getLatitudeSize();i++) {
			for (int j=0;j<world.getLongitudeSize();j++) {
				float expectedLon = (float) (j/world.getScale() - 180.0);
				Point longitude = world.lats[i].longitudes[j];
				if (longitude!=null) {
					assertEquals(longitude.getLon(), expectedLon);
					if (j==0) {
						assertEquals(expectedLon, -180.0f);
					} else if (j==world.getLongitudeSize() - 1) {
						assertEquals(expectedLon, 180.0f);
					}
				}
			}
		}
	}

//	@Test
	public void serializeDeserializeTextGeoToolsHEAVY() throws IOException {
		RouteCalculator calculator = new RouteCalculator();

		int scale = 2;
		NewWorld world = new NewWorld(180* scale,361* scale, new GeoToolsWaterVerifier());
		world.setScale(scale);

		world = calculator.calculateWorld(world);
		File file = new File("worldoutput.txt");
		FileWriter writer = new FileWriter(file);
		writer.write(world.toString());
		writer.close();

		NewWorld readWorld = NewWorld.readFromFile("worldoutput.txt");

		assertEquals(readWorld, world);
	}
//	@Test
	public void serializeDeserializeText() throws IOException {
		RouteCalculator calculator = new RouteCalculator();

		int scale = 2;
		NewWorld world = new NewWorld(180* scale,360* scale);
		world.setScale(scale);

		world = calculator.calculateWorld(world);
		File file = new File("worldoutput.txt");
		FileWriter writer = new FileWriter(file);
		writer.write(world.toString());
		writer.close();

		NewWorld readWorld = NewWorld.readFromFile("worldoutput.txt");

		assertEquals(readWorld, world);
	}

	@Test
	public void serializeDeserialize() throws SerializationException, MalformedURLException {
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
		assertEquals(readWorld, world);
	}

	@Test
	public void reverseLat() {
		NewWorld blankWorld = Util.getBlankWorld(1,0,0);

		assertEquals(blankWorld.reverseLat(90), 0);
		assertEquals(blankWorld.reverseLat(70), Math.round(20 * blankWorld.getScale()));
		assertEquals(blankWorld.reverseLat(0), Math.round(90 * blankWorld.getScale()));
		assertEquals(blankWorld.reverseLat(-15), Math.round(105 * blankWorld.getScale()));
		assertEquals(blankWorld.reverseLat(-90), Math.round(180 * blankWorld.getScale()));
		blankWorld.setNorthOffset(20);
		assertEquals(blankWorld.reverseLat(70), 0);
		assertEquals(blankWorld.reverseLat(90), Math.round(-20 * blankWorld.getScale()));

	}
	@Test
	public void reverseLon() {
		NewWorld blankWorld = Util.getBlankWorld(1,0,0);
		assertEquals(blankWorld.reverseLon(0), Math.round(blankWorld.getScale() * 180));
		assertEquals(blankWorld.reverseLon(75), Math.round(blankWorld.getScale() * (180 + 75)));
		assertEquals(blankWorld.reverseLon(-75), Math.round(blankWorld.getScale() * (180 - 75)));
	}

	@Test
	public void calcLatLong() {
		NewWorld blankWorld = Util.getBlankWorld(2,0,0);

		assertEquals((double) blankWorld.calcLon(10), -175.0);
		assertEquals((double) blankWorld.calcLat(10), 85.0 - blankWorld.getNorthOffset());
		assertEquals((double) blankWorld.calcLat(90), 45.0 - blankWorld.getNorthOffset());
		assertEquals((double) blankWorld.calcLat(210), -15.0 - blankWorld.getNorthOffset());
	}

	@Test(groups = {"heavy"})
	public void getWorldShouldMatchTestUtilCalculation() {
		Util.resetWorld();
		NewWorld utilWorld = Util.getSmallWorld((float) 0.5,0,180);
		NewWorld newWorld = NewWorld.getWorld((float) 0.5);
		assertEquals(newWorld, utilWorld);
	}
}
