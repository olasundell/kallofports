package kop.ships.blueprint;

import org.simpleframework.xml.Element;

/**
 * Blueprint for LNG carriers.
 */
public class LNGCarrierBlueprint extends ShipBlueprint {
	@Element
	private double lngCapacity;

	@Override
	public ShipType getType() {
		return ShipBlueprint.ShipType.lngcarrier;  //To change body of implemented methods use File | Settings | File Templates.
	}

	public double getLngCapacity() {
		return lngCapacity;
	}

	public void setLngCapacity(double lngCapacity) {
		this.lngCapacity = lngCapacity;
	}
}
