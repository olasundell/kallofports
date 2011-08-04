package kop.ports;

import kop.cargo.*;
import kop.game.Game;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

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
	private List<Destination> destinations;

	public PortCargoType() {
		destinations = new ArrayList<Destination>();
	}

	public PortCargoType(CargoType cargoType) {
		this();
		type = cargoType;
	}

	@ElementList
	public Collection<Destination> getDestinations() {
		return destinations;
	}

	@ElementList
	public void setDestinations(Collection<Destination> destinations) {
		this.destinations.addAll(destinations);
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

	private static class Destination {
		@Element
		private String name;
		@Element
		private double yearlyAmount;
	}
}
