package kop.ports;

import kop.game.Game;
import kop.ships.ModelSerializer;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

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
		port.setCountryCode("GB");
		port.setUnlocode("GB ABD");
		port.setLatitude("57","9","N");
		port.setLongitude("2","5","W");
		ModelSerializer.saveToFile("portwithlatlong.xml", Port.class, port);
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