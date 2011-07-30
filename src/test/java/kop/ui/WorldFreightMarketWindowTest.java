package kop.ui;

import kop.game.Game;
import kop.game.GameTestUtil;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.uispec4j.Panel;
import org.uispec4j.UISpec4J;

/**
 * @author Ola Sundell
 */
public class WorldFreightMarketWindowTest {
	private static WorldFreightMarketWindow window;

	static {
		UISpec4J.init();
	}

	@BeforeClass
	public static void beforeClass() {
		GameTestUtil.setupInstanceForTest();
		window = new WorldFreightMarketWindow();
	}

	@Test
	public void checkTable() {
		Panel panel = new Panel(window.getContentPane());

	}
}
