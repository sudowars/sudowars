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

import java.util.LinkedList;
import java.util.List;

import org.sudowars.Model.Game.Game;
import org.sudowars.Model.Game.SingleplayerGame;
import org.sudowars.Model.Game.GameCell;
import org.sudowars.Model.Game.Player;
import org.sudowars.Model.SudokuUtil.NoteManager;

/**
 * The Class InvertCellCommand.
 */
public class InvertCellCommand extends CellCommand {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5837404788478259217L;
	
	/** The value. */
	private int value = 0;

	/**
	 * Instantiates a new invert cell command.
	 *
	 * @param cell the cell
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	public InvertCellCommand(GameCell cell) throws IllegalArgumentException {
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
	
	GameCell cell = getCell(game);
	NoteManager currentNoteManager = game.getNoteManagerOfPlayer(executingPlayer);
	
	if (cell.isSet() && game instanceof SingleplayerGame) {
		value = cell.getValue();
		((SingleplayerGame) game).removeValue(cell);
		for (int i = 1; i <= game.getSudoku().getField().getStructure().getWidth(); i++) {
			if (i != value) {
				new AddNoteCommand(cell, i).execute(game, executingPlayer);
			}
		}
	} else {
		//Cell is not set		
		List<Integer> notes = currentNoteManager.getNotes(cell);
		if (notes != null) {

			//swap notes
			for (int n = 1; n <= game.getSudoku().getField().getStructure().getWidth(); n++) {
				if (notes.contains(n)) {
					currentNoteManager.removeNote(cell, n);
				} else {
					currentNoteManager.addNote(cell, n);
				}
			}
			
		}
		
	}
	
		//there is no way to check if everything worked
		return true;
	}
	
	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public int getValue() {
		return value;
	}

	@Override
	public GameCommand getInvertedCommand(Game game) {
		GameCommand invertedCommand = null;
		GameCell cell = getCell(game);
		if (getValue() != 0) {
			LinkedList<GameCommand> commands = new LinkedList<GameCommand>();
			commands.add(new ClearCellCommand(cell));
			commands.add(new SetCellValueCommand(cell, getValue()));
			invertedCommand = new CompositeCommand(commands);
		} else {
			invertedCommand = new InvertCellCommand(cell);
		}
		return invertedCommand;
	}

}
