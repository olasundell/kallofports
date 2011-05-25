package kop.ships;

/**
 * Model for container ships.
 * @author ola
 */
public class ContainerShipModel extends ShipModel {
    private int currentTeus;

	public ContainerShipModel() {
		super();
		blueprint = new ContainerShipBlueprint();
	}
}
