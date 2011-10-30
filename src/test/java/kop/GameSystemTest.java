package kop;

import kop.ships.ShipnameAlreadyExistsException;
import kop.ui.KopUITest;
import org.testng.annotations.Test;

/**
 * @author ola
 */
public class GameSystemTest extends KopUITest {
	public GameSystemTest() throws ShipnameAlreadyExistsException {
		super();
	}

	@Test(groups={"system"})
	public void playTheGame() {

	}
}
