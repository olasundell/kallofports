package kop.ui;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import kop.Main;
import kop.company.Company;
import kop.game.Game;
import kop.game.GameStateListener;
import kop.game.GameTestUtil;
import kop.ships.ShipnameAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	private Logger logger;
	private boolean componentsCreated = false;

	public CompanyInfoWindow() {
		$$$setupUI$$$();
		logger = LoggerFactory.getLogger(this.getClass());

		if (!componentsCreated) {
			createUIComponents();
		}

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
		componentsCreated = true;
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

	public static void main(String[] args) throws ShipnameAlreadyExistsException {
		GameTestUtil.setupInstanceForTest();
		CompanyInfoWindow window = new CompanyInfoWindow();
		Main.displayFrame(window);
		window.stateChanged();
	}

	/**
	 * Method generated by IntelliJ IDEA GUI Designer
	 * >>> IMPORTANT!! <<<
	 * DO NOT edit this method OR call it in your code!
	 *
	 * @noinspection ALL
	 */
	private void $$$setupUI$$$() {
		createUIComponents();
		contentPane = new JPanel();
		contentPane.setLayout(new FormLayout("fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:231px:noGrow,left:19dlu:noGrow,fill:max(d;4px):noGrow", "center:d:grow,top:4dlu:noGrow,center:d:grow,top:4dlu:noGrow,center:d:grow,top:4dlu:noGrow,center:d:grow,top:4dlu:noGrow,center:d:grow,top:4dlu:noGrow,center:d:grow,top:4dlu:noGrow,center:d:grow"));
		companyName.setText("Label");
		CellConstraints cc = new CellConstraints();
		contentPane.add(companyName, cc.xy(3, 1));
		currentFunds.setHorizontalAlignment(11);
		currentFunds.setText("Label");
		contentPane.add(currentFunds, cc.xy(3, 3));
		final JLabel label1 = new JLabel();
		label1.setText("Company name");
		contentPane.add(label1, cc.xy(1, 1));
		final JLabel label2 = new JLabel();
		label2.setText("Current funds");
		contentPane.add(label2, cc.xy(1, 3));
		final JLabel label3 = new JLabel();
		label3.setText("Number of ships");
		contentPane.add(label3, cc.xy(1, 11));
		numberOfShips.setHorizontalAlignment(11);
		numberOfShips.setText("Label");
		contentPane.add(numberOfShips, cc.xy(3, 11));
		final JLabel label4 = new JLabel();
		label4.setText("Gross worth");
		contentPane.add(label4, cc.xy(1, 5));
		final JLabel label5 = new JLabel();
		label5.setText("Total loans");
		contentPane.add(label5, cc.xy(1, 7));
		final JLabel label6 = new JLabel();
		label6.setText("Net worth");
		contentPane.add(label6, cc.xy(1, 9));
		grossWorth.setHorizontalAlignment(11);
		grossWorth.setText("Label");
		contentPane.add(grossWorth, cc.xy(3, 5));
		totalLoans.setHorizontalAlignment(11);
		totalLoans.setText("Label");
		contentPane.add(totalLoans, cc.xy(3, 7));
		netWorth.setHorizontalAlignment(11);
		netWorth.setText("Label");
		contentPane.add(netWorth, cc.xy(3, 9));
		administerLoansButton = new JButton();
		administerLoansButton.setText("...");
		contentPane.add(administerLoansButton, cc.xy(5, 7));
		displayShipsButton = new JButton();
		displayShipsButton.setText("...");
		contentPane.add(displayShipsButton, cc.xy(5, 11));
		backButton = new JButton();
		backButton.setText("Back");
		contentPane.add(backButton, cc.xyw(3, 13, 3));
	}

	/**
	 * @noinspection ALL
	 */
	public JComponent $$$getRootComponent$$$() {
		return contentPane;
	}
}
