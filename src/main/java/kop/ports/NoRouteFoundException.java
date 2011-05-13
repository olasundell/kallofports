package kop.ports;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/4/11
 * Time: 5:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class NoRouteFoundException extends Exception {
	public NoRouteFoundException(String s) {
		super(s);
	}
}
