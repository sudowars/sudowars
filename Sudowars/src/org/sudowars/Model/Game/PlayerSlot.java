/*******************************************************************************
 * Copyright (c) 2011 - 2012 Adrian Vielsack, Christof Urbaczek, Florian Rosenthal, Michael Hoff, Moritz Lüdecke, Philip Flohr.
 * 
 * This file is part of Sudowars.
 * 
 * Sudowars is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Sudowars is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Sudowars.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * 
 * 
 * Diese Datei ist Teil von Sudowars.
 * 
 * Sudowars ist Freie Software: Sie können es unter den Bedingungen
 * der GNU General Public License, wie von der Free Software Foundation,
 * Version 3 der Lizenz oder (nach Ihrer Option) jeder späteren
 * veröffentlichten Version, weiterverbreiten und/oder modifizieren.
 * 
 * Sudowars wird in der Hoffnung, dass es nützlich sein wird, aber
 * OHNE JEDE GEWÄHELEISTUNG, bereitgestellt; sogar ohne die implizite
 * Gewährleistung der MARKTFÄHIGKEIT oder EIGNUNG FÜR EINEN BESTIMMTEN ZWECK.
 * Siehe die GNU General Public License für weitere Details.
 * 
 * Sie sollten eine Kopie der GNU General Public License zusammen mit diesem
 * Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 * initial API and implementation:
 * Adrian Vielsack
 * Christof Urbaczek
 * Florian Rosenthal
 * Michael Hoff
 * Moritz Lüdecke
 * Philip Flohr 
 ******************************************************************************/
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
