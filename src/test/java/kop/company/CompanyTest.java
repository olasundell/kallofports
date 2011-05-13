package kop.company;

import kop.ships.*;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/13/11
 * Time: 9:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class CompanyTest {
	private Company company;
	private List<ShipClass> shipClasses;

	@Before
	public void setup() {
		company = new Company();

		ContainerShipModel shipModel = (ContainerShipModel) ShipModel.createShip(ShipClass.getShipClasses().get(ShipBlueprint.ShipType.container,0));
		shipModel.getBlueprint().setDailyCost(100);
		company.addShip(shipModel);

		Loan loan = new Loan(1000,12);
		company.addLoan(loan);
		shipClasses = ShipClass.getShipClasses();
	}

	@Test
	public void testMoveShips() throws Exception {
		company.moveShips();
	}

	@Test
	public void testDoDailyCosts() throws Exception {
		company.setMoney(1000);
		company.doDailyCosts();
		assertEquals(900.0, company.getMoney());
	}

	@Test
	public void testDoMonthlyCosts() throws Exception {
		company.setMoney(1000);
		company.doMonthlyCosts();
		assertEquals(990.0, company.getMoney());
	}

	@Test
	public void purchaseShipWithoutEnoughMoney() {
		company.setMoney(1);
		assertFalse(company.purchaseShip(shipClasses.get(0)));

	}

	@Test
	public void purchaseShipAndAssertThatCurrentMoneyAddsUp() {
		int money = 100000000;
		company.setMoney(money);
		assertTrue(company.purchaseShip(shipClasses.get(0)));
		assertEquals(2,company.getNumberOfShips());
		assertEquals(money - shipClasses.get(0).getPrice(), company.getMoney());
	}
}
