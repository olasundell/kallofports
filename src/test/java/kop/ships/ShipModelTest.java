package kop.ships;

import kop.game.Game;
import kop.ports.NoRouteFoundException;
import kop.ports.Port;
import org.junit.Before;
import org.junit.Test;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/4/11
 * Time: 1:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class ShipModelTest {
	private static final double SPEED = 10.0;

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
	public void createShipFromShipClass() {
		List<ShipClass> shipClasses = ShipClass.getShipClasses();

		ShipModel ship = ShipModel.createShip(shipClasses.get(0));
		assertNotNull(ship);
	}

	@Test
	public void testSerializeDeserialize() throws Exception {
		// TODO use kop.ships.ModelSerializer for this.
		ShipModel ship = new ContainerShipModel();
		ship.setName("foobar");
		ship.getBlueprint().addEngine(new Engine());
		Serializer serializer = new Persister();
		EngineList engineList = EngineList.getInstance();
		ship.getBlueprint().addEngine(engineList.getAnEngineForTest());
		ship.setPort(Game.getInstance().getPortByName("Singapore"));

		File result = new File("foobar.xml");
		serializer.write(ship, result);
		File source = new File("foobar.xml");
		ContainerShipModel r = serializer.read(ContainerShipModel.class, source);
		assertEquals(ship.getName(), r.getName());
	}

	// TODO add this test again!
	@Test
	public void testTravel() throws Exception, NoRouteFoundException {
		ShipModel ship = new ContainerShipModel();
		ship.setName("foobar");

		Port origin = Game.getInstance().getPortByName("Durban");
		Port destination = Game.getInstance().getPortByName("New York");

		ship.setCurrentPort(origin);
		ship.setSail(origin, destination, SPEED);
		assertEquals(true, ship.isAtSea());
		assertEquals(7571.0, ship.getDistanceLeft());
		assertEquals(758, ship.getHoursToDestination());

		assertEquals(-29.867, ship.getLatitude());
		assertEquals(31.05, ship.getLongitude());
		// TODO check if -179.39 (which is returned from the method) is correct
//		assertEquals(123.4, ship.getBearing());

		ship.travel();
		assertEquals(7561.0, ship.getDistanceLeft());
		assertEquals(757, ship.getHoursToDestination());
	}


}
