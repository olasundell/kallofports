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

/**
 *
 * @author ola
 */
public class Ship {
    private int dwt;
    private double maxSpeed;
    private double loa;
	private double beam;
    private double draft;
    private double maxFuel;
    private double currentFuel;
    private Map<Float, Float> fuelConsumption;
    private String name;
    private List<Cargo> cargoList;

	public enum ShipType {
		TANKER,
		BULK,
		CONTAINER
	};

    public Ship(String name) {
        this.name=name;
        cargoList= new ArrayList<Cargo>();
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

	public double getBeam() {
		return beam;
	}

	public void setBeam(double beam) {
		this.beam = beam;
	}
}
