package kop.ships;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/13/11
 * Time: 2:39 PM
 * To change this template use File | Settings | File Templates.
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
