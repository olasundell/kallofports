package kop.ui;

import kop.Main;
import kop.company.Company;
import kop.game.Game;
import kop.game.GameTestUtil;
import kop.ships.model.ShipModel;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/5/11
 * Time: 10:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class CompanyShipsWindow implements KopWindow {
	private JTable shipStatusTable;
	private JPanel contentPane;
	private JButton sellButton;
	private JButton repayMortgageButton;
	private JButton okButton;
	private JButton raiseLoanButton;
	TableModel model;

	public CompanyShipsWindow() {
		sellButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//To change body of implemented methods use File | Settings | File Templates.
			}
		});
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//To change body of implemented methods use File | Settings | File Templates.
			}
		});
		repayMortgageButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//To change body of implemented methods use File | Settings | File Templates.
			}
		});
		raiseLoanButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//To change body of implemented methods use File | Settings | File Templates.
			}
		});
	}

	private void createUIComponents() {
		model = new MyTableModel(Game.getInstance().getPlayerCompany());
		shipStatusTable = new JTable(model);
	}

	private static class MyTableModel implements TableModel {
		Company company;
		String columns[] = {
				"Name",
				"Status",
				"Position",
				"Departured",
				"Destination",
				"ETA",
				"ETD", "Speed", "Cargo", "Current bunker", "Max bunker", "Condition"
		};

		MyTableModel(Company company) {
			this.company = company;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			ShipModel ship = company.getShip(rowIndex);
			switch (columnIndex) {
				case 0:
					return ship.getName();
				case 1:
					return ship.getStatus();
				case 2:
					return ship.getPosition();
				case 3:
					return ship.isAtSea();
				case 4:
					return ship.getDestination();
				case 5:
					return ship.getETA();
				case 6:
					return ship.getETD();
				case 7:
					return ship.getSpeed();
				case 8:
					return ship.getFreights();
				case 9:
					return ship.getCurrentFuel();
				case 10:
					return ship.getMaxFuel();
				case 11:
					return ship.getCondition();
				default:
					return "TODO";
			}
		}

		@Override
		public int getRowCount() {
			return company.getNumberOfShips();
		}

		@Override
		public int getColumnCount() {
			return columns.length;
		}

		@Override
		public String getColumnName(int columnIndex) {
			return columns[columnIndex];
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			if (company.getShips().size() > 0) {
				return getValueAt(0,columnIndex).getClass();
			}

			return null;
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;  //To change body of implemented methods use File | Settings | File Templates.
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

	@Override
	public JPanel getContentPane() {
		return contentPane;
	}

	@Override
	public String getTitle() {
		return "Ship list";  //To change body of implemented methods use File | Settings | File Templates.
	}

	public static void main(String[] args) {
		GameTestUtil.setupInstanceForTest();
		Main.displayFrame(new CompanyShipsWindow());
	}
}
