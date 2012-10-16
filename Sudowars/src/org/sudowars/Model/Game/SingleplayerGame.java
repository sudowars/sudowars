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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.sudowars.Model.Sudoku.Sudoku;
import org.sudowars.Model.Sudoku.Field.Cell;
import org.sudowars.Model.Sudoku.Field.DataCell;
import org.sudowars.Model.Sudoku.Field.Field;
import org.sudowars.Model.Sudoku.RuleManagement.DependencyGroup;
import org.sudowars.Model.Sudoku.RuleManagement.DependencyManager;

/**
 * This class represents a single player game.
 */
public class SingleplayerGame extends Game {
		
	private static final long serialVersionUID = -56340307548778369L;
	private static final int PLAYER_COUNT = 1;
	
	private final List<GameCell> cellsContainingInvalidValues; 
	/**
	 * Initializes a new instance of the {@link SingleplayerGame} class with a given sudoku.
	 *
	 * @param sudoku The {@link Sudoku} to solve during the game
	 *
	 * @throws IllegalArgumentException if the given sudoku is <code>null</code>
	 */
	public SingleplayerGame(Sudoku<Cell> sudoku) throws IllegalArgumentException {
		super(sudoku);
		
		this.participatingPlayers = new ArrayList<PlayerSlot>(PLAYER_COUNT);
		this.participatingPlayers.add(createPlayerSlot());
		this.cellsContainingInvalidValues = new LinkedList<GameCell>();
	}
	
	/**
	 * Creates a new, empty {@link SingleplayerPlayerSlot}
	 *
	 * @return Reference to a {@link SingleplayerPlayerSlot} which has not yet been attached to a player.
	 */
	protected SingleplayerPlayerSlot createPlayerSlot() {
		return new SingleplayerPlayerSlot();
	}
	
	/**
	 * Gets all GameCells which contain an invalid value, i.e. a value to be contrary to the dependency groups of the cell.
	 * @return An {@link Iterable} containing cells with invalid values.
	 */
	public Iterable<GameCell> getCellsContainingInvalidValues() {
		return this.cellsContainingInvalidValues;
	}
	
	/**
	 * Sets the value of the given cell and attaches it to the given player.
	 *
	 * @param player Reference to the <code>Player</code> who sets the value.
	 * @param cell Reference to the <code>GameCell</code> whose value is to be set.
	 * @param value The new value of the cell.
	 *
	 * @return <code>true</code> if value was successfully set, otherwise <code>false</code>.
	 * If <code>true</code> is returned, the onChange event is triggered.
	 * If this operation led to a successful solve of the game's sudoku, the onSuccessfullyFinish event is also triggered.
	 * @throws IllegalArgumentException if given player or game cell was {@code null}, <br>
	 * or the given value was <= {@link DataCell.NOT_SET},<br>
	 * or the given cell doesn't fit to the game, i.e. the cell isn't part of the underlying field
	 * @throws IllegalArgumentException if no player has yet been attached to the game,<br>
	 * or the given player is not equal to the one attached to the game
	 * @see Game#onChange() 
	 * @see DataCell#NOT_SET
	 */
	public boolean setValue(Player player, GameCell cell, int value, long timestamp) throws IllegalArgumentException {
		if (cell == null || value <= DataCell.NOT_SET) {
			throw new IllegalArgumentException("invalid argument given.");
		}

		PlayerSlot slot = getPlayerSlotOfPlayer(player);
		//the following method can throw an IllegalArgumentException
		GameCell gameCell = getGameCellByIndex(cell.getIndex(), this.sudoku.getField());
		boolean result = false;
		if (!this.isPaused() && !this.isAborted()) {
			result = gameCell.attachToPlayer(slot);
			if (result) {
				//check if value if invalid, i.e. breaks a rule of one of the cell's dependency groups
				//if so, store that cell but set the value at any case
				updateInvalidCellList(value, gameCell);
				gameCell.setValue(value, timestamp);
				onChange(gameCell);
				if (successfullySolved(this.sudoku)) {
					onSuccessfullyFinish(player);
				}
			}
		}
		return result;
	}

