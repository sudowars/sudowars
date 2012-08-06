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
package org.sudowars.Model.Solver;

import java.util.LinkedList;

import org.sudowars.DebugHelper;
import org.sudowars.Model.Sudoku.Field.DataCell;
import org.sudowars.Model.Sudoku.Field.Field;
import org.sudowars.Model.Sudoku.RuleManagement.DependencyManager;

/**
 * The class defines the functionality to solve a {@link Sudoku} by using
 * an intelligent backtracking algorithm which used logical combinations to
 * solve the cells.
 */
public class BacktrackingSolver extends StrategyExecutor implements Solver {
		
	private static final long serialVersionUID = 8829931849869387289L;
	
	private StrategyExecutor.ExecuteResult result;

	/**
	 * Initialises the used strategies and adds them to the list by there priority.
	 */
	@Override
	protected void createStrategies() {
		
		this.solveStrategies = new LinkedList<SolverStrategy>();

		this.solveStrategies.add(new NakedSingleStrategy(1));
		this.solveStrategies.add(new HiddenSingleStrategy(3));
		this.solveStrategies.add(new LockedCandidateStrategy(6));
		this.solveStrategies.add(new NakedNCliqueStrategy(8));
		this.solveStrategies.add(new HiddenNCliqueStrategy(15));
		
		//Tests showed that the n-Fish-strategy works and reduces the lists
		//of candidates but it did not increase the performance of the solver.
		//In many situations the additional work to check all possible n-Fishs
		//result in a longer runtime than without the strategy.
		//this.solveStrategies.add(new NFishStrategy(20));
		
	}
	
	/**
	 * Saves the solution in the cell
	 * @param currentState the current solution state
	 * @param solvedCellIndex index of the solved cell
	 * @param solution the solution of the cell
	 * @return <code>true</code> if the cell was saved, <code>false</code> otherwise
	 */
	@Override
	protected boolean saveCell(SolverState currentState, int solvedCellIndex, int solution){
		//set value in the current state
		DataCell solvedCell = (DataCell) currentState.getField().getCell(solvedCellIndex);
		solvedCell.setValue(solution);
		//remove notes
		currentState.getNoteManager().removeAllNotes(solvedCell);
		return true;
	}
	
	/**
	 * Solves a {@link Field} of {@link DataCell}s and returns the fully filled solution-{@link Field}
	 *
	 * @param initialField The initial field to solve
	 * @param dependencyManager The dependency manager of the field
	 *
	 * @return the fully filled solution-{@link Field}, <code>null</code> if given field is not solvable
	 *
	 * @throws IllegalArgumentException if no field or no dependencyManager was given
	 */
	public Field<DataCell> solve(Field<DataCell> initialField, DependencyManager dependencyManager) throws IllegalArgumentException {
				
		DebugHelper.log(DebugHelper.PackageName.Solver, "*** solving process startet ***");
		
		if (initialField == null) {
			throw new IllegalArgumentException("given initialfield cannot be null.");
		}
		
		if (dependencyManager == null) {
			throw new IllegalArgumentException("given DependencyManager cannot be null.");
		}
		
		//initiate solver state
		SolverState currentState = new SolverState(initialField.clone().convert(), dependencyManager);
				
		//initiate solution field
		Field<DataCell> solutionField = initialField.clone();
		
		//clear used strategies to save all strategies necessary to solve the next cells 
		//from the current state
		this.usedStrategies.clear();
		
		//solve the given field
		if (!currentState.getField().isFilled()) {
			
			//execute strategies to solve the field
			result = this.executeStrategies(currentState, false);
			DebugHelper.log(DebugHelper.PackageName.Solver, "*** solving process finished : " + result.toString() + " ***");
			
			//save result
			if (result == StrategyExecutor.ExecuteResult.UNIQUESOLUTION) {
				
				//fill all empty cells of the solution field with the solved field of the current state
				for (DataCell cell : solutionField.getCells()) {
					if (!cell.isSet()) {
						int cellValue = currentState.getField().getCell(cell.getIndex()).getValue();
						cell.setValue(cellValue);
					}
				}
				
			} else {
				
				//field could not be solved
				solutionField = null;
				
			}
			
		}
		
		return solutionField;
		
	}
	
}


