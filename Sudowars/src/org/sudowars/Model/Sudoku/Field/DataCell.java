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
package org.sudowars.Model.Sudoku.Field;

/**
 * The class {@link DataCell} represents the smallest structure inside a {@link Sudoku}.
 */
public final class DataCell implements Cell {
	
	/**
	 * Serial version UID for serialization.
	 */
	private static final long serialVersionUID = 7952639650027214265L;

	/**
	 * Define, if this cell is a given number in the sudoku game.
	 * Is <code>0</code>, if the cell is empty and <code>1</code>, if the number is given.
	 */
	public static final int NOT_SET = 0;
	
	private int value;
	private int index;
	private boolean initial;
	
	/**
	 * Default constructor to initialize a new instance of type {@link DataCell} with a not-set value.
	 *
	 * @param index the {@link DataCell}s future index.
	 * @throws IllegalArgumentException when index is less than 0
	 */
	public DataCell(int index, boolean initial) {
		if (index < 0) {
			throw new IllegalArgumentException();
		}
		
		this.value = NOT_SET;
		this.index = index;
		this.initial = initial;
	}

	/**
	 * Returns the {@link DataCell}s value
	 *
	 * @return an not-negative integer
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * Returns the {@link DataCell}s index.
	 *
	 * @return the {@link DataCell}s index.
	 * @see FieldStructure#getIndex(int, int)
	 */
	public int getIndex() {
		return index;
	}
	
	/**
	 * Returns whether this is an inital {@link DataCell} inside
	 *
	 * @return whether this is an inital {@link DataCell} inside
	 */
	public boolean isInitial() {
		return initial;
	}
	
	/**
	 * Sets the {@link DataCell}s value.
	 *
	 * @param value the {@link DataCell}s new value.
	 *
	 * @throws IllegalArgumentException if the value is lower than <code>0</code>. 
	 */
	public void setValue(int value) {
		if (value < 0) {
			throw new IllegalArgumentException("Illegal cell value");
		}
		this.value = value;
	}
	
	/**
	 * Sets the {@link DataCell}s initial-state.
	 * 
	 * @param state the {@link DataCell}s new state.
	 */
	public void setInitial(boolean state) {
		this.initial = state;
	}

	/**
	 * Determines whether the {@link Cell} contains a value or not.
	 * @return <code>true</code> id cell contains a value, otherwise <code>false</code>
	 */
	@Override
	public boolean isSet() {
		return this.value != NOT_SET;
	}
	
	/**
	 * Returns a copy of the calling {@link DataCell}
	 * @return a deep copy of the calling {@link DataCell}
	 */
	public DataCell clone() {
		DataCell newCell = new DataCell(this.index, this.initial);
		if (this.value != DataCell.NOT_SET) {
			newCell.setValue(this.value);
		}
		return newCell;
	}

	
}
