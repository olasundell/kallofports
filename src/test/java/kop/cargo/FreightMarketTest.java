package kop.cargo;

import kop.game.CouldNotLoadFreightOntoShipException;
import kop.game.Game;
import kop.game.GameTestUtil;
import kop.ports.Port;
import kop.ports.PortProxy;
import kop.ports.PortsOfTheWorld;
import kop.serialization.SerializationException;
import kop.ships.model.ShipModel;
import org.testng.annotations.BeforeClass;
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
	@BeforeClass
	public static void init() {
		instance = GameTestUtil.setupInstanceForTest();
	}

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
	public void generateCargo() throws Exception {
		FreightMarket freightMarket = instance.getFreightMarket();
		CargoTypeList cargoTypeList = freightMarket.getCargoTypes();
		CargoImpl cargo = (CargoImpl) freightMarket.generateCargo(cargoTypeList.get(1));
		checkCargoOK(cargo);
		cargo = (CargoImpl) freightMarket.generateCargo(cargoTypeList.getCargoTypeByPackaging(CargoType.Packaging.container));
		checkCargoOK(cargo);
	}

	private void checkCargoOK(CargoImpl cargo) {
		assertNotNull(cargo);
		assertTrue(cargo.getWeight() > 0);
		Date time = instance.getCurrentDate();
		int daysLeft = cargo.getDaysLeft(time);
		assertTrue(daysLeft > 0, String.format("daysLeft should be above zero, time is %s, daysLeft are %d and deadline is %s.", time, daysLeft, cargo.getDeadline()));
		assertTrue(cargo.getTotalPrice() > 0);
	}

	@Test
	public void loadFreightOntoShip() throws CouldNotLoadFreightOntoShipException {
		ShipModel ship = instance.getPlayerCompany().getShip(0);
		FreightMarket market = instance.getFreightMarket();
		int marketSizeBefore = market.getFreights().size();
		// TODO this should fail if the ship isn't in the same port as the freight we're trying to load onto it.
		instance.loadFreightOntoShip(ship, market.getFreightFromPort(instance.getPlayerCompany().getHomePort()).get(0));

		assertEquals(market.getFreights().size(), marketSizeBefore - 1);
		assertEquals(1,ship.getFreights().size());
		assertFalse(market.getFreights().contains(ship.getFreights().get(0)));

//		game.loadFreightOntoShip();
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
