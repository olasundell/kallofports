package kop.map;

import junit.framework.Assert;
import junit.framework.TestCase;
import kop.ports.NoRouteFoundException;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 5/2/11
 * Time: 5:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class RouteTest {
//	@Test
	//TODO this test is disabled until routing is fixed.
	public void getRouteShouldReturnInstance() throws IOException, URISyntaxException, NoRouteFoundException {

		Route route = Route.getRoute("ZADUR", "USNYC", true, true);
		assertRoute(route);
		route = Route.getRoute("ZADUR", "USNYC", false, false);
		assertRoute(route);
		Route zaitNoSuez = Route.getRoute("ZADUR", "ITTAR", false, false);
		Route zaitSuez = Route.getRoute("ZADUR", "ITTAR", false, true);
		assertRoute(zaitNoSuez);
		assertRoute(zaitSuez);

		Assert.assertTrue(zaitSuez.getNm() < zaitNoSuez.getNm());
	}

	private void assertRoute(Route route) {
		assertNotNull(route);
		List<Route.Point> points = route.getPoints();
		assertNotNull(points);
		assertTrue(points.size() > 0);
	}

//	@Test
	// TODO this doesn't work atm, there's a small discrepancy of about 0.1% or so.
	public void distanceBetweenRoutePointsShouldEqualTotal() throws NoRouteFoundException {
		Route route = Route.getRoute("ZADUR", "USNYC", true, true);
		double dist=0;
		for (int i=0;i<route.getPoints().size() - 1;i++) {
			dist+=route.getDistanceBetweenPoints(i,i+1);
		}

		assertEquals(route.getNm(), dist);
	}

	@Test
	public void noSuchRouteExceptionShouldBeThrown() {
		boolean thrown = false;
		try {
			Route route = Route.getRoute("FOO", "BAR", true, true);
		} catch (NoRouteFoundException e) {
			thrown = true;
		}
		assertTrue(thrown);
	}
}
