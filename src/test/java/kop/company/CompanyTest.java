package kop.company;

import kop.ships.ContainerShipModel;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/13/11
 * Time: 9:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class CompanyTest {
	private Company company;

	@Before
	public void setup() {
		company = new Company();

		ContainerShipModel shipModel = new ContainerShipModel();
		shipModel.getBlueprint().setDailyCost(100);
		company.addShip(shipModel);

		Loan loan = new Loan(1000,12);
		company.addLoan(loan);
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
}
