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


