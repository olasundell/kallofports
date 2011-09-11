package kop.ui;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import kop.Main;
import kop.cargo.Freight;
import kop.company.Company;
import kop.game.CouldNotLoadFreightOntoShipException;
import kop.game.Game;
import kop.game.GameTestUtil;
import kop.ports.NoSuchPortException;
import kop.ports.PortProxy;
import kop.ships.model.ShipModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableRowSorter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/6/11
 * Time: 7:41 AM
 * To change this template use File | Settings | File Templates.
 */
public class PortWindow implements KopWindow {
	private PortProxy portProxy;
	private JLabel nameOfPort;
	private JTable portFreightTable;
	private JPanel contentPane;
	private JButton cancelButton;
	private JButton okButton;
	private JTable shipFreightTable;
	private JList shipsInPortListBox;
	private JLabel shipName;
	private JLabel shipType;
	private JLabel shipDWT;
	private JButton charterButton;
	private JLabel availableDWT;
	private JPanel shipInfoPanel;
	private FreightTableModel shipFreightTableModel;
	private FreightTableModel portFreightTableModel;
	private Logger logger;

	public PortWindow(PortProxy portProxy) {
		this.portProxy = portProxy;
		logger = LoggerFactory.getLogger(this.getClass());
		$$$setupUI$$$();

		shipsInPortListBox.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				ShipModel selectedValue = (ShipModel) shipsInPortListBox.getSelectedValue();
				shipName.setText(selectedValue.getName());
				shipType.setText(selectedValue.getBlueprint().getType().toString());
				shipDWT.setText(String.valueOf(selectedValue.getDwt()));
				availableDWT.setText(String.valueOf(selectedValue.getAvailableDWT()));
				shipFreightTable.setModel(new FreightTableModel(selectedValue));
			}
		});
		charterButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (portFreightTable.getSelectedRow() == -1) {
					logger.debug("Port freight table doesn't have a selected row");
					Main.fireDialog("Port freight table doesn't have a selected row");
					return;
				}

				if (shipsInPortListBox.getSelectedIndex() == -1) {
					logger.debug("Ship list box doesn't have a selected row");
					Main.fireDialog("Ship list box doesn't have a selected row");
					return;
				}

				Freight f = portFreightTableModel.getFreightAtRow(portFreightTable.convertRowIndexToModel(portFreightTable.getSelectedRow()));
				ShipModel ship = (ShipModel) shipsInPortListBox.getSelectedValue();

				// TODO add asserts, can the ship really take on the freight?
				// if not, add a part of the freight.
				try {
					Game.getInstance().loadFreightOntoShip(ship, f);
				} catch (CouldNotLoadFreightOntoShipException e1) {
					// TODO fire dialog box.
					logger.error("Couldn't load freight on ship for some reason.", e1);
					Main.fireDialog("Couldn't load freight on ship for some reason.");
					return;
				}
				portFreightTableModel.fireTableDataChanged();
				String msg = String.format("Loaded %s on %s", f.toString(), ship.toString());
				logger.debug(msg);
				Main.fireDialog(msg);
			}
		});
	}

	@Override
	public JPanel getContentPane() {
		return contentPane;
	}

	@Override
	public String getTitle() {
		return String.format("Port %s", portProxy.getName());
	}

	private void createUIComponents() {
		portFreightTableModel = new FreightTableModel(Game.getInstance().getFreightMarket());
		portFreightTable = new JTable(portFreightTableModel);
		TableRowSorter<FreightTableModel> portTableSorter = new TableRowSorter<FreightTableModel>(portFreightTableModel);
		portTableSorter.setRowFilter(new FreightTableModel.PortRowFilter(portProxy));
		portFreightTable.setRowSorter(portTableSorter);

		Company playerCompany = Game.getInstance().getPlayerCompany();
		List<ShipModel> shipsInPortList = playerCompany.findShipsInPort(portProxy);

		shipsInPortListBox = new JList(shipsInPortList.toArray());

		// TODO this is fugly-hacked at the moment.
		FreightTableModel shipFreightTableModel = null;

		if (shipsInPortList.size() > 0) {
			shipFreightTableModel = new FreightTableModel(shipsInPortList.get(0));
		} else {
			shipFreightTableModel = new FreightTableModel(null);
		}

		shipFreightTable = new JTable(shipFreightTableModel);
		TableRowSorter<FreightTableModel> shipTableSorter = new TableRowSorter<FreightTableModel>(shipFreightTableModel);
		shipFreightTable.setRowSorter(shipTableSorter);

		nameOfPort = new JLabel();
		nameOfPort.setText(portProxy.getName());
	}

	public static void main(String[] args) {
		GameTestUtil.setupInstanceForTest();
		String portName = "Durban";
		try {
			Main.displayFrame(new PortWindow(Game.getInstance().getPortByName(portName).getProxy()));
		} catch (NoSuchPortException e) {
			Logger logger = LoggerFactory.getLogger(PortWindow.class);
			logger.error(String.format("Could not find port %s", portName), e);
		}
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
		contentPane.setLayout(new FormLayout("left:5dlu:noGrow,fill:max(d;125dlu):grow(0.6),fill:d:noGrow,left:4dlu:noGrow,fill:max(d;60dlu):grow(0.30000000000000004),left:5dlu:noGrow,fill:max(d;60dlu):grow(0.30000000000000004),left:5dlu:noGrow,fill:d:noGrow", "center:d:noGrow,top:5dlu:noGrow,center:m:grow(0.1),top:5dlu:noGrow,center:max(d;4px):noGrow,center:d:noGrow,top:5dlu:noGrow,center:m:grow(0.1),top:5dlu:noGrow,center:d:noGrow,center:max(d;4px):noGrow,top:5dlu:noGrow,center:d:noGrow"));
		final JScrollPane scrollPane1 = new JScrollPane();
		CellConstraints cc = new CellConstraints();
		contentPane.add(scrollPane1, cc.xyw(2, 3, 2, CellConstraints.FILL, CellConstraints.FILL));
		shipFreightTable.setName("shipFreightTable");
		scrollPane1.setViewportView(shipFreightTable);
		final JScrollPane scrollPane2 = new JScrollPane();
		contentPane.add(scrollPane2, cc.xyw(2, 8, 2, CellConstraints.FILL, CellConstraints.FILL));
		portFreightTable.setName("portFreightTable");
		scrollPane2.setViewportView(portFreightTable);
		final JScrollPane scrollPane3 = new JScrollPane();
		contentPane.add(scrollPane3, cc.xyw(5, 3, 3, CellConstraints.FILL, CellConstraints.FILL));
		shipsInPortListBox.setSelectionMode(0);
		scrollPane3.setViewportView(shipsInPortListBox);
		cancelButton = new JButton();
		cancelButton.setText("Cancel");
		cancelButton.setMnemonic('C');
		cancelButton.setDisplayedMnemonicIndex(0);
		contentPane.add(cancelButton, cc.xy(7, 11));
		okButton = new JButton();
		okButton.setText("OK");
		okButton.setMnemonic('O');
		okButton.setDisplayedMnemonicIndex(0);
		contentPane.add(okButton, cc.xy(5, 11));
		charterButton = new JButton();
		charterButton.setName("charterButton");
		charterButton.setText("Charter");
		charterButton.setMnemonic('T');
		charterButton.setDisplayedMnemonicIndex(4);
		contentPane.add(charterButton, cc.xy(2, 5, CellConstraints.RIGHT, CellConstraints.DEFAULT));
		shipInfoPanel = new JPanel();
		shipInfoPanel.setLayout(new FormLayout("fill:d:grow(0.30000000000000004),left:4dlu:noGrow,fill:d:grow(0.19999999999999998)", "center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow"));
		shipInfoPanel.setName("shipInfoPanel");
		contentPane.add(shipInfoPanel, cc.xyw(5, 8, 3));
		final JLabel label1 = new JLabel();
		label1.setText("Ship name");
		shipInfoPanel.add(label1, cc.xy(1, 1));
		final JLabel label2 = new JLabel();
		label2.setText("Ship type");
		shipInfoPanel.add(label2, cc.xy(1, 3));
		final JLabel label3 = new JLabel();
		label3.setText("Max DWT");
		shipInfoPanel.add(label3, cc.xy(1, 5));
		shipName = new JLabel();
		shipName.setName("shipName");
		shipName.setText("Label");
		shipInfoPanel.add(shipName, cc.xy(3, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
		shipType = new JLabel();
		shipType.setName("shipType");
		shipType.setText("Label");
		shipInfoPanel.add(shipType, cc.xy(3, 3, CellConstraints.LEFT, CellConstraints.DEFAULT));
		shipDWT = new JLabel();
		shipDWT.setName("maxDWT");
		shipDWT.setText("Label");
		shipInfoPanel.add(shipDWT, cc.xy(3, 5, CellConstraints.LEFT, CellConstraints.DEFAULT));
		final JLabel label4 = new JLabel();
		label4.setText("Available DWT");
		shipInfoPanel.add(label4, cc.xy(1, 7));
		availableDWT = new JLabel();
		availableDWT.setName("availableDWT");
		availableDWT.setText("Label");
		shipInfoPanel.add(availableDWT, cc.xy(3, 7));
		contentPane.add(nameOfPort, cc.xy(5, 5, CellConstraints.DEFAULT, CellConstraints.FILL));
	}

	/**
	 * @noinspection ALL
	 */
	public JComponent $$$getRootComponent$$$() {
		return contentPane;
	}
}
