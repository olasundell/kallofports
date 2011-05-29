package kop.cargo;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 5/8/11
 * Time: 7:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class CargoTypeListTest {

	private static CargoTypeList list;

	@BeforeClass
	public static void init() throws Exception {
		list = FreightMarket.getCargoTypes();
	}

	@Test
	public void getCargoTypeByPackaging() {
		CargoType type = list.getCargoTypeByPackaging(CargoType.Packaging.container);
		assertNotNull(type);
		CargoType.Packaging packaging = type.getPackaging();
		assertNotNull(packaging);
		assertTrue("CargoType density isn't set.", type.getDensityAsDouble() > 0);

	}

	@Test
	public void findCargoType() {
		String cargoTypeSubstr = "crude";
		CargoTypeList crudeList = list.find(cargoTypeSubstr);
		assertNotNull(crudeList);
		assertTrue("Returned list is empty when searching for cargo type" + cargoTypeSubstr, crudeList.size() > 0);
	}
}
