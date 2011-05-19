package kop.map.routecalculator;

import com.bbn.openmap.io.FormatException;
import junit.framework.TestCase;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 5/17/11
 * Time: 3:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class RouteCalculatorTest {
	@Test
	public void reverseLatLong() throws FormatException, IOException {
		assertEquals(0,RouteCalculator.reverseLat(90));
		assertEquals(20, RouteCalculator.reverseLat(70));
		assertEquals(90, RouteCalculator.reverseLat(0));
		assertEquals(105, RouteCalculator.reverseLat(-15));
		assertEquals(180, RouteCalculator.reverseLat(-90));
		assertEquals(90, RouteCalculator.reverseLat((float) 0.4));
	}

	@Test
	public void calcLatLong() {
		float oldScale = RouteCalculator.getScale();
		RouteCalculator.setScale(2);
		assertEquals(-175.0,(double)RouteCalculator.calcLon(10));
		assertEquals(85.0 - RouteCalculator.NORTH_OFFSET, (double)RouteCalculator.calcLat(10));
		assertEquals(45.0 - RouteCalculator.NORTH_OFFSET, (double)RouteCalculator.calcLat(90));
		assertEquals(-15.0 - RouteCalculator.NORTH_OFFSET, (double)RouteCalculator.calcLat(210));
		RouteCalculator.setScale(oldScale);
	}
}
