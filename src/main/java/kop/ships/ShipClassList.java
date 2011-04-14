package kop.ships;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/9/11
 * Time: 2:59 PM
 * To change this template use File | Settings | File Templates.
 */

@Root
class ShipClassList {
	@ElementList
	public ArrayList<ShipClass> list;

	public ShipClassList() {
	}
}
