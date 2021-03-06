package kop.company;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import kop.cargo.Freight;
import kop.game.Game;
import kop.ports.PortProxy;
import kop.ships.OutOfFuelException;
import kop.ships.ShipClass;
import kop.ships.ShipnameAlreadyExistsException;
import kop.ships.model.ShipModel;

/**
 * Represents a Company, either a player company or an AI dito (should we ever write an AI...)
 */
public class Company {
	private double money;
	private final List<ShipModel> ships;
	private final List<Loan> loans;
	private String name;
	private PortProxy homePort;

	public Company() {
		ships = new ArrayList<ShipModel>();
		loans = new ArrayList<Loan>();
	}

	/**
	 * Moves ships, given that they are at sea, and delivers freights should any moved ship arrive at its destination.
	 * @throws OutOfFuelException if a ship runs out of fuel.
	 */

	public void moveShips() throws OutOfFuelException {
		for (ShipModel s: ships) {
			boolean atSea = s.isAtSea();

			s.travel();

			// TODO fix this, preferably with a shipModel.justArrivedAtPortDamnit() or something akin to that.
			if (atSea && s.isInPort()) {
				// arrived

				for (Freight f: s.getFreights()) {
					if (f.getDestination().equals(s.getCurrentPosition().getCurrentPort())) {
						Game.getInstance().addDeliveredFreights(this, f);
					}
				}
				Collection<?> deliveredFreights = Game.getInstance().getDeliveredFreights(this);
				s.getFreights().removeAll(deliveredFreights);
			}
		}
	}

	/**
	 * Subtracts daily costs from the company.
	 */
	public void doDailyCosts() {
		setMoney(getMoney() - getDailyCosts());
	}

	/**
	 * Subtracts monthly costs from the company, including mortgage from loans.
	 */
	public void doMonthlyCosts() {
		double costs = getMonthlyCosts();
		setMoney(getMoney() - costs);
	}

	public double getMonthlyCosts() {
		double costs = 0;
		for (Loan l: loans) {
			costs += l.doMortgage();
		}
		return costs;
	}

	public String getName() {
		return name;
	}

	public double getMoney() {
		return money;
	}

	public int getNumberOfShips() {
		return ships.size();
	}

	public void addShip(ShipModel shipModel) {
		shipModel.setCurrentPort(homePort);
		ships.add(shipModel);
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public void addLoan(Loan loan) {
		loans.add(loan);
	}

	public List<Loan> getLoans() {
		return loans;
	}

	public ShipModel getShip(int index) {
		return ships.get(index);
	}

	public boolean purchaseShip(int loanPercentage, String name, ShipClass shipClass) throws ShipnameAlreadyExistsException {
		if (Game.getInstance().isShipNameTaken(name)) {
			throw new ShipnameAlreadyExistsException(name);
		}

		double loanSize = shipClass.getPrice() * (loanPercentage / 100.0);
		setMoney(getMoney() - shipClass.getPrice() + loanSize);
		ShipModel model = ShipModel.createShip(name, shipClass);

		Loan loan = new Loan(loanSize, Game.getInstance().getInterestRate());
		loan.setSecurity(model);
		addLoan(loan);

		addShip(model);

		return true;
	}

	/**
	 * Creates an instance of the provided ShipClass and subtracts the corresponding amount of money from the company.
	 * TODO loans aren't implemented
	 * TODO should we use an exception instead of boolean return?
	 * @param shipClass the shipclass which will be purchased.
	 * @return false if there isn't enough money for the transaction, in which case no ship will be purchased.
	 */


	public boolean purchaseShip(ShipClass shipClass) {
		if (getMoney() < shipClass.getPrice()) {
			return false;
		}

		setMoney(getMoney() - shipClass.getPrice());
		addShip(ShipModel.createShip("Dummy", shipClass));

		return true;
	}

	public void setName(String text) {
		this.name = text;
	}

	public List<ShipModel> getShips() {
		return ships;
	}

	public void addMoney(double money) {
		setMoney(getMoney() + money);
	}

	/**
	 * Calculates daily costs, nothing more.
	 * @return daily costs for the Company instance.
	 */

	public double getDailyCosts() {
		double costs = 0;
		for (ShipModel s: ships) {
			costs += s.getBlueprint().getDailyCost();
		}

		return costs;
	}

	public void setHomePort(PortProxy port) {
		homePort = port;
	}

	public PortProxy getHomePort() {
		return homePort;
	}

	public List<ShipModel> findShipsInPort(PortProxy portProxy) {
		List<ShipModel> shipsInPort = new ArrayList<ShipModel>();
		for (ShipModel ship: getShips()) {
			if (ship.getCurrentPosition().isInPort() && ship.getCurrentPosition().getCurrentPort().equals(portProxy)) {
				shipsInPort.add(ship);
			}
		}

		return shipsInPort;
	}

	public double getGrossWorth() {
		double worth = 0.0;
		for (ShipModel s: getShips()) {
			worth+=s.getCurrentWorth();
		}

		worth+=getMoney();

		return worth;
	}

	public double getTotalLoans() {
		double loanTotal=0.0;
		for (Loan l: getLoans()) {
			loanTotal += l.getCurrentDebt();
		}

		return loanTotal;
	}

	public double getNetWorth() {
		return getGrossWorth() - getTotalLoans();
	}
}

