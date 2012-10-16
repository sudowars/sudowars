package org.sudowars.Model.SudokuManagement.Generator;

import java.util.Random;

import org.sudowars.Model.Difficulty.Difficulty;
import org.sudowars.Model.Difficulty.DifficultyEvaluator;
import org.sudowars.Model.Sudoku.Sudoku;
import org.sudowars.Model.Sudoku.Field.DataCell;
import org.sudowars.Model.Sudoku.Field.SquareStructure;
import org.sudowars.Model.SudokuManagement.Pool.SudokuFilePool;

/**
 * Provides functionality to generate a new {@link Sudoku} with a specific {@link Difficulty} from a 
 * fully-filled base-{@link Sudoku}. 
 */
public abstract class GeneratorBase implements Runnable {
	
	protected DifficultyEvaluator difficultyEvaluator;
	protected SudokuFilePool targetFilePool;
	protected Difficulty targetDifficulty = null;
	protected Sudoku<DataCell> baseSudoku = null;
	protected Random randGen = new Random();
	
	//defines bounds for the number of initial cells the field holds
	protected int maxInitialCellCount; // more initial cells makes it too easy to find a solution
	protected int minInitialCellCount; // minimum number of initial cells to get a unique solvable sudoku
		
	
	/**
	 * Generates a new {@link Sudoku} with the given {@link Difficulty} from the given fully-filled base-{@link Sudoku}
	 *
	 * @param difficulty the {@link Difficulty} to generate
	 * @param structure the fieldstructure the generated Sudoku shall have
	 */
	public GeneratorBase(SudokuFilePool sudokuFilePool) {

		if (sudokuFilePool == null) {
			throw new IllegalArgumentException("The given SudokuFilePool is null");
		}
		
		this.targetFilePool = sudokuFilePool;
		
	}

	/**
	 * Generates a new {@link Sudoku} with the given {@link Difficulty} from the given fully-filled base-{@link Sudoku}
	 *
	 * @param difficulty the {@link Difficulty} to generate
	 * @param baseSudoku the fully-filled base-{@link Sudoku}
	 * @throws IllegalArgumentException if at least one of the given parameters is <code>null</code> or the given sudoku 
	 * 				has an unknwon structure
	 */
	public void setTargetSudokuProperties(Difficulty difficulty, Sudoku<DataCell> baseSudoku) throws IllegalArgumentException {
		
		if (difficulty == null) {
			throw new IllegalArgumentException("Given difficulty is null");
		}
		
		if (baseSudoku == null) {
			throw new IllegalArgumentException("Given baseSudoku is null");
		}
		
		if (!baseSudoku.getField().isFilled()) {
			throw new IllegalArgumentException("Given baseSudoku is not filled");
		}
		
		//TODO optimise setting of initial bounds
		if (baseSudoku.getField().getStructure() instanceof SquareStructure && baseSudoku.getField().getStructure().getWidth() == 9) {
			this.maxInitialCellCount = 35;
			this.minInitialCellCount = 17;
		} else
		if (baseSudoku.getField().getStructure() instanceof SquareStructure && baseSudoku.getField().getStructure().getWidth() == 16) {
			this.maxInitialCellCount = 130;
			this.minInitialCellCount = 17;
		} else {
			throw new IllegalArgumentException("Given baseSudoku has an unknown structure, bounds could not be defined");
		}
		
		this.targetDifficulty = difficulty;
		this.baseSudoku = baseSudoku;
		
	}
}


