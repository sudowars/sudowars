/*
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
 */
package org.sudowars.Model.CommandManagement.GameCommands;

import org.sudowars.Model.CommandManagement.BaseCommand;
import org.sudowars.Model.Game.Game;
import org.sudowars.Model.Game.GameCell;
import org.sudowars.Model.Game.Player;

/**
 * The class provodes functionality to execute a command on a cell during a game.
 */
public abstract class CellCommand extends BaseCommand implements GameCommand {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 991268144633174614L;
	
	/** The cell index on which the command is executed. */
	protected int cellIndex;

	/**
	 * Initializes a new instance of the CellCommand class.
	 *
	 * @param cell The cellindex on which the command is executed.
	 *
	 * @throws IllegalArgumentException if given cell is <code>null</code>
	 */
	protected CellCommand(GameCell cell) throws IllegalArgumentException {
		super();
		if (cell == null) {
			throw new IllegalArgumentException("Cellis null");
		}
		if (cell.getIndex() < 0) {
			throw new IllegalArgumentException("CellIndex out of range");
		}
		this.cellIndex = cell.getIndex();
	}

	

	/**
	 * Gets the cell.
	 *
	 *@param game the game from which the cell is extracted
	 *
	 * @return the cell which i stored
	 */
	public GameCell getCell(Game game) {
		return game.getSudoku().getField().getCell(cellIndex);
	}
	
	
	/**
	 * Executes the current command on the specified game in the name of the given player.
	 *
	 * @param game A reference to game on which to execute the current instance.
	 * @param executingPlayer the player in whose name to execute the command
	 * @return <code>true</code>, if the command was executed successfully, <code>false</code> otherwise
	 *
	 * @throws IllegalArgumentException if at least one of the given params is <code>null</code>
	 */
	public abstract boolean execute(Game game, Player executingPlayer) throws IllegalArgumentException;	

}
