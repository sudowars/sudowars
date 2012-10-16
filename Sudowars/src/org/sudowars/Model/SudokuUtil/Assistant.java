package org.sudowars.Model.SudokuUtil;

import org.sudowars.Model.Game.SingleplayerGame;
import org.sudowars.Model.Solver.ConsecutiveSolver;
import org.sudowars.Model.Solver.HumanSolver;
import org.sudowars.Model.Solver.SolveStep;
import org.sudowars.Model.Solver.SolverState;
import org.sudowars.Model.Sudoku.Field.Cell;
import org.sudowars.Model.Sudoku.Field.Field;
import org.sudowars.Model.Sudoku.RuleManagement.DependencyManager;

/**
 * This class is used to assist the player during game.
 */
public class Assistant {

	private final SingleplayerGame game;
	private final ConsecutiveSolver solver;
	private final Field<Cell> convertedGameField;
	private final DependencyManager dependencies;
			
	/**
	 * Initializes a new instance of the {@link Assistant} class.
	 *
	 * @param game The {@link SingleplayerGame} to work on.
	 *
	 * @throws IllegalArgumentException if the given game is <code>null</code>
	 */
	public Assistant(SingleplayerGame game) throws IllegalArgumentException {
		if (game == null) {
			throw new IllegalArgumentException("given game cannot be null.");
		}
		this.game = game;
		this.solver = new HumanSolver();
		this.convertedGameField = this.game.getSudoku().getField().convert();
		this.dependencies = this.game.getSudoku().getDependencyManager();
	}
	
	/**
	 * Makes the current assistant solve a game cell whose value has not been set.
	 *
	 * @return Reference to a {@link SolveStep} holding the cell whose value has been found, if any,
	 * or <code>null</code> if no cell to solve was found.
	 */
	public SolveStep solveNext(){
		return this.solver.getCellToSolveNext(new SolverState(this.convertedGameField, this.dependencies));
	}
}


