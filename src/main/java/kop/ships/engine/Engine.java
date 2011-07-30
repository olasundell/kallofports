package kop.ships.engine;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Model for an engine, with util methods.
 */

@Root
public class Engine {
	@Element
	private double bsfc;
	@Element
	private String manufacturer;
	@Attribute
	private String modelName;
	@Element
	private double kW;

	public Engine() {
	}

	public double getBsfc() {
		return bsfc;
	}

	public void setBsfc(double bsfc) {
		this.bsfc = bsfc;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public double getkW() {
		return kW;
	}

	public void setkW(double kW) {
		this.kW = kW;
	}

	/**
	 * Gets the fuel usage for a fraction of the max power output.
	 * @param fractionOfMaxKW the fraction of the max power output, where 1 is max and 0 is min.
	 * @return
	 */
	public double getFuelUsage(double fractionOfMaxKW) {
		if (fractionOfMaxKW > 1 || fractionOfMaxKW < 0) {
			throw new IllegalArgumentException(String.format("The fraction of max power output must be between 0.0 and 1.0, and it is %f", fractionOfMaxKW));
		}
		// without the 1000000 this would be grammes per hour. Dividing makes it metric tonnes.
		return bsfc * kW * fractionOfMaxKW / 1000000;
	}
}