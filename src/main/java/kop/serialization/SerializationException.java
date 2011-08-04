package kop.serialization;

import java.net.URISyntaxException;

/**
 * @author Ola Sundell
 */
public class SerializationException extends Exception {
	public SerializationException(Exception e) {
		super(e);
	}

	public SerializationException(String str) {
		super(str);
	}

	public SerializationException(String str, Exception e) {
		super(str, e);
	}
}
