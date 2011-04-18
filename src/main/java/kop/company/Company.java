package kop.company;

import kop.ships.ContainerShipModel;
import kop.ships.ShipModel;

import java.util.ArrayList;
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

	public void moveShips() {
		for (ShipModel s: ships) {
			s.travel();
		}
	}

	public void doDailyCosts() {
		double costs = 0;
		for (ShipModel s: ships) {
			costs += s.getBlueprint().getDailyCost();
		}
		setMoney(getMoney() - costs);
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
}
