package kop.ports;

import kop.ships.ShipModel;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/3/11
 * Time: 4:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class DistanceTest {
	Distance dist;
	private ShipModel hugeShip;
	private double longDist;
	private double shortDist;
	private double reallyShortDist;
	private ShipModel smallShip;
	private ShipModel mediumShip;

	@Before
	public void setup() {
		longDist = 234;
		shortDist = 123;
		reallyShortDist=69;

		dist = new Distance(new Port(), new Port());
		dist.addRoute(longDist,false,false);
		dist.addRoute(shortDist,true,false);
		dist.addRoute(reallyShortDist,true,true);

		hugeShip = mock(ShipModel.class);
		given(hugeShip.isPostSuezmax()).willReturn(true);
		given(hugeShip.isPostPanamax()).willReturn(true);

		mediumShip = mock(ShipModel.class);
		given(mediumShip.isPostSuezmax()).willReturn(false);
		given(mediumShip.isPostPanamax()).willReturn(true);

		smallShip = mock(ShipModel.class);
		given(smallShip.isPostSuezmax()).willReturn(false);
		given(smallShip.isPostPanamax()).willReturn(false);
	}

	@Test
	public void shortestDistanceTest() {
		assertEquals(reallyShortDist, dist.shortestDistance());
	}

	@Test
	public void shortestDistanceWithShip() {
		assertEquals(longDist, dist.shortestDistance(hugeShip));
		assertEquals(reallyShortDist, dist.shortestDistance(smallShip));
		assertEquals(shortDist, dist.shortestDistance(mediumShip));
	}
}
