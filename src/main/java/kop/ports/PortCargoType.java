package kop.ports;

import kop.cargo.CargoType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * This will have more logic and attributes later.
 * TODO add amount produced per time unit.
 * TODO add favourite destinations for a certain cargo type.
 * @author Ola Sundell
 */
@Root
public class PortCargoType {
	@Element
	private CargoType type;

	public PortCargoType(CargoType cargoType) {
		type = cargoType;
	}
}
