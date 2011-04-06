package kop.ports;

import kop.ships.BulkShip;
import kop.ships.ContainerShip;
import kop.ships.Ship;
import kop.ships.TankerShip;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/3/11
 * Time: 4:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class DistanceTest {
	Distance dist;
	Ship hugeShip;
	private double longDist;
	private double shortDist;
	private double reallyShortDist;
	private BulkShip smallShip;
	private ContainerShip mediumShip;

	@Before
	public void setup() {
		longDist = 234;
		shortDist = 123;
		reallyShortDist=69;

		dist = new Distance();
		dist.addRoute(longDist,false,false);
		dist.addRoute(shortDist,true,false);
		dist.addRoute(reallyShortDist,true,true);

		hugeShip = new TankerShip("Post Suezmax");
		hugeShip.setDraft(25);
		hugeShip.setBeam(100);

		mediumShip = new ContainerShip("Post Panamax");
		mediumShip.setDraft(18);
		mediumShip.setBeam(49);

		smallShip = new BulkShip("Small Ship");
		smallShip.setDraft(1);
		smallShip.setBeam(2);
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
