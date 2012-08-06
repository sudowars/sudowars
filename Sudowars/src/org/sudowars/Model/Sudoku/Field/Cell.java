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
 * {@link Cell} defines the minimal functionality the smallest data structure inside a {@link Sudoku} has to be able to.
 */
public interface Cell extends Serializable, Cloneable {
	
	/**
	 * Returns the {@link Cell}s value.
	 *
	 * @returns the {@link Cell}s value.
	 */
	public int getValue();

	/**
	 * Returns the {@link Cell}s index.
	 *
	 * @returns the {@link Cell}s index
	 * @see FieldStructure#getIndex(int, int)
	 */
	public int getIndex();
	
	/**
	 * Determines whether this {@link Cell} was set since creation.
	 *
	 * @returns <code>true</code>, if the execution was successfully
	 */
	public boolean isInitial();

	/**
	 * Determines whether the {@link Cell} contains a legal value or not.
	 *
	 * @return <code>true</code>, if the execution was successfully
	 */
	public boolean isSet();
	
	/**
	 * Returns a copy of the calling {@link Cell}
	 * 
	 * @return a copy of the calling {@link Cell}
	 */
	public Object clone();
	
}
