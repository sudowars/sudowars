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
import org.sudowars.Model.Sudoku.Field.CellBuilder;
import org.sudowars.Model.Sudoku.Field.Field;

/**
 * Defines and implements a factory for {@link GameCell}s.
 */
public class GameCellBuilder implements CellBuilder<GameCell> {
	
	private final Field<Cell> field;
		
	/**
	 * Initializes a new instance of the {@link GameCellBuilder} class with to decorate the cells<br>
	 * of a given {@link Field}.
	 * @param field reference to a {@link Field} to relate.
	 * @throws IllegalArgumentException if the given field was {@code null}
	 * @see GameCell
	 */
	GameCellBuilder(Field<Cell> field) throws IllegalArgumentException {
		if (field == null) {
			throw new IllegalArgumentException("given field cannot be null.");
		}
		this.field = field;
	}
	
	/**
	 * Builds a {@link GameCell} with the given index.
	 *
	 * @param index the {@link GameCell}s future index.
	 *
	 * @return the new {@link GameCell} instance
	 *
	 * @throws IllegalArgumentException if given index was <= <code>0</code>
	 * @see CellBuilder#buildCell(int) 
	 */
	public GameCell buildCell(int index) throws IllegalArgumentException {
		if (index < 0) {
			throw new IllegalArgumentException("given cell index must be >= 0.");
		}
		GameCell result = null;
		Cell core = this.field.getCell(index);
		if (core != null) {
			result = new GameCell(core);
		}
		return result;
	}
}
