package kop.ui;

import kop.Main;
import kop.company.Company;
import kop.game.Game;
import kop.game.GameStateListener;
import kop.game.GameTestUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/4/11
 * Time: 8:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class CompanyInfoWindow implements KopWindow, GameStateListener {
	public static final String MONEY_TEXT_FORMAT = "%,.2f";
	public static final String COMPANY_NAME = "companyName";
	public static final String CURRENT_FUNDS = "currentFunds";
	public static final String NUMBER_OF_SHIPS = "numberOfShips";
	public static final String GROSS_WORTH = "grossWorth";
	public static final String TOTAL_LOANS = "totalLoans";
	public static final String NET_WORTH = "netWorth";
	private JLabel companyName;
	private JLabel currentFunds;
	private JLabel numberOfShips;
	private JPanel contentPane;
	private JButton backButton;
	private JButton administerLoansButton;
	private JButton displayShipsButton;
	private JLabel grossWorth;
	private JLabel totalLoans;
	private JLabel netWorth;

	public CompanyInfoWindow() {
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Main.displayFrame(new MainWindow());
			}
		});
		administerLoansButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//To change body of implemented methods use File | Settings | File Templates.
			}
		});
		displayShipsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//To change body of implemented methods use File | Settings | File Templates.
			}
		});
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//To change body of implemented methods use File | Settings | File Templates.
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

	private void createUIComponents() {
		companyName = new JLabel("");
		companyName.setName(COMPANY_NAME);
		currentFunds = new JLabel("");
		currentFunds.setName(CURRENT_FUNDS);
		numberOfShips = new JLabel("");
		numberOfShips.setName(NUMBER_OF_SHIPS);
		grossWorth = new JLabel("");
		grossWorth.setName(GROSS_WORTH);
		totalLoans = new JLabel("");
		totalLoans.setName(TOTAL_LOANS);
		netWorth = new JLabel("");
		netWorth.setName(NET_WORTH);
		updateLabels();
	}

	private void updateLabels() {
		Company playerCompany = Game.getInstance().getPlayerCompany();
		companyName.setText(playerCompany.getName());
		currentFunds.setText(String.format(MONEY_TEXT_FORMAT, playerCompany.getMoney()));
		numberOfShips.setText(String.format("%d", playerCompany.getNumberOfShips()));
		grossWorth.setText(String.format(MONEY_TEXT_FORMAT, playerCompany.getGrossWorth()));
		totalLoans.setText(String.format(MONEY_TEXT_FORMAT, playerCompany.getTotalLoans()));
		netWorth.setText(String.format(MONEY_TEXT_FORMAT, playerCompany.getNetWorth()));
	}

	@Override
	public void stateChanged() {
		updateLabels();
	}

	public static void main(String[] args) {
		GameTestUtil.setupInstanceForTest();
		CompanyInfoWindow window = new CompanyInfoWindow();
		Main.displayFrame(window);
		window.stateChanged();
	}
}
