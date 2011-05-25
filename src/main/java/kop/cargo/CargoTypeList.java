package kop.cargo;

import kop.game.Game;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A list of cargo types, which extends ArrayList. Used for serialisation purposes.
 */
@Root
public class CargoTypeList extends ArrayList<CargoType> {
	Map<CargoType.Packaging, CargoTypeList> packagingMap;

	@ElementList
	public void setList(List<CargoType> list) {
		packagingMap = new HashMap<CargoType.Packaging, CargoTypeList>();
		for (CargoType type: list) {
			if (packagingMap.get(type.getPackaging()) == null) {
				packagingMap.put(type.getPackaging(), new CargoTypeList());
			}
			packagingMap.get(type.getPackaging()).add(type);
		}
		this.addAll(list);
	}

	@ElementList
	public List<CargoType> getList() {
		return this;
	}

	public CargoTypeList() {}

	public CargoType getCargoTypeByPackaging(CargoType.Packaging packaging) {
		CargoTypeList list = packagingMap.get(packaging);
		return list.get(Game.getInstance().getRandom().nextInt(list.size()));
	}
}
