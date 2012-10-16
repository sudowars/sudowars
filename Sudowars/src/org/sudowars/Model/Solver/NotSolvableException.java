package org.sudowars.Model.Solver;

/**
 * This class defines an exception which is thrown if a strategy detects that the 
 * given sudoku is not solvable.
 */

public class NotSolvableException extends Exception {

	private static final long serialVersionUID = -1242771235592416781L;

	public NotSolvableException() {
		super("Current field is not solvable");
	}
	
}
