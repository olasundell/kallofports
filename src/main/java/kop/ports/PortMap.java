package kop.ports;

import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/26/11
 * Time: 7:56 PM
 * To change this template use File | Settings | File Templates.
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
