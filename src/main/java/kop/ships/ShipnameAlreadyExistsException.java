package kop.ships;

/**
 * @author Ola Sundell
 */
public class ShipnameAlreadyExistsException extends Exception {
	public ShipnameAlreadyExistsException(String name) {
		super(name);
	}
}
