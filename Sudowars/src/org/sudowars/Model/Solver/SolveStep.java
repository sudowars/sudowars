package org.sudowars.Model.Solver;

import java.io.Serializable;

import org.sudowars.Model.Sudoku.Field.Cell;

/**
 * This class defines a solution step of the {@link StrategyExecutor}. If a solution was found
 * it holds the solved {@link Cell}, its solution and the information if notes were changed.
 */
public class SolveStep implements Serializable {

	private static final long serialVersionUID = 4344624432671509197L;
	
	private Cell solvedCell;
	private boolean notesChanged;
	private int solution;
	
	/**
	 * Initialises a new instance of the {@link SolveStep} class
	 *
	 * @param cell Reference to the solved cell
	 * @param solution The calculated solution of the field
	 * @param notesChanged Defines if the notes were changed
	 * 
	 * @throws IllegalArgumentException given solution is smaller than zero
	 * 
	 */
	public SolveStep(Cell cell, int solution, boolean notesChanged) throws IllegalArgumentException {
		
		if (solution < 0) {
			throw new IllegalArgumentException("given solution can not be smaller than zero");
		}
		
		this.solvedCell = cell;
		this.notesChanged = notesChanged;
		this.solution = solution;
	}
	
	/**
	 * Initialises a new instance of the {@link SolveStep} class
	 * @param notesChanged Defines if the notes were changed
	 */
	public SolveStep(boolean notesChanged) {
		this.solvedCell = null;
		this.notesChanged = notesChanged;
		this.solution = 0;
	}
	
	/**
	 * Returns if the notes of one or more cells were changed.
	 * @return <code>true</code> if notes were changed, <code>false</code> otherwise
	 */
	public boolean hasChangedNotes() {
		return this.notesChanged;
	}
	
	/**
	 * Returns if the step found a new solution for a {@link Cell}
	 * @return <code>true</code> if a new solution was found, <code>false</code> otherwise
	 */
	public boolean hasSolvedCell() {
		return !(this.solvedCell == null);
	}
		
	/**
	 * Returns the solved cell.
	 * @return reference to a {@link Cell}, <code>null</code> if the step was not successful
	 */
	public Cell getSolvedCell() {
		return this.solvedCell;
	}
	
	/**
	 * Returns the solution of the solved cell.
	 * @return solution of the solved cell, <code>zero</code> if no cell was solved
	 */
	public int getSolution() {
		
		int result = 0;
		
		if (this.solvedCell != null) {
			result = (this.solution == 0) ? this.solvedCell.getValue() : this.solution;
		}
				
		return result;
		
	}
}


