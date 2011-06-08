package kop.ports;

import kop.map.routecalculator.CouldNotFindPointException;

/**
 * Whenever you can't find a route, this class is there with a pesky instance.
 */
public class NoRouteFoundException extends Exception {
	public NoRouteFoundException(String s) {
		super(s);
	}

	public NoRouteFoundException(CouldNotFindPointException e) {
		super(e);
	}
}
