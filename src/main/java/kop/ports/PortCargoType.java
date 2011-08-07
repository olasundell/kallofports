package kop.ports;

import com.sun.java.swing.plaf.gtk.GTKConstants;
import kop.cargo.*;
import kop.game.Game;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * This will have more logic and attributes later.
 * TODO add amount produced per time unit.
 * TODO add favourite destinations for a certain cargo type.
 * @author Ola Sundell
 */
@Root(name = "export")
public class PortCargoType {
	private CargoType type;
	private org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
//	@Element(name="typeName")
	private String typeName;
	private List<PortCargoTypeDestination> destinations;

	public PortCargoType() {
		destinations = new ArrayList<PortCargoTypeDestination>();
	}

	public PortCargoType(CargoType cargoType) {
		this();
		type = cargoType;
	}

	@ElementList
	public Collection<PortCargoTypeDestination> getDestinations() {
		return destinations;
	}

	@ElementList
	public void setDestinations(Collection<PortCargoTypeDestination> destinations) {
		this.destinations.addAll(destinations);
	}

	/**
	 * This needs to be run after all the ports are added to the PortsOfTheWorld portmap,
	 * otherwise we'll have a chicken-and-egg scenario when we try to retrieve PortProxy results for our destinations.
	 * @param ports we cannot use Game.getInstance() since we're setting up, remember?
	 */
	public void calculateDestinationPorts(PortsOfTheWorld ports) {
		Continent continent = new Continent();
		Country country = new Country();

		String unlocode = "";
		for (PortCargoTypeDestination destination: destinations) {
			if (continent.isContinent(destination.getName())) {
				unlocode = continent.getPortForCargoType(destination.getName(), type);
			} else if (country.isCountry(destination.getName()))	{
				unlocode = country.getPortForCargoType(destination.getName(), type);
			}

			try {
				destination.setPortProxy(ports.getPortByUnlocode(unlocode).getProxy());
			} catch (NoSuchPortException e) {
				logger.error(String.format("Could not find port for PortCargoDestination %s, tried unlocode %s", destination.getName(), unlocode),e);
			}
		}
	}

	@Element
	public String getTypeName() {
		return type.getName();
	}

	@Element
	public void setTypeName(String typeName) {
		try {
			type = FreightMarket.getCargoTypes().getTypeByName(typeName);
		} catch (CouldNotLoadCargoTypesException e) {
			logger.error("Could not load cargo types", e);
		} catch (NoSuchCargoTypeException e) {
			logger.error("Could not find cargo type.", e);
		}
	}

	public CargoType getType() {
		return type;
	}

	// TODO these classes need a LOT of refactoring. Lots!
	private static class Continent {
		private String[] arr = {
				"North America",
				"South America",
				"Oceania",
				"Africa",
				"Middle East",
				"Asia",
				"Europe"
		};

		public boolean isContinent(String name) {
			for (String continent: arr) {
				if (name.equalsIgnoreCase(continent)) {
					return true;
				}
			}

			return false;
		}

		public String getPortForCargoType(String name, CargoType type) {
			return "";
		}
	}

	private static class Country {
		private String[] arr = {
				"Japan",
				"Korea"
		};

		public boolean isCountry(String name) {
			for (String country: arr) {
				if (name.equalsIgnoreCase(country)) {
					return true;
				}
			}

			return false;
		}

		public String getPortForCargoType(String name, CargoType type) {
			if (name.equals("Japan")) {
				return "JP TYO";
			} else if (name.equals("Korea")) {
				return "KR PUS";
			} else {
				return "";
			}
		}
	}
}
