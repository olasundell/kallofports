package kop.game;

import kop.company.Company;
import kop.ports.NoRouteFoundException;
import kop.ports.NoSuchPortException;
import kop.ports.Port;
import kop.ports.PortsOfTheWorld;
import kop.ships.Ship;
import kop.ships.ShipClassesFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/4/11
 * Time: 4:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class Game {
	private List<Company> companies;
	// 1970-01-01 00:00 + currentHour
	private GregorianCalendar calendar;
	private static Game instance;
	private PortsOfTheWorld world;
	private List<Ship> shipTypes;

	public Game() {
		world = new PortsOfTheWorld();
		calendar = new GregorianCalendar(1970,0,0,0,0);
	}

	public static void createInstance() {
		instance = new Game();
		instance.populatePorts();
		instance.createShipTypes();
	}

	private void createShipTypes() {
//		shipTypes = new ShipClassesFactory().createShipTypes();
	}

	private void populatePorts() {
		Port london = world.putPort("London");
		Port haifa = world.putPort("Haifa");
		world.setDistance(london, haifa, 3320);
		world.setDistance(haifa, london, 3320);
	}

	public double getDistance(Port origin, Port destination, Ship ship) throws NoRouteFoundException {
		return world.getDistance(origin, destination, ship);
	}

	public static Game getInstance() {
		return instance;
	}

	public Date getCurrentDate() {
		return calendar.getTime();
	}

	public void stepTime() {
		calendar.add(Calendar.HOUR, 1);
		// TODO move ships

		for (Company c: companies) {
			c.moveShips();

			if (calendar.get(Calendar.HOUR) == 0) {
				// a new day dawns
				c.doDailyCosts();
				if (calendar.get(Calendar.DAY_OF_MONTH)==0) {
					// a new month!
					c.doMonthlyCosts();
				}
			}
		}
	}

	public Port getPortByName(String portName) throws NoSuchPortException {
		return world.getPortByName(portName);
	}
}
