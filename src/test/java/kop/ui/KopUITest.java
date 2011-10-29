package kop.ui;

import kop.game.Game;
import kop.game.GameTestUtil;
import org.uispec4j.UISpec4J;

/**
 * @author ola
 */
public class KopUITest {
	protected Game gameInstance;

	static {
		System.setProperty("uispec4j.test.library","testng");
		UISpec4J.init();
	}

	public KopUITest() {
		gameInstance = GameTestUtil.setupInstanceForTest();
	}
}
