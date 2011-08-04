package kop.cargo;

/**
 * @author Ola Sundell
 */
public class NoSuchCargoTypeException extends Exception {
	public NoSuchCargoTypeException(String s) {
		super(s);
	}
}
