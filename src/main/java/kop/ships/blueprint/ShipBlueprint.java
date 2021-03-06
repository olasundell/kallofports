package kop.ships.blueprint;

import kop.cargo.CargoType;
import kop.ships.engine.Engine;
import kop.ships.engine.EngineList;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * A blueprint contains the constant attributes of a ship, for instance length, dwt and max speed.
 */

@Root
public abstract class ShipBlueprint {
	@Element
	private int dwt;
	@Element
	private int grossTonnage;
	@Element(required = false)
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
	private final List<Engine> engines;

	/**
	 * TODO fix this method and the corresponding XML so this isn't made up, ie use real values instead.
	 * We might even do away with ship type?
	 * @return
	 */
	public List<CargoType.Packaging> getCargoCapabilities() {
		List<CargoType.Packaging> list = new ArrayList<CargoType.Packaging>();
		switch (getType()) {
			case container:
				list.add(CargoType.Packaging.container);
				break;
			case tanker:
				list.add(CargoType.Packaging.wetbulk);
				break;
			case bulk:
				list.add(CargoType.Packaging.drybulk);
				break;
			case lngcarrier:
				list.add(CargoType.Packaging.lng);
				list.add(CargoType.Packaging.chemical);
				break;
		}
		return  list;
	}

	public double getBlockCoefficient() {
		return 0.8;
	}

	public enum ShipType {
		bulk {  public String toString() { return "Bulk hauler"; } },
		container { public String toString() { return "Container ship"; } },
		tanker { public String toString() { return "Tanker"; } },
		lngcarrier{ public String toString() { return "LNG carrier"; } }
	}

	ShipBlueprint() {
		engines = new ArrayList<Engine>();
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

	@ElementList
	public void setEngines(List<String> modelNames) {
		for (String e: modelNames) {
			engines.add(EngineList.getInstance().getByName(e));
		}
	}

	@ElementList
	public List<String> getEngines() {
		List<String> modelNames = new ArrayList<String>();
		for (Engine e: engines) {
			modelNames.add(e.getModelName());
		}

		return modelNames;
	}

	/**
	 * Calculates fuel usage per hour at a fraction of max power output.
	 * @param fraction fraction of max power output.
	 * @return fuel usage for ship at a fraction of max power output.
	 */
	public double getFuelUsageFractionOfMaxPower(double fraction) {
		double usage=0;

		for (Engine e: engines) {
			usage+=e.getFuelUsage(fraction);
		}

		return usage;
	}

	/**
	 * Calculates fuel usage per hour at a given speed.
	 * TODO this needs to be implemented properly, calculate towing resistance for the ship and use that.
	 * @param speed the speed of the ship
	 * @return fuel usage at a specific speed.
	 */
	public double getFuelUsage(double speed) {
		double usage=0;

		for (Engine e: engines) {
			// this isn't correct at all.
			usage+=e.getFuelUsage(speed/maxSpeed);
		}

		return usage;
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

	public void addEngine(Engine engine) {
		engines.add(engine);
	}

	/**
	 * Calculates the hourly fuel usage based on current speed.
	 * TODO this isn't exponential atm, it just uses the fraction of speed/maxSpeed. It needs to be fixed properly.
	 * @param speed current ship speed
	 * @return fuel usage at current speed
	 */

	public double getFuelConsumption(double speed) {
		double consumption=0;
		for (Engine e: engines) {
			consumption+=e.getFuelUsage(speed/getMaxSpeed());
		}
		return consumption;
	}

	public abstract ShipType getType();

	public double holtropMennen() {
		double L = getLoa() * 3.2808399;
		double T = getDraft() * 3.28088399;
		double B = getBeam() *  3.28088399;
		double Cm = 0.98;
		double Cb = getBlockCoefficient();
		double Cw = Cb + 0.10;
		double ABT = 25;

		return Math.pow((L * (B + (2 * T)) * Cm),(((0.5) * (((0.453 + (0.4425 * Cb)) - (0.2862 * Cm)) + ((0.003467 * B) / T) + (0.3696 * Cw))) + ((19.65 * ABT) / Cb)));
	}

	public double dennyMumford() {
		double L = getLoa() * 3.2808399;
		double T = getDraft() * 3.28088399;
		// should be displacement in square metres, but who cares?
		double Vol = getDwt();

		return 1.7*L*T + Vol/T;
	}
}
