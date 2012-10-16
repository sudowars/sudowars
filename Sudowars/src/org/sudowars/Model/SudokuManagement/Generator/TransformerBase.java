package org.sudowars.Model.SudokuManagement.Generator;

import java.io.Serializable;

import org.sudowars.Model.Sudoku.Sudoku;
import org.sudowars.Model.Sudoku.Field.DataCell;

/**
 * Provides functionality to transform a given {@link Sudoku} into a Sudoku with another {@link Field} arrangement
 */
public abstract class TransformerBase implements Serializable {
	
	private static final long serialVersionUID = 2315752398929711602L;

	/**
	 * Transforms a given {@link Sudoku} into a Sudoku with another {@link Field} arrangement
	 *
	 * @param sudoku the {@link Sudoku} to transform
	 *
	 * @return the transformed {@link Sudoku}
	 */
	public abstract Sudoku<DataCell> transformSudoku(Sudoku<DataCell> sudoku);

}


