package kop.ports;

import kop.cargo.*;
import kop.serialization.SerializationException;
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
	private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
//	@Element(name="typeName")
	private String typeName;
	@ElementList
	private final List<PortCargoTypeDestination> destinations;

	public PortCargoType() {
		destinations = new ArrayList<PortCargoTypeDestination>();
	}

	public PortCargoType(CargoType cargoType) {
		this();
		type = cargoType;
	}

	public Collection<PortCargoTypeDestination> getDestinations() {
		return destinations;
	}

	public void setDestinations(Collection<PortCargoTypeDestination> destinations) {
		this.destinations.addAll(destinations);
	}

	/**
	 * This needs to be run after all the ports are added to the PortsOfTheWorld portmap,
	 * otherwise we'll have a chicken-and-egg scenario when we try to retrieve PortProxy results for our destinations.
	 */
	public void calculateDestinationPorts(PortsOfTheWorld ports) throws SerializationException {

		Countries countries = null;

		countries = Countries.getInstance();

		PortProxy portProxy = null;
		for (PortCargoTypeDestination destination: destinations) {

			try {
//				if (continent.isContinent(destination.getName())) {
//				unlocode = continent.getPortForCargoType(destination.getName(), type);
//				} else
				if (countries.isCountry(destination.getName()))	{
					portProxy = ports.getPortForCargoType(destination.getName(), type).get(0).getProxy();
				}
				destination.setPortProxy(portProxy);
			} catch (NoSuchPortException e) {
				logger.error(String.format("Could not find port for PortCargoDestination %s", destination.getName()),e);
			} catch (Countries.NoSuchCountryException e) {
				logger.error(String.format("Could not find country for PortCargoDestination %s", destination.getName()),e);
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

}
