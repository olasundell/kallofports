package kop.ports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/3/11
 * Time: 11:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class PortsOfTheWorld {
	private Map<String, Port> ports;

	public PortsOfTheWorld() {
		ports = new HashMap<String, Port>();
	}

	public Port putPort(String name) {
		Port p = new Port();
		p.setName(name);
		ports.put(name, p);

		return p;
	}
}
