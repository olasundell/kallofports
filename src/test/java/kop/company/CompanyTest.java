package kop.company;

import kop.game.Game;
import kop.game.GameTestUtil;
import kop.ports.NoSuchPortException;
import kop.ships.*;
import kop.ships.blueprint.ShipBlueprint;
import kop.ships.model.ContainerShipModel;
import kop.ships.model.ShipModel;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertEquals;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/13/11
 * Time: 9:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class CompanyTest {
	private static final String DUMMY_SHIP_NAME = "Dummy container ship";
	private Company company;
	private List<ShipClass> shipClasses;

	@BeforeClass
	public static void resetBeforeTests() {
		Game.getInstance().resetPlayerCompany();
	}

	@BeforeMethod
	public void setup() throws NoSuchPortException, ShipnameAlreadyExistsException {
		Game.getInstance().resetPlayerCompany();
		company = GameTestUtil.setupInstanceForTest().getPlayerCompany();
		shipClasses = Game.getInstance().getShipClasses();
//		Game.getInstance().resetPlayerCompany();
//		company = Game.getInstance().getPlayerCompany();
//		company.setHomePort(Game.getInstance().getPortByName("Durban").getProxy());
//
//		ContainerShipModel shipModel = (ContainerShipModel) ShipModel.createShip(DUMMY_SHIP_NAME,
//				ShipClass.getShipClasses().get(ShipBlueprint.ShipType.container, 0));
//		shipModel.getBlueprint().setDailyCost(100);
//		company.addShip(shipModel);
//
//		Loan loan = new Loan(1000,12);
//		company.addLoan(loan);
//		shipClasses = ShipClass.getShipClasses();
	}

	@AfterClass
	public static void resetAfterTests() {
		Game.getInstance().resetPlayerCompany();
	}

	@Test
	public void testMoveShips() throws Exception {
		company.moveShips();
	}

	@Test
	public void testDoDailyCosts() throws Exception {
		company.setMoney(1000);
		company.doDailyCosts();
		assertEquals(1000 - company.getDailyCosts(), company.getMoney());
	}

	@Test
	public void testDoMonthlyCosts() throws Exception {
		company.setMoney(1000);
		company.doMonthlyCosts();
		assertEquals(1000 - company.getMonthlyCosts(), company.getMoney());
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
		int oldNoOfShips = company.getNumberOfShips();
		assertTrue(company.purchaseShip(shipClasses.get(0)));
		assertEquals(oldNoOfShips+1,company.getNumberOfShips());
		assertEquals(money - shipClasses.get(0).getPrice(), company.getMoney());
	}

	@Test
	public void purchaseShipWithLoans() throws ShipnameAlreadyExistsException {
		int money = 100000000;
		company.setMoney(money);
		int loanPercentage = 60;

		assertEquals(1, company.getLoans().size());


		int numberOfShips = company.getNumberOfShips();
		company.purchaseShip(loanPercentage, "Dummy", shipClasses.get(0));

		assertEquals(numberOfShips + 1, company.getNumberOfShips());

		double v = ((100.0 - loanPercentage) / 100.0) * shipClasses.get(0).getPrice();

		assertEquals(money - v, company.getMoney());
		assertEquals(2, company.getLoans().size());
		assertEquals(shipClasses.get(0).getPrice() - v, company.getLoans().get(1).getCurrentDebt());
	}

	@Test
	public void purchaseShipWithExistingNameShouldFail() {
		boolean exceptionThrown = false;
		try {
			Game.getInstance().getPlayerCompany().purchaseShip(100, DUMMY_SHIP_NAME, shipClasses.get(0));
			Game.getInstance().getPlayerCompany().purchaseShip(100, DUMMY_SHIP_NAME, shipClasses.get(0));
		} catch (ShipnameAlreadyExistsException e) {
			exceptionThrown = true;
		}

		assertTrue(exceptionThrown, "Exception wasn't thrown!");
	}

	@Test
	public void findShipsDockedAtASpecificPort() {
		List<ShipModel> list = company.findShipsInPort(company.getHomePort());
		assertNotNull(list);
		List<ShipModel> shipsThatShouldBeThere = new ArrayList<ShipModel>();
		for (ShipModel s: company.getShips()) {
			if (s.isInPort() && s.getCurrentPosition().getCurrentPort().equals(company.getHomePort())) {
				shipsThatShouldBeThere.add(s);
			}
		}
		assertEquals(shipsThatShouldBeThere, list);
	}
}
