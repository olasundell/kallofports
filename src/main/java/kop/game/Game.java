package kop.game;

import kop.cargo.Freight;
import kop.cargo.FreightMarket;
import kop.company.Company;
import kop.ports.*;
import kop.ships.*;
import kop.ui.MainWindow;

import javax.swing.*;
import javax.swing.text.DateFormatter;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.*;

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
	private ShipClassList shipClasses;
	private FreightMarket market;
	private boolean paused = false;
	private Company playerCompany;
	private String playerName;
	private EngineList engineList;

	private ArrayList<GameStateListener> listeners;
	private Random random;
	private Map<Company, List<Freight>> deliveredFreights;

	protected Game() {
		world = new PortsOfTheWorld();
		calendar = new GregorianCalendar(1970,0,0,0,0);
		market = new FreightMarket();

		companies = new ArrayList<Company>();
		resetPlayerCompany();
		listeners = new ArrayList<GameStateListener>();
		// TODO randseed will be zero for reproducing reasons. Fix this later!
		random = new Random(0);
		deliveredFreights = new HashMap<Company, List<Freight>>();

		populatePorts();
		populateShipClasses();
	}

	public void addListener(GameStateListener listener) {
		listeners.add(listener);
	}

	private static void createInstance() {
		instance = new Game();
	}

	private void populateShipClasses() {
		try {
			shipClasses = (ShipClassList) ModelSerializer.readFromFile("kop/ships/shipclasses.xml", ShipClassList.class);
		} catch (Exception e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}
	}

	private void populatePorts() {
		world.populatePorts();
	}

	public double getDistance(Port origin, Port destination, ShipModel ship) throws NoRouteFoundException {
		return world.getDistance(origin, destination, ship);
	}

	public static Game getInstance() {
		if (instance==null) {
			createInstance();
		}

		return instance;
	}

	public Date getCurrentDate() {
		return calendar.getTime();
	}

	public void mainLoop() {
		while (isRunning()) {
			stepTime();
		}
	}

	public void stepTime() {
		calendar.add(Calendar.HOUR, 1);

		for (Company c: companies) {

			try {
				c.moveShips();
			} catch (OutOfFuelException e) {
				// TODO do something here.
			}

			if (calendar.get(Calendar.HOUR) == 0) {
				// a new day dawns
				if (deliveredFreights.get(c) != null) {
					for (Freight f: deliveredFreights.get(c)) {
						c.addMoney(f.getCargo().getTotalPrice());
					}
				}
				c.doDailyCosts();
				if (calendar.get(Calendar.DAY_OF_MONTH)==0) {
					// a new month!
					c.doMonthlyCosts();
				}
			}
		}
		// a new day dawns
		if (calendar.get(Calendar.HOUR) == 0) {
			generateDailyFreights();
		}

		for (GameStateListener listener:listeners) {
			listener.stateChanged();
		}
	}

	private void generateDailyFreights() {
		for (Port p: world.getPortsAsList()) {
			market.getFreightFromPort(p);
		}
	}

	public boolean isRunning() {
		return !paused;
	}

	public Port getPortByName(String portName) throws NoSuchPortException {
		return world.getPortByName(portName);
	}

	public Company getPlayerCompany() {
		return playerCompany;
	}

	public void setPlayerCompanyName(String text) {
		getPlayerCompany().setName(text);
	}

	public void setPlayerName(String text) {
		playerName = text;
	}

	public EngineList getEngineList() {
		if (engineList == null) {
			try {
				engineList = (EngineList) ModelSerializer.readFromFile("kop/ships/engines.xml", EngineList.class);
			} catch (Exception e) {
				e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
			}
		}

		return engineList;
	}

	public void setDate(Date d) {
		calendar.setTime(d);
	}

	public String getCurrentDateAsString() {
		return calendar.toString();
	}

	public ShipClassList getShipClasses() {
		return shipClasses;
	}

	public Date getFutureDate(int days) {
		return new Date(calendar.getTime().getTime() + days * 3600*24*1000);
	}

	public Random getRandom() {
		return random;
	}

	protected PortsOfTheWorld getWorld() {
		return world;
	}

	protected FreightMarket getMarket() {
		return market;
	}

	public void addDeliveredFreights(Company c, Freight f) {
		if (deliveredFreights.get(c) == null) {
			deliveredFreights.put(c, new ArrayList<Freight>());
		}
		deliveredFreights.get(c).add(f);
	}

	public Collection<?> getDeliveredFreights(Company company) {
		return deliveredFreights.get(company);
	}

	public boolean isNextTimeStepNewDay() {
		return calendar.get(Calendar.HOUR) == calendar.getActualMaximum(Calendar.HOUR);
	}

	protected void resetPlayerCompany() {
		companies.clear();
		companies.add(new Company());
		playerCompany = companies.get(0);
	}
}
