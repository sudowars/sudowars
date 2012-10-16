package org.sudowars.Model.Solver;

import java.util.LinkedList;
import java.util.List;

import org.sudowars.DebugHelper;
import org.sudowars.Model.Sudoku.Field.Cell;
import org.sudowars.Model.SudokuUtil.NoteManager;

/**
 * The class defines the functionality to execute the given strategies to solve a {@link Sudoku}
 */
public abstract class StrategyExecutor {

	public enum ExecuteResult {
	    UNIQUESOLUTION, NOSOLUTION, MULTIPLESOLUTION
	}
	
	protected List<SolverStrategy> solveStrategies;
	protected List<SolverStrategy> usedStrategies = new LinkedList<SolverStrategy>();
		
	/**
	 * Returns the defined list of {@link SolverStrategy}.
	 * @return The defined list of {@link SolverStrategy}
	 */
	public List<SolverStrategy> getSolveStrategies() {
		return this.solveStrategies;
	}
	
	/**
	 * Returns the list of used {@link SolverStrategy} to solve the field.
	 * @return The list of used {@link SolverStrategy}
	 */
	public List<SolverStrategy> getUsedStrategies() {
		return this.usedStrategies;
	}
	
	/**
	 * Initialises the used strategies and adds them to the list by there priority.
	 */
	protected abstract void createStrategies();
	
	/**
	 * Saves the solution in the cell
	 * @param currentState the current solution state
	 * @param solvedCell the solved cell
	 * @param solution the solution of the cell
	 * @return <code>true</code> if the cell was saved, <code>false</code> otherwise
	 */
	protected abstract boolean saveCell(SolverState currentState, int solvedCellIndex, int solution);
		
