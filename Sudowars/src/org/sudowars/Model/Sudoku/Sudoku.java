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
package org.sudowars.Model.Sudoku;

import java.io.Serializable;

import org.sudowars.Model.Sudoku.Field.Cell;
import org.sudowars.Model.Sudoku.Field.Field;
import org.sudowars.Model.Sudoku.RuleManagement.DependencyManager;

/**
 * This class represents a sudoku. 
 *
 * @param <T> class type implementing {@link Cell} defining the type of cells used in the sudoku
 * @see Field
 * @see DependencyManager
 */
public final class Sudoku<T extends Cell> implements Serializable, Cloneable {
	
	/**
	 * Serial version UID for serialization
	 */
	private static final long serialVersionUID = 6899839274929615372L;
	
	private final Field<T> field;
	private final DependencyManager dependencyManager;
	
	/**
	 * Initializes a new instance of the {@link Sudoku} class
	 *
	 * @param field the underlying {@link Field} of the {@link Sudoku} 
	 * @param dm reference to a {@link DependencyManager} instance
	 * holding the dependencies between the cells of the given field
	 *
	 * @throws IllegalArgumentException if at least one of the given parameters is <code>null</code>
	 */
	public Sudoku(Field<T> field, DependencyManager dm) throws IllegalArgumentException {
		if (field == null || dm == null) {
			// if any parameter is null...
			throw new IllegalArgumentException();
		}
		this.field = field;
		this.dependencyManager = dm;
	}
	
	/**
	 * Returns the underlying {@link Field} of this instance
	 *
	 * @return {@link Field} instance representing the underlying {@link Field} of this {@link Sudoku}
	 */
	public Field<T> getField(){
		return this.field;
	}
	
	/**
	 * Returns the {@link DependencyManager} of this instance
	 *
	 * @return the instances {@link DependencyManager} 
	 */
	public DependencyManager getDependencyManager() {
		return this.dependencyManager;
	}
	
	/**
	 * Returns a copy of the calling {@link Sudoku}.
	 * @return a copy containing the cloned {@link Field} and the identical {@link DependencyManager}
	 */
	@Override
	public Sudoku<T> clone() {
		return new Sudoku<T>(this.field.clone(), this.dependencyManager);
	}
}


