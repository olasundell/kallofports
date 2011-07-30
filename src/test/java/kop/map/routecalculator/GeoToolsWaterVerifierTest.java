package kop.map.routecalculator;

import junit.framework.TestCase;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertTrue;

/**
 * @author Ola Sundell
 */
public class GeoToolsWaterVerifierTest {
	@Test
	public void isWaterShouldReturnTrueForObviousWateryPoints() {
		GeoToolsWaterVerifier verifier = new GeoToolsWaterVerifier();
		verifier.setupInstance("data/shape/10m_ocean.shp");
		double[] wateryPoints = {
				36.265536,-43.081056,
				-28.667455,-119.633791,
				-45.819612,-41.008302,
				42.442917,157.924803
		};
		for (int i=0;i<wateryPoints.length;i+=2) {
		assertTrue(String.format("Verifying %.2f,%.2f didn't work!",wateryPoints[i],wateryPoints[i+1]),
				verifier.isWater(wateryPoints[i],wateryPoints[i+1]));
		}
	}
}
