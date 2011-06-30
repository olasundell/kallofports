package kop.ships.engine;

import kop.ships.ModelSerializer;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A list of engines, meant as a singleton.
 * @author Ola Sundell
 */
@Root
public class EngineList {
	private static EngineList instance = null;
	@ElementList(inline = true)
	private List<Engine> engines;
	private static final URL ENGINELIST_FILENAME = ClassLoader.getSystemClassLoader().getResource("kop/ships/engines.xml");
	private Logger logger;

	public EngineList() {
		logger = LoggerFactory.getLogger(this.getClass());
		engines = new ArrayList<Engine>();
	}

	public EngineList(List<Engine> l) {
		this();
		engines.addAll(l);
	}

	public Engine getByName(String modelName) {
		for (Engine e: engines) {
			if (e.getModelName().equals(modelName)) {
				return e;
			}
		}
		return null;
	}

	public static EngineList getInstance() {
		Logger logger = LoggerFactory.getLogger(EngineList.class);
		if (instance == null) {
			try {
				URL r = ClassLoader.getSystemClassLoader().getResource("kop/ships/engines.xml");
				instance = (EngineList) ModelSerializer.readFromFile(r, EngineList.class);
			} catch (Exception e) {
				logger.error("Could not create engine list",e);
			}
		}
		return instance;
	}

	/**
	 * Used for test purposes, always returns first engine in the list.
	 * @return
	 */
	public Engine getAnEngineForTest() {
		return engines.get(0);
	}
}
