package kop.ships;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/6/11
 * Time: 9:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class ShipClassesFactory {
	public ArrayList<ShipClass> createShipClasses(String fileName) throws Exception {
		Serializer serializer = new Persister();

		File source = new File(fileName);
		ShipClassList list = serializer.read(ShipClassList.class, source);

		return list.list;
	}

	public void saveShipClasses(String fileName, ArrayList<ShipClass> list) throws Exception {
		Serializer serializer = new Persister();
		File result = new File(fileName);
		ShipClassList scl = new ShipClassList();
		scl.list = list;
		serializer.write(scl, result);
	}
}
