package kop.ui;

import kop.game.Game;
import kop.game.GameTestUtil;
import kop.ships.ShipnameAlreadyExistsException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.uispec4j.Panel;
import org.uispec4j.UISpec4J;

/**
 * @author Ola Sundell
 */
public class WorldFreightMarketWindowTest extends KopUITest {
	private WorldFreightMarketWindow window;

	public WorldFreightMarketWindowTest() throws ShipnameAlreadyExistsException {
		super();
		window = new WorldFreightMarketWindow();
	}

	@Test
	public void checkTable() {
		Panel panel = new Panel(window.getContentPane());
		// TODO writeme
	}
}
