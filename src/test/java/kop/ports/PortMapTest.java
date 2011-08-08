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
