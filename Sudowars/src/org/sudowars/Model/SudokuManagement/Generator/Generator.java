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

import java.util.LinkedList;
import java.util.List;

import org.sudowars.Model.Difficulty.Difficulty;
import org.sudowars.Model.Difficulty.HumanDifficultyEvaluator;
import org.sudowars.Model.Solver.BacktrackingSolver;
import org.sudowars.Model.Sudoku.Sudoku;
import org.sudowars.Model.Sudoku.Field.Cell;
import org.sudowars.Model.Sudoku.Field.DataCell;
import org.sudowars.Model.Sudoku.Field.Field;
import org.sudowars.Model.SudokuManagement.Pool.SudokuFilePool;

/**
 * Provides functionality to generate a new {@link Sudoku} with a specific {@link Difficulty} from a 
 * fully-filled base-{@link Sudoku}. 
 */
public class Generator extends GeneratorBase implements Runnable {
	
	//difficulty evaluator to identify the difficulty of the generated sudoku
	private HumanDifficultyEvaluator diffEvaluator = new HumanDifficultyEvaluator();
		
	//solver to check if a sudoku has a unique solution
	private BacktrackingSolver solver = new BacktrackingSolver();
	
	public Generator(SudokuFilePool sudokuFilePool) {
		super(sudokuFilePool);
	}
	
	/**
	 * Returns the initial cells of a sudoku
	 * @param sudoku the sudoku
	 * @return list of the initial cells of the given sudoku
	 */
	private static List<DataCell> getInitialCells(Sudoku<DataCell> sudoku) {
		
		List<DataCell> resultList = new LinkedList<DataCell>();
		
		for (DataCell cell : sudoku.getField().getCells()) {
			if (cell.isInitial()) resultList.add(cell);
		}
		
		return resultList;
		
	}
	
	/**
	 * Returns the initial field of a sudoku where only the initial cells are set
	 * @param sudoku the sudoku
	 * @return initial field of a sudoku 
	 */
	private static Field<DataCell> getInitialField(Sudoku<DataCell> sudoku) {
		
		//generate initial field
		Field<DataCell> initialField = sudoku.getField().clone();
		for (DataCell cell : initialField.getCells()) {
			if (!cell.isInitial()) cell.setValue(DataCell.NOT_SET);
		}
		
		return initialField;
		
	}
	
	/**
	 * Generates a sudoku with the difficulty defined in the properties by reducing
	 * the number of initial cells.
	 * @param sudoku the sudoku to start generating
	 * @return sudoku with the given difficulty, <code>null</code> if no sudoku could be found
	 */
	private Sudoku<DataCell> generateSudoku(Sudoku<DataCell> sudoku) {
		
		//initiate the result sudoku
		Sudoku<DataCell> resultSudoku = null;	
		
		//get the list of initial cells
		List<DataCell> initialCells = getInitialCells(sudoku);
	
		//define list which contains the cells not resulting in a unique solution after removing their initial flag
		LinkedList<DataCell> changedCells = new LinkedList<DataCell>();
		
		//if the generator tries to iterate through all initial cells it is possible that he
		//just finds solutions that has a difficulty smaller than the searched one. It could
		//happen that all backtracking steps of a cell must be calculated to identify there is no
		//better (more difficult) solution. Thats why we reduce the number of tries which set
		//a random cell to not initial. Most of the time a solution could be found much faster if
		//we change the backtracking branch.		
		int maxTries = 3;
		int countTries = 0;
		
		while (countTries < maxTries && initialCells.size() >= this.minInitialCellCount && changedCells.size() < initialCells.size()) {
		
			//find random initial cell which has not been checked so far
			int randomIndex = this.randGen.nextInt(initialCells.size());
			DataCell randomCell = initialCells.get(randomIndex);
			if (changedCells.contains(randomCell)) continue;
			
			//try to solve the field with this cell set to not initial
			countTries++;
			
			//remove initial-flag from the random initial cell	
			randomCell.setInitial(false);	
			changedCells.add(randomCell);
			
			//check if the sudoku (still) has an unique solution
			if (this.solver.solve(getInitialField(sudoku), sudoku.getDependencyManager()) != null) {
				
				Difficulty currentDifficulty = null;
				
				//unique solution found - if maximum initial cell count was reached check the difficulty, otherwise backtrack to reduce the number of initial cells
				if (initialCells.size() <= this.maxInitialCellCount) {
					
					//Evaluate the current sudoku
					currentDifficulty = this.diffEvaluator.evaluateDifficulty(sudoku, this.solver.getUsedStrategies());
					
					//if difficulty could be found check if it is the searched difficulty
					if (currentDifficulty != null) {
						
						try {
							this.targetDifficulty.setValue(currentDifficulty.getValue());
							resultSudoku = sudoku;
						} catch (IllegalArgumentException ex) {
							resultSudoku = null;
						}
						
						//if difficulty is not the the searched one add generated sudoku to the pool
						if (resultSudoku == null) {
							this.targetFilePool.addSudoku(sudoku.clone(), currentDifficulty, true);
						}
					}
					
				}
				
				//if the found difficulty is not the target difficulty or it is too small
				//backtracking will be called to reduce number of initial cells
				if (initialCells.size() > this.maxInitialCellCount || currentDifficulty != null && currentDifficulty.getValue() < this.targetDifficulty.getLowerBound()) {
					resultSudoku = this.generateSudoku(sudoku);
				}
				
			}
			
			//break if a solution was found
			if (resultSudoku != null) break;
			
			//reset the checked cell and try next one
			randomCell.setInitial(true);
						
		}
		
		return resultSudoku;
		
	}
		
