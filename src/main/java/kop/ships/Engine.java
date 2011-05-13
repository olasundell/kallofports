package kop.ships;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

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

	public double getFuelUsage(double delta) {
		// without the 1000000 this would be grammes per hour. Dividing makes it metric tonnes.
		return bsfc * kW * delta / 1000000;
	}
}