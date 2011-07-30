package kop.map.routecalculator;


import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

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
