package kop.game;

import kop.ports.NoSuchPortException;
import kop.ships.ShipClassList;
import kop.ships.blueprint.ShipBlueprint;
import kop.ships.model.ShipModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

		String portName = "Durban";

		try {
			g.getPlayerCompany().setHomePort(g.getPortByName(portName).getProxy());
		} catch (NoSuchPortException e) {
			Logger logger = LoggerFactory.getLogger(GameTestUtil.class);
			logger.error(String.format("Could not find port %s", portName));
		}

		g.getPlayerCompany().addShip(ShipModel.createShip("Ares Ardour", g.getShipClasses().get(ShipBlueprint.ShipType.lngcarrier, 0)));
		g.getPlayerCompany().addShip(ShipModel.createShip("Ares Fervor", g.getShipClasses().get(ShipBlueprint.ShipType.container, 0)));
	}
}
