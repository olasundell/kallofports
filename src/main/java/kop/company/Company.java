package kop.company;

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

	}

	public void doMonthlyCosts() {

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
}
