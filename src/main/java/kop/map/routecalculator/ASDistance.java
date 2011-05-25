package kop.map.routecalculator;

import com.bbn.openmap.tools.roads.Route;
import kop.ports.Port;
import kop.ships.ShipModel;

import java.util.ArrayList;
import java.util.List;

/**
 * The ASDistance (A* or A Star Distance) class contains all relevant ASRoutes between two Ports and helper methods to use them.
 * @see ASRoute
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

	/**
	 * Returns the shortest ASRoute for this ASDistance
	 * TODO we should use a ShipModel parameter and check for panamax and suezmax.
	 * @return the shortest ASRoute
	 */
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

	/**
	 * Returns the shortest route's distance in nautical miles.
	 * @return
	 */
	public double shortestDistance() {
		return shortestRoute().getTotalDistance();
	}

	/**
	 * Returns the shortest route's distance in nautical miles, given the provided ShipModel's size.
	 * @param ship used to check for panamax and suezmax.
	 * @return the shortest distance in nautical miles.
	 * TODO use shortestRoute with parameters instead.
	 */
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
