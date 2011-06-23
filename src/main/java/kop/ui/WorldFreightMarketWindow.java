package kop.ui;

import kop.Main;
import kop.cargo.Freight;
import kop.cargo.FreightMarket;
import kop.game.Game;
import kop.game.GameTestUtil;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/6/11
 * Time: 7:42 AM
 * To change this template use File | Settings | File Templates.
 */
public class WorldFreightMarketWindow implements Window {

	private JTable freightTable;
	private JButton closeButton;
	private JPanel contentPane;

	public WorldFreightMarketWindow() {
		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Main.displayFrame(new MainWindow());
			}
		});
	}

	@Override
	public JPanel getContentPane() {
		return contentPane;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public String getTitle() {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	private void createUIComponents() {
		FreightTableModel freightTableModel = new FreightTableModel();
		freightTable = new JTable(freightTableModel);
	}

	private class FreightTableModel extends AbstractTableModel {
		String[] columnNames = {
				"Origin",
				"Destination",
				"Type of cargo",
				"Units",
				"Price per unit",
				"Total price",
				"Days left"
		};
		@Override
		public int getRowCount() {
			return Game.getInstance().getFreightMarket().getFreights().size();
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
			return getValueAt(0,columnIndex).getClass();
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Freight freight = Game.getInstance().getFreightMarket().getFreights().get(rowIndex);
			switch (columnIndex) {
				case 0:
					return freight.getOrigin().getName();
				case 1:
					return freight.getDestination().getName();
				case 2:
					return freight.getCargo().getCargoType().getName();
				case 3:
					return freight.getCargo().getWeight();
				case 4:
					return freight.getCargo().getPricePerUnit();
				case 5:
					return freight.getCargo().getTotalPrice();
				case 6:
					return freight.getCargo().getDaysLeft(Game.getInstance().getCurrentDate());
			}

			return null;
		}

		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		}
	}

	public static void main(String[] args) {
		GameTestUtil.setupInstanceForTest();
		Main.displayFrame(new WorldFreightMarketWindow());
	}
}
