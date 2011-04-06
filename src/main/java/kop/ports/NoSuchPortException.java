package kop.ports;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/4/11
 * Time: 5:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class NoSuchPortException extends Exception {
	public NoSuchPortException(String s) {
		super(s);
	}
}
