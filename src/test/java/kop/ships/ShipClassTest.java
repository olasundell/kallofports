package kop.ships;

import org.junit.Test;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;

import static junit.framework.Assert.assertEquals;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/9/11
 * Time: 9:46 AM
 * To change this template use File | Settings | File Templates.
 */
public class ShipClassTest {
	@Test
	public void testSerializeDeserialize() throws Exception {
		ShipClass c = new ShipClass(new Ship("foobar"), 10.0, "Feeder");
		Serializer serializer = new Persister();
		File result = new File("foobar.xml");
		serializer.write(c, result);
		File source = new File("foobar.xml");
		ShipClass r = serializer.read(ShipClass.class, source);
		assertEquals(c.getClassName(), r.getClassName());
	}
}