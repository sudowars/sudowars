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


