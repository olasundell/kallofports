package kop.cargo;

import kop.game.Game;
import kop.ports.Port;
import kop.ports.PortProxy;
import kop.ports.PortsOfTheWorld;
import kop.ships.ModelSerializer;

import java.util.*;

/**
 * The FreightMarket contains all Freights which are in Ports. Ie, those that are on ships should not be here.
 */
public class FreightMarket {
	private List<Freight> market;
	private static CargoTypeList cargoTypes;
	private Game gameInstance;

	public FreightMarket() {
		market = new ArrayList<Freight>();
	}

	public FreightMarket(Game game) {
		this();
		gameInstance = game;
	}

	public Freight generateFreight(PortProxy origin, PortProxy destination, Cargo cargo) {
		Freight f = new Freight();

		f.setOrigin(origin);
		f.setDestination(destination);
		f.setCargo(cargo);

		market.add(f);

		return f;
	}

	public List<Freight> getFreights() {
		return market;
	}

	public List<Freight> getFreightFromPort(PortProxy origin) {
		List<Freight> list = new ArrayList<Freight>();

		for (Freight f: market) {
			if (f.getOrigin().equals(origin)) {
				list.add(f);
			}
		}

		return list;
	}

	public static CargoTypeList getCargoTypes() throws Exception {
		if (cargoTypes == null) {
			cargoTypes = (CargoTypeList) ModelSerializer.readFromFile("kop/cargo/cargotypes.xml", CargoTypeList.class);
		}

		return cargoTypes;
	}

	public Cargo generateCargo(CargoType type) {
		CargoImpl cargo = new CargoImpl(type);
		Random random;
		if (gameInstance == null) {
			random = Game.getInstance().getRandom();
		} else {
			random = gameInstance.getRandom();
		}

		cargo.setWeight((int) Math.abs(random.nextGaussian() * 10000));

		cargo.setPricePerVolume(Math.abs(random.nextGaussian() * 10));
		cargo.setDeadline(Game.getInstance().getFutureDate(1 + (int) Math.abs(random.nextGaussian()*100)));
		return cargo;
	}
}
