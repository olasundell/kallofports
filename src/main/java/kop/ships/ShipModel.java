/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kop.ships;

import java.util.ArrayList;
import java.util.List;

import kop.cargo.Cargo;
import kop.ports.NoRouteFoundException;
import kop.ports.Port;
import kop.ports.PositionOrDirection;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 *
 * @author ola
 */
@Root
public abstract class ShipModel {
	@Element
    protected double currentFuel;

	@Attribute
	protected String name;

    protected List<Cargo> cargoList;
	@Element
	protected PositionOrDirection currentPosition;
	@Element
	protected ShipBlueprint shipBlueprint;

	public ShipModel() {
		currentPosition = new PositionOrDirection();
	}

	public int getHoursToDestination() {
		return currentPosition.getHoursToDest();
	}

	public void travel() {
		// TODO something with costs
		currentPosition.travel();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public enum ShipType {
		TANKER,
		BULKER,
		CONTAINER
	};

    public ShipModel(String name) {
        this.name=name;
        cargoList= new ArrayList<Cargo>();
		currentPosition = new PositionOrDirection();
    }

	public Object getDistanceLeft() {
		return currentPosition.getDistanceLeft();
	}

	public void setSail(Port origin, Port destination, double speed) throws NoRouteFoundException {
		currentPosition.travelTo(origin, destination, speed, this);
	}

	public void setCurrentPort(Port currentPort) {
		currentPosition.setCurrentPort(currentPort);
	}

	public boolean isAtSea() {
		return currentPosition.isAtSea();
	}

    public int getAvailableDWT() {
        int currentCargo = 0;
        
        for (Cargo c: cargoList) {
            currentCargo+=c.getWeight();
        }
        
        return shipBlueprint.getDwt() -currentCargo;
    }

	public boolean isPostPanamax() {
		// TODO loa Container ship and passenger ship: 965 ft  294.13

		return shipBlueprint.isPostPanamax();
	}

	public boolean isPostSuezmax() {
		return shipBlueprint.isPostSuezmax();
	}

	public int getDwt() {
		return shipBlueprint.getDwt();
	}

	public void setDwt(int dwt) {
		shipBlueprint.setDwt(dwt);
	}

	public double getMaxSpeed() {
		return shipBlueprint.getMaxSpeed();
	}

	public void setMaxSpeed(double maxSpeed) {
		shipBlueprint.setMaxSpeed(maxSpeed);
	}

	public double getLoa() {
		return shipBlueprint.getLoa();
	}

	public void setLoa(double loa) {
		shipBlueprint.setLoa(loa);
	}

	public double getDraft() {
		return shipBlueprint.getDraft();
	}

	public void setDraft(double draft) {
		shipBlueprint.setDraft(draft);
	}

	public double getMaxFuel() {
		return shipBlueprint.getMaxFuel();
	}

	public void setMaxFuel(double maxFuel) {
		shipBlueprint.setMaxFuel(maxFuel);
	}

	public double getCurrentFuel() {
		return currentFuel;
	}

	public void setCurrentFuel(double currentFuel) {
		this.currentFuel = currentFuel;
	}

}
