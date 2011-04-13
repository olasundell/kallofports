package kop.ships;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

import java.util.Map;

/**
 * A blueprint contains the constant attributes of a ship, for instance length, dwt and max speed.
 */

@Root
public abstract class ShipBlueprint {
	@Element
	private int dwt;
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

	public ShipBlueprint() {
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
}