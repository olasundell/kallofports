package kop.ui;

import kop.game.Game;
import org.testng.annotations.Test;
import org.uispec4j.Button;
import org.uispec4j.Panel;
import org.uispec4j.TextBox;

import static org.testng.Assert.assertNotNull;

/**
 * @author ola
 */
public class NewGameWindowTest extends KopUITest {
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

		companyName.setText(Game.getInstance().getPlayerCompany().getName());
	}
}
