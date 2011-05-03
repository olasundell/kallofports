package kop.map;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

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
	@Test
	public void getRouteShouldReturnInstance() throws IOException, URISyntaxException {

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
}
