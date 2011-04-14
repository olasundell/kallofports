package kop.cargo;

import kop.ports.Port;
import kop.ports.PortsOfTheWorld;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/3/11
 * Time: 11:48 AM
 * To change this template use File | Settings | File Templates.
 */
public class FreightMarket {
	private List<Freight> market;

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
}
