package kop.game;

import kop.ports.NoSuchPortException;
import kop.ports.Port;
import kop.ships.EngineList;
import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/26/11
 * Time: 1:47 PM
 * To change this template use File | Settings | File Templates.
 */

public class GameTest {
	private Game instance;

	@Before
	public void setUp() throws Exception {
		instance = Game.getInstance();
		String s = "1999-12-31 23:00";
		Date d = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, new Locale("sv")).parse(s);
		instance.setDate(d);
	}

	@Test
	public void playerCompanyShouldNotBeNull() {
		assertNotNull(instance.getPlayerCompany());
	}

	@Test
	public void engineListShouldContainEntries() {
		EngineList el = instance.getEngineList();
		assertNotNull(el);
		assertNotNull(el.getAnEngineForTest());
	}

	@Test
	public void gettingPortByNameShouldReturnAPort() throws NoSuchPortException {
		Port p = instance.getPortByName("Singapore");
		assertNotNull(p);
	}

	/**
	 *  This is more of an integration test.
	 */

	@Test
	public void timeStepShouldPlayTheGame() {
		MyGameStateListener listener = new MyGameStateListener();

		instance.addListener(listener);

		Date before = instance.getCurrentDate();
		instance.stepTime();
		Date now = instance.getCurrentDate();

		assertNotNull(before);
		assertNotNull(now);
		assertTrue(before.before(now));

		GregorianCalendar cal = (GregorianCalendar) GregorianCalendar.getInstance();
		cal.setTime(now);

		assertEquals(2000, cal.get(Calendar.YEAR));
		assertEquals(Calendar.JANUARY,cal.get(Calendar.MONTH));
		assertEquals(0,cal.get(Calendar.HOUR));
		assertTrue(listener.state);
	}

	private static class MyGameStateListener implements GameStateListener {
		public boolean state = false;

		@Override
		public void stateChanged() {
			state = true;
		}
	}
}
