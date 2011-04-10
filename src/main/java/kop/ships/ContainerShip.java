/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kop.ships;

/**
 *
 * @author ola
 */
public class ContainerShip extends Ship {
    private int teus;
	private int maxTEU;

	public ContainerShip() {
		super();
	}

	public ContainerShip(String name) {
        super(name);
    }

	public void setMaxTEU(int maxTEU) {
		this.maxTEU = maxTEU;
	}
}
