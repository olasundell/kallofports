package kop.ships;

import kop.game.Game;
import kop.ports.Port;
import kop.ports.PortProxy;
import kop.serialization.ModelSerializer;
import kop.ships.blueprint.ShipBlueprint;
import kop.ships.engine.Engine;
import kop.ships.engine.EngineList;
import kop.ships.model.ContainerShipModel;
import kop.ships.model.ShipModel;
import org.testng.annotations.Test;

import java.io.File;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertEquals;


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
		ShipClass c = ShipClass.getShipClasses().get(ShipBlueprint.ShipType.container, 0);

		ShipModel ship = ShipModel.createShip("Ship name",c);
		assertNotNull(ship);
		assertTrue(ship instanceof ContainerShipModel);
	}

	@Test
	public void testSerializeDeserialize() throws Exception {
		// TODO use kop.serialization.ModelSerializer for this.
		ShipModel ship = new ContainerShipModel();
		ship.setName("foobar");
		ship.getBlueprint().addEngine(new Engine());

		EngineList engineList = EngineList.getInstance();
		ship.getBlueprint().addEngine(engineList.getAnEngineForTest());
		Port singapore = Game.getInstance().getPortByName("Singapore");
		ship.setPort(singapore.getProxy());

		String fileName = "containershipmodel.xml";
		ModelSerializer.saveToFile(fileName,ContainerShipModel.class, ship);
		ShipModel r = (ShipModel) ModelSerializer.readFromFile(new File(fileName).toURI().toURL(), ContainerShipModel.class);
		assertEquals(ship.getName(), r.getName());
		assertNotNull(ship.getCurrentPosition().getCurrentPort());
		assertEquals(singapore.getUnlocode(),ship.getCurrentPosition().getCurrentPort().getUnlocode());
	}

	// TODO rewrite this using the correct factory methods for ShipModel instancing.
//	@Test
	public void testTravel() throws Exception {
		ShipModel ship = new ContainerShipModel();
		ship.setName("foobar");

		PortProxy origin = Game.getInstance().getPortByName("Durban").getProxy();
		PortProxy destination = Game.getInstance().getPortByName("New York").getProxy();

		ship.setCurrentPort(origin);
		ship.setCurrentFuel(ship.getMaxFuel());
		ship.setSail(origin, destination, SPEED);
		assertEquals(true, ship.isAtSea());
		assertEquals(7571.0, ship.getDistanceLeft());
		assertEquals(758, ship.getHoursToDestination());

		assertEquals(-29.867, ship.getLatitude());
		assertEquals(31.05, ship.getLongitude());
		// TODO check if -179.39 (which is returned from the method) is correct
//		assertEquals(123.4, ship.getBearing());

		ship.travel();
		assertTrue(ship.getCurrentFuel() < ship.getMaxFuel());
		assertEquals(7561.0, ship.getDistanceLeft());
		assertEquals(757, ship.getHoursToDestination());
	}


}
