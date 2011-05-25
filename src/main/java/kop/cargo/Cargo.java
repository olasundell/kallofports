package kop.cargo;

import java.util.Date;

/**
 * Interface for all cargo types.
 * TODO change this, needs to be more generic.
 * TODO add getCargoType, add generic getters for volume and units (TEUs shouldn't be here, naturally)
 */
public interface Cargo {
	/**
	 * Gets the total price for the cargo.
	 * @return the total price based either on volume, TEUs or weight, depending on cargo type.
	 */
	public double getTotalPrice();

	/**
	 * Gets number of days left for the cargo based on the parameter.
	 * @param now current date (but can be any date, if that's what's asked)
	 * @return number of days left for the cargo.
	 * TODO should this be in Freight instead? Isn't Freight the logical bearer of the time?
	 */
	public int getDaysLeft(Date now);

	/**
	 * Gives the weight in metric tons, based on @CargoType information.
	 * @return weight in MT.
	 */
	public int getWeight();
}
