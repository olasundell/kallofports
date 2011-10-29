package kop.cargo;

import junit.framework.TestCase;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

/**
 * @author ola
 */
public class CargoTypeTest {
	@Test
	public void setDensityByStringShouldThrowExceptionIfStringIsGarbage() {
		CargoType type = new CargoType();
		boolean exceptionThrown = false;
		try {
			type.setDensity("ab,qe!");
		} catch (NumberFormatException e) {
			exceptionThrown = true;
		}

		assertTrue(exceptionThrown, "Passing a garbage string to setDensity did not result in an exception!");
	}
}
