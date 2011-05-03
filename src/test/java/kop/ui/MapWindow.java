package kop.ui;

import com.bbn.openmap.MapBean;
import kop.game.Game;
import kop.map.MapBeanFactory;
import kop.ports.NoSuchPortException;
import kop.ships.ShipModel;
import org.junit.Test;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/30/11
 * Time: 1:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class MapWindow {
	public static void main(String[] args) throws NoSuchPortException {
		JFrame frame = createJFrame();
		frame.setVisible(true);
	}

	@Test
	public void createdJFrameWithMapBeanSmokeTest() throws NoSuchPortException {
		JFrame frame = MapWindow.createJFrame();
		assertNotNull(frame);
	}

	private static JFrame createJFrame() throws NoSuchPortException {
		// Create a Swing frame
		JFrame frame = new JFrame("Simple Map");

		// Size the frame appropriately
		frame.setSize(1024, 768);

		ShipModel ship = ShipModel.createShip(Game.getInstance().getShipClasses().get(0));
		Game.getInstance().getPlayerCompany().addShip(ship);
		ship.setPort(Game.getInstance().getPortByName("Helsinki"));

		// Create a MapBean
		MapBean mapBean = new MapBeanFactory().createMapBean();

		// Add the map to the frame
		frame.getContentPane().add(mapBean);

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		// Display the frame
		return frame;
	}
}
