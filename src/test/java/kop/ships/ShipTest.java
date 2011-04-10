package kop.ships;

import kop.game.Game;
import kop.ports.NoRouteFoundException;
import kop.ports.Port;
import org.junit.Before;
import org.junit.Test;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;

import static junit.framework.Assert.assertEquals;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/4/11
 * Time: 1:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class ShipTest {
	private static final double SPEED = 10.0;

	@Before
	public void setup() {
		Game.createInstance();
	}

	@Test
	public void testGetAvailableDWT() throws Exception {

	}

	@Test
	public void testIsPostPanamax() throws Exception {

	}

	@Test
	public void testIsPostSuezmax() throws Exception {

	}

	@Test
	public void testSerializeDeserialize() throws Exception {
		Ship ship = new ContainerShip("foobar");
		Serializer serializer = new Persister();
		File result = new File("foobar.xml");
		serializer.write(ship, result);
		File source = new File("foobar.xml");
		ContainerShip r = serializer.read(ContainerShip.class, source);
		assertEquals(ship.getName(), r.getName());
	}

	@Test
	public void testTravel() throws Exception, NoRouteFoundException {
		Ship ship = new ContainerShip("foobar");

		Port origin = Game.getInstance().getPortByName("London");
		Port destination = Game.getInstance().getPortByName("Haifa");

		ship.setCurrentPort(origin);
		ship.setSail(origin, destination, SPEED);
		assertEquals(true, ship.isAtSea());
		assertEquals(3320.0, ship.getDistanceLeft());
		assertEquals(332, ship.getHoursToDestination());

		ship.travel();
		assertEquals(3310.0, ship.getDistanceLeft());
		assertEquals(331, ship.getHoursToDestination());
	}


}