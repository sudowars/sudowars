package org.sudowars.Model.Difficulty;

import java.util.ArrayList;
import java.util.List;

import org.sudowars.Model.Solver.SolverStrategy;
import org.sudowars.Model.Sudoku.Sudoku;
import org.sudowars.Model.Sudoku.Field.DataCell;

/**
 * Provides functionality to evaluate the {@link Difficulty} of a {@link Sudoku} for a human player
 * @see DifficultyEvaluator
 */
public class HumanDifficultyEvaluator extends DifficultyEvaluator {
		
	/**
	 * Evaluates the {@link Difficulty} of a given {@link Sudoku}
	 *
	 * @param sudoku the {@link Sudoku} to evaluate
	 * @param usedStrategies list of the used strategies to solve the sudoku
	 * @return the {@link Difficulty} of the {@link Sudoku}, <code>null</code> if no difficulty could be found
	 *
	 * @throws IllegalArgumentException if the given sudoku was <code>null</code>
	 */
	public Difficulty evaluateDifficulty(Sudoku<DataCell> sudoku, List<SolverStrategy> usedStrategies) throws IllegalArgumentException {
				
		//initiate difficulty list
		this.difficulties = new ArrayList<Difficulty>(3);
		this.difficulties.add(new DifficultyEasy());
		this.difficulties.add(new DifficultyMedium());
		this.difficulties.add(new DifficultyHard());
		
		//count unset cells
		int countUnsetCells = 0;
		for (DataCell cell : sudoku.getField().getCells()) {
			if (!cell.isInitial()) countUnsetCells++;
		}
		
		//evaluate difficulty by analysing the used strategies
		int weightSum = 0;
		for (SolverStrategy strategy : usedStrategies) weightSum += strategy.getStrategyWeight();
		double rating = (double)weightSum / (double)countUnsetCells;
				
		//find difficulty
		Difficulty resultDifficulty = null;
		for (Difficulty difficulty : this.difficulties) {
			try {
				difficulty.setValue(rating);
				resultDifficulty = difficulty;
				break;
			} catch (IllegalArgumentException ex) {
				
			}
		}
		
		return resultDifficulty;
		
	}

}


