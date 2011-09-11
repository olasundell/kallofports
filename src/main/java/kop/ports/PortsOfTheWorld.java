package kop.ports;

import kop.cargo.CargoType;
import kop.game.Game;
import kop.serialization.ModelSerializer;
import kop.serialization.SerializationException;

import java.util.*;

/**
 * Contains all ports in the world and some util methods as well!
 */
public class PortsOfTheWorld {
	private PortMap ports;

	public PortsOfTheWorld() throws SerializationException {
//		populatePorts();
		ports = null;
	}

	public final void populatePorts() throws SerializationException {
		if (ports!=null) {
			return;
		}

//		ports = (PortMap) ModelSerializer.readFromFile("kop/ports/ports.xml", PortMap.class);
		ModelSerializer<Port> serializer = new ModelSerializer<Port>();
		List<Port> list = serializer.readFromDirectory("src/main/resources/kop/ports/World",Port.class);

		ports = new PortMap();

		for (Port p: list) {
			ports.put(p.getName(), p);
		}

		for (Port p: ports.getMap().values()) {
			p.calculateDestinationPorts(this);
		}
	}

	public static PortMap getPorts() throws SerializationException {
		PortsOfTheWorld instance = new PortsOfTheWorld();
		instance.populatePorts();
		return instance.ports;
	}

	public List<Port> getPortsForCountry(String country) throws NoSuchPortException, Countries.NoSuchCountryException {
		List<Port> retList = new ArrayList<Port>();
		Countries countries = null;

		try {
			countries = Countries.getInstance();
		} catch (SerializationException e) {
			throw new NoSuchPortException(e);
		}

		String countryCode = countries.getCountryCode(country);

		for (Port p: ports.values()) {
			if (p.getCountryCode().equals(countryCode)) {
				retList.add(p);
			}
		}

		return retList;
	}

	public List<Port> getPortForCargoType(String countryName, CargoType type) throws Countries.NoSuchCountryException, NoSuchPortException {
		// TODO filter ports.
		return getPortsForCountry(countryName);
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
