package kop.ui;

import kop.Main;
import kop.game.GameTestUtil;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
		TableRowSorter<FreightTableModel> sorter = new TableRowSorter<FreightTableModel>(freightTableModel);
		freightTable.setRowSorter(sorter);
		sorter.setRowFilter(null);
	}

	public static void main(String[] args) {
		GameTestUtil.setupInstanceForTest();
		Main.displayFrame(new WorldFreightMarketWindow());
	}
}
