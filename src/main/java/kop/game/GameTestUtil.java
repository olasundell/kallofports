package kop.game;

import kop.ports.NoSuchPortException;

/**
 * @author Ola Sundell
 */
public class GameTestUtil {
	public static void setupInstanceForTest() {
		Game g = Game.getInstance();

		g.resetPlayerCompany();

		g.generateDailyFreights();
		g.getPlayerCompany().setMoney(1000000000);
		g.getPlayerCompany().setName("Testy Freights Inc.");
		try {
			g.getPlayerCompany().setHomePort(g.getPortByName("Durban").getProxy());
		} catch (NoSuchPortException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}
	}
}
