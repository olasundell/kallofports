package kop.ships;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * A blueprint contains the constant attributes of a ship, for instance length, dwt and max speed.
 */

@Root
public abstract class ShipBlueprint {
	@Element
	private int dwt;
	@Element
	private int grossTonnage;
	@Element
	private int netTonnage;
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
	@Element
	private Engine engine;

	public enum ShipType {
		bulk { public String toString() { return "Bulk hauler"; } },
		container { public String toString() { return "Container ship"; } },
		tanker { public String toString() { return "Tanker"; } }
	}

	ShipBlueprint() {
	}

	public boolean isPostPanamax() {
		// TODO loa Container ship and passenger ship: 965 ft  294.13
		return getLoa() > 289.56 || getBeam() > 32.31 || getDraft() > 12.04;

	}

	public boolean isPostSuezmax() {
//		20.1 m (66 ft) of draught for ships with the beam no wider than 50.0 m (164.0 ft) or 12.2 m (40 ft) of draught for ships with maximum allowed beam of 77.5 m (254 ft 3 in).
		return (getBeam() > 75.0 && getDraft() > 12.2) || (getBeam() > 50.0 && getDraft() > 20.1);
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

	public void setDailyCost(int dailyCost) {
		this.dailyCost = dailyCost;
	}

	public double getDailyCost() {
		return dailyCost;
	}

	public void setEngine(Engine engine) {
		this.engine = engine;
	}

	public int getGrossTonnage() {
		return grossTonnage;
	}

	public void setGrossTonnage(int grossTonnage) {
		this.grossTonnage = grossTonnage;
	}

	public int getNetTonnage() {
		return netTonnage;
	}

	public void setNetTonnage(int netTonnage) {
		this.netTonnage = netTonnage;
	}

	public double getBeam() {
		return beam;
	}

	public void setBeam(double beam) {
		this.beam = beam;
	}

	public abstract ShipType getType();
}