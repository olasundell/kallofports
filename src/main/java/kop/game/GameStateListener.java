package kop.game;

import java.util.EventListener;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/30/11
 * Time: 7:20 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GameStateListener extends EventListener {
	public void stateChanged();
}
