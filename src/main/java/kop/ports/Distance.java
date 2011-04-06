package kop.ports;

import kop.ships.Ship;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/3/11
 * Time: 3:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class Distance {
	List<Route> routes;

	public Distance() {
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

	public double shortestDistance(Ship ship) {
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
