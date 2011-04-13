package kop.cargo;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/11/11
 * Time: 10:51 AM
 * To change this template use File | Settings | File Templates.
 */
public class CargoTypesFactoryTest {
	@Test
	public void testSaveCargoTypes() throws Exception {
		CargoTypesFactory factory = new CargoTypesFactory();
		ArrayList<CargoType> list = new ArrayList<CargoType>();
		CargoType type = new CargoType();
		type.setDescription("Lots of them");
		type.setName("Chemicals");
		type.setPackaging(CargoType.Packaging.chemical);

		list.add(type);

		factory.saveCargoTypes("testcargotypes.xml", list);
	}

	@Test
	public void testCreateCargoTypes() throws Exception {
		CargoTypesFactory factory = new CargoTypesFactory();
		try {
			factory.createCargoTypes("cargotypes.xml");
		} catch (FileNotFoundException e) {
			// obviously it didn't work. ignore.
			factory.createCargoTypes("src/main/resources/cargotypes.xml");
		}
	}
}
