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

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.sudowars.Model.Sudoku.Field.Cell;
import org.sudowars.Model.Sudoku.RuleManagement.DependencyGroup;
import org.sudowars.Model.Sudoku.RuleManagement.DependencyManager;

/**
 * This class defines a strategy to solve the next {@link Cell}s of a {@link Field}. It holds a weight, so
 * the caller can identify the {@link Difficulty} of using this strategy. 
 */
public abstract class SolverStrategy implements Serializable {
	
	private static final long serialVersionUID = 534899926791496026L;
	
	private int strategyWeight;

	/**
	 * Returns the weight of the strategy to evaluate the {@link Difficulty}
	 * @return the weight of the strategy
	 */
	public int getStrategyWeight() {
		return this.strategyWeight;
	}
	
	/**
	 * Initialises a new instance of the {@link SolverStrategy} class
	 */
	public SolverStrategy() {
		this.strategyWeight = 0;
	}
	
	/**
	 * Initialises a new instance of the {@link SolverStrategy} class
	 * @param strategyWeight the weight of the strategy
	 */
	public SolverStrategy(int strategyWeight) {
		this.strategyWeight = strategyWeight;
	}
	
	/**
	 * Executes the strategy on the current state
	 * @param currentState The current solution state
	 * @return List of SolveSteps calculated by the strategy
	 * @throws NotSolvableException if the strategy detects that the current field is not solvable
	 * @throws IllegalArgumentException if the given state is <code>null</code>
	 */
	public abstract List<SolveStep> executeStrategy(SolverState currentState) throws NotSolvableException, IllegalArgumentException;
	
	/**
	 * Deletes the cell value from all candidate lists of cells that are in the same DependencyGroup.
	 * @param solvedCell The cell that holds the solutionValue
	 * @param solutionValue The set value
	 * @param currentState The current solution state
	 * @return <code>true</code> if notes in other fields was changed, <code>false</code> was not found in other cells
	 */
	protected Boolean removeValueFromNeighbourNotes(Cell solvedCell, int solutionValue, SolverState currentState) {
		
		Boolean result = false;
		
		for (DependencyGroup dependencyGroup : currentState.getDependencyManager().getDependencyGroupsOfCell(solvedCell)) {
			
			for (Cell neighbourCell : dependencyGroup.getCells(currentState.getField())) {
				if (!neighbourCell.equals(solvedCell)) {
					if (currentState.getNoteManager().removeNote(neighbourCell, solutionValue)) {
						result = true;
					}
				}
			}
			
		}
		
		return result;
		
	}
	
	/**
	 * Returns a list of all used candidates in a {@link DependencyGroup}
	 * @param dependencyGroup The {@link DependencyGroup} to search the candidates
	 * @param currentState The current solution state of the {@link Sudoku}
	 * @return List of the candidates
	 */
	protected List<Integer> getCandidateList(DependencyGroup dependencyGroup, SolverState currentState) {
		
		List<Integer> candidateList = new LinkedList<Integer>();
		
		//iterate through all cells of the group and save the candidates
		for (Cell cell : dependencyGroup.getCells(currentState.getField())) {
			if (cell.isSet()) continue;
			List<Integer> candidates = currentState.getNoteManager().getNotes(cell);
			for (Integer candidate : candidates) {
				if (!candidateList.contains(candidate)) {
					candidateList.add(candidate);
				}
			}
		}
		
		return candidateList;
		
	}
	
	/**
	 * Returns a list of all used candidates all over the field
	 * @param currentState The current solution state of the {@link Sudoku}
	 * @return List of the candidates
	 */
	protected List<Integer> getCandidateList(SolverState currentState) {
		
		List<Integer> candidateList = new LinkedList<Integer>();
		
		for (DependencyGroup dependencyGroup : currentState.getDependencyManager().getDependencyGroups()) {
			for (Integer candidate : this.getCandidateList(dependencyGroup, currentState)) {
				if (!candidateList.contains(candidate)) {
					candidateList.add(candidate);
				}
			}
		}
		
		return candidateList;
		
	}
	
	/**
	 * Returns all {@link Cell}s of a {@link DependencyGroup} that use the value in their candidate list
	 * @param value The value to search for
	 * @param dependencyGroup The {@link DependencyGroup} to search the value
	 * @param currentState The current solution state
	 * @return List of the cells that use the value in their candidate list
	 */
	protected List<Cell> getCandidateCells(int value, DependencyGroup dependencyGroup, SolverState currentState) {
		
		List<Cell> cellList = new LinkedList<Cell>();
		
		//iterate through all cells of the group and check if they use the value in their candidate list
		for (Cell cell : dependencyGroup.getCells(currentState.getField())) {
			if (currentState.getNoteManager().hasNote(cell, value)) {
				cellList.add(cell);
				continue;
			}
		}
		
		return cellList;
		
	}
	
