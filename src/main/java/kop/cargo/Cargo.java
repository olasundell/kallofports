package kop.cargo;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/3/11
 * Time: 12:14 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Cargo {
	public double getTotalPrice();
	public int getDaysLeft(Date now);
	public int getWeight();
}
