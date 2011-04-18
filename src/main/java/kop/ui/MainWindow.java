package kop.ui;

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
public class MainWindow {
	private JButton startButton;
	private JButton displayShips;
	private JPanel contentPane;
	private JButton newShip;

	public MainWindow() {
		displayShips.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				displayFrame("Ship listing", new CompanyShipsWindow().getContentPane());
			}
		});
		newShip.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				displayFrame("New ship", new NewShipWindow().getContentPane());
			}
		});
	}

	public JPanel getContentPane() {
		return contentPane;
	}

}
