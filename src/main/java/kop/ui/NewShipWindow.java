package kop.ui;

import kop.Main;
import kop.game.Game;
import kop.ships.ShipClassList;
import kop.ships.ShipnameAlreadyExistsException;
import kop.ships.blueprint.ShipBlueprint;
import kop.ships.ShipClass;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
	private JSlider loanSlider;
	private JLabel totalPrice;
	private JLabel maxLoanLabel;
	private JLabel currentCash;
	private JLabel priceUpFront;
	private JLabel cashAfterPurchase;
	private JTextField shipName;
	private ShipClassList shipClasses;
	private NewShipTableModel tableModel;
	private TableRowSorter<NewShipTableModel> sorter;

	public NewShipWindow() {
		if (shipClasses == null) {
			shipClasses = ShipClass.getShipClasses();
		}

		purchase.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO check validation here, does the company have enough money to purchase etc?
				if (shipName.getText().length() == 0) {
					// TODO display error message.
					return;
				}

				try {
					Game.getInstance().getPlayerCompany().purchaseShip(loanSlider.getValue(),
							shipName.getText(),
							shipClasses.get(newShipTable.getSelectedRow()));
				} catch (ShipnameAlreadyExistsException e1) {
					// TODO display error message
					return;
				}

				Main.displayFrame(new MainWindow());
			}
		});
		shipTypeSelector.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox source = (JComboBox) e.getSource();
				Object o = source.getSelectedItem();
				if (o instanceof ShipBlueprint.ShipType) {
					sorter.setRowFilter(new ShipTypeRowFilter((ShipBlueprint.ShipType) o));
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

		loanSlider.setMajorTickSpacing(10);
		loanSlider.setMinorTickSpacing(1);
		loanSlider.setPaintTicks(true);
		loanSlider.setPaintLabels(true);

		loanSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				calcPrice(newShipTable.getSelectedRow());
			}
		});
		newShipTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				ListSelectionModel model = (ListSelectionModel) e.getSource();

				calcPrice(model.getLeadSelectionIndex());
			}
		});
		calcPrice(-1);
	}

	private void calcPrice(int selected) {
		double money = Game.getInstance().getPlayerCompany().getMoney();

		currentCash.setText(String.format("%.2f", money));

		loanSlider.setEnabled(selected > -1);

		if (selected == -1) {
			maxLoanLabel.setText("");
			totalPrice.setText("");
			priceUpFront.setText("");
			cashAfterPurchase.setText(String.format("%.2f", money));

			return;
		}

		int maxLoan=60;
		maxLoanLabel.setText(String.format("%d%%",maxLoan));

		ShipClass c = shipClasses.get(selected);
		totalPrice.setText(String.format("%.2f", c.getPrice()));

//		loanSlider.setExtent(c.getMaxLoanPercent());
		loanSlider.setMinimum(0);
		loanSlider.setExtent(100 - c.getMaxLoanPercent());
		loanSlider.setMaximum(100);

		double d = c.getPrice() * (100-loanSlider.getValue()) / 100;
		priceUpFront.setText(String.format("%.2f", d));
		cashAfterPurchase.setText(String.format("%.2f", money - d));
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
		private ShipClassList shipClasses;

		String columnNames[] = {
				"Class type",
				"Class name",
				"Price",
				"Daily cost",
				"Deadweight",
				"Max speed",
				"Max fuel",
				"Fuel/day at 80%"
		};
		private Object filter = null;

		NewShipTableModel(ShipClassList shipClasses) {
			this.shipClasses = shipClasses;
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			if (shipClasses.size() > 0) {
				return getValueAt(0,columnIndex).getClass();
			}

			return null;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			ShipClass shipClass = shipClasses.get(rowIndex);
			switch (columnIndex) {
				case 0:
					return shipClass.getClassType();
				case 1:
					return shipClass.getClassName();
				case 2:
					return shipClass.getPrice();
				case 3:
					return shipClass.getBlueprint().getDailyCost();
				case 4:
					return shipClass.getBlueprint().getDwt();
				case 5:
					return shipClass.getBlueprint().getMaxSpeed();
				case 6:
					return shipClass.getBlueprint().getMaxFuel();
				case 7:
					return 24*shipClass.getBlueprint().getFuelUsageFractionOfMaxPower(0.8);
			}

			throw new NotImplementedException();
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;  //To change body of implemented methods use File | Settings | File Templates.
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
			return entry.getValue(0).equals(type);
		}
	}

	public static void main(String[] args) {
		Game.getInstance().getPlayerCompany().setMoney(1000000000);
		Main.displayFrame(new NewShipWindow());
	}
}
