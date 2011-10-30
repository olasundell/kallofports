package kop.cargo;

import kop.ports.Port;
import kop.ports.PortProxy;

/**
 * A Freight is a Cargo from an origin Port to a destination Port.
 */
public class Freight {
	private PortProxy origin;
	private PortProxy destination;
	private Cargo cargo;

	public PortProxy getOrigin() {
		return origin;
	}

	public void setOrigin(PortProxy origin) {
		this.origin = origin;
	}

	public PortProxy getDestination() {
		return destination;
	}

	public void setDestination(PortProxy destination) {
		this.destination = destination;
	}

	public Cargo getCargo() {
		return cargo;
	}

	public void setCargo(Cargo cargo) {
		this.cargo = cargo;
	}

	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		Freight f;

		if (o instanceof Freight) {
			f= (Freight) o;
		} else {
			return false;
		}

		return f.getDestination().equals(this.getDestination()) &&
				f.getCargo().equals(this.getCargo()) &&
				f.getOrigin().equals(this.getOrigin());
	}

	@Override
	public int hashCode() {
		int result = origin != null ? origin.hashCode() : 0;
		result = 31 * result + (destination != null ? destination.hashCode() : 0);
		result = 31 * result + (cargo != null ? cargo.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return String.format("Freight{origin=%s, destination=%s, cargo=%s}", origin, destination, cargo);
	}
}
