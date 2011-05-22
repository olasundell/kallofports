package kop.map.routecalculator;

import com.bbn.openmap.LatLonPoint;
import com.bbn.openmap.proj.Length;
import kop.ports.Port;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
* Created by IntelliJ IDEA.
* User: ola
* Date: 5/17/11
* Time: 8:10 AM
* To change this template use File | Settings | File Templates.
*/
@Root
public class Point implements Cloneable {
	private LatLonPoint coord;
	private Point parent;
	private double parentCost;
	@Attribute
	private int x;
	@Attribute
	private int y;

	public Point() {
		coord = new LatLonPoint();
	}

	Point(double lat, double lon) {
		this((float)lat, (float)lon);
	}

	Point(float lat, float lon) {
		this(0,0,lat,lon);
	}

	public Point(int x, int y, float lat, float lon) {
		this.x = x;
		this.y = y;
		coord = new LatLonPoint(lat, lon);
	}

	public Point(Port port) {
		this((float)port.getLatitude().getCoordinate(),
				(float)port.getLongitude().getCoordinate());
	}

	public double getParentCost() {
		return parentCost;
	}

	public double getTotalCost() {
		// TODO do we want to save this result for optimisation?
		double v = parentCost;

		if (parent!=null) {
			if (this == parent || parent.getParent() == parent || parent.getParent() == this) {
				throw new IllegalArgumentException("Circular reference!");
			}

			v += parent.getTotalCost();
		}

		return v;
	}

	private void setParentCost(double parentCost) {
		this.parentCost = parentCost;
	}

	public void resetParent() {
		parent = null;
	}

	public void setParent(Point p) {
		if (p == this) {
			throw new IllegalArgumentException("Trying to create a circular reference by setting parent to this!");
		}

		if (p.getParent() == this) {
			throw new IllegalArgumentException("Trying to create a circular reference by setting parent's parent to this!");
		}
		setParentCost(distance(p));
		parent = p;
	}

	public Point getParent() {
		return parent;
	}

	public List<Point> getNeighbours(NewWorld world) {
		ArrayList<Point> list = new ArrayList<Point>();

		// TODO this could, of course, be a little bit prettier
		int yMinus = getY(-1, world);
		int yPlus = getY(1, world);

		if (x > 0) {
			list.add(world.lats[x-1].longitudes[yMinus]);
			list.add(world.lats[x-1].longitudes[y]);
			list.add(world.lats[x-1].longitudes[yPlus]);
		}

		list.add(world.lats[x].longitudes[yMinus]);
		list.add(world.lats[x].longitudes[yPlus]);

		if (x < world.lats.length-1) {
			list.add(world.lats[x+1].longitudes[yMinus]);
			list.add(world.lats[x+1].longitudes[y]);
			list.add(world.lats[x+1].longitudes[yPlus]);
		}

		return list;
	}

	protected int getY(int rel, NewWorld world) {
		return (y+rel+world.lats[0].longitudes.length) % world.lats[0].longitudes.length;
	}

	public double distance(Point p) {
		double radius = 6371.009;
		Point p1 = this;
		Point p2 = p;

		double lat1 = p1.getCoord().getLatitude();
		double lat2 = p2.getCoord().getLatitude();
		double lon1 = p1.getCoord().getLongitude();
		double lon2 = p2.getCoord().getLongitude();
		double dLat = Math.toRadians(lat2-lat1);
		double dLon = Math.toRadians(lon2-lon1);
		double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
				Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
						Math.sin(dLon/2) * Math.sin(dLon/2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

		return radius * c / 1.852;
	}

	public LatLonPoint getCoord() {
		return coord;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Point point = (Point) o;

		if (coord != null ? !coord.equals(point.coord) : point.coord != null) return false;

		return true;
	}

	@Element
	public float getLat() {
		return coord.getLatitude();
	}

	@Element
	public float getLon() {
		return coord.getLongitude();
	}

	@Element
	public void setLat(float lat) {
		coord.setLatitude(lat);
	}

	@Element
	public void setLon(float lon) {
		coord.setLongitude(lon);
	}

	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}

		return null;
	}
}
