package kop.ports;

import kop.game.Game;
import kop.ships.ShipModel;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/4/11
 * Time: 4:35 PM
 * To change this template use File | Settings | File Templates.
 */
@Root
public class PositionOrDirection {
	@Element(required = false)
	private Port currentPort;
	@Element(required = false)
	private Port originPort;
	@Element(required = false)
	private Port destinationPort;
	@Element
	private double currentSpeed;
	@Element
	private double distanceLeft;
	@Element
	private int hoursToDest;
	@Element(required = false)
	private Date leftPortDate;
	@Element(required = false)
	private Date arrivedAtPortDate;

	public Port getCurrentPort() {
		return currentPort;
	}

	public void setCurrentPort(Port currentPort) {
		this.currentPort = currentPort;
		setDestinationPort(null);
		setOriginPort(null);
		setArrivedAtPortDate(Game.getInstance().getCurrentDate());
	}

	public boolean isInPort() {
		return currentPort!=null;
	}

	public boolean isAtSea() {
		return currentPort == null &&
				originPort != null &&
				destinationPort != null;
	}

	public Port getDestinationPort() {
		return destinationPort;
	}

	public void setDestinationPort(Port destinationPort) {
		this.destinationPort = destinationPort;
	}

	public int getHoursToDest() {
		return (int) (getDistanceLeft() / currentSpeed);
	}

	public Port getOriginPort() {
		return originPort;
	}

	public void setOriginPort(Port originPort) {
		this.originPort = originPort;
	}

	public void travelTo(Port origin, Port destination, double speed, ShipModel ship) throws NoRouteFoundException {
		setCurrentPort(null);
		setDestinationPort(destination);
		setOriginPort(origin);
		setLeftPortDate(Game.getInstance().getCurrentDate());
		setDistanceLeft(Game.getInstance().getDistance(origin, destination, ship));
		setCurrentSpeed(speed);
	}

	private void setDistanceLeft(double distance) {
		this.distanceLeft = distance;
	}

	public Date getLeftPortDate() {
		return leftPortDate;
	}

	public void setLeftPortDate(Date leftPortDate) {
		this.leftPortDate = leftPortDate;
	}

	public Date getArrivedAtPortDate() {
		return arrivedAtPortDate;
	}

	public void setArrivedAtPortDate(Date arrivedAtPortDate) {
		this.arrivedAtPortDate = arrivedAtPortDate;
	}

	public double getDistanceLeft() {
		return distanceLeft;
	}

	public void setCurrentSpeed(double currentSpeed) {
		this.currentSpeed = currentSpeed;
	}

	public void travel() {
		setDistanceLeft(getDistanceLeft() - currentSpeed);
	}
}
