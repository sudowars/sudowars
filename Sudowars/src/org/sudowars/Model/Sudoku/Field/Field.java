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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The class {@link Field} represents a 2 dimensional field of {@link Cell}s.
 * The {@link Field}s structure is defined and retrievable by the inherited {@link FieldStructure}.
 * This {@link FieldStructure} also allows the {@link Field} to not only hold {@link Cell}s but also gaps.
 */
public final class Field<T extends Cell> implements Cloneable, Serializable {

	/**
	 * Serial version UID for serialization.
	 */
	private static final long serialVersionUID = -2097355744839829536L;
	
	private List<T> cells;
	private FieldStructure structure;
	
	/**
	 * Initializes a new instance of {@link Field} with the given parameters.
	 *
	 * @param cells a {@link List} of {@link Cell}s
	 * @param structure the instances future {@link FieldStructure}
	 *
	 * @throws IllegalArgumentException if at least one given parameter is <code>null</code>
	 */
	Field(List<T> cells, FieldStructure structure) throws IllegalArgumentException {
		
		if (cells == null || structure == null) {
			throw new IllegalArgumentException();
		}
		
		this.cells = cells;
		this.structure = structure;
	}
	
	/**
	 * Determines whether all used slots are holding {@link Cell}s returning <code>true</code> when invoking {@link Cell#isSet()}. 
	 *
	 * @return <code>true</code> if all contained cells are {@link Cell#isSet()}.
	 * @see Cell#getValue()
	 * @see Cell#isSet()
	 */
	public boolean isFilled() {
		for (Cell cell : cells) {
			if (!cell.isSet()) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Returns the {@link Cell} at the position given by x and y.
	 *
	 * @param x horizontal coordinate of the {@link Cell}s position 
	 * @param y vertical coordinate of the {@link Cell}s position
	 *
	 * @return the {@link Cell} at the given position or <code>null</code> if the position is unused.
	 *
	 * @throws IllegalArgumentException if the position defined by x and y is not covered by the {@link Field}.
	 * @throws IllegalArgumentException if the position defined by x and y points to a unused slot inside this {@link Field}
	 * @see FieldStructure#isSlotUsed(int, int) 
	 * @see FieldStructure#getStructure()
	 */
	public T getCell(int x, int y) throws IllegalArgumentException{
		try {
			return cells.get(structure.getIndex(x, y));
		} catch (IllegalArgumentException e) {
			throw e;
		}
	}
	
	/**
	 * Returns the {@link Cell} with the given index.
	 *
	 * @param index the given index.
	 *
	 * @return the {@link Cell} with the given index.
	 *
	 * @throws IllegalArgumentException if the given index is out of bounds<br> defined by the underlying structure
	 */
	public T getCell(int index) throws IllegalArgumentException {
		if (index >= 0 && index < structure.getUsedSlotCount()) {
			return cells.get(index);
		} else {
			throw new IllegalArgumentException("Illegal index");
		}
	}
	
	/**
	 * Returns the {@link Field}s {@link FieldStructure} which represents the {@link Field}s structure.
	 * @return the {@link Field}s {@link FieldStructure}
	 * @see FieldStructure
	 * @see FieldBuilder#buildField()
	 */
	public FieldStructure getStructure() {
		return structure;
	}
	
	/**
	 * Returns all {@link Cell}s of this {@link Field}
	 * @return an unmodifiable {@link List} with all {@link Cell}s of this {@link Field}
	 */
	public List<T> getCells() {
		return Collections.unmodifiableList(cells);
	}
	
	/**
	 * Returns a deep copy of the calling {@link Field}
	 * @return a deep copy of the calling {@link Field}
	 */
	@SuppressWarnings("unchecked")
	public Field<T> clone() {
		List<T> newCellList = new ArrayList<T>(cells.size());
		for (T c : cells) {
			newCellList.add((T) c.clone());
			/*				^^^^^^^^^^^^^
			 * clone() is meant to copy the given calling object.
			 * In this case the calling objects type is <T> and the cast should be legal.
			 */
		}
		return new Field<T>(newCellList, structure);
	}
	
	/**
	 * Returns a shallow copy of the calling {@link Field} while changing the 
	 * parameterization to the base interface {@link Cell}
	 * @return a new {@link Field<Cell>} with the calling {@link Field}s {@link Cell}s.
	 */
	public Field<Cell> convert() {
		ArrayList<Cell> newCells = new ArrayList<Cell>(this.cells.size());
		newCells.addAll(this.cells);
		// valid as order is retained.
		return new Field<Cell>(newCells, this.structure);
	}
}

