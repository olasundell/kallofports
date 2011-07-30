package kop.map;

import com.bbn.openmap.MapBean;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/30/11
 * Time: 10:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class MapBeanFactoryTest {
	@Test
	public void createMapBeanShouldReturnValidObject() {
		MapBeanFactory mapBeanFactory = new MapBeanFactory();
		MapBean mapBean = mapBeanFactory.createOpenMapBean();
		assertNotNull(mapBean);
	}
}
