package kop.serialization;

import kop.ports.Port;
import kop.ports.PortTest;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.util.List;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * @author Ola Sundell
 */
public class ModelSerializerTest {
	@Test
	public void readFromDirectory() throws MalformedURLException, SerializationException {
		ModelSerializer<Port> serializer = new ModelSerializer<Port>();
		List<Port> list = serializer.readFromDirectory("src/main/resources/kop/ports/World", Port.class);

		assertNotNull(list);
		assertTrue(list.size() > 2);
		Port first = list.get(0);
		assertNotNull(first);
	}
}
