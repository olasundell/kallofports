package kop.ships;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.List;

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

	private static ShipClassList shipClasses;

	public ShipClass() {

	}

	public ShipClass(ShipBlueprint ship, double price, String className) {
		this.price = price;
		this.className = className;
		this.ship = ship;
	}

	public static ShipClassList getShipClasses() {
		if (shipClasses == null) {
			try {
				shipClasses = (ShipClassList) ModelSerializer.readFromFile("kop/ships/shipclasses.xml", ShipClassList.class);
			} catch (Exception e) {
				e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
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

	protected ShipBlueprint getBlueprint() {
		return ship;
	}
}
