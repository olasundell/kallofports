package kop.ships;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/15/11
 * Time: 7:38 PM
 * To change this template use File | Settings | File Templates.
 */
@Root
public class EngineList {
	private static EngineList instance = null;
	@ElementList(inline = true)
	private List<Engine> engines;
	private static final URL ENGINELIST_FILENAME = ClassLoader.getSystemClassLoader().getResource("kop/ships/engines.xml");

	public EngineList() {
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
		if (instance == null) {
			try {
				URL r = ClassLoader.getSystemClassLoader().getResource("kop/ships/engines.xml");
				instance = (EngineList) ModelSerializer.readFromFile(r, EngineList.class);
			} catch (Exception e) {
				e.printStackTrace();
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
