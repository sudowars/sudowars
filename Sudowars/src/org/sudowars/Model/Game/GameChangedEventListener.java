package org.sudowars.Model.Game;

import java.util.EventListener;

/**
 * This interface must be implemented to be able to listen to the onGameChanged of {@link Game} instances.
 *
 */
public interface GameChangedEventListener extends EventListener {
	
	/**
	 * This event is triggered if the observed game changed.
	 * @param eventData The event data.
	 */
	public void onGameChanged(GameChangedEvent event); 
}
