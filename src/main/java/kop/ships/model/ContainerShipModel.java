package kop.ships.model;

import kop.ships.blueprint.ContainerShipBlueprint;

/**
 * Model for container ships.
 * @author ola
 */
public class ContainerShipModel extends ShipModel {
    private int currentTeus;

	ContainerShipModel() {
		super();
		blueprint = new ContainerShipBlueprint();
	}
}
