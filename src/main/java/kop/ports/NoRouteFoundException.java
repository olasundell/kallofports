package kop.ports;

/**
 * Whenever you can't find a route, this class is there with a pesky instance.
 */
public class NoRouteFoundException extends Exception {
	public NoRouteFoundException(String s) {
		super(s);
	}
}
