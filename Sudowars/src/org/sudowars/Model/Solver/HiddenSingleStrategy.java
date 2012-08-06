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
 * This class defines the "hidden-single"-strategy to solve the next {@link Cell} of a {@link Field}. 
 * 
 * The strategy checks if there are {@link DependencyGroup}s where just one {@link Cell} can hold a
 * specific value. As it is the only possible {@link Cell} to hold that value it is its solution.
 * The dependency groups have to use standard ruleset to apply this strategy.
 */
public class HiddenSingleStrategy extends SolverStrategy {

	private static final long serialVersionUID = -4317976003170134645L;

	/**
	 * Executes the strategy on the current state
	 * @param currentState The current solution state
	 * @return List of SolveSteps calculated by the strategy
	 * @throws NotSolvableException if the strategy detects that the current field is not solvable
	 * @throws IllegalArgumentException if the given state is <code>null</code>
	 */
	public List<SolveStep> executeStrategy(SolverState currentState) throws IllegalArgumentException {
		
		if (currentState == null) {
			throw new IllegalArgumentException("given SolverState cannot be null.");
		}
		
		//strategy needs standard ruleset to solve cells or rather reduce candidates
		if (!StandardRulesetFactory.getInstance().isStandardRuleset(currentState.getDependencyManager())) {
			return new LinkedList<SolveStep>();
		}
		
		//initiate result
		List<SolveStep> result = new LinkedList<SolveStep>();
		List<Cell> solvedCells = new LinkedList<Cell>();
		
		//iterate through the dependency groups and find cells that hold a candidate which no other cell do			
		for (DependencyGroup dependencyGroup : currentState.getDependencyManager().getDependencyGroups()) {
						
			//get all candidates that are used within the DependencyGroup
			List<Integer> candidateList = this.getCandidateList(dependencyGroup, currentState);
			 
			for (int candidateValue : candidateList) {
				
				//get all cells that use the value in their candidate list
				List<Cell> candidateCells = this.getCandidateCells(candidateValue, dependencyGroup, currentState);
				assert (candidateCells != null) && (candidateCells.size() > 0);
				
				//if there is only one cell that uses the value as a candidate it is the solution of the cell
				if (candidateCells.size() == 1) {
					//get cell and check if it is already solved by the strategy
					Cell solvedCell = candidateCells.get(0);
					if (solvedCells.contains(solvedCell)) continue;
					//delete solution from all DependencyGroups of the solved Cell
					Boolean notesChanged = this.removeValueFromNeighbourNotes(solvedCell, candidateValue, currentState);
					//add SolveStep to the result list
					result.add(new SolveStep(solvedCell, candidateValue, notesChanged));
				}
			}
			
		}
		
		return result;
		
	}

	/**
	 * Initialises a new instance of the {@link HiddenSingleStrategy} class
	 */
	public HiddenSingleStrategy() {
		super();
	}
	
	/**
	 * Initialises a new instance of the {@link HiddenSingleStrategy} class
	 * @param strategyWeight the weight of the strategy
	 */
	public HiddenSingleStrategy(int strategyWeight) {
		super(strategyWeight);
	}

}


