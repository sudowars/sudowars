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
package org.sudowars.Model.SudokuManagement.Pool;

import java.io.Serializable;

import org.sudowars.Model.Difficulty.Difficulty;
import org.sudowars.Model.Sudoku.Sudoku;
import org.sudowars.Model.Sudoku.Field.DataCell;
import org.sudowars.Model.Sudoku.Field.FieldStructure;

/**
 * This interface provides functionality to get a new sudoku to solve.
 */
public interface SudokuPool extends Serializable {

	/**
	 * Indicates whether the pool is empty, i.e. does not contain any sudokus.
	 *
	 * @return {@code true}, if the pool is empty, otherwise {@code false}.
	 */
	public boolean empty();
	
	/**
	 * Indicates if the pool has changed.
	 *
	 * @return <code>true</code> if pool has changed, otherwise <code>false</code>.
	 */
	public boolean hasChanged();
	
	/**
	 * Extracts an unsolved sudoku with the given structure and difficulty from the pool.
	 *
	 * @param structure The {@link FieldStructure} of the sudoku to extract.
	 * @param difficulty The {@link Difficulty} of the sudoku to extract.
	 *
	 * @return An unsolved {@link Sudoku} with the given structure and difficulty<br>
	 * or <code>null</code> if no sudoku could be found.
	 *
	 * @throws IllegalArgumentException if at least one of the given params was <code>null</code>
	 */
	public Sudoku<DataCell> extractSudoku(FieldStructure structure, Difficulty difficulty) throws IllegalArgumentException;
		
}


