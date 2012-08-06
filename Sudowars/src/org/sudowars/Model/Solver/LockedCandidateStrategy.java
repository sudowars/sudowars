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
import org.sudowars.Model.Sudoku.RuleManagement.DependencyManager;
import org.sudowars.Model.Sudoku.RuleManagement.RuleApplier.StandardRulesetFactory;

/**
 * This class defines the "locked-candidate"-strategy to solve the next {@link Cell} of a {@link Field}.
 * 
 * The strategy checks if there is a value that can be "locked" in a specific {@link DependencyGroup} e.g. if 
 * the nearby groups are already holding the value. The dependency groups have to use standard ruleset to apply 
 * this strategy.
 */
public class LockedCandidateStrategy extends SolverStrategy {
	
	private static final long serialVersionUID = -8138170225990765476L;

	/**
	 * Returns the group all given cells are located in except of the current one. Two (or more) cells can only
	 * be located in two common groups so there is a maximum of one common group except of the current one.
	 * @param cellList The cells to search for a common group
	 * @param currentGroup The currently reviewed group 
	 * @return The group all given cells are located in, <code>null</code> if there is no common group
	 */
	private DependencyGroup getCommonGroup(List<Cell> cellList, DependencyGroup currentGroup, DependencyManager dependencyManager) {
		
		DependencyGroup resultGroup = null;
		
		//load the common groups and remove current one
		List<DependencyGroup> commonGroups = this.getCommonGroups(cellList, dependencyManager);
		commonGroups.remove(currentGroup);
		
		//check if there is a common group left
		if (commonGroups.size() == 1) {
			resultGroup = commonGroups.get(0);
		}
		
		return resultGroup;
		
	}
	
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
		Boolean notesChanged = false;

		//iterate through the dependency groups and find cells that hold the same candidate and are located together in another group		
		for (DependencyGroup dependencyGroup : currentState.getDependencyManager().getDependencyGroups()) {
			
			//get all candidates that are used within the DependencyGroup
			List<Integer> candidateList = this.getCandidateList(dependencyGroup, currentState);
					
			for (int candidateValue : candidateList) {
				//get all cells that use the value in their candidate list
				List<Cell> candidateCells = this.getCandidateCells(candidateValue, dependencyGroup, currentState);
				//check if the cells are located in a single group
				DependencyGroup commonGroup = this.getCommonGroup(candidateCells, dependencyGroup, currentState.getDependencyManager());
				//if there is a common group the solution is located within the current group
				//and the candidates of the common group can be removed
				if (commonGroup != null) {					
					//delete candidates from other cells in the group
					for (Cell cell : commonGroup.getCells(currentState.getField())) {
						if (!candidateCells.contains(cell)) {
							notesChanged = currentState.getNoteManager().removeNote(cell, candidateValue) ? true : notesChanged;
						}
					}					
				}
			}
			
		}
		
		//if notes were changed add solveStep to the result list
		if (notesChanged) {
			result.add(new SolveStep(notesChanged));
		}
		
		return result;
		
	}
	
	/**
	 * Initialises a new instance of the {@link LockedCandidateStrategy} class
	 */
	public LockedCandidateStrategy() {
		super();
	}
	
	/**
	 * Initialises a new instance of the {@link LockedCandidateStrategy} class
	 * @param strategyWeight the weight of the strategy
	 */
	public LockedCandidateStrategy(int strategyWeight) {
		super(strategyWeight);
	}

}


