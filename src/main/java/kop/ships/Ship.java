/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kop.ships;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import kop.cargo.Cargo;
import kop.cargo.CargoImpl;
import kop.ports.NoRouteFoundException;
import kop.ports.Port;
import kop.ports.PositionOrDirection;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 *
 * @author ola
 */
@Root
public class Ship {
	@Element
    private double currentFuel;
	@Element
	protected int dwt;
	@Element
	private double maxSpeed;
	@Element
	private double loa;
	@Element
	private double beam;
	@Element
	private double draft;
	@Element
	private double maxFuel;
	@Element
	private double dailyCost;
	private Map<Float, Float> fuelConsumption;

	@Attribute
	private String name;

    private List<Cargo> cargoList;
	private PositionOrDirection currentPosition;

	// needed for simple-xml
	public Ship() {

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

	public enum ShipType {
		TANKER,
		BULKER,
		CONTAINER
	};

    public Ship(String name) {
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
        
        return dwt-currentCargo;
    }

	public boolean isPostPanamax() {
		// TODO loa Container ship and passenger ship: 965 ft  294.13
		return loa > 289.56 || beam > 32.31 || draft > 12.04;

	}

	public boolean isPostSuezmax() {
//		20.1 m (66 ft) of draught for ships with the beam no wider than 50.0 m (164.0 ft) or 12.2 m (40 ft) of draught for ships with maximum allowed beam of 77.5 m (254 ft 3 in).
		return (beam > 75.0 && draft > 12.2) || (beam > 50.0 && draft > 20.1);
	}

	public int getDwt() {
		return dwt;
	}

	public void setDwt(int dwt) {
		this.dwt = dwt;
	}

	public double getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(double maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public double getLoa() {
		return loa;
	}

	public void setLoa(double loa) {
		this.loa = loa;
	}

	public double getDraft() {
		return draft;
	}

	public void setDraft(double draft) {
		this.draft = draft;
	}

	public double getMaxFuel() {
		return maxFuel;
	}

	public void setMaxFuel(double maxFuel) {
		this.maxFuel = maxFuel;
	}

	public double getCurrentFuel() {
		return currentFuel;
	}

	public void setCurrentFuel(double currentFuel) {
		this.currentFuel = currentFuel;
	}

}
