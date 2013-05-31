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

import org.sudowars.Model.Sudoku.Field.Cell;
import org.sudowars.Model.Sudoku.Field.DataCell;
import org.sudowars.Model.Sudoku.Field.FieldStructure;

/**
 * This class is used to decorate a {@link Cell} during a <code>Game</code>
 */
public class GameCell implements Cell {
	
	private static final long serialVersionUID = 228998130172313091L;
	private final Cell core;
	private int currentValue = DataCell.NOT_SET;
	private PlayerSlot playerSlot = null;
	
	private static final long TIMESTAMP_UNSET = Long.MIN_VALUE;
	
	private long timestamp;
		
	/**
	 * Initializes a new instance of the {@link GameCell} class.
	 *
	 * @param core A reference to a {@link Cell} which is decorated by this instance.
	 *
	 * @throws IllegalArgumentException if the given cell is <code>null</code>
	 */
	public GameCell(Cell core) throws IllegalArgumentException {
		if (core == null) {
			throw new IllegalArgumentException("given cell cannot be null.");
		}
		this.core = core;
		this.timestamp = TIMESTAMP_UNSET;
	}
	
	/**
	 * Determines whether the given timestamp is legal
	 * @param timestamp the given timestamp
	 * @return {@code true} if timestamp is legal, {@code false} otherwise
	 */
	public static final boolean isTimestampLegal(long timestamp) {
		return timestamp > TIMESTAMP_UNSET;
	}
	
	/**
	 * Determines whether the cell owner is still pending
	 * @return {@code true} if the cells value has been set but its owner is ambiguous, {@code false} otherwise
	 */
	public boolean isOwnerPending() {
		return getOwningPlayer() == null && this.timestamp != TIMESTAMP_UNSET;
	}
	
	/**
	 * Returns the point in time at which the cells value has been set
	 * @return the point in time at which the cells value has been set
	 */
	public long getTimestamp() {
		return this.timestamp;
	}

	/**
	 * Gets the player who is attached to this instance, i.e. that player who set the cell's value
	 *
	 * @return A reference to a {@link Player}, or <code>null</code> if the cell was not yet attached to a player.
	 *
	 * @see GameCell#isSet()
	 */
	public Player getOwningPlayer() {
		if (this.playerSlot == null) {
			return null;
		}
		return this.playerSlot.getPlayer();
	}
	
	/**
	 * Gets the current value of the cell, where the constant {@code DataCell.NotSet} indicates that a value is NOT yet set.
	 *
	 * @return An integer indicating the cell value.
	 */
	public int getValue() {
		if (this.isInitial()) {
			return this.getSolution();
		}
		return this.currentValue;
	}
	
	/**
	 * Indicates whether this cell is initial, i.e. is part of those which have to be shown
	 * to solve the sudoku.
	 *
	 * @return <code>true</code> if cell is initial, otherwise <code>false</code>
	 */
	public boolean isInitial() {
		return this.core.isInitial();
	}
	
	/**
	 * Indicates if the cell value was already set.
	 *
	 * @return <code>true</code> if the cell has a value or is initial, otherwise <code>false</code>
	 * @see GameCell#setValue(int)
	 * @see GameCell#isInitial()
	 * @see DataCell#NOT_SET
	 */
	public boolean isSet() {
		return this.isInitial() || this.currentValue != DataCell.NOT_SET;
	}
	
	/**
	 * Sets the value of the current instance
	 *
	 * @param value The new value of the cell.
	 * @throws IllegalArgumentException if given value is out of range, i.e less or equal than constant defined by DataCell.NOT_SET <br>
	 * or given timestamp was equal to or less than constant defined by {@link GameCell#TIMESTAMP_UNSET}
	 * @throws IllegalStateException if the current cell is initial
	 * @see DataCell#NOT_SET
	 * @see DataCell#isInitial()
	 */
	void setValue(int value, long timestamp) throws IllegalArgumentException, IllegalStateException {
		if (!isTimestampLegal(timestamp)) {
			throw new IllegalArgumentException("illegal timestamp given.");
		}
		if (isInitial()) {
			throw new IllegalStateException();
		}
		this.setValue(value);
		this.timestamp = timestamp;
	}
	
	private void setValue(int value) {
		if (value <= DataCell.NOT_SET) {
			throw new IllegalArgumentException("illegal cell value passed.");
		}
		this.currentValue = value;
	}
	
