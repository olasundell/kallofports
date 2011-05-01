package kop.ui;

import kop.company.Company;
import kop.game.Game;
import kop.ships.ShipModel;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/5/11
 * Time: 10:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class CompanyShipsWindow implements Window {
	private JTable shipStatusTable;
	private JPanel contentPane;
	TableModel model;

	private void createUIComponents() {
		model = new MyTableModel(Game.getInstance().getPlayerCompany());
		shipStatusTable = new JTable(model);
	}

	private static class MyTableModel implements TableModel {
		Company company;
		String columns[] = {
				"Name", "Status", "Position",
				"Departured", "Destination",
				"ETA", "ETD", "Speed", "Cargo", "Current bunker", "Max bunker", "Condition"
		};

		MyTableModel(Company company) {
			this.company = company;
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
			return null;  //To change body of implemented methods use File | Settings | File Templates.
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;  //To change body of implemented methods use File | Settings | File Templates.
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			ShipModel ship = company.getShip(rowIndex);
			switch (columnIndex) {
				case 0:
					return ship.getName();
				default:
					return "TODO";
			}
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

	public JPanel getContentPane() {
		return contentPane;
	}

	@Override
	public String getTitle() {
		return "Ship list";  //To change body of implemented methods use File | Settings | File Templates.
	}
}
