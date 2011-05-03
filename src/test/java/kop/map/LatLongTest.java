package kop.map;

import junit.framework.TestCase;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 5/2/11
 * Time: 6:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class LatLongTest {
	@Test
	public void coordConstructorShouldReturnValidInstance() {
		LatLong latLong = new LatLong("-29.92", LatLong.Direction.latitude);
		assertNotNull(latLong);
		assertEquals("S", latLong.getHemisphere());
		assertEquals("29", latLong.getDeg());
		assertEquals(String.valueOf(Math.round(0.92*60)), latLong.getMin());
	}
}
