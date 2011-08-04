package kop.ships;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO replace this factory with kop.serialization.ModelSerializer usage.
 * @deprecated
 */
class ShipClassesFactory {
	public List<ShipClass> createShipClasses(String fileName) throws Exception {
		Serializer serializer = new Persister();

		File source = new File(fileName);
		ShipClassList list = serializer.read(ShipClassList.class, source);

		return list;
	}

	/*
	Tankers:
	Size 	1985 	2005
	32,000–45,000 DWT 	US$18M 	$43M
	80,000–105,000 DWT 	$22M 	$58M
	250,000–280,000 DWT 	$47M 	$120M

	 */
	public void saveShipClasses(String fileName, List<ShipClass> list) throws Exception {
		Serializer serializer = new Persister();
		File result = new File(fileName);
		ShipClassList scl = new ShipClassList();
		scl.setList(list);
		serializer.write(scl, result);
	}
}
