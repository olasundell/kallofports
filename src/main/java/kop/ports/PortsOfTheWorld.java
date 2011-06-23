package kop.ports;

import kop.game.Game;
import kop.ships.ModelSerializer;

import java.util.*;

/**
 * Contains all ports in the world and some util methods as well!
 */
public class PortsOfTheWorld {
	private PortMap ports;
	// TODO remove this.

	public PortsOfTheWorld() throws Exception {
		populatePorts();
	}

	public void populatePorts() throws Exception {
		if (ports!=null) {
			return;
		}

		ports = (PortMap) ModelSerializer.readFromFile("kop/ports/ports.xml", PortMap.class);
	}

	public static PortMap getPorts() throws Exception {
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

	public Port getPortByName(String portName) throws NoSuchPortException {
		Port port = ports.get(portName);

		if (port == null) {
			throw new NoSuchPortException("There's no port named " + portName);
		}

		return port;
	}

	public Collection<Port> getPortsAsList() {
		return ports.values();
	}

	public Port getPortByUnlocode(String unlocode) throws NoSuchPortException {
		for (Port p: ports.values()) {
			if (p.getUnlocode().equals(unlocode)) {
				return p;
			}
		}

		throw new NoSuchPortException(String.format("Could not find port based on unlocode %s", unlocode));
	}

	public PortProxy getRandomDestination(Port origin) {
		ArrayList<Port> portList = new ArrayList<Port>(getPortsAsList());

		int randomPortNum = Game.getInstance().getRandom().nextInt((portList.size() - 1));

		// if we happen to pick the origin origin, move up a notch.
		if (randomPortNum >= portList.indexOf(origin)) {
			randomPortNum++;
		}

		return portList.get(randomPortNum).getProxy();
	}
}
