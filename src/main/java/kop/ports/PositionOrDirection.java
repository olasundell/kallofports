package kop.ports;

import com.bbn.openmap.event.CenterEvent;
import kop.game.Game;
import kop.map.LatLong;
import kop.map.Route;
import kop.ships.ShipModel;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/4/11
 * Time: 4:35 PM
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
	@Element
	private LatLong currentLatitude;
	@Element
	private LatLong currentLongitude;
	private Route route;
	private int routeLeg;

	public Port getCurrentPort() {
		return currentPort;
	}

	/**
	 * Sets the current port and nulls destination and origin port, which in turn will switch values of is* methods.
	 * @param currentPort Current port.
	 */
	public void setCurrentPort(Port currentPort) {
		this.currentPort = currentPort;
		setDestinationPort(null);
		setOriginPort(null);
		if (currentPort != null) {
			setArrivedAtPortDate(Game.getInstance().getCurrentDate());
			setCurrentLatitude(currentPort.getLatitude());
			setCurrentLongitude(currentPort.getLongitude());
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

	public Port getDestinationPort() {
		return destinationPort;
	}

	void setDestinationPort(Port destinationPort) {
		this.destinationPort = destinationPort;
	}

	public int getHoursToDest() {
		return (int) Math.ceil(getDistanceLeft() / currentSpeed);
	}

	public Port getOriginPort() {
		return originPort;
	}

	void setOriginPort(Port originPort) {
		this.originPort = originPort;
	}

	public void travelTo(Port origin, Port destination, double speed, ShipModel ship) throws NoRouteFoundException {
		// an entirely new model instance might neither have a current port nor be at sea.
		if (isInPort() || !isAtSea()) {
			setCurrentLatitude(origin.getLatitude());
			setCurrentLongitude(origin.getLongitude());
		}

		setCurrentPort(null);
		setDestinationPort(destination);
		setOriginPort(origin);
		setRoute(Route.getRoute(origin.getUnlocode(), destination.getUnlocode(), !ship.isPostPanamax(), !ship.isPostSuezmax()));
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
		this.currentSpeed = currentSpeed;
	}

	public void travel() {
		setDistanceLeft(getDistanceLeft() - currentSpeed);
	}

	public double getLongitude() {
		return currentLongitude.getCoordinate();
	}

	public double getLatitude() {
		return currentLatitude.getCoordinate();
	}

	private void setCurrentLatitude(LatLong currentLatitude) {
		this.currentLatitude = currentLatitude;
	}

	private void setCurrentLongitude(LatLong currentLongitude) {
		this.currentLongitude = currentLongitude;
	}

	/**
	 * Calculates bearing based on next route point.
	 * @return current bearing in degrees
	 */
	public double getBearing() {
		if (isInPort()) {
			return 0.0;
		}

		double lat2 = getNextPoint().getLatitude().getCoordinate();
		double lon2 = getNextPoint().getLongitude().getCoordinate();

//		double dLat = Math.toRadians(lat2 -getLatitude());
		double dLon = Math.toRadians(lon2 -getLongitude());

		double y = Math.sin(dLon) * Math.cos(lon2);
		double x = Math.cos(getLatitude())*Math.sin(lat2) - Math.sin(getLatitude())*Math.cos(lat2)*Math.cos(dLon);
		return Math.round(100*Math.toDegrees(Math.atan2(y, x)))/100.0;
	}

	private Route.Point getNextPoint() {
		return route.getPoints().get(routeLeg+1);
	}

	public void setRoute(Route route) {
		this.route = route;
		routeLeg = 0;
	}

	public Route getCurrentRoute() {
		return route;
	}

	public double getCurrentSpeed() {
		return currentSpeed;
	}
}
