package kop.ports;

import kop.map.Route;
import kop.ships.ModelSerializer;
import kop.ships.ShipModel;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

/**
 * Contains all ports in the world and some util methods as well!
 */
public class PortsOfTheWorld {
	private PortMap ports;
	// TODO remove this.
	private List<Distance> distances;

	public PortsOfTheWorld() {
		distances = new ArrayList<Distance>();
		populatePorts();
	}

	public void populatePorts()  {
		if (ports!=null) {
			return;
		}

		try {
//			ports = factory.createPorts();
			ports = (PortMap) ModelSerializer.readFromFile("kop/ports/ports.xml", PortMap.class);
		} catch (Exception e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}
	}

	public static PortMap getPorts() {
		PortsOfTheWorld instance = new PortsOfTheWorld();
		instance.populatePorts();
		return instance.ports;
	}

	public Port putPort(String name) {
		Port p = new Port();
		p.setName(name);
		ports.put(name, p);

		return p;
	}

	/**
	 * @param origin
	 * @param destination
	 * @param ship
	 * @return
	 * @throws NoRouteFoundException
	 * TODO this needs to be rewritten according to the new pathfinding.
	 */

	public double getDistance(Port origin, Port destination, ShipModel ship) throws NoRouteFoundException {
		Route route = Route.getRoute(origin.getUnlocode(), destination.getUnlocode(), !ship.isPostPanamax(), !ship.isPostSuezmax());

		if (route == null) {
			throw new NoRouteFoundException("Could not find route between the ports "+origin.getName() + " and " + destination.getName());
		}

		return route.getNm();
	}

	public Port getPortByName(String portName) throws NoSuchPortException {
		Port port = ports.get(portName);

		if (port == null) {
			throw new NoSuchPortException("There's no port named " + portName);
		}

		return port;
	}

	/**
	 * @deprecated Not relevant anymore, we're using A* SPF routing these days.
	 * @param origin
	 * @param destination
	 * @param nm
	 */
	public void setDistance(Port origin, Port destination, int nm) {
		Distance d = new Distance(origin, destination);
		d.addRoute(nm, false, false);
		distances.add(d);
	}

	public Collection<Port> getPortsAsList() {
		return ports.values();
	}
}