	private void updateInvalidCellList(int value, GameCell gameCell) {
		assert gameCell != null;
		
		DependencyManager depManager = this.sudoku.getDependencyManager();
		Field<Cell> convertedField = this.getSudoku().getField().convert();
		for (DependencyGroup group : depManager.getDependencyGroupsOfCell(gameCell)) {
			if (group.getRule().getValidValues(convertedField, group, gameCell).indexOf(value) == -1) {
				this.cellsContainingInvalidValues.add(gameCell);
				break;
			}
		}
	}
		
	/**
	 * Removes the current value of the given cell, and detaches the cell from its owning player.
	 *
	 * @param cell The <code>GameCell</code> whose value shall be removed.
	 *
	 * @return <code>true</code> in case of success (which also triggers the onChange event),<br>
	 * <code>false</code> if game is currently paused,<br>if the given cell is initial or has no cell value
	 * @throws IllegalArgumentException if given game cell was {@code null},<br>
	 *  or the given cell doesn't fit to the game, i.e. the cell isn't part of the underlying field
	 * @see Game#onChange()
	 * @see GameCell#isInitial()
	 * @see GameCell#isSet()
	 * @see DataCell#NOT_SET
	 */
	public boolean removeValue(GameCell cell) throws IllegalArgumentException {
		if (cell == null) {
			throw new IllegalArgumentException("given game cell cannot be null.");
		}
		
		boolean result = false;
		if (!this.isPaused() && !this.isAborted()) {
			GameCell gameCell = getGameCellByIndex(cell.getIndex(), this.sudoku.getField());
			
			if (gameCell.isSet() && !gameCell.isInitial()) {
				int oldValue = gameCell.getValue();
				gameCell.removeValue();
				gameCell.detachFromPlayer();
				fixUpInvalidCellList(gameCell, oldValue);
				
				result = true;
				onChange(gameCell);
			}
		}
		return result;
	}

	private void fixUpInvalidCellList(GameCell gameCell, int value) {
		assert gameCell != null && !gameCell.isSet();
		
		//cell value was removed, so cell is not invalid anymore
		this.cellsContainingInvalidValues.remove(gameCell);
				
		if (this.cellsContainingInvalidValues.size() > 0) {
			//check other cells who have been previously invalid
			//if still invalid
			Field<Cell>	convertedField = this.sudoku.getField().convert();
			DependencyManager dependencies = this.sudoku.getDependencyManager();
			
			boolean valid;
			
			//use iterator for ensuring removing during iteration works
			for (Iterator<GameCell> it = this.cellsContainingInvalidValues.iterator(); it.hasNext(); ) {
  				GameCell c = it.next();
				valid = true;
				for (DependencyGroup grp : dependencies.getDependencyGroupsOfCell(c)) {
					if (grp.getRule().getValidValues(convertedField, grp, c).indexOf(c.getValue()) == -1) {
						valid = false;
						break;
					}
				}
				if (valid) {
					it.remove();
				}
			}
		}
	}
	
	/**
	 * Attaches a player to the current game.
	 *
	 * @param player The <code>Player</code> playing the current game.
	 *
	 * @throws IllegalArgumentException if given player is <code>null</code>
	 */
	public void setPlayer(Player player) throws IllegalArgumentException {
		if (player == null) {
			throw new IllegalArgumentException("given player cannot be null.");
		}
		this.participatingPlayers.get(0).setPlayer(player) ;
	}

	/**
	* Aborts the current game and exposes all cells which have not been solved yet.<br>
	* The call of this method triggers the onChange event followed by the onGameAborted event.
	* @param abortingPlayer reference to the {@link Player} who triggered the abandonment.
	* @param timestamp A Long value indicating the abortion time. 
	* @throws IllegalArgumentException if no player has yet been attached to the game,<br>
	* or the given player is not equal to one of the players attached 
	*/
	@Override
	public void abortGame(Player abortingPlayer, long timestamp) throws IllegalArgumentException {
		if (!this.isPaused()) {
			PlayerSlot playerSlot = getPlayerSlotOfPlayer(abortingPlayer);
			if (playerSlot != null && !this.isAborted() && !this.isPaused()) {
				//expose all cells first, but do not disperse points for that
				exposeAllCells(this.sudoku, playerSlot, timestamp);
				this.cellsContainingInvalidValues.clear();
				onChange(null);
				onGameAborted(playerSlot);
			}
		}
	}
}
