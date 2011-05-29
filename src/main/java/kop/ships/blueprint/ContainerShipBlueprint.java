package kop.ships.blueprint;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Blueprint for container ships.
 */
@Root
public class ContainerShipBlueprint extends ShipBlueprint {
	@Element
	private int maxTEU;

	public ContainerShipBlueprint() {

	}

	public int getMaxTEU() {
		return maxTEU;
	}

	public void setMaxTEU(int maxTEU) {
		this.maxTEU = maxTEU;
	}

	@Override
	public ShipType getType() {
		return ShipType.container;  //To change body of implemented methods use File | Settings | File Templates.
	}
}
