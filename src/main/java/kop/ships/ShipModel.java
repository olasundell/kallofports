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
	private double currentFuel;

	@Attribute
	private String name;

    private List<Cargo> cargoList;
	@Element
	private PositionOrDirection currentPosition;
	@Element
	ShipBlueprint blueprint;

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

	public ShipBlueprint getBlueprint() {
		return blueprint;
	}

	public static ShipModel createShip(ShipClass shipClass) {
		ShipModel model = null;
		switch (shipClass.getClassType()) {
			case container:
				model = new ContainerShipModel();
			case tanker:
				model = new TankerShipModel();
			case bulk:
				model = new BulkShipModel();
		}
		model.setBlueprint(shipClass.getBlueprint());

		return model;
	}

    ShipModel(String name) {
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

	public void setDwt(int dwt) {
		blueprint.setDwt(dwt);
	}

	public double getMaxSpeed() {
		return blueprint.getMaxSpeed();
	}

	public void setMaxSpeed(double maxSpeed) {
		blueprint.setMaxSpeed(maxSpeed);
	}

	public double getLoa() {
		return blueprint.getLoa();
	}

	public void setLoa(double loa) {
		blueprint.setLoa(loa);
	}

	public double getDraft() {
		return blueprint.getDraft();
	}

	public void setDraft(double draft) {
		blueprint.setDraft(draft);
	}

	public double getMaxFuel() {
		return blueprint.getMaxFuel();
	}

	public void setMaxFuel(double maxFuel) {
		blueprint.setMaxFuel(maxFuel);
	}

	public double getCurrentFuel() {
		return currentFuel;
	}

	public void setCurrentFuel(double currentFuel) {
		this.currentFuel = currentFuel;
	}

	public void setBlueprint(ShipBlueprint blueprint) {
		this.blueprint = blueprint;
	}
}
