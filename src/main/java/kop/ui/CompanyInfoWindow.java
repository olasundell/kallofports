package kop.ui;

import kop.company.Company;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/4/11
 * Time: 8:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class CompanyInfoWindow {
	private JLabel companyName;
	private JLabel currentFunds;
	private JLabel numberOfShips;
	private JPanel contentPane;

	public void setData(Company data) {
		companyName.setText(data.getName());
		currentFunds.setText(String.valueOf(data.getMoney()));
		numberOfShips.setText(String.valueOf(data.getNumberOfShips()));
	}

	public void getData(Company data) {
	}

	public boolean isModified(Company data) {
		return false;
	}

	public JPanel getContentPane() {
		return contentPane;
	}
}
