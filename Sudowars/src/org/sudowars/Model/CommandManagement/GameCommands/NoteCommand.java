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
package org.sudowars.Model.CommandManagement.GameCommands;

import org.sudowars.Model.Game.GameCell;
import org.sudowars.Model.Game.Player;
import org.sudowars.Model.Game.Game;

/**
 * This class is the base of all classes executing commands with notes during a game.
 */
public abstract class NoteCommand extends CellCommand {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6995125752200561052L;
	/**
	 * The value of the note.
	 */
	protected int noteValue;

	/**
	 * Initializes a new instance of the NoteCommand class.
	 *
	 * @param cell The cell on which the command is executed.
   	 * @param noteValue The value of the note used during the command.
         *
	 * @throws IllegalArgumentException if given cell is <code>null</code> or noteValue < <code>1</code>
	 */
	protected NoteCommand(GameCell cell, int noteValue) throws IllegalArgumentException {
		super(cell);
		if (noteValue < 1 || noteValue > 16) {
			throw new IllegalArgumentException("NoteValue out of range");
		}
		this.noteValue = noteValue;
	}

	/**
	 * Gets the note value.
	 *
	 * @return the note value
	 */
	public int getNoteValue() {
		return noteValue;
	}

	/**
	 * Executes the current command on the specified game in the name of the given player.
	 *
	 * @param game A reference to game on which to execute the current instance.
	 * @param executingPlayer The player in whose name to execute the command
	 *
	 * @return <code>true</code>, if the command was executed successfully, <code>false</code> otherwise
	 *
	 * @throws IllegalArgumentException if at least one of the given params is <code>null</code>
	 */
	public abstract boolean execute(Game game, Player executingPlayer) throws IllegalArgumentException;	

}
