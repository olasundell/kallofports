package kop.ships;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * List of ship classes.
 */

@Root
public class ShipClassList extends ArrayList<ShipClass> {
	@ElementList
	public Collection<ShipClass> getList() {
		return this;
	}

	@ElementList
	public void setList(Collection<ShipClass> list) {
		this.addAll(list);
	}

	public ShipClassList() {
	}

	/**
	 * TODO code is ugly and documentation is needed.
	 * @param type
	 * @param num
	 * @return
	 */

	public ShipClass get(ShipBlueprint.ShipType type, int num) {
		int i=0;

		for (ShipClass c: this) {
			if (c.getClassType().equals(type)) {
				if (i == num) {
					return c;
				} else {
					i++;
				}
			}
		}

		return null;
	}
}
