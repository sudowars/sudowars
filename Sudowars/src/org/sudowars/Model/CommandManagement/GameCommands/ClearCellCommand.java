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

import java.util.LinkedList;
import java.util.List;

import org.sudowars.Model.Game.Game;
import org.sudowars.Model.Game.GameCell;
import org.sudowars.Model.Game.Player;
import org.sudowars.Model.Game.SingleplayerGame;
import org.sudowars.Model.SudokuUtil.NoteManager;

// TODO: Auto-generated Javadoc
/**
 * The Class ClearCellCommand. It is used to clear a cell;)
 */
public class ClearCellCommand extends CellCommand {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5837404788478259217L;
	
	
	/** The notes. */
	List<Integer> notes = new LinkedList<Integer>();
	
	/** The value. */
	private int value = 0;
	

	/**
	 * Instantiates a new clear cell command.
	 *
	 * @param cell the cell which is cleared
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	public ClearCellCommand(GameCell cell) throws IllegalArgumentException {
		super(cell);
	}

	/* (non-Javadoc)
	 * @see org.sudowars.Model.CommandManagement.CellCommand#execute(org.sudowars.Model.Game.Game, 
	 * org.sudowars.Model.Game.Player)
	 */
	@Override
	public boolean execute(Game game, Player executingPlayer) throws IllegalArgumentException {
	
		if (game == null) {
			throw new IllegalArgumentException("game is null");
		}
		if (executingPlayer == null) {
			throw new IllegalArgumentException("executingPlayer is null");
		}
		
		GameCell cell = game.getSudoku().getField().getCell(cellIndex);
		
		//save current notes and value
		NoteManager noteManager = game.getNoteManagerOfPlayer(executingPlayer);
		if (noteManager.getNotes(cell) != null) {
			for (int i : noteManager.getNotes(cell)) {
				this.notes.add(i);
			}
		}
		if (cell.isSet()) {
			this.value = cell.getValue();
		}
		// Remove all Notes
		boolean result = true;
		if (!game.getNoteManagerOfPlayer(executingPlayer).getNotes(cell).isEmpty()) {
			result = noteManager.removeAllNotes(cell);
		}
		// Remove cell value if game is sp game
		if (game instanceof SingleplayerGame) {
			if (cell.isSet()) {
				result = ((SingleplayerGame) game).removeValue(cell) && result;
			}
		}
		return result;
	}

	/**
	 * Gets the notes.
	 *
	 * @return the notes which were removed by this command on execution
	 */
	public List<Integer> getNotes() {
		return notes;
	}

	/**
	 * Gets the value.
	 *
	 * @return the value which was removed from the cell on execution
	 */
	public int getValue() {
		return value;
	}

	@Override
	public GameCommand getInvertedCommand(Game game) {
		GameCell cell = getCell(game);
		CompositeCommand invertedCommand = new CompositeCommand();
		for (Integer note : getNotes()) {
			invertedCommand.pushCommand(new AddNoteCommand(cell, note));
		}
		if (getValue() != 0) invertedCommand.pushCommand(new SetCellValueCommand(cell, getValue()));
		return invertedCommand;
	}
	
	

}
