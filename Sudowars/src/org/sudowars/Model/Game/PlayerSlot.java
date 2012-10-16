package org.sudowars.Model.Game;

import java.io.Serializable;

import org.sudowars.Model.SudokuUtil.NoteManager;


/**
 * This class defines a slot for a player during a game. 
 */
public abstract class PlayerSlot implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected boolean hasPaused;
	protected Player attachedPlayer = null;
	protected NoteManager notes = null;
	
	/**
	 * Attaches to current slot to a specific player.
	 *
	 * @param player Reference to a <code>Player</code> which will be attached to this slot.
	 *
	 * @throws IllegalArgumentException if specified player is <code>null</code>
	 */
	public void setPlayer(Player player) throws IllegalArgumentException {
		if (player == null) {
			throw new IllegalArgumentException("player to attach to slot cannot be null.");
		}
		this.attachedPlayer = player;
	}
	
	/**
	 * Gets the player currently attached to this instance.
	 *
	 * @return Reference to a {@link Player}.
	 */
	public Player getPlayer() {
		return this.attachedPlayer;
	}
	
	/**
	 * Gets the {@link NoteManager} associated with the current instance.
	 *
	 * @return Reference to a note manager instance.
	 */
	public NoteManager getNoteManager() {
		return this.notes;
	}
	
	/**
	 * Sets the paused state of this slot indicating of the player attached to it triggered a pause.
	 *
	 * @param state A boolean flag to indicate if a paused was triggered.
	 */
	void setPausedState(boolean state) {
		this.hasPaused = state;
	}
	
	/**
	 * Sets the {@link NoteManager} of the current instance.
	 *
	 * @param noteManager Reference to a note manager instance.
	 *
	 * @throws IllegalArgumentException if given reference is <code>null</code>
	 */
	void setNoteManager(NoteManager noteManager) throws IllegalArgumentException {
		if (noteManager == null) {
			throw new IllegalArgumentException("noteManager to set cannot be null.");
		}
		this.notes = noteManager;
	}
}
