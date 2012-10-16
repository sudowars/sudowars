package org.sudowars.Model.Solver;

import java.io.Serializable;

/**
 * The interface defines the functionality of consecutive solvers
 */
public interface ConsecutiveSolver extends Serializable {
	
	/**
	 * Returns the {@link SolveStep} to solve the next {@link Cell}
	 *
	 * @param currentState the current field and notes of the solver
	 *
	 * @return {@link SolveStep} to solve the next {@link Cell}
	 */
	public SolveStep getCellToSolveNext(SolverState currentState);

}


