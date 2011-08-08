package kop.ports;

import kop.serialization.SerializationException;

/**
 * These are not the ports you're looking for. Move along.
 */
public class NoSuchPortException extends Exception {
	public NoSuchPortException(String s) {
		super(s);
	}

	public NoSuchPortException(Exception e) {
		super(e);
	}
}
