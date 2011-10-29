/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kop.cargo;

import java.util.Date;

/**
 * Implementation of the Cargo interface.
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
        return (int) ((deadline.getTime() - now.getTime()) / (1000*3600*24));
    }

	public int getWeight() {
		return weight;
	}

	@Override
	public CargoType getCargoType() {
		return type;
	}

	@Override
	public double getPricePerUnit() {
		if (pricePerTon > pricePerVolume) {
			return pricePerTon;
		} else {
			return pricePerVolume;
		}
	}

	public final void setWeight(int weight) {
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
		if (this == o) { return true; }
		if (o == null || getClass() != o.getClass()) { return false; }

		CargoImpl cargo = (CargoImpl) o;

		if (Double.compare(cargo.pricePerTon, pricePerTon) != 0) { return false; }
		return (weight == cargo.weight) && !(deadline != null ? !deadline.equals(cargo.deadline) : cargo.deadline != null) && !(type != null ? !type.equals(cargo.type) : cargo.type != null);
	}

	public double getPricePerTon() {
		return pricePerTon;
	}

	public final void setPricePerTon(double pricePerTon) {
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

	public final void setType(CargoType type) {
		this.type = type;
	}

	public Date getDeadline() {
		return deadline;
	}

	public final void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public int getVolume() {
		return volume;
	}

	public final void setVolume(int volume) {
		this.volume = volume;
	}

	@Override
	public int hashCode() {
		int result;
		long temp;
		result = weight;
		temp = pricePerTon != +0.0d ? Double.doubleToLongBits(pricePerTon) : 0L;
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		temp = pricePerVolume != +0.0d ? Double.doubleToLongBits(pricePerVolume) : 0L;
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		result = 31 * result + (type != null ? type.hashCode() : 0);
		result = 31 * result + (deadline != null ? deadline.hashCode() : 0);
		result = 31 * result + volume;
		return result;
	}

	public String toString() {
		return type + " weight: " +weight + " due date: " +deadline + " price per vol:  " + pricePerVolume + " price per weight: "+pricePerTon + " total price: "+getTotalPrice();
	}
}
