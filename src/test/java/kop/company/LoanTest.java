package kop.company;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/4/11
 * Time: 4:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class LoanTest {
	private final double INTEREST = 4.0;
	private final double AMOUNT = 10000;

	@Test
	public void testDoMortgage() throws Exception {
		Loan loan = new Loan(AMOUNT, INTEREST);
		assertEquals(33.0, loan.doMortgage());
		assertEquals(33.0, loan.doMortgage());
	}

	@Test
	public void testDoMortgageWithPayback() {
		Loan loan = new Loan(AMOUNT, INTEREST);
		loan.setMortagePerMonth(1000);
		assertEquals(1033.0, loan.doMortgage());
		assertEquals(1030.0, loan.doMortgage());
		assertEquals(1027.0, loan.doMortgage());
	}

	@Test
	public void testDoMortgageFullyRepay() {
		Loan loan = new Loan(AMOUNT, INTEREST);
		loan.setMortagePerMonth(9000);
		assertEquals(9033.0, loan.doMortgage());
		assertEquals(1003.0, loan.doMortgage());
	}
}
