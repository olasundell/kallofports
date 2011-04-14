package kop.company;

import kop.ships.ShipModel;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/4/11
 * Time: 4:24 PM
 * To change this template use File | Settings | File Templates.
 */
class Loan {
	private ShipModel security;
	private double interest;
	private double mortagePerMonth;
	private double currentDebt;

	public Loan(double debt, double interest) {
		this.currentDebt = debt;
		this.interest = interest;
	}

	/**
	 * Monthly mortgage.
	 * @return the monthly cost
	 */
	public double doMortgage() {
		double mortgage = currentDebt * (interest / 1200);

		if (mortagePerMonth > currentDebt) {
			mortagePerMonth = currentDebt;
		}

		currentDebt = currentDebt - mortagePerMonth;

		return Math.round(mortgage) + mortagePerMonth;
	}

	public ShipModel getSecurity() {
		return security;
	}

	public void setSecurity(ShipModel security) {
		this.security = security;
	}

	public double getInterest() {
		return interest;
	}

	public void setInterest(double interest) {
		this.interest = interest;
	}

	public double getMortagePerMonth() {
		return mortagePerMonth;
	}

	public void setMortagePerMonth(double mortagePerMonth) {
		this.mortagePerMonth = mortagePerMonth;
	}

	public double getCurrentDebt() {
		return currentDebt;
	}

	public void setCurrentDebt(double currentDebt) {
		this.currentDebt = currentDebt;
	}
}
