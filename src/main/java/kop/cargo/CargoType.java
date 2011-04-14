/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kop.cargo;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 *
 * @author ola
 */
@Root
public class CargoType {
	public enum Packaging {
		drybulk,
		wetbulk,
		container,
		chemical
	}

	@Attribute
	private String name;
	@Element(required = false)
	private String description;
	@Element
	private
	Packaging packaging;

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
}

