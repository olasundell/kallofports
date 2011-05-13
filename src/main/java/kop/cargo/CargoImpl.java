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
	private int volume;

	public CargoImpl(CargoType type) {
		setType(type);
	}

    public CargoImpl(int weight, CargoType type, double pricePerTon, Date deadline) {
		this(type);
		setWeight(weight);
		setPricePerTon(pricePerTon);
		setDeadline(deadline);
    }
    
    @Override
	public double getTotalPrice() {
        if (getTotalPriceByWeight() > getTotalPriceByVolume()) {
			return getTotalPriceByWeight();
		} else {
			return getTotalPriceByVolume();
		}
    }

	private double getTotalPriceByVolume() {
		return volume*pricePerVolume;
	}

	private double getTotalPriceByWeight() {
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
//		if ((type.getPackaging() == CargoType.Packaging.wetbulk) || (type.getPackaging() == CargoType.Packaging.lng)) {
		setVolume((int) (weight / type.getDensityAsDouble()));
//		}
		// TODO handle TEU in some nice way.
//		else if (type.getPackaging() == CargoType.Packaging.container) {
//			setTEU()
//		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		CargoImpl cargo = (CargoImpl) o;

		if (Double.compare(cargo.pricePerTon, pricePerTon) != 0) return false;
		if (weight != cargo.weight) return false;
		if (deadline != null ? !deadline.equals(cargo.deadline) : cargo.deadline != null) return false;
		return !(type != null ? !type.equals(cargo.type) : cargo.type != null);
	}

	public double getPricePerTon() {
		return pricePerTon;
	}

	public void setPricePerTon(double pricePerTon) {
		this.pricePerTon = pricePerTon;
	}

	public double getPricePerVolume() {
		return pricePerVolume;
	}

	public void setPricePerVolume(double pricePerVolume) {
		this.pricePerVolume = pricePerVolume;
	}

	public CargoType getType() {
		return type;
	}

	public void setType(CargoType type) {
		this.type = type;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}

	public String toString() {
		return type + " weight: " +weight + " due date: " +deadline + " price per vol:  " + pricePerVolume + " price per weight: "+pricePerTon + " total price: "+getTotalPrice();
	}
}
