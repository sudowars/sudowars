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

import java.io.Serializable;

/**
 * The interface {@link FieldStructure} defines how a 2 dimensional {@link Field} is structured. 
 */
public interface FieldStructure extends Serializable{
	
	/**
	 * Returns the width of the structure represented by {@link FieldStructure}.
	 *
	 * @return an integer value greater than 0.
	 */
	public int getWidth();
	
	/** 
	 * Returns the height of the structure represented by {@link FieldStructure}.
	 *
	 * @return an integer value greater than 0.
	 */
	public int getHeight();
	
	/**
	 * Determines whether the slot at row y and column x is used in this {@link FieldStructure}.
	 * 
	 * @param x horizontal position
	 * @param y vertical position
	 *
	 * @return <code>true</code> if the slot is used, <code>true</code> otherwise.
	 *
	 * @throws IllegalArgumentException if the position defined by x and y is not covered by this {@link FieldStructure}
	 */
	public boolean isSlotUsed(int x, int y) throws IllegalArgumentException;
	
	/**
	 * Translates the position given by row y and column x to an index.
	 * The returned index has the following properties:
	 * <ul>
	 *	<li>No 2 slots inside a {@link FieldStructure} have equals indices</li>
	 *	<li>Only used slots have indices</li>
	 *	<li>The value is bounded by 0 and {@link FieldStructure#getUsedSlotCount(int, int)}</li>
	 * </ul>
	 *
	 * @return a positive integer
	 *
	 * @throws IllegalArgumentException if the position defined by x and y is not covered by this {@link FieldStructure}
	 * @throws IllegalArgumentException if the position defined by x and y points to a unused slot inside this {@link FieldStructure}
	 */
	public int getIndex(int x, int y) throws IllegalArgumentException;
	
	/**
	 * Returns the amount of slots used in this {@link FieldStructure}.
	 *
	 * @return a positive integer
	 *
	 * @throws IllegalArgumentException if the position defined by x and y ist not covered by this {@link FieldStructure}
	 */
	public int getUsedSlotCount() throws IllegalArgumentException;
	
}
