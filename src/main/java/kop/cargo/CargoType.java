/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kop.cargo;

/**
 *
 * @author ola
 */
public class CargoType {
    private static int FOOD=1;
    private static int METAL=2;
	public static final CargoType FOODSTUFFS = new CargoType(FOOD);

	private enum Packaging {
		BREAKBULK,
		BULK,
		TANKER,

	}

	private int type;

	public CargoType(int type) {
		this.type = type;
	}
}

