package kop.ports;

import kop.serialization.ModelSerializer;
import kop.serialization.SerializationException;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertEquals;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/26/11
 * Time: 7:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class PortMapTest {
	@Test
	public void serializePortMap() throws SerializationException {
		PortMap map = new PortMap();

		Port port = new Port();
		port.setName("London");
		port.setUnlocode("GB LON");
		map.put("London", port);
		port.setLatitude(51.30);
		ModelSerializer.saveToFile("portmap.xml",PortMap.class, map);
	}

//	@Test
	public void deserializePortMap() throws SerializationException {
		PortMap map = (PortMap) ModelSerializer.readFromFile("portmap.xml", PortMap.class);
		assertNotNull(map);
		Port london = map.get("London");
		assertNotNull(london);
		assertEquals(51.30,london.getLatitude());
	}

	@Test
	public void allPortsShouldHaveCompleteInformation() throws SerializationException {
		for (Port p: PortsOfTheWorld.getPorts().values()) {
			assertNotNull(p);
			assertNotNull(String.format("Port %s lacks latitude!",p.toString()),p.latitude);
			assertNotNull(String.format("Port %s lacks longitude!",p.toString()),p.longitude);
			assertFalse(p.getLatitude() == 0.0);
			assertFalse(p.getLongitude() == 0.0);
		}
	}
}
