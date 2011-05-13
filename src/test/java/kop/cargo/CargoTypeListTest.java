package kop.cargo;

import org.junit.Test;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 5/8/11
 * Time: 7:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class CargoTypeListTest {
	@Test
	public void getCargoTypeByPackaging() throws Exception {
		CargoTypeList list = FreightMarket.getCargoTypes();
		CargoType type = list.getCargoTypeByPackaging(CargoType.Packaging.container);
		assertNotNull(type);
	}
}
