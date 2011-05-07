package kop.cargo;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/11/11
 * Time: 10:50 AM
 * To change this template use File | Settings | File Templates.
 */
@Root
class CargoTypeList extends ArrayList<CargoType> {
	@ElementList
	public void setList(List<CargoType> list) {
		this.addAll(list);
	}

	@ElementList
	public List<CargoType> getList() {
		return this;
	}

	public CargoTypeList() {}
}
