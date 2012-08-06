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
import java.util.List;

import org.sudowars.Model.Sudoku.Field.Cell;
import org.sudowars.Model.Sudoku.RuleManagement.DependencyGroup;
import org.sudowars.Model.Sudoku.RuleManagement.RuleApplier.StandardRulesetFactory;

/**
 * This class defines the "naked-single"-strategy to solve the next {@link Cell} of a {@link Field}. 
 * 
 * The strategy checks if there are {@link DependencyGroup}s where just one {@link Cell} can hold a value.
 * The missing value is the solution of this {@link Cell} then. The dependency groups have to use standard 
 * ruleset to apply this strategy.
 */
public class NakedSingleStrategy extends SolverStrategy {
	
	private static final long serialVersionUID = 7179356384165654525L;

	/**
	 * Executes the strategy on the current state
	 * @param currentState The current solution state
	 * @return List of SolveSteps calculated by the strategy
	 * @throws NotSolvableException if the strategy detects that the current field is not solvable
	 * @throws IllegalArgumentException if the given state is <code>null</code>
	 */
	public List<SolveStep> executeStrategy(SolverState currentState) throws IllegalArgumentException, NotSolvableException {
		
		if (currentState == null) {
			throw new IllegalArgumentException("given SolverState cannot be null.");
		}
		
		//strategy needs standard ruleset to solve cells or rather reduce candidates
		if (!StandardRulesetFactory.getInstance().isStandardRuleset(currentState.getDependencyManager())) {
			return new LinkedList<SolveStep>();
		}
		
		List<Cell> solvedCells = new LinkedList<Cell>();
		List<SolveStep> result = new LinkedList<SolveStep>();
		
		//iterate through the dependency groups and find cells that can hold just one value						
		for (DependencyGroup dependencyGroup : currentState.getDependencyManager().getDependencyGroups()) {
					
			//get all cells of the group that are not set or solved yet
			List<Cell> unsetCells = this.getUnsetCells(currentState, dependencyGroup, solvedCells);
					
			if (unsetCells.size() == 1) {
				
				//get the cell and its notes
				Cell unsetCell = unsetCells.get(0);
				List<Integer> cellNotes = currentState.getNoteManager().getNotes(unsetCell);
				if (cellNotes.size() != 1) throw new NotSolvableException();		
				int cellValue = cellNotes.get(0);
				
				//delete solution from all DependencyGroups of the solved Cell
				Boolean notesChanged = this.removeValueFromNeighbourNotes(unsetCell, cellValue, currentState);
				
				//add solution to result list
				result.add(new SolveStep(unsetCell, cellValue, notesChanged));
				
				//save solved cell
				solvedCells.add(unsetCell);
				
			}
			
		}
		
		return result;
				
	}
	
	/**
	 * Initialises a new instance of the {@link NakedSingleStrategy} class
	 */
	public NakedSingleStrategy() {
		super();
	}

	/**
	 * Initialises a new instance of the {@link NakedSingleStrategy} class
	 * @param strategyWeight the weight of the strategy
	 */
	public NakedSingleStrategy(int strategyWeight) {
		super(strategyWeight);
	}

}