	/**
	 * Uses the given strategies to solve the field or rather reduce the candidate list. The 
	 * result will be stored in the current state.
	 * @param currentState the current solution state of the field
	 * @param breakAfterFirstHit <code>true</code> stops the execution after the first found cell solution, <code>false</code> will try to solve all cells
	 * @return ExecuteResult which represents the success or the failure occurred when executing the strategies
	 */
	protected ExecuteResult executeStrategies(SolverState currentState, boolean breakAfterFirstHit) {
		
		//initiate result
		ExecuteResult result = null;
		
		//initiate control parameters		
		boolean loopStrategyListAgain = false;
		
		do {
			
			loopStrategyListAgain = false;
			
			//iterate through strategy list
			for (SolverStrategy strategy : this.solveStrategies) {
				
				try {
					
					//execute strategy
					List<SolveStep> strategyResults = strategy.executeStrategy(currentState);
					
					//log debug info
					DebugHelper.log(DebugHelper.PackageName.Solver, "strategy \"" + strategy.toString() + "\" executed, generated " + strategyResults.size() + " solve steps");
					
					//save strategy solutions
					boolean changedField = false;
					for (SolveStep solveStep : strategyResults) {
						
						//check if the field was changed by the strategy
						changedField = (solveStep.hasSolvedCell() || solveStep.hasChangedNotes()) ? true : changedField;
						
						//save strategies which changed the field
						if (changedField) {
							
							this.usedStrategies.add(strategy);
							
							DebugHelper.log(DebugHelper.PackageName.Solver, "--- " + 
									(solveStep.hasSolvedCell() ? "cell #" + solveStep.getSolvedCell().getIndex() + " solved: " + solveStep.getSolution() : "no cell solved") + ", " +
									(solveStep.hasChangedNotes() ? "candidates changed" : "candidates NOT changed"));
							
						}
						
						//check if a cell was solved by the strategy
						if (solveStep.hasSolvedCell()) {
							//save solve step
							currentState.setLastSolveStep(solveStep);
							//save solution							
							this.saveCell(currentState, solveStep.getSolvedCell().getIndex(), solveStep.getSolution());
							//break if parameter is set	
							//TODO find better solution
							if (breakAfterFirstHit) return ExecuteResult.UNIQUESOLUTION;
						}
						
					}
					
					//abort loop if field is solved
					if (currentState.getField().isFilled()) {
						loopStrategyListAgain = false;
						result = ExecuteResult.UNIQUESOLUTION;
						DebugHelper.log(DebugHelper.PackageName.Solver, "strategy loop finished, field is unique solvable");
						break;
					}
					
					//restart loop if field was changed
					if (changedField) {
						loopStrategyListAgain = true;
						break;
					}
					
				} catch (NotSolvableException ex) {
					
					//current field is not solvable
					result = ExecuteResult.NOSOLUTION;
					loopStrategyListAgain = false;
					break;
					
				}
				
			}
			
			//if strategies were not successful use Backtracking to solve the next cell
			if (!loopStrategyListAgain && result == null) {
				
				DebugHelper.log(DebugHelper.PackageName.Solver, "start backtracking");
				
				//search cell with the fewest candidates
				NoteManager notes = currentState.getNoteManager();
				Cell nextCellToSolve = null;
				for (Cell cell : currentState.getField().getCells()) {
					if (cell.isSet()) continue;					
					if (nextCellToSolve == null || notes.getNotes(cell).size() < notes.getNotes(nextCellToSolve).size()) {
						nextCellToSolve = cell;
					}
				}
				
				//cell with the fewest candidates is the next cell to solve
				if (breakAfterFirstHit) {
					SolveStep backtrackSolveStep = new SolveStep(nextCellToSolve, 0, false);
					currentState.setLastSolveStep(backtrackSolveStep);
					return ExecuteResult.UNIQUESOLUTION;
				}
				
				//if there is no cell left the field is not solvable
				if (nextCellToSolve == null) {
					
					loopStrategyListAgain = false;
					result = ExecuteResult.NOSOLUTION;
					DebugHelper.log(DebugHelper.PackageName.Solver, "no next cell to solve, field has no solution");
					
				} else {
				
					//if there is only one candidate left it is the solution of the cell
					//TODO implement check or assert use of NakedSingleStrategy?
					assert (notes.getNotes(nextCellToSolve).size() >= 2);
					assert (breakAfterFirstHit == false);
									
					//save the solution if one of the analysed candidates resulted in a unique solution
					SolverState foundSolveState = null;
					
					//analyse the first two candidates, set the value and try to solve the field again
					List<Integer> cellNotes = notes.getNotes(nextCellToSolve);
					for (int candidate : cellNotes) {
						
						//clone currentState
						SolverState newState = currentState.clone();
						Cell newNextCellToSolve = newState.getField().getCell(nextCellToSolve.getIndex());
						
						//remove all notes except the currently analysed candidate, strategies handle to solve the cell
						newState.getNoteManager().removeAllNotes(newNextCellToSolve);
						newState.getNoteManager().addNote(newNextCellToSolve, candidate);
						
						//execute strategies with the set candidate
						switch (this.executeStrategies(newState, breakAfterFirstHit)) {
						
							case NOSOLUTION :
							
								//The candidate which results in no solution can be removed from the candidates
								//list of the cell. If a unique solution was found with a previous candidate the
								//next one have to be checked too to identify if it is a unique solution. It is
								//more efficient to remove the candidate which can not be the solution and loop
								//through the strategy list again.								
								if (cellNotes.size() == 2 && foundSolveState != null) {
									
									DebugHelper.log(DebugHelper.PackageName.Solver, "Backtracking cell #" + nextCellToSolve.getIndex() + " with candidate " + candidate + ": found no solution, other candidat is solution");
									
									currentState.setField(foundSolveState.getField());
									currentState.setDependencyManager(foundSolveState.getDependencyManager());
									currentState.setNoteManager(foundSolveState.getNoteManager());
									
									loopStrategyListAgain = true;
									
								} else {

									DebugHelper.log(DebugHelper.PackageName.Solver, "Backtracking cell #" + nextCellToSolve.getIndex() + " with candidate " + candidate + ": found no solution, remove candidate");
									
									currentState.getNoteManager().removeNote(nextCellToSolve, candidate);
									loopStrategyListAgain = true;
									
								}
								
								break;
							
							case UNIQUESOLUTION :
							
								//all strategies necessary to found the solution were added to the used strategies list
								//if more than one candidate leads to an unique solution the sudoku is multiple solvable
								//and the execution can be interrupted.
								if (foundSolveState != null) {
								
									DebugHelper.log(DebugHelper.PackageName.Solver, "Backtracking cell #" + nextCellToSolve.getIndex() + " with candidate " + candidate + ": already found a valid candidate => multiple solution");
									
									loopStrategyListAgain = false;
									result = ExecuteResult.MULTIPLESOLUTION;
									
								} else {
									
									DebugHelper.log(DebugHelper.PackageName.Solver, "Backtracking cell #" + nextCellToSolve.getIndex() + " with candidate " + candidate + ": found unique solution, check next candidate");
									
									//save the information that the candidate leads to an unique solution
									foundSolveState = newState.clone();
									
								}

								break;
								
							case MULTIPLESOLUTION :
								
								DebugHelper.log(DebugHelper.PackageName.Solver, "Backtracking cell #" + nextCellToSolve.getIndex() + " with candidate " + candidate + ": found multiple solution");
								
								//If a multiple solution was found the hole sudoko has no unique solution
								result = ExecuteResult.MULTIPLESOLUTION;
								
								break;
								
							
						}
						
						//if the strategy loop flag is set or the result has changed no more candidates have to be checked
						if (loopStrategyListAgain || result != null) break;
						
					}
				
				}
				
			}
			
		} while (loopStrategyListAgain);
				
		//if no result was defined no solution could be found
		if (result == null) {
			result = ExecuteResult.NOSOLUTION;
		}
		
		//return result
		return result;
		
	}
	
	/**
	 * Initialises a new instance of the {@link BacktrackingSolver} class
	 */
	public StrategyExecutor() {
		this.createStrategies();
	}
	
}


