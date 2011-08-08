package kop.ports;

import kop.cargo.FreightMarket;
import kop.game.Game;
import kop.serialization.ModelSerializer;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;


/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 5/1/11
 * Time: 10:04 AM
 * To change this template use File | Settings | File Templates.
 */
public class PortTest {
	@Test
	public void serializePortWithLatLong() throws Exception {
		Port port = new Port();
		port.setName("Aberdeen");
		port.setUnlocode("GB ABD");
		port.setLatitude("57", "9", "N");
		port.setLongitude("2", "5", "W");
		port.addAllCargoTypes(FreightMarket.getCargoTypes().find("crude"));
		assertEquals(port.getCountryCode(), "GB");
		ModelSerializer.saveToFile("portwithlatlongandcargotype.xml", Port.class, port);
	}

	@Test
	public void noSuchPortExceptionShouldBeThrown() {
		boolean thrown = false;
		try {
			Game.getInstance().getPortByName("blahablaha");
		} catch (NoSuchPortException e) {
			thrown = true;
		}
		assertTrue("Exception wasn't thrown when searching for non-existing port!", thrown);
	}
}
