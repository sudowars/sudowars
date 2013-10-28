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
package org.sudowars.Model.SudokuUtil;

import org.sudowars.Model.Sudoku.Field.Cell;

/**
 * This class holds the data of the onChange event of the {@link NoteManager}. 
 */
public final class NoteManagerChangedEvent {

	private final Cell cell;
	private final NoteManager source;
	
	/**
	 * Initializes a new instance of the {@link NoteManagerChangedEvent}.
	 * @param source the source of the event.
	 * @param affectedCell The {@link Cell} which was affected by the event.
	 * @throws IllegalArgumentException if one of the arguments was {@code null}
	 */
	NoteManagerChangedEvent(NoteManager source, Cell affectedCell) throws IllegalArgumentException {
		if (source == null || affectedCell == null) {
			throw new IllegalArgumentException("invalid argument given: null");
		}
		this.source = source;
		this.cell = affectedCell;
	}

	/**
	 * Gets the cell which was affected by the event.
	 * @return Reference to the {@link Cell}.
	 */
	public Cell getCell() {
		return this.cell;
	}

	/**
	 * Gets the source of the event.
	 * @return The note manager which triggered the event.
	 */
	public NoteManager getSource() {
		return this.source;
	}
}
