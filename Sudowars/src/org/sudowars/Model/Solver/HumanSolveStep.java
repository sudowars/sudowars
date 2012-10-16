package org.sudowars.Model.Solver;

import java.util.List;

import org.sudowars.Model.Sudoku.Field.Cell;

/**
 * This class defines a solution step of the {@link HumanSolver}. It extends {@link SolveStep} by the strategies 
 * used to solve the {@link Cell}.
 */
public class HumanSolveStep extends SolveStep {

	private static final long serialVersionUID = -2401288797609366533L;
	private List<SolverStrategy> strategies;
	
	/**
	 * Initialises a new instance of the {@link HumanSolveStep} class.
	 *
	 * @param cell A reference to the solvedCell.
	 * @param notesChanged Defines if the notes were changed 
	 * @param strategies The strategies used to solve the cell.
	 * 
	 * @throws IllegalArgumentException given solution is smaller than zero
	 * 
	 */
	public HumanSolveStep(Cell cell, int solution, boolean notesChanged, List<SolverStrategy> strategies) throws IllegalArgumentException {
		super(cell, solution, notesChanged);
		this.strategies = strategies;		
	}
	
	/**
	 * Returns the strategies used to solve the {@link Cell}.
	 *
	 * @return A reference to a {@link SolverStrategy}, <code>null</code> if the step was not successful.
	 */
	public List<SolverStrategy> getUsedStrategies(){
		return this.strategies;
	}
	
}


