package kop.ships;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/8/11
 * Time: 11:17 AM
 * To change this template use File | Settings | File Templates.
 */
@Root
class ShipClass {
	@Element
	private double cost;
	@Attribute
	private String className;
	@Element
	private
	ShipBlueprint ship;

	public ShipClass() {

	}

	public ShipClass(ShipBlueprint ship, double cost, String className) {
		this.cost = cost;
		this.className = className;
		this.ship = ship;
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
}
