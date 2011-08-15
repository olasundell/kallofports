package kop.map.routecalculator;

import kop.ports.NoRouteFoundException;
import kop.ports.PortProxy;
import kop.ships.model.ShipModel;

import java.util.ArrayList;
import java.util.List;

/**
 * The ASDistance (A* or A Star Distance) class contains all relevant ASRoutes between two Ports and helper methods to use them.
 * @see ASRoute
 */
public class ASDistance {
	private PortProxy origin;
	private PortProxy destination;

	private List<ASRoute> routes;

	/**
	 * Constructor
	 * @param origin originating port
	 * @param destination destination port
	 */
	public ASDistance(PortProxy origin, PortProxy destination) {
		this.origin = origin;
		this.destination = destination;
		routes = new ArrayList<ASRoute>();
	}

	/**
	 * Adds route to the Collection of distances.
	 * @param route the route to add
	 */
	protected void addRoute(ASRoute route) {
		routes.add(route);
	}

	/**
	 * Returns the shortest ASRoute for this ASDistance
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
	 * @return the shortest distance found.
	 */
	public double shortestDistance() {
		return shortestRoute().getTotalDistance();
	}

	/**
	 * Returns the shortest route's distance in nautical miles, given the provided ShipModel's size.
	 * @param ship used to check for panamax and suezmax.
	 * @return the shortest distance in nautical miles.
	 */
	public double shortestDistance(ShipModel ship) throws NoRouteFoundException {
		return shortestRoute(ship).getTotalDistance();
	}

	/**
	 * Retrieves the shortest ASRoute for a ship, checks for canal passage and whatnot.
	 * @param ship a ShipModel to check whether it's viable to pass the major canals.
	 * @return the shortest ASRoute given the given ShipModel
	 * @throws NoRouteFoundException if there's no ASRoutes in the ASDistance, or if the given ship is too big to pass any of the ASRoutes.
	 */

	public ASRoute shortestRoute(ShipModel ship) throws NoRouteFoundException {
		if (routes.size() == 0) {
			throw new NoRouteFoundException("Route list is empty");
		}

		ASRoute route = routes.get(0);

		for (ASRoute r: routes) {
			if (!(ship.isPostPanamax() && r.passesPanama()) &&
					!(ship.isPostSuezmax() && r.passesSuez()) &&
					r.getTotalDistance() < route.getTotalDistance()) {
				route = r;
			}
		}

		return route;
	}

	/**
	 * Getter.
	 * @return the origin PortProxy
	 */
	public PortProxy getOrigin() {
		return origin;
	}

	/**
	 * Getter.
	 * @return the destination PortProxy
	 */
	public PortProxy getDestination() {
		return destination;
	}
}
