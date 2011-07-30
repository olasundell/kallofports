package kop.map.routecalculator;

import com.bbn.openmap.io.FormatException;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.testng.Assert.assertNotNull;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 5/17/11
 * Time: 3:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class RouteCalculatorTest {
	@Test(groups = {"heavy"})
	public void createWorldWithOpenMap() {
		NewWorld smallWorld = Util.getSmallWorld(new OpenMapWaterVerifier());
		assertNotNull(smallWorld);
		Point[][] arr=smallWorld.getPointsAsArray();
		assertNotNull(arr);

		// TODO add more tests here.
	}

	@Test(groups = {"heavy"})
	public void createWorldWithGeoTools() {
		NewWorld smallWorld = Util.getSmallWorld(new GeoToolsWaterVerifier());
		assertNotNull(smallWorld);
		Point[][] arr=smallWorld.getPointsAsArray();
		assertNotNull(arr);
	}
}
