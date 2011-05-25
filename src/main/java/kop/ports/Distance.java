package kop.ports;

import kop.ships.ShipModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @deprecated use ASDistance instead.
 * @see kop.map.routecalculator.ASDistance
 */
class Distance {
	private Port origin;
	private Port destination;

	private List<Route> routes;

	public Distance(Port origin, Port destination) {
		this.origin = origin;
		this.destination = destination;
		routes = new ArrayList<Route>();
	}

	public void addRoute(double nm, boolean suez, boolean panama) {
		routes.add(new Route(nm, suez, panama));
	}

	public double shortestDistance() {
		if (routes.size() == 0) {
			return -1;
		}

		double d = routes.get(0).getNauticalMiles();

		for (Route r: routes) {
			if (r.getNauticalMiles() < d) {
				d = r.getNauticalMiles();
			}
		}

		return d;
	}

	public double shortestDistance(ShipModel ship) {
		if (routes.size() == 0) {
			return -1;
		}

		double d = routes.get(0).getNauticalMiles();

		for (Route r: routes) {
			if (!(ship.isPostPanamax() && r.passesPanama()) &&
					!(ship.isPostSuezmax() && r.passesSuez()) &&
					r.getNauticalMiles() < d) {
				d = r.getNauticalMiles();
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

	private class Route {
		private boolean suez;
		private boolean panama;
		private double nauticalMiles;

		public Route(double nm, boolean suez, boolean panama) {
			this.nauticalMiles = nm;
			this.suez = suez;
			this.panama = panama;
		}

		public boolean passesSuez() {
			return suez;
		}

		public boolean passesPanama() {
			return panama;
		}

		public double getNauticalMiles() {
			return nauticalMiles;
		}
	}
}
