package kop.game;

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

	private Game() {
		world = new PortsOfTheWorld();
		calendar = new GregorianCalendar(1970,0,0,0,0);
		market = new FreightMarket();

		companies = new ArrayList<Company>();
		companies.add(new Company());
		playerCompany = companies.get(0);
		listeners = new ArrayList<GameStateListener>();
	}

	public void addListener(GameStateListener listener) {
		listeners.add(listener);
	}

	private static void createInstance() {
		instance = new Game();
		instance.populatePorts();
		instance.populateShipClasses();
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
}
