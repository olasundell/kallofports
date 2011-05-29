package kop.ui;

import kop.Main;
import kop.game.Game;
import kop.ships.blueprint.ShipBlueprint;
import kop.ships.ShipClass;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
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
public class NewShipWindow implements Window {
	private JPanel contentPane;
	private JTable newShipTable;
	private JButton purchase;
	private JComboBox shipTypeSelector;
	private JButton cancelButton;
	private List<ShipClass> shipClasses;
	private NewShipTableModel tableModel;
	private TableRowSorter<NewShipTableModel> sorter;

	public NewShipWindow() {
		if (shipClasses == null) {
			shipClasses = ShipClass.getShipClasses();
		}

		purchase.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Game.getInstance().getPlayerCompany().purchaseShip(shipClasses.get(newShipTable.getSelectedRow()));
			}
		});
		shipTypeSelector.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox source = (JComboBox) e.getSource();
				Object o = source.getSelectedItem();
				if (o instanceof ShipBlueprint.ShipType) {
					sorter.setRowFilter(new ShipTypeRowFilter((ShipBlueprint.ShipType)o));
				} else {
					sorter.setRowFilter(null);
				}
			}
		});
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Main.displayFrame(new MainWindow());
			}
		});
	}

	public JPanel getContentPane() {
		return contentPane;
	}

	@Override
	public String getTitle() {
		return "New ship";  //To change body of implemented methods use File | Settings | File Templates.
	}

	private void createUIComponents() {
		if (shipClasses == null) {
			shipClasses = ShipClass.getShipClasses();
		}

		tableModel = new NewShipTableModel(shipClasses);
		sorter = new TableRowSorter<NewShipTableModel>(tableModel);
		newShipTable = new JTable(tableModel);
		newShipTable.setRowSorter(sorter);

		shipTypeSelector = new JComboBox();
		shipTypeSelector.addItem("");
		for (ShipBlueprint.ShipType type: ShipBlueprint.ShipType.values()) {
			shipTypeSelector.addItem(type);
		}
	}

	private class NewShipTableModel implements TableModel {
		private List<ShipClass> shipClasses;

		String columnNames[] = {
			"Class type", "Class name", "Price"
		};
		private Object filter = null;

		NewShipTableModel(List<ShipClass> shipClasses) {
			this.shipClasses = shipClasses;
		}
		@Override
		public int getRowCount() {
			return shipClasses.size();
		}

		@Override
		public int getColumnCount() {
			return columnNames.length;
		}

		@Override
		public String getColumnName(int columnIndex) {
			return columnNames[columnIndex];
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			switch (columnIndex) {
				case 0:
				case 1:
					return String.class;
				case 2:
					return Double.class;
			}

			return null;
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;  //To change body of implemented methods use File | Settings | File Templates.
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			switch (columnIndex) {
				case 0:
					return shipClasses.get(rowIndex).getClassType();
				case 1:
					return shipClasses.get(rowIndex).getClassName();
				case 2:
					return shipClasses.get(rowIndex).getPrice();
			}

			throw new NotImplementedException();
		}

		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			//To change body of implemented methods use File | Settings | File Templates.
		}

		@Override
		public void addTableModelListener(TableModelListener l) {
			//To change body of implemented methods use File | Settings | File Templates.
		}

		@Override
		public void removeTableModelListener(TableModelListener l) {
			//To change body of implemented methods use File | Settings | File Templates.
		}
	}

	private class ShipTypeRowFilter extends RowFilter<NewShipTableModel, Integer> {
		ShipBlueprint.ShipType type;
		public ShipTypeRowFilter(ShipBlueprint.ShipType type) {
			this.type = type;
		}
		@Override
		public boolean include(Entry<? extends NewShipTableModel, ? extends Integer> entry) {
			return entry.getValue(0).equals(type.toString());
		}
	}
}
