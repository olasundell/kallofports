/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kop.ships;

/**
 *
 * @author ola
 */
public class ContainerShipModel extends ShipModel {
    private int currentTeus;

	public ContainerShipModel() {
		super();
		blueprint = new ContainerShipBlueprint();
	}
}
