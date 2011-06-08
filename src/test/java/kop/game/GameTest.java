package kop.game;

import kop.cargo.*;
import kop.map.routecalculator.ASRoute;
import kop.ports.NoRouteFoundException;
import kop.ports.NoSuchPortException;
import kop.ports.Port;
import kop.ports.PortProxy;
import kop.ships.engine.EngineList;
import kop.ships.model.ShipModel;
import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.util.*;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/26/11
 * Time: 1:47 PM
 * To change this template use File | Settings | File Templates.
 */

public class GameTest {
	private Game instance;

	@Before
	public void setUp() throws Exception {
		instance = Game.getInstance();
		String s = "1999-12-31 23:00";
		Date d = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, new Locale("sv")).parse(s);
		instance.setDate(d);
	}

	@Test
	public void playerCompanyShouldNotBeNull() {
		assertNotNull(instance.getPlayerCompany());
	}

	@Test
	public void engineListShouldContainEntries() {
		EngineList el = instance.getEngineList();
		assertNotNull(el);
		assertNotNull(el.getAnEngineForTest());
	}

	@Test
	public void gettingPortByNameShouldReturnAPort() throws NoSuchPortException {
		Port p = instance.getPortByName("Singapore");
		assertNotNull(p);
	}

	/**
	 *  This is more of an integration test.
	 */

	@Test
	public void timeStepShouldPlayTheGame() {
		MyGameStateListener listener = new MyGameStateListener();

		instance.addListener(listener);

		Date before = instance.getCurrentDate();
		assertTrue(instance.isNextTimeStepNewDay());

		instance.stepTime();
		Date now = instance.getCurrentDate();

		assertNotNull(before);
		assertNotNull(now);
		assertTrue(before.before(now));

		GregorianCalendar cal = (GregorianCalendar) GregorianCalendar.getInstance();
		cal.setTime(now);

		assertEquals(2000, cal.get(Calendar.YEAR));
		assertEquals(Calendar.JANUARY,cal.get(Calendar.MONTH));
		assertEquals(0,cal.get(Calendar.HOUR));
		assertTrue(listener.state);
	}

	@Test
	public void getDateForwardInTime() {
		Date date = instance.getFutureDate(10);
		assertTrue(date.after(instance.getCurrentDate()));
	}

	// TODO this test is disabled until routing is fixed.
//	@Test
	public void sailShipBuyAndDeliverCargo() throws Exception {
		instance = Game.getInstance();
		instance.resetPlayerCompany();

		// add a ship
		ShipModel ship = ShipModel.createShip("Dummy name",instance.getShipClasses().get(0));
		instance.getPlayerCompany().addShip(ship);

		// choose ports
		PortProxy fromPort = instance.getPortByName("Durban").getProxy();
		PortProxy toPort = instance.getPortByName("Taranto").getProxy();

		// add freight from port A and port B
		FreightMarket market = instance.getFreightMarket();
		Cargo cargo = market.generateCargo(FreightMarket.getCargoTypes().getCargoTypeByPackaging(ship.getBlueprint().getCargoCapabilities().get(0)));
		Freight freight = market.generateFreight(fromPort, toPort, cargo);
		ship.addFreight(freight);

		// route ship between A and B and set sail
		ship.setSail(fromPort, toPort, 10.0);
		double distanceLeft = ship.getDistanceLeft();

		// time step, make sure things happen correctly.
		instance.stepTime();
		assertTrue(distanceLeft == ship.getDistanceLeft()+10);

		// time step until arrived, make sure that freight is delivered properly.

		// after this we've almost arrived
		while (ship.getDistanceLeft() > 10) {
			instance.stepTime();
		}

		double money = instance.getPlayerCompany().getMoney();
		// arrive

		if (instance.isNextTimeStepNewDay()) {
			// if the day rolls here we will be slammed by daily costs, which will break the calculations below.
			money -= instance.getPlayerCompany().getDailyCosts();
		}
		instance.stepTime();

		// freight should have been delivered, money should be in the account.
		List<Freight> freights = ship.getFreights();
		assertTrue(freights.size() == 0);
		double currentMoney = instance.getPlayerCompany().getMoney();
		double totalPrice = freight.getCargo().getTotalPrice();
		double expected = money + totalPrice;
		assertEquals("Current money does not equal expected value.", expected, currentMoney);
	}

	// TODO this test is broken, needs to be fixed.
//	@Test

	public void getRoute() throws NoSuchPortException, NoRouteFoundException {
		PortProxy origin = Game.getInstance().getPortByName("Aberdeen").getProxy();
		PortProxy destination = Game.getInstance().getPortByName("Durban").getProxy();
//		ShipModel ship = Game.getInstance().;
		ShipModel ship = null;
		ASRoute route = Game.getInstance().getRoute(origin, destination, ship);
		assertNotNull(route);
	}

	private static class MyGameStateListener implements GameStateListener {
		public boolean state = false;

		@Override
		public void stateChanged() {
			state = true;
		}
	}


}
