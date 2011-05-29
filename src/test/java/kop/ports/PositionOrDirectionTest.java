package kop.ports;

import junit.framework.TestCase;
import kop.game.Game;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 5/2/11
 * Time: 4:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class PositionOrDirectionTest {
	@Test
	public void setPortShouldUpdateLatLong() throws NoSuchPortException {
		PositionOrDirection pos = new PositionOrDirection();
		Port helsinki = Game.getInstance().getPortByName("Helsinki");
		pos.setCurrentPort(helsinki.getProxy());
		assertEquals(helsinki.getLatitude(), pos.getLatitude());
	}
}
