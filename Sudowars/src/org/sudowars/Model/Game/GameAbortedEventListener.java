package org.sudowars.Model.Game;

import java.util.EventListener;


/**
 * This interface must be implemented to be able to listen to the onGameAbortedEvent of {@link Game} instances.
 *
 */
public interface GameAbortedEventListener extends EventListener {
	/**
	 * This event is triggered if the observed game is aborted.
	 * @param eventData The event data.
	 */
	public void onGameAborted(GameAbortedEvent eventData);
}
