package kop.ui;

import kop.Main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/20/11
 * Time: 4:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class StartGameWindow implements KopWindow {
	private JButton newGame;
	private JButton loadSave;
	private JButton options;
	private JButton quit;
	private JPanel contentPane;

	public StartGameWindow() {
		newGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Main.displayFrame(new NewGameWindow());
			}
		});
		quit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Main.exit();
			}
		});
	}

	@Override
	public JPanel getContentPane() {
		return contentPane;
	}

	@Override
	public String getTitle() {
		return "Welcome to KoP!";  //To change body of implemented methods use File | Settings | File Templates.
	}
}
