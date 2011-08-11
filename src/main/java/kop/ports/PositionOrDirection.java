package kop.ports;

import kop.game.Game;
import kop.map.routecalculator.ASRoute;
import kop.map.routecalculator.Point;
import kop.ships.model.ShipModel;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.Date;

/**
 * Describes the position or direction for a ship, with util methods to boot. It's either going somewhere or it's in port.
 */
@Root
public class PositionOrDirection {
	@Element(required = false)
	private PortProxy currentPort;
	@Element(required = false)
	private PortProxy originPort;
	@Element(required = false)
	private PortProxy destinationPort;
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
	@Element
	private Point currentPosition;
	private ASRoute route;
	private int routeLeg;

	public PositionOrDirection() {
		currentPosition = new Point();
		setCurrentSpeed(0.0);
	}

	public PortProxy getCurrentPort() {
		return currentPort;
	}

	/**
	 * Sets the current port and nulls destination and origin port, which in turn will switch values of is* methods.
	 * @param currentPort Current port.
	 */
	public void setCurrentPort(PortProxy currentPort) {
		this.currentPort = currentPort;
		setDestinationPort(null);
		setOriginPort(null);
		if (currentPort != null) {
			setArrivedAtPortDate(Game.getInstance().getCurrentDate());
			setCurrentLatitude(currentPort.getLatitude());
			setCurrentLongitude(currentPort.getLongitude());
			setCurrentSpeed(0.0);
			setDistanceLeft(0.0);
		}
	}

	/**
	 * Is the ship in a port?
	 * @return true when currentPort isn't null, ie in a port.
	 */
	public boolean isInPort() {
		return currentPort!=null;
	}

	/**
	 * Is the ship at sea?
	 * @return true when currentPort is null, and both originPort and destinationPort isn't.
	 */

	public boolean isAtSea() {
		return currentPort == null &&
				originPort != null &&
				destinationPort != null;
	}

	public PortProxy getDestinationPort() {
		return destinationPort;
	}

	void setDestinationPort(PortProxy destinationPort) {
		this.destinationPort = destinationPort;
	}

	public int getHoursToDest() {
		return (int) Math.ceil(getDistanceLeft() / currentSpeed);
	}

	public Date getETA() {
		return Game.getInstance().getFutureDate((long) Math.ceil(getHoursToDest()/24.0));
	}

	public PortProxy getOriginPort() {
		return originPort;
	}

	void setOriginPort(PortProxy originPort) {
		this.originPort = originPort;
	}

	/**
	 * Starts a travel via the shortest route from Port to Port.
	 * @param origin
	 * @param destination
	 * @param speed
	 * @param ship
	 * @throws NoRouteFoundException
	 */
	public void travelTo(PortProxy origin, PortProxy destination, double speed, ShipModel ship) throws NoRouteFoundException {
		// an entirely new model instance might neither have a current port nor be at sea.
		if (isInPort() || !isAtSea()) {
			setCurrentLatitude(origin.getLatitude());
			setCurrentLongitude(origin.getLongitude());
		}

		setCurrentPort(null);
		setDestinationPort(destination);
		setOriginPort(origin);
		setRoute(Game.getInstance().getRoute(origin, destination, ship));
		setLeftPortDate(Game.getInstance().getCurrentDate());
		setDistanceLeft(getCurrentRoute().getTotalDistance());
		setCurrentSpeed(speed);
	}

	private void setDistanceLeft(double distance) {
		this.distanceLeft = distance;
	}

	public Date getLeftPortDate() {
		return leftPortDate;
	}

	void setLeftPortDate(Date leftPortDate) {
		this.leftPortDate = leftPortDate;
	}

	public Date getArrivedAtPortDate() {
		return arrivedAtPortDate;
	}

	void setArrivedAtPortDate(Date arrivedAtPortDate) {
		this.arrivedAtPortDate = arrivedAtPortDate;
	}

	public double getDistanceLeft() {
		return distanceLeft;
	}

	void setCurrentSpeed(double currentSpeed) {
		// TODO we should include a check here to see if the current speed is greater than the max speed.
		// hard to do in ths class, we need to rethink and add the check somewhere else.
		this.currentSpeed = currentSpeed;
	}

	public void travel() {
		setDistanceLeft(getDistanceLeft() - currentSpeed);
	}

	public double getLongitude() {
		return currentPosition.getLon();
	}

	public double getLatitude() {
		return currentPosition.getLat();
	}

	private void setCurrentLatitude(double currentLatitude) {
		currentPosition.setLat((float) currentLatitude);
	}

	private void setCurrentLongitude(double currentLongitude) {
		currentPosition.setLon((float) currentLongitude);
	}

	/**
	 * Calculates bearing based on next route point.
	 * @return current bearing in degrees
	 */
	public double getBearing() {
		if (isInPort()) {
			return 0.0;
		}

		double lat2 = getNextPoint().getLat();
		double lon2 = getNextPoint().getLon();

//		double dLat = Math.toRadians(lat2 -getLatitude());
		double dLon = Math.toRadians(lon2 -getLongitude());

		double y = Math.sin(dLon) * Math.cos(lon2);
		double x = Math.cos(getLatitude())*Math.sin(lat2) - Math.sin(getLatitude())*Math.cos(lat2)*Math.cos(dLon);
		return Math.round(100*Math.toDegrees(Math.atan2(y, x)))/100.0;
	}

	private Point getNextPoint() {
		return route.getPoints().get(routeLeg+1);
	}

	public void setRoute(ASRoute route) {
		this.route = route;
		routeLeg = 0;
	}

	public ASRoute getCurrentRoute() {
		return route;
	}

	public double getCurrentSpeed() {
		return currentSpeed;
	}

	public Point getCurrentPosition() {
		return currentPosition;
	}
}
