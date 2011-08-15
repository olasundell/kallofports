package kop.ui;

import kop.cargo.Freight;
import kop.cargo.FreightCarrier;
import kop.cargo.FreightMarket;
import kop.game.Game;
import kop.game.GameStateListener;
import kop.ports.PortProxy;
import kop.ships.model.BulkShipModel;
import kop.ships.model.ShipModel;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

/**
* @author Ola Sundell
*/
class FreightTableModel extends AbstractTableModel implements GameStateListener {
	private Object filter = null;
	private transient FreightCarrier freightCarrier;

	private FreightTableModel() {
		this(Game.getInstance().getFreightMarket());
	}

	FreightTableModel(FreightCarrier freightCarrier) {
		Game.getInstance().addListener(this);
		this.freightCarrier = freightCarrier;
	}

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
//		return Game.getInstance().getFreightMarket().getFreights().size();
		if (freightCarrier==null) {
			return 0;
		}

		return freightCarrier.getFreights().size();
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
//		return getValueAt(0,columnIndex).getClass();
		return getValueFromFreight(columnIndex, Game.getInstance().getFreightMarket().generateDummyFreight()).getClass();
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (freightCarrier == null) {
			return null;
		}

		Freight freight = freightCarrier.getFreights().get(rowIndex);

		return getValueFromFreight(columnIndex, freight);
	}

	private Object getValueFromFreight(int columnIndex, Freight freight) {
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

	@Override
	public void stateChanged() {
		fireTableDataChanged();
	}

	public static class PortRowFilter extends RowFilter<FreightTableModel, Integer> {
		PortProxy portProxy;

		public PortRowFilter(PortProxy portProxy) {
			this.portProxy=portProxy;
		}
		@Override
		public boolean include(Entry<? extends FreightTableModel, ? extends Integer> entry) {
			// TODO this is inherently buggy, two ports might have the same name.
			return entry.getValue(0).equals(portProxy.getName());
		}
	}

	public Freight getFreightAtRow(int rownum) {
		return freightCarrier.getFreights().get(rownum);
	}
}
