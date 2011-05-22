package kop.map.routecalculator;

import com.bbn.openmap.tools.roads.Route;
import kop.ports.Port;
import kop.ships.ShipModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/3/11
 * Time: 3:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class ASDistance {
	private Port origin;
	private Port destination;

	private List<ASRoute> routes;

	public ASDistance(Port origin, Port destination) {
		this.origin = origin;
		this.destination = destination;
		routes = new ArrayList<ASRoute>();
	}

	protected void addRoute(ASRoute route) {
		routes.add(route);
	}

	public ASRoute shortestRoute() {
		if (routes.size() == 0) {
			return null;
		}

		double d = routes.get(0).getTotalDistance();
		ASRoute route = routes.get(0);

		for (ASRoute r: routes) {
			if (r.getTotalDistance() < d) {
				d = r.getTotalDistance();
				route = r;
			}
		}

		return route;
	}

	public double shortestDistance() {
		return shortestRoute().getTotalDistance();
	}

	public double shortestDistance(ShipModel ship) {
		if (routes.size() == 0) {
			return -1;
		}

		double d = routes.get(0).getTotalDistance();

		for (ASRoute r: routes) {
			if (!(ship.isPostPanamax() && r.passesPanama()) &&
					!(ship.isPostSuezmax() && r.passesSuez()) &&
					r.getTotalDistance() < d) {
				d = r.getTotalDistance();
			}
		}

		return d;
	}

	public Port getOrigin() {
		return origin;
	}

	public Port getDestination() {
		return destination;
	}
}
