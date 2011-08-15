package kop.ui;

import kop.company.Company;
import kop.game.Game;
import kop.game.GameTestUtil;
import kop.ports.NoSuchPortException;
import kop.ships.model.ShipModel;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.uispec4j.*;

import javax.swing.*;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;


/**
 * @author Ola Sundell
 */
public class PortWindowTest {
	private PortWindow window;
	private Panel panel;
	private Panel shipInfoPanel;

	static {
		System.setProperty("uispec4j.test.library","testng");
		UISpec4J.init();
	}

//	@BeforeClass
//	public static void beforeClass() {
//		GameTestUtil.setupInstanceForTest();
//	}

	@BeforeMethod
	public void beforeMethod() throws NoSuchPortException {
		Game game = GameTestUtil.setupInstanceForTest();
		window = new PortWindow(Game.getInstance().getPortByName("Durban").getProxy());
		panel = new Panel(window.getContentPane());
		shipInfoPanel = new Panel(panel.findSwingComponent(JPanel.class, "shipInfoPanel"));
	}

	@Test
	public void selectShip() {
		ListBox shipsInPort = panel.getListBox();

		shipsInPort.selectIndex(0);
		ShipModel ship = (ShipModel) shipsInPort.getAwtComponent().getSelectedValue();

		String shipName = shipInfoPanel.findSwingComponent(JLabel.class, "shipName").getText();
		assertEquals(shipName, ship.getName());
		// TODO add more assertions here.
	}

	@Test
	public void portFreightTableShouldContainFreights() {
		Table portFreightTable = panel.getTable("portFreightTable");
		assertTrue(portFreightTable.getRowCount() > 0);
	}

//	@Test
	// TODO doesn't work.
	public void charterFreight() {
		ListBox shipsInPort = panel.getListBox();
		Table portFreightTable = panel.getTable("portFreightTable");
		Table shipFreightTable = panel.getTable("shipFreightTable");
		Button charterButton = panel.getButton("charterButton");

		// select ship
		shipsInPort.selectIndex(0);
		ShipModel ship = (ShipModel) shipsInPort.getAwtComponent().getSelectedValue();

		portFreightTable.selectRow(0);
		int rowCountBeforeCharter = portFreightTable.getRowCount();

		assertEquals(shipFreightTable.getRowCount(), 0);
		assertEquals(ship.getFreights().size(), 0);

		charterButton.click();

		int rowCountAfterCharter = portFreightTable.getRowCount();

		assertEquals(rowCountAfterCharter, rowCountBeforeCharter - 1);
		assertEquals(shipFreightTable.getRowCount(), 1);
		assertEquals(ship.getFreights().size(), 1);
	}
}
