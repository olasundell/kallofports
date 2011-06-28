package kop.ui;

import kop.Main;
import kop.company.Company;
import kop.game.Game;
import kop.game.GameTestUtil;
import kop.ports.NoSuchPortException;
import kop.ports.PortProxy;
import kop.ships.model.ShipModel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableRowSorter;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/6/11
 * Time: 7:41 AM
 * To change this template use File | Settings | File Templates.
 */
public class PortWindow implements Window {
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

	public PortWindow(PortProxy portProxy) {
		this.portProxy = portProxy;
		shipsInPortListBox.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				ShipModel selectedValue = (ShipModel) shipsInPortListBox.getSelectedValue();
				shipName.setText(selectedValue.getName());
				shipType.setText(selectedValue.getBlueprint().getType().toString());
				shipDWT.setText(String.valueOf(selectedValue.getDwt()));
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

		FreightTableModel portFreightTableModel = new FreightTableModel();
		portFreightTable = new JTable(portFreightTableModel);
		TableRowSorter<FreightTableModel> portTableSorter = new TableRowSorter<FreightTableModel>(portFreightTableModel);
		portFreightTable.setRowSorter(portTableSorter);
		portTableSorter.setRowFilter(new FreightTableModel.PortRowFilter(portProxy));

		Company playerCompany = Game.getInstance().getPlayerCompany();
		List<ShipModel> shipsInPortList = playerCompany.findShipsInPort(portProxy);

		shipsInPortListBox = new JList(shipsInPortList.toArray());

		// TODO this is fugly-hacked at the moment.
		FreightTableModel shipFreightTableModel = new FreightTableModel(shipsInPortList.get(0));
		shipFreightTable = new JTable(shipFreightTableModel);
		TableRowSorter<FreightTableModel> shipTableSorter = new TableRowSorter<FreightTableModel>(shipFreightTableModel);
		shipFreightTable.setRowSorter(shipTableSorter);

		nameOfPort = new JLabel();
		nameOfPort.setText(portProxy.getName());
	}

	public static void main(String[] args) {
		GameTestUtil.setupInstanceForTest();
		try {
			Main.displayFrame(new PortWindow(Game.getInstance().getPortByName("Durban").getProxy()));
		} catch (NoSuchPortException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}
	}
}
