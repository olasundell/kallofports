import kop.cargo.*;
import kop.ports.Port;
import kop.ports.PortsOfTheWorld;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/3/11
 * Time: 12:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestItAll {
	public void freightMarketTest() {
		PortsOfTheWorld world = new PortsOfTheWorld();
		Port origin = world.putPort("Rotterdam");
		Port destination = world.putPort("London");
		FreightMarket market = new FreightMarket();
//		Cargo cargo = new CargoImpl(1, CargoType.FOODSTUFFS, 1.0, GregorianCalendar.getInstance().getTime());

//		market.generateFreight(origin, destination, cargo);

//		List<Freight> list = market.getFreightFromPort(origin);
	}
}
