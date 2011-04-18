package kop.ships;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/8/11
 * Time: 11:17 AM
 * To change this template use File | Settings | File Templates.
 */
@Root
public class ShipClass {
	@Element
	private double cost;
	@Attribute
	private String className;
	@Element
	private ShipBlueprint ship;

	private static List<ShipClass> shipClasses;

	public ShipClass() {

	}

	public ShipClass(ShipBlueprint ship, double cost, String className) {
		this.cost = cost;
		this.className = className;
		this.ship = ship;
	}

	public static List<ShipClass> getShipClasses() {
		ShipClassList l = null;
		try {
			l = (ShipClassList) ModelSerializer.readFromFile("kop/ships/shipclasses.xml", ShipClassList.class);
		} catch (Exception e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
			return null;
		}

		return l.list;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Object getClassType() {
		return ship.getType();
	}
}
