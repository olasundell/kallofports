package kop.map.routecalculator;

/**
 * @author Ola Sundell
 */
public interface WaterVerifier extends Cloneable {
	boolean isWater(double lat, double lon);
	Object clone();
	void setupInstance(String filename);
}
