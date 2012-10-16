package org.sudowars.Model.Game;

import java.util.EventListener;

/**
 * This interface must be implemented to be able to listen to the onGameSuccessfullyFinish of {@link Game} instances.
 *
 */
public interface GameFinishedEventListener extends EventListener {
	
	/**
	 * This event is triggered if the observed game is successfully finished.
	 * @param eventData The event data.
	 */
	public void onGameSuccessfullyFinish(GameFinishedEvent eventData);
}
