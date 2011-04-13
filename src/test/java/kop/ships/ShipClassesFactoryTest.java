package kop.ships;

import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/9/11
 * Time: 3:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class ShipClassesFactoryTest {
	@Test
	public void testSaveShipClasses() throws Exception {
		ShipClassesFactory factory = new ShipClassesFactory();
		ArrayList<ShipClass> list = new ArrayList<ShipClass>();
		ShipBlueprint shipSpec = new ContainerShipBlueprint();
		ShipClass shipClass = new ShipClass(shipSpec, 10.0, "Feeder");

		list.add(shipClass);
		factory.saveShipClasses("shipclasses.xml", list);
	}
	@Test
	public void testCreateShipClasses() throws Exception {
		ShipClassesFactory factory = new ShipClassesFactory();
		ArrayList<ShipClass> list =  factory.createShipClasses("shipclasses.xml");
	}
}
