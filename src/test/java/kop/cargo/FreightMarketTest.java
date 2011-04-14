package kop.cargo;

import kop.game.Game;
import kop.ports.Port;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/13/11
 * Time: 8:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class FreightMarketTest {
	@Test
	public void testGenerateFreight() throws Exception {
		FreightMarket market = new FreightMarket();
		Port origin = new Port();
		Port destination = new Port();
		CargoType type = new CargoType();
		Game instance = Game.getInstance();
		Date currentDate = instance.getCurrentDate();
		Cargo cargo = new CargoImpl(10, type, 123.4, currentDate);

		market.generateFreight(origin, destination, cargo);
		List<Freight> list = market.getFreightFromPort(origin);
		assertNotNull(list);
		assertTrue(list.size() == 1);
	}
}
