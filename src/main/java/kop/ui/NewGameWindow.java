package kop.ui;

import kop.Main;
import kop.game.Game;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/20/11
 * Time: 5:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class NewGameWindow implements Window {
	private JButton startGameButton;
	private JTextField playerName;
	private JTextField companyName;
	private JButton cancelButton;
	private JPanel contentPane;

	public NewGameWindow() {
		startGameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Game.getInstance().setPlayerName(playerName.getText());
				Game.getInstance().setPlayerCompanyName(companyName.getText());
				Main.displayFrame(new MainWindow());
			}
		});
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Main.displayFrame((new StartGameWindow()));
			}
		});
	}

	public JPanel getContentPane() {
		return contentPane;
	}

	@Override
	public String getTitle() {
		return "New game";  //To change body of implemented methods use File | Settings | File Templates.
	}
}
