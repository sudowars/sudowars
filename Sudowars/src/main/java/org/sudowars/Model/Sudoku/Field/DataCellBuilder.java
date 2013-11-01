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
package org.sudowars.Model.Sudoku.Field;

/**
 * Defines and implements a factory for not initial {@link DataCell}s.
 */
public class DataCellBuilder implements CellBuilder<DataCell> {
	
	/**
	 * Builds a {@link DataCell} with the given index and initial state {@code false}.
	 *
	 * @param index the {@link DataCell}s future index.
	 *
	 * @return the new {@link DataCell} instance
	 *
	 * @throws IllegalArgumentException if given index was < <code>0</code>
	 * @see CellBuilder#buildCell(int) 
	 */
	public DataCell buildCell(int index) throws IllegalArgumentException {
		return new DataCell(index, false);
	}
	
}
