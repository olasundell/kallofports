package kop.map.routecalculator;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * @author Ola Sundell
 */
public class OpenMapWaterVerifierTest {
	@Test
	public void verifyClonedObject() {
		OpenMapWaterVerifier first = new OpenMapWaterVerifier();
		first.setupInstance("data/shape/10m_ocean.shp");
		OpenMapWaterVerifier second = (OpenMapWaterVerifier) first.clone();
		assertNotNull(second);
		assertEquals(first,second);
	}

	@Test
	public void isWaterShouldReturnTrueForObviousWateryPoints() {
		OpenMapWaterVerifier verifier = new OpenMapWaterVerifier();
		verifier.setupInstance("data/shape/10m_ocean.shp");
		assertTrue("Verifying 0,0 didn't work!",verifier.isWater(0,0));
	}
}
