package kop.ports;

import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

import java.util.HashMap;
import java.util.Map;

/**
 * A map of all ports, with the name as key.
 */
@Root
public class PortMap extends HashMap<String, Port> {
	@ElementMap
	public Map<String, Port> getMap() {
		return this;
	}

	@ElementMap
	public void setMap(Map<String, Port> map) {
		this.putAll(map);
	}
}
