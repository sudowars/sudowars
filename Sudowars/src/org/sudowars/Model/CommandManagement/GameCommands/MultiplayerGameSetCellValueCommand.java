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

import org.sudowars.Model.Game.Game;
import org.sudowars.Model.Game.GameCell;
import org.sudowars.Model.Game.MultiplayerGame;
import org.sudowars.Model.Game.Player;

public class MultiplayerGameSetCellValueCommand extends CellCommand {

	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3467974332713719607L;
	private int cellValue;
	private boolean prepare;
	private long timestamp;
	private Player creatingPlayer;
	private boolean executed;
	
	public MultiplayerGameSetCellValueCommand(GameCell cell, int cellValue, long timestamp, Player localPlayer)throws IllegalArgumentException {
		super(cell);
		assert (cellValue > 0 && cellValue < 17);		
		if (cellValue > 0 && cellValue < 17) {
			this.cellValue = cellValue;
		} else {
			throw new IllegalArgumentException("cellValue is out of range");
		}
		this.prepare = true;
		this.timestamp = timestamp;
		this.creatingPlayer = localPlayer;
		this.executed = false;
	}

	@Override
	public GameCommand getInvertedCommand(Game game) {
		throw new IllegalStateException();
	}
		
	@Override
	public boolean execute(Game game, Player executingPlayer) throws IllegalArgumentException {
		if (game == null || executingPlayer == null || !(game instanceof MultiplayerGame)) {
			throw new IllegalArgumentException();
		}
		boolean result = false;
		if (this.prepare) {
			// local preparation
			// make cell pending and set value while owner stays null
			
			GameCell cell = game.getSudoku().getField().getCell(this.cellIndex);
			
			if (cell.isOwnerPending()) {
				// if cell already pending drop request
				// as an earlier command has already started execution
				return false;
			}
			
			result = game.setValue(this.creatingPlayer, cell, this.cellValue, this.timestamp);
			this.prepare = false;
		} else {
			// local or remote execution
			if (!this.creatingPlayer.equals(executingPlayer)) {
				// local execution
				
				GameCell cell = game.getSudoku().getField().getCell(this.cellIndex);
				
				if (!cell.isSet()) {
					this.executed = true;
					return false;
				}
				
				if (this.timestamp == cell.getTimestamp()) {
					// this command made the cell pending
					if (((MultiplayerGame) game).attachCellToPlayer(this.creatingPlayer, cell, this.cellValue)) {
						result = true;					
					} else {
						// game already aborted: drop
					}
				} else {
					// otherwise drop command
				}
				this.executed = true;
			} else {
				// remote execution
				GameCell cell = game.getSudoku().getField().getCell(this.cellIndex);
				if(game.setValue(this.creatingPlayer, cell, this.cellValue, this.timestamp)) {
					result = ((MultiplayerGame)game).attachCellToPlayer(this.creatingPlayer, cell, this.cellValue);
				} else {
					this.executed = true;
				}
			}
			
			if (result) {
				// execution successful
				GameCell cell = game.getSudoku().getField().getCell(this.cellIndex);
				if (cell.isOwnerPending()) {
					throw new IllegalStateException();
				}
				if (cell.getOwningPlayer() == null) {
					throw new IllegalStateException();
				}
			}
			
		}
		
		return result;
	}

	public boolean wasExecuted() {
		return this.executed;
	}
	
	public boolean isCreatingPlayer(Player player) {
		return this.creatingPlayer.equals(player);
	}
}
