package kop.map.routecalculator;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.convert.Convert;
import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Old world implementation.
 * @deprecated Use NewWorld instead. Go west!
 */
@Root
public class World {
	@Element
	WorldList points;

	public World() {
		points = new WorldList();
	}

	protected World(Point[][] pointsArr) {
		this();
		for (Point[] arr: pointsArr) {
			WorldListArray pList = new WorldListArray();
			for (Point p: arr) {
				if (p==null) {
					p=new Point(-1, -1);
				}
				pList.add(p);
			}
			points.add(pList);
		}
	}

	public Point[][] getPointsAsArray() {
		Point[][] ret = new Point[points.size()][points.get(0).size()];

		for (int i=0;i<points.size();i++) {
			for (int j=0;j<points.get(i).size();j++) {
				ret[i][j] =  points.get(i).get(j);
				if (ret[i][j].getLat() == -1) {
					ret[i][j]=null;
				}
			}
		}

		return ret;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		World world = (World) o;

		if (points != null ? !points.equals(world.points) : world.points != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		return points != null ? points.hashCode() : 0;
	}

//	@Root
	public static class WorldList extends ArrayList<WorldListArray> {
		public WorldList() {
			super();
		}

		@ElementList(inline = true)
		public List<WorldListArray> getList() {
			return this;
		}

		@ElementList(inline = true)
		public void setList(List<WorldListArray> list) {
			//apparently simple-xml does this for us, no need.
//			this.addAll(list);
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			WorldList list = (WorldList) o;
			if (this.size() != list.size()) {
				return false;
			}

			for (int i=0;i<this.size();i++) {
				if (!get(i).equals(list.get(i))) {
					return false;
				}
			}

			return true;
		}
	}

//	@Root
	public static class WorldListArray extends ArrayList<Point> {
		public WorldListArray() {
			super();
		}

		@ElementList(inline = true)
		public List<Point> getList() {
			return this;
		}

		@ElementList(inline = true)
		public void setList(List<Point> list) {
			// apparently simple-xml does this for us, no need.
//			this.addAll(list);
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			WorldListArray list = (WorldListArray) o;
			if (this.size() != list.size()) {
				return false;
			}

			for (int i=0;i<this.size();i++) {
				if (!get(i).equals(list.get(i))) {
					return false;
				}
			}

			return true;
		}
	}

	public static class WorldListConverter implements Converter<WorldListArray> {
		public WorldListConverter() {

		}

		@Override
		public WorldListArray read(InputNode inputNode) throws Exception {
			return null;
		}

		@Override
		public void write(OutputNode outputNode, WorldListArray arr) throws Exception {
//			outputNode.
		}
	}
}
