package kop.ships;

import kop.serialization.ModelSerializer;
import kop.serialization.SerializationException;
import kop.ships.blueprint.ShipBlueprint;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
@Root
public class ShipClass {
	@Element
	private double price;
	@Attribute
	private String className;
	@Element
	private ShipBlueprint ship;
	// TODO this should be an element in the ship class XML.
//	@Element
	private int maxLoanPercent = 60;

	private static ShipClassList shipClasses;
	private Logger logger;

	public ShipClass() {
		logger = LoggerFactory.getLogger(this.getClass());
	}

	public ShipClass(ShipBlueprint ship, double price, String className) {
		this();
		this.price = price;
		this.className = className;
		this.ship = ship;
	}

	public static ShipClassList getShipClasses() {
		if (shipClasses == null) {
			String resourceName = "kop/ships/shipclasses.xml";
			try {
				shipClasses = (ShipClassList) ModelSerializer.readFromFile(resourceName, ShipClassList.class);
			} catch (SerializationException e) {
				Logger logger = LoggerFactory.getLogger(ShipClass.class);
				logger.error(String.format("Could not deserialise ship classes from file %s", resourceName), e);
				return null;
			}
		}

		return shipClasses;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public ShipBlueprint.ShipType getClassType() {
		return ship.getType();
	}

	public ShipBlueprint getBlueprint() {
		return ship;
	}

	public int getMaxLoanPercent() {
		return maxLoanPercent;
	}
}
