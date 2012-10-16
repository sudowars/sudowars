package org.sudowars.Model.Difficulty;

import java.util.List;

import org.sudowars.Model.Solver.SolverStrategy;
import org.sudowars.Model.Sudoku.Sudoku;
import org.sudowars.Model.Sudoku.Field.DataCell;

/**
 * Provides functionality to evaluate the {@link Difficulty} of a {@link Sudoku}. The
 * {@link Difficulty} will be calculated by interpreting the difficulty of the the 
 * strategies necessary to solve the given Sudoku.
 */
public abstract class DifficultyEvaluator {

	protected List<Difficulty> difficulties;
	
	/**
	 * Evaluates the {@link Difficulty} of a given {@link Sudoku}
	 *
	 * @param sudoku the {@link Sudoku} to evaluate
	 * @param usedStrategies list of the used strategies to solve the sudoku
	 * @return the {@link Difficulty} of the {@link Sudoku}, <code>null</code> if no difficulty could be found
	 *
	 * @throws IllegalArgumentException if the given sudoku was <code>null</code>
	 */
	public abstract Difficulty evaluateDifficulty(Sudoku<DataCell> sudoku, List<SolverStrategy> usedStrategies) throws IllegalArgumentException;
		
}


