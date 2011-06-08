package kop.ships;

import kop.ships.blueprint.ShipBlueprint;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

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

	public ShipBlueprint getBlueprint() {
		return ship;
	}

	public int getMaxLoanPercent() {
		return maxLoanPercent;
	}
}
