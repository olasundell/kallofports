package kop.cargo;

import kop.ports.Port;
import kop.ports.PortsOfTheWorld;
import kop.ships.ModelSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/3/11
 * Time: 11:48 AM
 * To change this template use File | Settings | File Templates.
 */
public class FreightMarket {
	private List<Freight> market;
	private static CargoTypeList cargoTypes;

	public FreightMarket() {
		market = new ArrayList<Freight>();
	}

	public void generateFreight(Port origin, Port destination, Cargo cargo) {
		Freight f = new Freight();

		f.setOrigin(origin);
		f.setDestination(destination);
		f.setCargo(cargo);

		market.add(f);
	}

	public List<Freight> getFreightFromPort(Port origin) {
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

	public static Cargo generateCargo(CargoType type) {
		CargoImpl cargo = new CargoImpl();
		Random random = new Random();
		cargo.setWeight(random.nextInt() % 100000 + 1);
		return cargo;
	}
}
