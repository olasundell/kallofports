package kop.ui;

import kop.game.Game;
import kop.ships.ShipnameAlreadyExistsException;
import org.testng.annotations.Test;
import org.uispec4j.Button;
import org.uispec4j.Panel;
import org.uispec4j.TextBox;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * @author ola
 */
public class NewGameWindowTest extends KopUITest {
	public NewGameWindowTest() throws ShipnameAlreadyExistsException {
		super();
	}

	@Test
	public void startGame() {
		NewGameWindow window = new NewGameWindow();
		Panel panel = new Panel(window.getContentPane());
		TextBox playerName = panel.getTextBox("playerName");
		assertNotNull(playerName);
		TextBox companyName = panel.getTextBox("companyName");
		assertNotNull(companyName);
		Button startGame = panel.getButton("startGameButton");
		assertNotNull(startGame);

		companyName.setText(gameInstance.getPlayerCompany().getName());
		playerName.setText(gameInstance.getPlayerName());
		JButton button = startGame.getAwtComponent();

		for (ActionListener listener: button.getActionListeners()) {
			button.removeActionListener(listener);
		}

		final boolean[] gameStarted = {false};

		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gameStarted[0] = true;
			}
		});

		startGame.click();
		assertTrue(gameStarted[0]);
	}
}
