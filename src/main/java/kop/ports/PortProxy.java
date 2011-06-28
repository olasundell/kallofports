package kop.ports;

import kop.game.Game;
import kop.map.routecalculator.Point;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * @author Ola Sundell
 */
@Root
public class PortProxy {
	private Port delegatePort;

	public PortProxy() {}

	public PortProxy(Port port) {
		delegatePort = port;
	}

	public double getLatitude() {
		return delegatePort.getLatitude();
	}

	public double getLongitude() {
		return delegatePort.getLongitude();
	}

	public Point getPosition() {
		return delegatePort.getPosition();
	}

	@Attribute
	public String getUnlocode() {
		return delegatePort.getUnlocode();
	}

	@Attribute
	public void setUnlocode(String unlocode) {
		try {
			delegatePort = Game.getInstance().getPortByUnlocode(unlocode);
		} catch (NoSuchPortException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}
	}

	public String getName() {
		return delegatePort.getName();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		PortProxy portProxy = (PortProxy) o;

		if (delegatePort != null ? !delegatePort.equals(portProxy.delegatePort) : portProxy.delegatePort != null)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		return delegatePort != null ? delegatePort.hashCode() : 0;
	}
}
