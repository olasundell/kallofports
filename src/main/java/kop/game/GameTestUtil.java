package kop.game;

import kop.ports.NoSuchPortException;
import kop.ships.ShipClassList;
import kop.ships.blueprint.ShipBlueprint;
import kop.ships.model.ShipModel;

/**
 * @author Ola Sundell
 */
public class GameTestUtil {
	public static void setupInstanceForTest() {
		Game g = Game.getInstance();

		g.resetPlayerCompany();

		g.generateDailyFreights();
		g.getPlayerCompany().setMoney(1000000000);
		g.getPlayerCompany().setName("Ares Freights Inc.");
		try {
			g.getPlayerCompany().setHomePort(g.getPortByName("Durban").getProxy());
		} catch (NoSuchPortException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}

		g.getPlayerCompany().addShip(ShipModel.createShip("Ares Ardour", g.getShipClasses().get(ShipBlueprint.ShipType.lngcarrier, 0)));
		g.getPlayerCompany().addShip(ShipModel.createShip("Ares Fervor", g.getShipClasses().get(ShipBlueprint.ShipType.container, 0)));
	}
}
