package kop.cargo;

import com.sun.media.jai.util.MathJAI;
import kop.game.CouldNotLoadFreightOntoShipException;
import kop.game.Game;
import kop.game.GameTestUtil;
import kop.ports.Port;
import kop.ports.PortProxy;
import kop.ships.model.ShipModel;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static org.testng.AssertJUnit.*;


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
		assertEquals("The list returned form the port wasn't what we expected.",1, list.size());
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
		assertTrue(String.format("daysLeft should be above zero, time is %s, daysLeft are %d and deadline is %s.", time, daysLeft, cargo.getDeadline()),daysLeft > 0);
		assertTrue(cargo.getTotalPrice() > 0);
	}

	@Test
	public void loadFreightOntoShip() throws CouldNotLoadFreightOntoShipException {
//		Game game = new Game();
//		game.generateDailyFreights();

//		ShipModel ship = ShipModel.createShip("Foo", instance.getShipClasses().get(0));
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
}
