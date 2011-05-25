package kop.game;

import java.util.EventListener;

/**
 * Implement this if you'd like to be notified whenever the game state changes. Good if you're an UI, for instance.
 */
public interface GameStateListener extends EventListener {
	public void stateChanged();
}
