package kop.ships;

import org.simpleframework.xml.Element;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 5/5/11
 * Time: 8:04 PM
 * To change this template use File | Settings | File Templates.
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
