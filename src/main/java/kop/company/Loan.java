package kop.company;

import kop.ships.model.ShipModel;

/**
 * Represents a loan with security, interest and monthly mortgage amount should the player wish to
 */
public class Loan {
	private ShipModel security;
	private double interest;
	private double mortagePerMonth;
	private double currentDebt;


	public Loan(double debt, double interest) {
		this.currentDebt = debt;
		this.interest = interest;
	}

	/**
	 * Monthly mortgage, mortgage amount (which will be subtracted from the loan), and interest.
	 * @return the monthly interest cost plus the mortgage amount.
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
