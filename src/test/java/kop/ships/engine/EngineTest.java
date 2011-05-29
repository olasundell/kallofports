package kop.ships.engine;

import kop.ships.ModelSerializer;
import kop.ships.engine.Engine;
import kop.ships.engine.EngineList;
import org.junit.Before;
import org.junit.Test;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/15/11
 * Time: 7:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class EngineTest {
	Engine engine;
	@Before
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
		// TODO replace this factory with kop.ships.ModelSerializer usage.
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
}