	/**
	 * Return a random initial cell from the given list
	 * @param cellList the cell list
	 * @return random initial cell from the given list, <code>null</code> if no initial cell could be found
	 */
	private Cell getRandomInitialCell(List<Cell> cellList) {
		
		if (cellList.size() == 0) return null;
		
		int randomInitialCellIndex;
		do {
			randomInitialCellIndex = this.randGen.nextInt(cellList.size());
		} while (!cellList.get(randomInitialCellIndex).isInitial());
		
		return cellList.get(randomInitialCellIndex);
		
	}
	
	/**
	 * Set n random initial cells to non-initial
	 * 
	 * @param sudoku the sudoku to reduce the initial cells
	 * @param cellsToUninitialise number of cells to change the initial flag to false
	 * @return the cells which are changed
	 * @throws IllegalArgumentException if cellsToUninitialize is not greater than zero or there are not enough cells to reduce
	 */
	private LinkedList<DataCell> reduceInitialCells(Sudoku<DataCell> sudoku, int cellsToUninitialise) throws IllegalArgumentException {
		
		if (cellsToUninitialise < 0) {
			throw new IllegalArgumentException("number of cells to change the initial-flag have to be greater than zero");
		}
		
		if (cellsToUninitialise > sudoku.getField().getCells().size()) {
			throw new IllegalArgumentException("not enough cells to reduce the given amount");
		}
		
		//initialise the list of changed cells
		LinkedList<DataCell> changedCells = new LinkedList<DataCell>();
		
		//search initial cells and remove initial flag
		int uninitialisedCells = 0;
		
		while (uninitialisedCells < cellsToUninitialise) {
			
			//search random initial cell			
			Cell randomInitialCell = getRandomInitialCell(sudoku.getField().convert().getCells());
			assert (randomInitialCell != null);
			
			//set initial to false
			sudoku.getField().getCell(randomInitialCell.getIndex()).setInitial(false);
			changedCells.add(sudoku.getField().getCell(randomInitialCell.getIndex()));
			uninitialisedCells++;
			
		}

		return changedCells;
		
	}
	

	/**
	 * Reduces the initial cells of the given sudoku to the defined minimum of initial cells. This reduces 
	 * the number of backtracking steps and results in a better performance of the generating process.
	 * @param sudoku the sudoku to reduce the initial fields
	 */
	private void minimiseBaseSudoku() {
		
		do {
			
			//Set all cells to initial
			for (DataCell c : this.baseSudoku.getField().getCells()) {
				c.setInitial(true);
			}
			
			this.reduceInitialCells(this.baseSudoku, this.baseSudoku.getField().getCells().size() - this.maxInitialCellCount);
						
		} while (this.solver.solve(getInitialField(this.baseSudoku), this.baseSudoku.getDependencyManager()) == null);
		 
	}
	
	/**
	 * Generates a sudoku with the given properties and adds it to the given pool
	 * @throws IllegalArgumentException if not all needed properties are set
	 */
	@Override
	public void run() throws IllegalArgumentException {
		
		//checks if properties are set
		if (this.targetDifficulty == null) {
			throw new IllegalArgumentException("target not set");
		}
		
		if (this.baseSudoku == null) {
			throw new IllegalArgumentException("given sudoku is null"); 
		}
		
		Sudoku<DataCell> resultSudoku;
		do {		
			//reduce initial field to a minimum
			this.minimiseBaseSudoku();
			//generate sudoku with the given difficulty
			resultSudoku = this.generateSudoku(this.baseSudoku);
			
		} while (resultSudoku == null);
		
		//add sudoku to the pool 
		this.targetFilePool.addSudoku(resultSudoku, this.targetDifficulty, true);
		
	}
	
}
