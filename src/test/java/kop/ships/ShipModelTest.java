package kop.ships;

import kop.game.Game;
import kop.ports.NoRouteFoundException;
import kop.ports.NoSuchPortException;
import kop.ports.Port;
import kop.ports.PortProxy;
import kop.serialization.ModelSerializer;
import kop.ships.blueprint.ShipBlueprint;
import kop.ships.engine.Engine;
import kop.ships.engine.EngineList;
import kop.ships.model.ContainerShipModel;
import kop.ships.model.ShipModel;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.fail;


/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/4/11
 * Time: 1:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class ShipModelTest {
	private static final double SPEED = 10.0;
	private static final String NAME = "name";
	private ShipModel ship;
	private ShipClass containerShipClass;

	@BeforeMethod
	protected void setUp() throws Exception {
		containerShipClass = ShipClass.getShipClasses().get(ShipBlueprint.ShipType.container, 0);
		ship = ShipModel.createShip(NAME, containerShipClass);
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
	public void createShipFromShipClass() {
		ShipModel ship = ShipModel.createShip("Ship name", containerShipClass);
		assertNotNull(ship);
		assertTrue(ship instanceof ContainerShipModel);
	}

	@Test
	public void testSerializeDeserialize() throws Exception {
		// TODO use kop.serialization.ModelSerializer for this.
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
		assertEquals(singapore.getUnlocode(), ship.getCurrentPosition().getCurrentPort().getUnlocode());
	}

	@Test
	public void testTravel() throws Exception {
		PortProxy origin = Game.getInstance().getPortByName("Durban").getProxy();
		PortProxy destination = Game.getInstance().getPortByName("New York").getProxy();

		ship.setCurrentPort(origin);
		ship.setCurrentFuel(ship.getMaxFuel());
		ship.setSail(origin, destination, SPEED);
		assertEquals(true, ship.isAtSea());
		long distanceLeft = Math.round(ship.getDistanceLeft());
		assertEquals(distanceLeft, 7911);
		int hoursToDestination = ship.getHoursToDestination();
		assertEquals(hoursToDestination, 792);

		assertEquals(Math.round(ship.getLatitude()), -30);
		assertEquals(Math.round(ship.getLongitude()), 31);
		// TODO check if -179.39 (which is returned from the method) is correct
//		assertEquals(123.4, ship.getBearing());

		ship.travel();
		assertTrue(ship.getCurrentFuel() < ship.getMaxFuel());
		assertEquals(Math.round(distanceLeft - SPEED), Math.round(ship.getDistanceLeft()));
		assertEquals(hoursToDestination - 1, ship.getHoursToDestination());
	}

	@Test
	public void travelWithoutEnoughFuelShouldThrowException() throws NoRouteFoundException, NoSuchPortException {
		PortProxy origin = Game.getInstance().getPortByName("Durban").getProxy();
		PortProxy destination = Game.getInstance().getPortByName("New York").getProxy();

		ship.setCurrentPort(origin);
		ship.setCurrentFuel(ship.getMaxFuel());
		ship.setSail(origin, destination, SPEED);
		ship.setCurrentFuel(0.1);
		try {
			ship.travel();
			fail("Exception wasn't thrown when ship ran out of fuel.");
		} catch (OutOfFuelException e) {
			// ignore
		}
	}

	@Test
	public void setCurrentFuelParametersShouldRangeCheck() {
		try {
			ship.setCurrentFuel(-0.1);
			Assert.fail("Range check failed");
		} catch (IllegalArgumentException e) {
			// ignore
		}

		try {
			ship.setCurrentFuel(ship.getMaxFuel() + 1.0);
			Assert.fail("Range check failed");
		} catch (IllegalArgumentException e) {
			// ignore
		}
	}
}