	/**
	 * Returns all {@link Cell}s of a {@link DependencyGroup} that are not set or already solved
	 * @param currentState The current solution state
	 * @param dependencyGroup The {@link DependencyGroup} of the {@link Cell}s
	 * @param solvedCells list of cells already solved by the strategy
	 * @return List of all {@link Cell}s that are not set
	 */
	protected List<Cell> getUnsetCells(SolverState currentState, DependencyGroup dependencyGroup, List<Cell> solvedCells) {
		
		List<Cell> result = new LinkedList<Cell>();
		
		for (Cell groupCell : dependencyGroup.getCells(currentState.getField())) {
			if (groupCell.isSet() || solvedCells.contains(groupCell)) continue;
			result.add(groupCell);
		}
		
		return result;
		
	}
	
	/**
	 * Returns all {@link Cell}s of a {@link DependencyGroup} that are not set
	 * @param currentState The current solution state
	 * @param dependencyGroup The {@link DependencyGroup} of the {@link Cell}s
	 * @return List of all {@link Cell}s that are not set
	 */
	protected List<Cell> getUnsetCells(SolverState currentState, DependencyGroup dependencyGroup) {
		
		return this.getUnsetCells(currentState, dependencyGroup, new LinkedList<Cell>());
		
	}
	
	/**
	 * Clones the list
	 * @param listToClone list to clone
	 * @return the cloned list
	 */
	protected List<Integer> cloneList(List<Integer> listToClone) {
		
		List<Integer> resultList = new LinkedList<Integer>();
		for (int listValue : listToClone) {
			resultList.add(listValue);
		}
		return resultList;
		
	}
	
	/**
	 * Checks if the list is a subset of the given set
	 * @param subset the list to check
	 * @param set the set
	 * @return <code>true</code> if the set contains all elements of the subset, <code>false</code> otherwise 
	 */
	protected Boolean isSubset(List<Integer> subset, List<Integer> set) {
		
		Boolean isSubset = true;
		
		for (int value : subset) {
			if (!set.contains(value)) {
				isSubset = false;
				break;
			}
		}
		
		return isSubset;
		
	}
	
	/**
	 * Returns all subsets of the given set.
	 * @param set the set to build the subsets
	 * @return all subsets of the given set
	 */
	protected List<List<Integer>> getSubsets(List<Integer> set) {
		
		List<List<Integer>> result = new LinkedList<List<Integer>>();
		
		if (set.size() > 0) {
			
			List<Integer> newCandidates = this.cloneList(set);
			int currentElement = newCandidates.get(0);
			newCandidates.remove(0);

			result = getSubsets(newCandidates);
			List<List<Integer>> subsetsWithElement = new LinkedList<List<Integer>>();
			
			for (List<Integer> subset : result) {
				
				List<Integer> newSubset = this.cloneList(subset);
				newSubset.add(currentElement);
				subsetsWithElement.add(newSubset);
				
			}
			
			result.addAll(subsetsWithElement);
			
		} else {
			
			result.add(set);
			
		}	
		
		return result;
		
	}
	
	/**
	 * Returns all subsets of a set from <code>zero</code> to the given value
	 * @param maxValue the maximum value
	 * @return all subsets of a set from <code>zero</code> to the given value
	 */
	protected List<List<Integer>> getSubsets(int maxValue) {
		
		//create set
		List<Integer> set = new LinkedList<Integer>();
		for (int i = 0; i <= maxValue; i++) set.add(i);
		
		//return subsets
		return this.getSubsets(set);
		
	}
	
	/**
	 * Returns the groups all given cells are located in.
	 * @param cellList The cells to search for a common group
	 * @param dependencyManager The dependency manager of the field
	 * @return List of dependency groups all given cells are located in, empty list if there is no common group
	 */
	protected List<DependencyGroup> getCommonGroups(List<Cell> cellList, DependencyManager dependencyManager) {
		
		LinkedList<DependencyGroup> resultList = new LinkedList<DependencyGroup>();
		
		if (cellList.size() > 0) {
		
			//initialise possible groups by analysing the first cell of the list
			Cell firstCell = cellList.get(0);
			List<DependencyGroup> possibleGroups = dependencyManager.getDependencyGroupsOfCell(firstCell);
			
			//check if the other cells also located in one of the possible groups
			for (Cell cell : cellList) {
				if (!cell.equals(firstCell)) {
					//load groups of cell and initiate reduced group list to save further possible groups
					List<DependencyGroup> cellGroups = dependencyManager.getDependencyGroupsOfCell(cell);
					List<DependencyGroup> possibleGroupsReduced = new LinkedList<DependencyGroup>();
					//check if the cell's groups are within the currently possible groups 
					for (DependencyGroup possibleGroup : possibleGroups) {
						if (cellGroups.contains(possibleGroup)) {
							possibleGroupsReduced.add(possibleGroup);
						}
					}
					//save the reduced group list
					possibleGroups = possibleGroupsReduced;
					if (possibleGroups.size() == 0) break;
				}
			}
			
			//add the common group to result list
			resultList.addAll(possibleGroups);
			
		}
		
		return resultList;
		
	}

}
