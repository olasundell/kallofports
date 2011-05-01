package kop.ui;

import com.bbn.openmap.MapBean;
import com.bbn.openmap.layer.shape.ShapeLayer;
import kop.game.Game;
import kop.game.GameStateListener;
import kop.map.MapBeanFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

import static kop.Main.*;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/3/11
 * Time: 2:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class MainWindow implements Window, GameStateListener {
	private JButton startButton;
	private JButton displayShips;
	private JPanel contentPane;
	private JButton newShip;
	private JButton companyInfo;
	private MapBean mapBean;
	private JLabel currentDate;

	public MainWindow() {
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
		mapBean = mapBeanFactory.createMapBean();
		currentDate = new JLabel();
		currentDate.setText(Game.getInstance().getCurrentDateAsString());
	}

	@Override
	public void stateChanged() {
		// TODO do something relevant and nice.
	}
}
