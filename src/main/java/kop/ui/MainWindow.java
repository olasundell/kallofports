package kop.ui;

import com.bbn.openmap.MapBean;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import kop.Main;
import kop.game.Game;
import kop.game.GameStateListener;
import kop.game.GameTestUtil;
import kop.map.MapBeanFactory;
import kop.ships.ShipnameAlreadyExistsException;
import org.geotools.map.MapLayer;
import org.geotools.swing.JMapPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static kop.Main.*;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/3/11
 * Time: 2:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class MainWindow implements KopWindow, GameStateListener {
	private JToggleButton startButton;
	private JButton displayShips;
	private JPanel contentPane;
	private JButton newShip;
	private JButton companyInfo;
	private JMapPane mapPane;
	private JLabel currentMoney;
	private JLabel currentDateTime;
	private Logger logger = null;
	private MapLayer shipLayer;

	public MainWindow() {
		$$$setupUI$$$();
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

		currentDateTime = new JLabel();
		currentMoney = new JLabel();
		mapPane = mapBeanFactory.createGeoToolsBean();
		for (MapLayer layer : mapPane.getMapContext().getLayers()) {
			if (layer.getTitle().equals(MapBeanFactory.SHIP_LAYER)) {
				shipLayer = layer;
			}
		}
		stateChanged();
	}

	@Override
	public void stateChanged() {
		currentDateTime.setText(Game.getInstance().getCurrentDateAsString());
		currentMoney.setText(String.format(KopWindow.MONEY_TEXT_FORMAT, Game.getInstance().getPlayerCompany().getMoney()));
	}

	public static void main(String[] args) throws ShipnameAlreadyExistsException {
		GameTestUtil.setupInstanceForTest();
		Main.displayFrame(new MainWindow());
	}

	/**
	 * Method generated by IntelliJ IDEA GUI Designer
	 * >>> IMPORTANT!! <<<
	 * DO NOT edit this method OR call it in your code!
	 *
	 * @noinspection ALL
	 */
	private void $$$setupUI$$$() {
		createUIComponents();
		contentPane = new JPanel();
		contentPane.setLayout(new FormLayout("fill:d:noGrow,left:4dlu:noGrow,fill:55dlu:noGrow,left:5dlu:noGrow,fill:p:grow,left:5dlu:noGrow,fill:d:grow,left:5dlu:noGrow,fill:max(d;4px):noGrow,left:5dlu:noGrow,fill:d:noGrow", "center:d:noGrow,top:5dlu:noGrow,center:273px:grow,top:5dlu:noGrow,center:max(d;4px):noGrow,top:6dlu:noGrow,center:max(d;4px):noGrow,top:5dlu:noGrow,center:max(d;4px):noGrow,top:5dlu:noGrow,center:d:noGrow"));
		displayShips = new JButton();
		displayShips.setText("Show ships");
		CellConstraints cc = new CellConstraints();
		contentPane.add(displayShips, cc.xy(9, 7));
		newShip = new JButton();
		newShip.setText("New ship");
		contentPane.add(newShip, cc.xy(9, 5));
		startButton = new JToggleButton();
		startButton.setActionCommand("startButton");
		startButton.setHorizontalAlignment(0);
		startButton.setLabel("START");
		startButton.setText("START");
		startButton.setMnemonic('S');
		startButton.setDisplayedMnemonicIndex(0);
		contentPane.add(startButton, cc.xywh(3, 5, 1, 5, CellConstraints.DEFAULT, CellConstraints.FILL));
		companyInfo = new JButton();
		companyInfo.setText("Company info");
		contentPane.add(companyInfo, cc.xy(9, 9));
		final JLabel label1 = new JLabel();
		label1.setText("Current date and time:");
		contentPane.add(label1, cc.xy(5, 5));
		contentPane.add(mapPane, cc.xyw(3, 3, 7, CellConstraints.DEFAULT, CellConstraints.FILL));
		final JLabel label2 = new JLabel();
		label2.setText("Current money:");
		contentPane.add(label2, cc.xy(5, 7));
		contentPane.add(currentDateTime, cc.xy(7, 5));
		contentPane.add(currentMoney, cc.xy(7, 7, CellConstraints.DEFAULT, CellConstraints.FILL));
	}

	/**
	 * @noinspection ALL
	 */
	public JComponent $$$getRootComponent$$$() {
		return contentPane;
	}
}
