package kop.company;

import kop.ships.Ship;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/3/11
 * Time: 2:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class Company {
	private double money;
	private List<Ship> ships;
	private List<Loan> loans;
	private String name;

	public Company() {
		ships = new ArrayList<Ship>();
		loans = new ArrayList<Loan>();
	}

	public void moveShips() {
		for (Ship s: ships) {
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
