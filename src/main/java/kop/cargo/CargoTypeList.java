package kop.cargo;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/11/11
 * Time: 10:50 AM
 * To change this template use File | Settings | File Templates.
 */
@Root
public class CargoTypeList {
	@ElementList
	ArrayList<CargoType> list;
	public CargoTypeList() {}
}
