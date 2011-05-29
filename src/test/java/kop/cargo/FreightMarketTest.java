package kop.cargo;

import kop.game.Game;
import kop.ports.Port;
import kop.ports.PortProxy;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
		instance = new Game();
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

		System.out.println(cargo);
	}

	private void checkCargoOK(CargoImpl cargo) {
		assertNotNull(cargo);
		assertTrue(cargo.getWeight() > 0);
		Date time = instance.getCurrentDate();
		int daysLeft = cargo.getDaysLeft(time);
		assertTrue(String.format("daysLeft should be above zero, time is %s, daysLeft are %d and deadline is %s.", time, daysLeft, cargo.getDeadline()),daysLeft > 0);
		assertTrue(cargo.getTotalPrice() > 0);
	}
}
