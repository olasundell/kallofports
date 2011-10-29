package kop.ships.engine;

import kop.serialization.ModelSerializer;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/15/11
 * Time: 7:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class EngineTest {
	Engine engine;
	@BeforeMethod
	public void setup() {
		engine = new Engine();
		engine.setBsfc(178);
		engine.setManufacturer("Deutz");
		engine.setModelName("TBD 645 L9");
		engine.setkW(3825);
	}

	@Test
	public void writeToDisk() throws Exception {
		String fileName="engines.xml";
		// TODO replace this factory with kop.serialization.ModelSerializer usage.
		Serializer serializer = new Persister();
		File result = new File(fileName);
		serializer.write(engine, result);
	}

	@Test
	public void writeEngineList() throws Exception {
		List<Engine> l = new ArrayList<Engine>();
		l.add(engine);

		EngineList el = new EngineList(l);
		ModelSerializer.saveToFile("enginelist.xml", EngineList.class, el);
	}

	@Test
	public void getFuelUsageShouldThrowExceptionForIllegalParameterData() {
		try {
			engine.getFuelUsage(-0.1);
			fail("engine.getFuelUsage() did not throw expected exception for garbage parameter data");
		} catch (IllegalArgumentException e) {
		}

		try {
			engine.getFuelUsage(1.1);
			fail("engine.getFuelUsage() did not throw expected exception for garbage parameter data");
		} catch (IllegalArgumentException e) {

		}
	}

	@Test
	public void getProxyShouldReturnInstance() {
		EngineProxy proxy = engine.getProxy();
		assertNotNull(proxy);
	}
}
