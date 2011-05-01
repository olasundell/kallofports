package kop.ui;

import kop.Main;
import kop.company.Company;
import kop.game.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/4/11
 * Time: 8:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class CompanyInfoWindow implements Window {
	private JLabel companyName;
	private JLabel currentFunds;
	private JLabel numberOfShips;
	private JPanel contentPane;
	private JButton backButton;

	public CompanyInfoWindow() {
		backButton.addActionListener(new ActionListener() {
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
		return "Company information";  //To change body of implemented methods use File | Settings | File Templates.
	}
}
