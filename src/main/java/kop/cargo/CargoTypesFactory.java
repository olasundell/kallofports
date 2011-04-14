package kop.cargo;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/11/11
 * Time: 10:26 AM
 * To change this template use File | Settings | File Templates.
 */
class CargoTypesFactory {
	public ArrayList<CargoType> createCargoTypes(String fileName) throws Exception {
		Serializer serializer = new Persister();

		File source = new File(fileName);
		CargoTypeList list = serializer.read(CargoTypeList.class, source);
		return list.list;
	}

	public void saveCargoTypes(String fileName, ArrayList<CargoType> list) throws Exception {
		Serializer serializer = new Persister();
		File result = new File(fileName);
		CargoTypeList ctl = new CargoTypeList();
		ctl.list = list;
		serializer.write(ctl, result);
	}
}