	/**
	 * Removes the value of the current instance.
	 * <br><br>
	 * NOTE: This method does not remove the attached player of the current cell, if any given.
	 * It is strongly recommended that {@link GameCell.detachFromPlayer()} is called after the execution of this method in order to avoid
	 * inconsistency and ambiguity within this instance
	 * @see GameCell#detachFromPlayer()
	 * @see DataCell#NOT_SET
	 */
	void removeValue() {
		if (!this.isInitial()) {
			this.currentValue = DataCell.NOT_SET;
			this.timestamp = TIMESTAMP_UNSET;
		}
	}
	
	/**
	 * Attaches the cell to a player.
	 *
	 * @param playerSlot {@link PlayerSlot} associated with the player to be attached to the cell.
	 *
	 * @return <code>true</code> if attaching succeeded, <code>false</code> in one of the following cases:<br>
	 * <ul> 
	 * <li>if cell is initial</li>
	 * <li>if the cell is already attached to another player</li></ul>
	 * @throws IllegalArgumentException if given playerSlot was {@code null}
	 * @see GameCell#isInitial()
	 * @see GameCell#getOwningPlayer()
	 * @see GameCell#detachFromPlayer()
	 */
	boolean attachToPlayer(PlayerSlot playerSlot) throws IllegalArgumentException {
		if (playerSlot == null || playerSlot.getPlayer() == null) {
			throw new IllegalArgumentException("player slot cannot be null.");
		}
		boolean result = false;
		if (!this.isInitial() && this.getOwningPlayer() == null) {
			this.playerSlot = playerSlot;
			result = true;
		}
		return result;
	}
	
	/**
	 * Detaches the current instance from the currently attached player.<br>
	 * This method should be invoked, if the cell's value is removed, i.e. changed to 
	 * {@link DataCell#NOT_SET}.
	 * @see GameCell#attachToPlayer(PlayerSlot)
	 * @see GameCell#removeValue()
	 * @see SingleplayerGame#removeValue(GameCell)
	 */
	void detachFromPlayer() {
		this.playerSlot = null;
	}
	
	/**
	 * Gets the original cell decorated by this instance.
	 *
	 * @return A reference to the underlying {@link Cell}.
	 */
	Cell getCore() {
		return this.core;
	}

	/**
	 * Gets the solution of the cell, which is the value of the decorated core cell.
	 * @return an integer value indicating the solution of the cell
	 */
	public int getSolution() {
		return getCore().getValue();
	}
	
	/**
	 * Returns the cell's index.
	 *
	 * @return A positive integer value defining the index.
	 * @see FieldStructure#getIndex(int, int)
	 */
	@Override
	public int getIndex() {
		return this.core.getIndex();
	}
		
	/**
	 * Gets the hashcode of the current instance.
	 * @return an integer value indicating the hashcode of the current instance
	 */
	@Override
	public int hashCode() {
		//Start with a non-zero constant.     
		int result = 11;  
		// Include a hash for each field.
		result = 31 * result + this.currentValue;
		result = 31 * result + (int) (this.timestamp ^ (this.timestamp >>> 32));
		result = 31 * result + this.core.hashCode();    
		result = 31 * result + (this.playerSlot == null ? 0 : this.playerSlot.hashCode());    
		return result;
	}
	
	/**
	 * Indicates whether the current instance is equal to a given object.
	 * @param otherObject reference to an object to check for equality
	 * @return {@code true} if given object refers to the same instance,<br>
	 * or refers to another GameCell instance with equal attributes/properties,<br>
	 * otherwise {@code false}
	 */
	@Override
	public boolean equals(Object otherObject) {
		boolean result = false;
		if (otherObject instanceof GameCell) {
			GameCell otherGameCell = (GameCell) otherObject;
			if (this == otherGameCell || attributesEqual(this, otherGameCell)) {
				result = true;
			}
		}
		return result;
	}

	private static boolean attributesEqual(GameCell first, GameCell second) {
		assert first != null && second != null;
		
		return (first.core.equals(second.core) && first.currentValue == second.currentValue && first.timestamp == second.timestamp
		&& SingleplayerPlayerSlot.objectsEqual(first.playerSlot, second.playerSlot));
	}
	
	/**
	 * Creates a copy of the current instance, which is a shallow copy of the instance,<br>
	 * but with a deep copied cell which is decorated by this instance.
	 * @return reference to the cloned instance
	 */
	public GameCell clone() {
		GameCell newCell = new GameCell((Cell) this.core.clone());
		if (this.currentValue != DataCell.NOT_SET) {
			if  (isTimestampLegal(this.getTimestamp())) {
					newCell.setValue(this.currentValue, this.timestamp);
			} else {
				newCell.setValue(this.currentValue);
			}
			
		}
		if (this.playerSlot != null) {
			newCell.attachToPlayer(this.playerSlot);
		}
		return newCell;		
	}
}
