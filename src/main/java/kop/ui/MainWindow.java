package kop.ui;

import com.bbn.openmap.MapBean;
import kop.Main;
import kop.game.Game;
import kop.game.GameStateListener;
import kop.map.MapBeanFactory;
import org.geotools.map.MapLayer;
import org.geotools.swing.JMapPane;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.AncestorListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelListener;
import java.util.logging.Logger;

import static kop.Main.*;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/3/11
 * Time: 2:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class MainWindow implements Window, GameStateListener {
	private JToggleButton startButton;
	private JButton displayShips;
	private JPanel contentPane;
	private JButton newShip;
	private JButton companyInfo;
	private MapBean mapBean;
	private JLabel currentDate;
	private JMapPane mapPane;
	private JLabel currentMoney;
	private MapLayer shipLayer;
	private org.slf4j.Logger logger = null;

	public MainWindow() {
		logger = LoggerFactory.getLogger(this.getClass());

		displayShips.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				displayFrame(new CompanyShipsWindow());
			}
		});
		newShip.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				displayFrame(new NewShipWindow());
			}
		});
		companyInfo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				displayFrame(new CompanyInfoWindow());
			}
		});
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (startButton.getModel().isSelected()) {
					logger.info("Selected");
					startButton.setText("STOP");
				} else {
					logger.info("Not selected");
					startButton.setText("START");
				}
			}
		});
	}

	public JPanel getContentPane() {
		return contentPane;
	}

	@Override
	public String getTitle() {
		return "Main window";  //To change body of implemented methods use File | Settings | File Templates.
	}

	private void createUIComponents() {
		MapBeanFactory mapBeanFactory = new MapBeanFactory();
		mapBean = mapBeanFactory.createOpenMapBean();
		mapBean.setVisible(false);

		currentDate = new JLabel();
		currentMoney = new JLabel();
		mapPane = mapBeanFactory.createGeoToolsBean();
		for (MapLayer layer: mapPane.getMapContext().getLayers()) {
			if (layer.getTitle().equals(MapBeanFactory.SHIP_LAYER)) {
				shipLayer = layer;
			}
		}
		stateChanged();
	}

	@Override
	public void stateChanged() {
		currentDate.setText(Game.getInstance().getCurrentDateAsString());
		currentMoney.setText(String.valueOf(Game.getInstance().getPlayerCompany().getMoney()));
	}

	public static void main(String[] args) {
		Game.getInstance().getPlayerCompany().setMoney(1000000000);
		Main.displayFrame(new MainWindow());
	}
}
