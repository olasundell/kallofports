package kop.map.routecalculator;

/**
 * @author Ola Sundell
 */
public interface WaterVerifier extends Cloneable {
	public abstract boolean isWater(double lat, double lon);
	public abstract Object clone();
	public abstract void setupInstance(String filename);
}
