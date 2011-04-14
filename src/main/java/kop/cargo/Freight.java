package kop.cargo;

import kop.ports.Port;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/3/11
 * Time: 11:47 AM
 * To change this template use File | Settings | File Templates.
 */
class Freight {
	private Port origin;
	private Port destination;
	private Cargo cargo;

	public Port getOrigin() {
		return origin;
	}

	public void setOrigin(Port origin) {
		this.origin = origin;
	}

	Port getDestination() {
		return destination;
	}

	public void setDestination(Port destination) {
		this.destination = destination;
	}

	Cargo getCargo() {
		return cargo;
	}

	public void setCargo(Cargo cargo) {
		this.cargo = cargo;
	}

	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		Freight f=null;

		if (o instanceof Freight) {
			f= (Freight) o;
		} else {
			return false;
		}

		return f.getDestination().equals(this.getDestination()) &&
				f.getCargo().equals(this.getCargo()) &&
				f.getOrigin().equals(this.getOrigin());
	}
}
