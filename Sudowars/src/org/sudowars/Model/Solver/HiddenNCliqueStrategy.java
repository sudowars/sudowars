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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.sudowars.Model.Sudoku.Field.Cell;
import org.sudowars.Model.Sudoku.RuleManagement.DependencyGroup;
import org.sudowars.Model.Sudoku.RuleManagement.RuleApplier.StandardRulesetFactory;

/**
 * This class defines the "hidden n-Clique"-strategy to solve the next {@link Cell} of a {@link Field}. 
 * 
 * This strategy searches for {@link Cell}s within a {@link DependencyGroup} which hold the same group of 
 * candidates. If there are <code>n</code> {@link Cell}s that have in total the same <code>n</code> candidates 
 * the values can only be found in one of these {@link Cell}s, the candidates can be removed from the other 
 * {@link Cell}s. The dependency groups have to use standard ruleset to apply this strategy.
 */
public class HiddenNCliqueStrategy extends SolverStrategy {
	
	private static final long serialVersionUID = 6417133206019328525L;

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
		
		//iterate through the dependency groups and find cells that hold the same group of candidates				
		for (DependencyGroup dependencyGroup : currentState.getDependencyManager().getDependencyGroups()) {
		
			//get all cells of the group with a candidate list
			List<Cell> cellList = this.getUnsetCells(currentState, dependencyGroup);
			
			//get all used candidates within the group
			List<Integer> usedCandidates = this.getCandidateList(dependencyGroup, currentState);
			
			//initiate position list for all candidates
			HashMap<Integer, List<Integer>> candidatePositions = new HashMap<Integer, List<Integer>>();
			for (Integer candidate : usedCandidates) candidatePositions.put(candidate, new LinkedList<Integer>());
			
			//iterate through all cells and save candidates' positions
			for (int position = 0; position < cellList.size(); position++) {
				for (Integer candidate : currentState.getNoteManager().getNotes(cellList.get(position))) {
					candidatePositions.get(candidate).add(position);
				}
			}
			
			//generate all subsets of the used positions
			List<List<Integer>> subsets = this.getSubsets(cellList.size()-1);
							
			//search for n position lists that contain all n positions of the subset
			for (List<Integer> subset : subsets) {
				
				//just use subsets with at least 2 positions
				if (subset.size() < 2) continue;
				
				//initiate the list to save the clique candidates
				List<Integer> cliqueCandidateList = new LinkedList<Integer>();
				
				//find candidates which positions contains only the positions of the subset
				for (Integer candidate : usedCandidates) {
					if (this.isSubset(candidatePositions.get(candidate), subset)) {
						cliqueCandidateList.add(candidate);
					}
				}
				
				//if there are as much clique elements as positions in the subset the candidates are within theses positions.
				//The other candidates at the same position can be removed.
				if (cliqueCandidateList.size() == subset.size()) {
					
					for (Integer candidate : usedCandidates) {
						if (cliqueCandidateList.contains(candidate)) continue;
						for (Integer position : candidatePositions.get(candidate)) {
							if (subset.contains(position)) {
								notesChanged = currentState.getNoteManager().removeNote(cellList.get(position), candidate) ? true : notesChanged;
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
	public HiddenNCliqueStrategy() {
		super();
	}
	
	/**
	 * Initialises a new instance of the {@link HiddenNCliqueStrategy} class
	 * @param strategyWeight the weight of the strategy
	 */
	public HiddenNCliqueStrategy(int strategyWeight) {
		super(strategyWeight);
	}

}


