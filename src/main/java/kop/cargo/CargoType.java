/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kop.cargo;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Describes the properties of a type of cargo.
 */
@Root
public class CargoType {
	/**
	 * What capability a ship must have to be able to transport this type of cargo.
	 */
	public enum Packaging {
		drybulk,
		wetbulk,
		container,
		chemical,
		lng
	}

	@Attribute
	private String name;
	@Element(required = false)
	private String description;
	@Element
	private Packaging packaging;
	private double density;

	public CargoType() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Packaging getPackaging() {
		return packaging;
	}

	public void setPackaging(Packaging packaging) {
		this.packaging = packaging;
	}

	@Element
	public String getDensity() {
		return String.valueOf(density);
	}

	/**
	 * Cargo type density
	 * @return density in kilogrammes
	 */
	public double getDensityAsDouble() {
		return density;
	}

	@Element
	public void setDensity(String density) {
		if (density.matches("[0-9]+-[0-9]+")) {
			String[] split = density.split("-");
			double a = Double.parseDouble(split[0]);
			double b = Double.parseDouble(split[1]);
			setDensity((a+b)/2);
		} else if (density.matches("[0-9]+(\\.[0-9]+)*")) {
			setDensity(Double.parseDouble(density));
		} else {
			throw new NumberFormatException("Could not parse cargo type density string: "+density);
		}
	}

	public void setDensity(double density) {
		this.density = density;
	}

	public String toString() {
		return "Name: "+name+" Packaging: "+packaging+" Density: "+getDensity();
	}
}

