package kop.ui;

import kop.game.Game;
import kop.game.GameTestUtil;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.uispec4j.Panel;
import org.uispec4j.UISpec4J;

import javax.swing.*;

import static org.testng.AssertJUnit.assertEquals;

/**
 * @author Ola Sundell
 */
public class CompanyInfoWindowTest {

	private static CompanyInfoWindow window;

	static {
		UISpec4J.init();
	}

	@BeforeClass
	public static void beforeClass() {
		GameTestUtil.setupInstanceForTest();
		window = new CompanyInfoWindow();
		window.stateChanged();
	}

	@Test
	public void checkInfoExists() {
		Panel panel = new Panel(window.getContentPane());

		checkLabel(panel, CompanyInfoWindow.CURRENT_FUNDS, Game.getInstance().getPlayerCompany().getMoney());
		checkLabel(panel, CompanyInfoWindow.GROSS_WORTH, Game.getInstance().getPlayerCompany().getGrossWorth());
		checkLabel(panel, CompanyInfoWindow.NET_WORTH, Game.getInstance().getPlayerCompany().getNetWorth());
		checkLabel(panel, CompanyInfoWindow.NUMBER_OF_SHIPS, Game.getInstance().getPlayerCompany().getNumberOfShips());
		checkLabel(panel, CompanyInfoWindow.TOTAL_LOANS, Game.getInstance().getPlayerCompany().getTotalLoans());
	}

	private void checkLabel(Panel panel, String componentName, int value) {
		JLabel label = panel.findSwingComponent(JLabel.class, componentName);
		assertEquals(String.valueOf(value), label.getText());
	}

	private void checkLabel(Panel panel, String componentName, double value) {
		JLabel label = panel.findSwingComponent(JLabel.class, componentName);
		assertEquals(String.format(KopWindow.MONEY_TEXT_FORMAT, value), label.getText());
	}
}
