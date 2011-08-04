package kop.serialization;

import com.sun.org.apache.bcel.internal.generic.ObjectType;
import kop.serialization.SerializationException;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.Strategy;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * XML serialiser/deserialiser class with static util methods..
 */

public class ModelSerializer<T> {
	public static Object readFromFile(URL fileName, Class<?> aClass) throws SerializationException {
//		Strategy strategy = new AnnotationStrategy();
//		Serializer serializer = new Persister(strategy);
		Serializer serializer = new Persister();
		File source = null;
		try {
			source = new File(fileName.toURI());
		} catch (URISyntaxException e) {
			throw new kop.serialization.SerializationException(e);
		}

		try {
			return serializer.read(aClass, source);
		} catch (Exception e) {
			throw new kop.serialization.SerializationException(e);
		}
	}

	public static void saveToFile(String fileName, Class<?> aClass, Object obj) throws SerializationException {
//		Strategy strategy = new AnnotationStrategy();
//		Serializer serializer = new Persister(strategy);
		Serializer serializer = new Persister();
		File result = new File(fileName);
		try {
			serializer.write(aClass.cast(obj), result);
		} catch (Exception e) {
			throw new kop.serialization.SerializationException(e);
		}
	}

	/**
	 * Deserialises a file on the class path to an instance of the the provided class.
	 * @param resourceName
	 * @param aClass
	 * @return
	 * @throws Exception
	 */
	public static Object readFromFile(String resourceName, Class<?> aClass) throws SerializationException {
		return readFromFile(ClassLoader.getSystemClassLoader().getResource(resourceName), aClass);
	}

	public List<T> readFromDirectory(String dirname, Class<T> instance) throws SerializationException {
		List<T> list = new ArrayList<T>();

		File dir = new File(dirname);
		if (!dir.exists()) {
			throw new SerializationException(String.format("%s doesn't exist!", dirname));
		}

		if (!dir.isDirectory()) {
			throw new SerializationException(String.format("%s isn't a directory!", dirname));
		}

		for (File f: dir.listFiles()) {
			if (f.isDirectory()) {
				list.addAll(readFromDirectory(f.getAbsolutePath(), instance));
			} else if (f.isFile() && f.getName().endsWith(".xml")) {
				list.add(readFromFile(f.toURI(), instance));
			}
		}

		return list;
	}

	public T readFromFile(URI fileName, Class<T> instance) throws SerializationException {
		Serializer serializer = new Persister();
		File source = new File(fileName);

		try {
			return serializer.read(instance, source);
		} catch (Exception e) {
			throw new kop.serialization.SerializationException(String.format("Error reading from file %s", fileName.toString()), e);
		}
	}
}