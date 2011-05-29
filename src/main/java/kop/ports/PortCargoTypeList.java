package kop.ports;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ola Sundell
 */
@Root
public class PortCargoTypeList extends ArrayList<PortCargoType> {
	public PortCargoTypeList() {
		super();
	}

	@ElementList(inline = true)
	public void setList(List<PortCargoType> list) {
		addAll(list);
	}

	@ElementList(inline = true)
	public List<PortCargoType> getList() {
		return this;
	}
}
