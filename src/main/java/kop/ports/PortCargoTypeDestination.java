package kop.ports;

import kop.game.Game;
import org.apache.log4j.Logger;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.slf4j.LoggerFactory;

/**
* @author Ola Sundell
*/
@Root
public class PortCargoTypeDestination {
	@Element
	private String name;
	@Element
	private double yearlyAmount;

	private PortProxy portProxy = null;

	public PortCargoTypeDestination() {
		org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getYearlyAmount() {
		return yearlyAmount;
	}

	public void setYearlyAmount(double yearlyAmount) {
		this.yearlyAmount = yearlyAmount;
	}

	public PortProxy getPortProxy() {
		return portProxy;
	}

	public void setPortProxy(PortProxy portProxy) {
		this.portProxy = portProxy;
	}
}
