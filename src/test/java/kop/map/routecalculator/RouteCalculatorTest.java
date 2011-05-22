package kop.map.routecalculator;

import com.bbn.openmap.io.FormatException;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 5/17/11
 * Time: 3:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class RouteCalculatorTest {
	@Test
	public void createWorld() {
		NewWorld smallWorld = Util.getSmallWorld();
		assertNotNull(smallWorld);
		Point[][] arr=smallWorld.getPointsAsArray();
		assertNotNull(arr);
		// TODO add more tests here.
	}
}
