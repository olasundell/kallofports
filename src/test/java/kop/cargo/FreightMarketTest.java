package kop.cargo;

import kop.game.CouldNotLoadFreightOntoShipException;
import kop.game.Game;
import kop.game.GameTestUtil;
import kop.ports.Port;
import kop.ports.PortProxy;
import kop.ports.PortsOfTheWorld;
import kop.serialization.SerializationException;
import kop.ships.ShipnameAlreadyExistsException;
import kop.ships.model.ShipModel;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.*;

import static org.testng.Assert.*;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/13/11
 * Time: 8:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class FreightMarketTest {
	private static Game instance;
//	private Game instance;

	@BeforeClass
	public static void init() throws ShipnameAlreadyExistsException {
		instance = GameTestUtil.setupInstanceForTest();
	}

//	@BeforeMethod
//	public void beforeMethod() {
//		instance = GameTestUtil.setupInstanceForTest();
//	}

	@Test
	public void testGenerateFreight() throws Exception {
		FreightMarket market = new FreightMarket();
		PortProxy origin = instance.getPortByName("Singapore").getProxy();
		PortProxy destination = instance.getPortByName("Durban").getProxy();
		CargoType type = FreightMarket.getCargoTypes().get(0);
		Date currentDate = instance.getCurrentDate();
		Cargo cargo = new CargoImpl(10, type, 123.4, currentDate);

		market.generateFreight(origin, destination, cargo);
		List<Freight> list = market.getFreightFromPort(origin);
		assertNotNull(list);
		assertEquals(list.size(), 1, "The list returned form the port wasn't what we expected.");
	}

	@Test
	public void listCargoTypes() throws Exception {
		CargoTypeList cargoTypeList = FreightMarket.getCargoTypes();
		assertNotNull(cargoTypeList);
		assertTrue(cargoTypeList.size() > 0);
	}

	@Test
	public void generateCargo() throws CouldNotLoadCargoTypesException {
		FreightMarket freightMarket = instance.getFreightMarket();
		CargoTypeList cargoTypeList = FreightMarket.getCargoTypes();
		CargoImpl cargo = (CargoImpl) freightMarket.generateCargo(cargoTypeList.get(1));
		checkCargoOK(cargo);
		cargo = (CargoImpl) freightMarket.generateCargo(cargoTypeList.getCargoTypeByPackaging(CargoType.Packaging.container));
		checkCargoOK(cargo);
	}

	@Test
	public void cargoShouldNotHaveWeightLessThanDensity() throws CouldNotLoadCargoTypesException {
		FreightMarket freightMarket = instance.getFreightMarket();
		CargoTypeList cargoTypeList = FreightMarket.getCargoTypes();
		CargoImpl cargo = (CargoImpl) freightMarket.generateCargo(cargoTypeList.get(1));
		try {
			cargo.setWeight(-1);
			fail("Setting a weight below the cargo density would result in a zero cubic metre volume cargo, which is invalid");
		} catch (IllegalArgumentException e) {
			// ignore
		}
	}

	private void checkCargoOK(CargoImpl cargo) {
		assertNotNull(cargo);
		assertTrue(cargo.getWeight() > 0, "Cargo does not have a weight above zero");
		assertTrue(cargo.getVolume() > 0, "Cargo does not have a volume above zero");
		Date time = instance.getCurrentDate();
		int daysLeft = cargo.getDaysLeft(time);
		assertTrue(daysLeft > 0, String.format("daysLeft should be above zero, time is %s, daysLeft are %d and deadline is %s.", time, daysLeft, cargo.getDeadline()));
		assertTrue(cargo.getTotalPrice() > 0, "Total price for cargo is less than or equal to zero.");
	}

	@Test
	public synchronized void loadFreightOntoShip() throws CouldNotLoadFreightOntoShipException {
		ShipModel ship = instance.getPlayerCompany().getShip(0);
		FreightMarket market = instance.getFreightMarket();
		int marketSizeBefore = market.getFreights().size();

		// TODO this fires a GameStateChanged event, and since we are reusing the game instance we'll have loads of listeners who will update stuff.
		instance.loadFreightOntoShip(ship, market.getFreightFromPort(instance.getPlayerCompany().getHomePort()).get(0));

		assertEquals(ship.getFreights().size(), 1);
		assertEquals(market.getFreights().size(), marketSizeBefore - 1);
		assertFalse(market.getFreights().contains(ship.getFreights().get(0)));
	}

	@Test
	public void loadFreightOntoShipShouldFailIfShipAndFreightAreInDifferentPorts() throws SerializationException {
		ShipModel ship = instance.getPlayerCompany().getShip(0);
		FreightMarket market = instance.getFreightMarket();

		// TODO this should fail if the ship isn't in the same port as the freight we're trying to load onto it.
		PortProxy currentPort = ship.getCurrentPosition().getCurrentPort();
		List<Freight> fromPort = null;

		for (Port port: PortsOfTheWorld.getPorts().getMap().values()) {
			PortProxy proxy = port.getProxy();
			if (!proxy.equals(currentPort) && !market.getFreightFromPort(proxy).isEmpty()) {
				fromPort = market.getFreightFromPort(proxy);
			}
		}

		assertNotNull(fromPort, "Could not find freights in other ports than the current");

		boolean exceptionThrown = false;

		try {
			instance.loadFreightOntoShip(ship, fromPort.get(0));
		} catch (CouldNotLoadFreightOntoShipException e) {
			exceptionThrown = true;
		}

		assertTrue(exceptionThrown, "Exception wasn't thrown when we tried to load cargo from a port to a ship which was in another port");
	}
}
