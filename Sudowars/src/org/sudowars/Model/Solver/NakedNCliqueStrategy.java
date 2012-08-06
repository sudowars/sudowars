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
import org.sudowars.Model.Sudoku.Field.Field;
import org.sudowars.Model.Sudoku.RuleManagement.DependencyGroup;
import org.sudowars.Model.Sudoku.RuleManagement.RuleApplier.StandardRulesetFactory;

/**
 * This class defines the "naked n-Clique"-strategy to solve the next {@link Cell} of a {@link Field}. 
 * 
 * This strategy searches for {@link Cell}s within a {@link DependencyGroup} which hold the same amount of 
 * candidates. If there are <code>n</code> {@link Cell}s that have in total the same <code>n</code> candidates 
 * the values can only be found in one of these {@link Cell}s, the candidates can be removed from the other 
 * {@link Cell}s. The dependency groups have to use standard ruleset to apply this strategy.
 */
public class NakedNCliqueStrategy extends SolverStrategy {
	
	private static final long serialVersionUID = -7808464980666550386L;

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
		
		//iterate through the dependency groups and find cells that hold identical candidate lists
		for (DependencyGroup dependencyGroup : currentState.getDependencyManager().getDependencyGroups()) {
		
			//get all cells of the group with a candidate list
			List<Cell> cellList = this.getUnsetCells(currentState, dependencyGroup);
			
			//get all used candidates within the group
			List<Integer> usedCandidates = this.getCandidateList(dependencyGroup, currentState);
			
			//generate all possible subsets of the used candidates
			List<List<Integer>> subsets = this.getSubsets(usedCandidates);
			
			//search for n cells that contain all n candidates of the subset
			for (List<Integer> subset : subsets) {
				
				//just use subsets with at least 2 and candidates
				if (subset.size() < 2) continue;
				
				//initiate the list of cells that hold the cells of the potential clique
				List<Cell> cliqueCellList = new LinkedList<Cell>();
				
				//iterate through the cells to find cells that only contain the candidates of the subset
				for (int i = 0; i < cellList.size(); i++) {
					Cell currentCell = cellList.get(i);
					List<Integer> cellCandidates = currentState.getNoteManager().getNotes(currentCell);
					if (this.isSubset(cellCandidates, subset)) {
						cliqueCellList.add(currentCell);
					}
				}
				
				//if there are as much cells as candidates the value are located within these fields.
				//the candidates of other fields with the same value can be removed.
				if (cliqueCellList.size() == subset.size()) {
										
					for (Cell cell : cellList) {
						if (!cliqueCellList.contains(cell)) {
							for (int candidateValue : subset) {
								notesChanged = currentState.getNoteManager().removeNote(cell, candidateValue) ? true : notesChanged;
							}
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
	 * Initialises a new instance of the {@link HiddenNCliqueStrategy} class
	 */
	public NakedNCliqueStrategy() {
		super();
	}
	
	/**
	 * Initialises a new instance of the {@link HiddenNCliqueStrategy} class
	 * @param strategyWeight the weight of the strategy
	 */
	public NakedNCliqueStrategy(int strategyWeight) {
		super(strategyWeight);
	}

}


