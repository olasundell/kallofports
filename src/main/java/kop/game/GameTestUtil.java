package kop.game;

import kop.cargo.*;
import kop.company.Company;
import kop.ports.NoRouteFoundException;
import kop.ports.NoSuchPortException;
import kop.ports.PortProxy;
import kop.ships.OutOfFuelException;
import kop.ships.ShipClassList;
import kop.ships.ShipnameAlreadyExistsException;
import kop.ships.blueprint.ShipBlueprint;
import kop.ships.model.ShipModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.measure.unit.SI;
import java.util.Locale;

/**
 * @author Ola Sundell
 */
public abstract class GameTestUtil {
	public static ShipModel[] shipModels;
	public static final String PORT_NAME = "Durban";
	public static final String DEST_PORT_NAME = "Singapore";

	public static synchronized Game setupInstanceForTest() throws ShipnameAlreadyExistsException {
		Logger logger = LoggerFactory.getLogger(GameTestUtil.class);

		logger.debug("Setting up a test player company");

		Locale.setDefault(new Locale("sv","se"));

//		Game g = Game.getInstance();
		Game g = new Game();
		Game.setInstance(g);

		g.resetPlayerCompany();
		g.getFreightMarket().resetFreightMarket();
		g.generateDailyFreights();

		Company playerCompany = g.getPlayerCompany();

		playerCompany.setMoney(1000000000);
		playerCompany.setName("Ares Freights Inc.");

		try {
			playerCompany.setHomePort(g.getPortByName(PORT_NAME).getProxy());
		} catch (NoSuchPortException e) {
			logger.error(String.format("Could not find port %s", PORT_NAME), e);
		}

		shipModels = createShips(g);

		playerCompany.addShip(shipModels[0]);
		playerCompany.addShip(shipModels[1]);

		try {
			playerCompany.purchaseShip(40, shipModels[2].getName(), shipModels[2].getShipClass());
		} catch (ShipnameAlreadyExistsException e) {
			logger.error("Tried to purchase a test ship, it failed miserably!", e);
			throw e;
		}

		playerCompany.addShip(shipModels[3]);
		playerCompany.addShip(shipModels[4]);

		playerCompany.addShip(shipModels[5]);

		addCargoAndSetSail(logger, g, playerCompany, PORT_NAME, DEST_PORT_NAME, shipModels[5]);
		return g;
	}

	private static void addCargoAndSetSail(Logger logger, Game g, Company playerCompany, String portName, String destPortName, ShipModel ship) {
		try {
			Cargo cargo = g.getFreightMarket().generateCargo(FreightMarket.getCargoTypes().getCargoTypeByPackaging(CargoType.Packaging.wetbulk));
			PortProxy destProxy = g.getPortByName(destPortName).getProxy();
			Freight f = g.getFreightMarket().generateFreight(playerCompany.getHomePort(),
					destProxy,
					cargo);
			ship.addFreight(f);
			ship.setSail(ship.getCurrentPosition().getCurrentPort(), destProxy, Math.floor(ship.getMaxSpeed() * 0.8));

			for (int i=0;i<20;i++) {
				ship.travel();
			}

		} catch (NoSuchPortException e) {
			logger.error(String.format("Could not find port %s", portName), e);
		} catch (OutOfFuelException e) {
			logger.error("Out of fuel!", e);
		} catch (CouldNotLoadCargoTypesException e) {
			logger.error("Could not find load cargo types!", e);
		} catch (NoRouteFoundException e) {
			logger.error(String.format("Could not find route between ports %s and %s", portName, destPortName), e);
		}
	}

	private static ShipModel[] createShips(Game g) {
		ShipModel[] ships = new ShipModel[6];
		ships[0] = ShipModel.createShip("Ares Ardour", g.getShipClasses().get(ShipBlueprint.ShipType.lngcarrier, 0));
		ships[1] = ShipModel.createShip("Ares Earnest", g.getShipClasses().get(ShipBlueprint.ShipType.container, 0));
		ships[2] = ShipModel.createShip("Ares Fervour", g.getShipClasses().get(ShipBlueprint.ShipType.container, 0));
		ships[3] = ShipModel.createShip("Ares Passion", g.getShipClasses().get(ShipBlueprint.ShipType.container, 0));
		ships[4] = ShipModel.createShip("Ares Spirit", g.getShipClasses().get(ShipBlueprint.ShipType.container, 0));
		ships[5] = ShipModel.createShip("Ares Zeal", g.getShipClasses().get(ShipBlueprint.ShipType.tanker, 0));

		for (int i=0;i<ships.length;i++) {
			ships[i].setCurrentPort(g.getPlayerCompany().getHomePort());
		}

		return ships;
	}
}
