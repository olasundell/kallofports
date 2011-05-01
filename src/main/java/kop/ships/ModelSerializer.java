package kop.ships;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.net.URI;
import java.net.URL;

public class ModelSerializer {
	public static Object readFromFile(URL fileName, Class<?> aClass) throws Exception {
		Serializer serializer = new Persister();
		File source = new File(fileName.toURI());

		return serializer.read(aClass, source);
	}

	public static void saveToFile(String fileName, Class<?> aClass, Object obj) throws Exception {
		Serializer serializer = new Persister();
		File result = new File(fileName);
		serializer.write(aClass.cast(obj), result);
	}

	public static Object readFromFile(String resourceName, Class<?> aClass) throws Exception {
		return readFromFile(ClassLoader.getSystemClassLoader().getResource(resourceName), aClass);
	}
}