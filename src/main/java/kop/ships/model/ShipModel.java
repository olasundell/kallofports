/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kop.ships.model;

import java.util.ArrayList;
import java.util.List;

import kop.cargo.Freight;
import kop.cargo.FreightCarrier;
import kop.map.routecalculator.ASRoute;
import kop.ports.NoRouteFoundException;
import kop.ports.PortProxy;
import kop.ports.PositionOrDirection;
import kop.ships.OutOfFuelException;
import kop.ships.blueprint.ShipBlueprint;
import kop.ships.ShipClass;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.w3c.dom.ls.LSProgressEvent;

/**
 * The abstract ship model class, contains the majority of the logic and attributes for a ship model instance.
 * @author ola
 */
@Root
public abstract class ShipModel implements FreightCarrier {
	@Element
	private double currentFuel;

	@Attribute
	private String name;

    private List<Freight> freightList;
	@Element
	private PositionOrDirection currentPosition;
	@Element
	protected ShipBlueprint blueprint;
	protected ShipClass shipClass;

	public ShipModel() {
		currentPosition = new PositionOrDirection();
		freightList = new ArrayList<Freight>();
	}

	public int getHoursToDestination() {
		return currentPosition.getHoursToDest();
	}

	/**
	 * Moves the ship one increment.
	 * @throws kop.ships.OutOfFuelException
	 */

	public void travel() throws OutOfFuelException {
		if (!isAtSea()) {
			return;
		}

		currentPosition.travel();
		double newFuel = getCurrentFuel() - blueprint.getFuelConsumption(currentPosition.getCurrentSpeed());

		if (newFuel <= 0) {
			throw new OutOfFuelException();
		}

		setCurrentFuel(newFuel);
		// TODO fix this mess.
		// See Company class for suggestions.
		if (isAtSea() && currentPosition.getDistanceLeft() <= 0) {
			// we've arrived.
			currentPosition.setCurrentPort(currentPosition.getDestinationPort());
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ShipBlueprint getBlueprint() {
		return blueprint;
	}

	/**
	 * Creates a ShipModel instance from a ShipClass description.
	 * @param shipClass
	 * @return
	 */
	public static ShipModel createShip(String name, ShipClass shipClass) {
		ShipModel model;

		switch (shipClass.getClassType()) {
			case container:
				model = new ContainerShipModel();
				break;
			case tanker:
				model = new TankerShipModel();
				break;
			case bulk:
				model = new BulkShipModel();
				break;
			case lngcarrier:
				model = new LNGCarrierShipModel();
				break;
			default:
				throw new IllegalArgumentException(String.format("Ship class has an illegal class type, %s", shipClass.getClassType()));
		}
		model.setShipClass(shipClass);
		// TODO should we fill'er up by default? What if the player purchases a used vessel?
		model.setCurrentFuel(model.getMaxFuel());
		model.setName(name);

		return model;
	}

    ShipModel(String name) {
		this();
        this.name=name;
    }

	public double getDistanceLeft() {
		return currentPosition.getDistanceLeft();
	}

	public void setSail(PortProxy origin, PortProxy destination, double speed) throws NoRouteFoundException {
		currentPosition.travelTo(origin, destination, speed, this);
	}

	public void setCurrentPort(PortProxy currentPort) {
		currentPosition.setCurrentPort(currentPort);
	}

	public boolean isAtSea() {
		return currentPosition.isAtSea();
	}

	/**
	 * Calculates available deadweight tonnage.
	 * TODO ignores fuel level.
	 * @return
	 */
    public int getAvailableDWT() {
        int currentCargo = 0;
        
        for (Freight f: freightList) {
            currentCargo+=f.getCargo().getWeight();
        }
        
        return blueprint.getDwt() -currentCargo;
    }

	public boolean isPostPanamax() {
		// TODO loa Container ship and passenger ship: 965 ft  294.13

		return blueprint.isPostPanamax();
	}

	public boolean isPostSuezmax() {
		return blueprint.isPostSuezmax();
	}

	public int getDwt() {
		return blueprint.getDwt();
	}

	public double getMaxSpeed() {
		return blueprint.getMaxSpeed();
	}

	public double getLoa() {
		return blueprint.getLoa();
	}

	public double getDraft() {
		return blueprint.getDraft();
	}

	public double getMaxFuel() {
		return blueprint.getMaxFuel();
	}

	public double getCurrentFuel() {
		return currentFuel;
	}

	public void setCurrentFuel(double currentFuel) {
		if (currentFuel < 0.0 || currentFuel > getMaxFuel()) {
			String format = String.format("Trying to set current fuel level to %f, which is an invalid amount", currentFuel);
			throw new IllegalArgumentException(format);
		}
		this.currentFuel = currentFuel;
	}

	public void setBlueprint(ShipBlueprint blueprint) {
		this.blueprint = blueprint;
	}

	public double getLatitude() {
		return currentPosition.getLatitude();
	}

	public double getLongitude() {
		return currentPosition.getLongitude();
	}

	public void setPort(PortProxy port) {
		currentPosition.setCurrentPort(port);
	}

	public double getBearing() {
		return currentPosition.getBearing();
	}

	public ASRoute getCurrentRoute() {
		return currentPosition.getCurrentRoute();
	}

	public void addFreight(Freight freight) {
		freightList.add(freight);
	}

	public List<Freight> getFreights() {
		return freightList;
	}

	public PositionOrDirection getCurrentPosition() {
		return currentPosition;
	}

	public boolean isInPort() {
		return currentPosition.isInPort();
	}

	public String toString() {
		return String.format("%s (%s)", name,blueprint.getType());
	}

	public String getStatus() {
		if (currentPosition.isAtSea()) {
			return "At sea";
		} else if (currentPosition.isInPort()) {
			return "In port";
		}

		return "Undefined";
	}

	public String getDestination() {
		if (currentPosition.getDestinationPort() != null) {
			return currentPosition.getDestinationPort().getName();
		} else {
			return "N/A";
		}
	}

	public String getPosition() {
		if (currentPosition.isInPort()) {
			return currentPosition.getCurrentPort().getName();
		}
		return currentPosition.getCurrentPosition().toString();
	}

	public String getETA() {
		if (currentPosition.isAtSea()) {
			// TODO we shouldn't return a string, we should return a Date and leave conversion to the other party.
			return currentPosition.getETA().toString();
		}

		return "N/A";
	}

	public String getETD() {
		if (currentPosition.isAtSea()) {
			return String.format("%d", currentPosition.getHoursToDest());  //To change body of created methods use File | Settings | File Templates.
		}

		return "N/A";
	}

	public double getSpeed() {
		return currentPosition.getCurrentSpeed();
	}

	// TODO implement a damage/wear/fouling system. Huge task if it should be done properly.
	public String getCondition() {
		return "Superb";
	}

	public double getCurrentWorth() {
		// TODO add damage/wear/age stuff here.
		return shipClass.getPrice();
	}

	public void setShipClass(ShipClass shipClass) {
		this.shipClass = shipClass;
		setBlueprint(shipClass.getBlueprint());
	}

	public ShipClass getShipClass() {
		return shipClass;
	}
}
