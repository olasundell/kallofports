package kop.company;

import kop.cargo.Freight;
import kop.game.Game;
import kop.ships.OutOfFuelException;
import kop.ships.ShipClass;
import kop.ships.ShipModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/3/11
 * Time: 2:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class Company {
	private double money;
	private List<ShipModel> ships;
	private List<Loan> loans;
	private String name;

	public Company() {
		ships = new ArrayList<ShipModel>();
		loans = new ArrayList<Loan>();
	}

	public void moveShips() throws OutOfFuelException {
		for (ShipModel s: ships) {
			boolean atSea = s.isAtSea();

			s.travel();

			// TODO fix this, preferrably with a shipModel.justArrivedAtPortDamnit() or something akin to that.
			if (atSea==true && s.isInPort()) {
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

	public void doDailyCosts() {
		setMoney(getMoney() - getDailyCosts());
	}

	public void doMonthlyCosts() {
		double costs = 0;
		for (Loan l: loans) {
			costs += l.doMortgage();
		}
		setMoney(getMoney() - costs);
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
		ships.add(shipModel);
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public void addLoan(Loan loan) {
		loans.add(loan);
	}

	public ShipModel getShip(int index) {
		return ships.get(index);
	}

	public boolean purchaseShip(ShipClass shipClass) {
		if (getMoney() < shipClass.getPrice()) {
			return false;
		}

		setMoney(getMoney() - shipClass.getPrice());
		addShip(ShipModel.createShip(shipClass));

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

	public double getDailyCosts() {
		double costs = 0;
		for (ShipModel s: ships) {
			costs += s.getBlueprint().getDailyCost();
		}

		return costs;
	}
}
