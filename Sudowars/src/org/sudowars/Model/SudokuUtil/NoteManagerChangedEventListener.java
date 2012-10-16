package org.sudowars.Model.SudokuUtil;

import java.util.EventListener;

import org.sudowars.Model.Game.Game;

/**
 * This interface must be implemented to be able to listen to the onGame of {@link NoteManager} instances.
 *
 */
public interface NoteManagerChangedEventListener extends EventListener {
	/**
	 * This event is triggered if the observed note manager changed.
	 * @param event The event data.
	 */
	public void onChange(NoteManagerChangedEvent event);
}
