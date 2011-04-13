/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kop.cargo;

import java.util.Date;

/**
 *
 * @author ola
 */
public class CargoImpl implements Cargo {
	private int weight;
    private double pricePerTon;
	// TODO the total price is the highest of either per volume or per weight
	// a low-density (ie high volume per weight) cargo type will use up the available space.
	// a high-density cargo type will use up the available weight.
	// therefore the market rate is based on the highest of the two.
	private double pricePerVolume;
    private CargoType type;
    private Date deadline;
    
    public CargoImpl(int weight, CargoType type, double pricePerTon, Date deadline) {
        this.weight=weight;
        this.type=type;
        this.pricePerTon=pricePerTon;
        this.deadline=deadline;
    }
    
    @Override
	public double getTotalPrice() {
        return weight*pricePerTon;
    }
    
    @Override
	public int getDaysLeft(Date now) {
        return (int) (deadline.getTime() - now.getTime() / (1000*3600*24));
    }

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		CargoImpl cargo = (CargoImpl) o;

		if (Double.compare(cargo.pricePerTon, pricePerTon) != 0) return false;
		if (weight != cargo.weight) return false;
		if (deadline != null ? !deadline.equals(cargo.deadline) : cargo.deadline != null) return false;
		if (type != null ? !type.equals(cargo.type) : cargo.type != null) return false;

		return true;
	}
}
