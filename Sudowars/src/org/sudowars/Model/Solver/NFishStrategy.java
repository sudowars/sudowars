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

import org.sudowars.DebugHelper;
import org.sudowars.Model.Sudoku.Field.Cell;
import org.sudowars.Model.Sudoku.Field.Field;
import org.sudowars.Model.Sudoku.Field.SquareStructure;
import org.sudowars.Model.Sudoku.RuleManagement.DependencyGroup;
import org.sudowars.Model.Sudoku.RuleManagement.RuleApplier.StandardRulesetFactory;

/**
 * This class defines the "n-Fish"-strategy to solve the next {@link Cell} of a {@link Field}. 
 * 
 * This strategy reduce the candidate lists by checking if a candidate of a {@link DependencyGroup} 
 * also can be found at the same positions in a parallel {@link DependencyGroup}. If such a candidate 
 * exists the value could only be found in one of this cells.
 */
public class NFishStrategy extends SolverStrategy {
		
	private static final long serialVersionUID = -8655943794410835173L;

	/**
	 * Executes the strategy on the current state
	 * @param currentState The current solution state
	 * @return List of SolveSteps calculated by the strategy
	 * @throws NotSolvableException if the strategy detects that the current field is not solvable
	 * @throws IllegalArgumentException if the given state is <code>null</code>
	 */
	public List<SolveStep> executeStrategy(SolverState currentState) throws IllegalArgumentException {
				
		DebugHelper.log(DebugHelper.PackageName.SolverStrategy, "*** Execute nFish strategy ***");
		
		if (currentState == null) {
			throw new IllegalArgumentException("given SolverState cannot be null.");
		}
		
		//strategy needs standard ruleset to solve cells or rather reduce candidates
		if (!StandardRulesetFactory.getInstance().isStandardRuleset(currentState.getDependencyManager())) {
			return new LinkedList<SolveStep>();
		}
		
		//strategy needs square structure to reduce candidate list
		if (!(currentState.getField().getStructure() instanceof SquareStructure)) {
			return new LinkedList<SolveStep>();
		}
		
		//TODO ensure that field is a "normal" sudoku field
		
		//initiate result
		List<SolveStep> result = new LinkedList<SolveStep>();
		Boolean notesChanged = false;
		
		//get current candidate list of the field
		List<Integer> candidateList = this.getCandidateList(currentState);
		DebugHelper.log(DebugHelper.PackageName.SolverStrategy, "total candidate list : " + DebugHelper.getListString(candidateList));
		
		//iterate through the candidate list and find columns/rows that hold the candidate at the same position
		for (Integer candidate : candidateList) {
			
			DebugHelper.log(DebugHelper.PackageName.SolverStrategy, "check candidate " + candidate);
			
			//initiate working variables
			List<Integer> possiblePositions;
			List<List<Integer>> subsets;
			
			//--- check columns ---------------------------------------------------------------------------------------------------------
			
			//initiate position list of the candidate in the columns
			HashMap<Integer, List<Integer>> candidateColPositions = new HashMap<Integer, List<Integer>>();
			possiblePositions = new LinkedList<Integer>();
			for (int colID = 0 ; colID < currentState.getField().getStructure().getWidth() ; colID++) {
				//initiate position list
				candidateColPositions.put(colID, new LinkedList<Integer>());
				//iterate through all cells of the columns and save candidate's positions
				for (int rowID = 0 ; rowID < currentState.getField().getStructure().getHeight() ; rowID++) {
					if (currentState.getNoteManager().hasNote(currentState.getField().getCell(colID,rowID), candidate)) {
						candidateColPositions.get(colID).add(rowID);
						if (!possiblePositions.contains(rowID)) possiblePositions.add(rowID);
					}
				}
			}
			
			DebugHelper.log(DebugHelper.PackageName.SolverStrategy, "colPositionLists of candidate " + candidate);
			for (int colID = 0 ; colID < currentState.getField().getStructure().getWidth() ; colID++) {
				DebugHelper.log(DebugHelper.PackageName.SolverStrategy, "--- col " + colID + ": " + DebugHelper.getListString(candidateColPositions.get(colID)));
			}
			DebugHelper.log(DebugHelper.PackageName.SolverStrategy, "possible positions = " + DebugHelper.getListString(possiblePositions));
			
			//generate all subsets of the used positions
			subsets = this.getSubsets(possiblePositions);
			
			//search for n position lists that contain all n positions of the subset
			for (List<Integer> subset : subsets) {
				
				//just use subsets with at least 2 positions
				if (subset.size() < 2 || subset.size() == possiblePositions.size()) continue;
				
				//initiate the list to save the columns that contains the subset
				List<Integer> colCandidateList = new LinkedList<Integer>();
				
				//find position lists that only contains the positions of the subset
				for (int colID = 0 ; colID < currentState.getField().getStructure().getWidth() ; colID++) {	
					if (candidateColPositions.get(colID).size() > 0 && this.isSubset(candidateColPositions.get(colID), subset)) {
						colCandidateList.add(colID);
					}
				}
								
				//if there are as much columns as positions in the subset the candidate are within theses positions.
				//The candidates in the rows of the same position can be removed.
				if (colCandidateList.size() == subset.size()) {
					
					DebugHelper.log(DebugHelper.PackageName.SolverStrategy, "nFish found in columns " + DebugHelper.getListString(colCandidateList) + " (subset = " + DebugHelper.getListString(subset) + ")");
					
					for (Integer rowID : subset) {
						for (int colID = 0 ; colID < currentState.getField().getStructure().getWidth() ; colID++) {
							if (!colCandidateList.contains(colID)) {
								if (currentState.getNoteManager().hasNote(currentState.getField().getCell(colID, rowID), candidate))
									DebugHelper.log(DebugHelper.PackageName.SolverStrategy, "remove candidate " + candidate + " from (" + colID + "," + rowID + ")");
								notesChanged = currentState.getNoteManager().removeNote(currentState.getField().getCell(colID, rowID), candidate) ? true : notesChanged;
							}
						}
					}
					
				} else {
					
					//DebugHelper.log(DebugHelper.PackageName.SolverStrategy, "subset did not match: " + DebugHelper.getListString(subset));
					
				}

			}
			
			//--- check rows ---------------------------------------------------------------------------------------------------------			
			
			//initiate position list of the candidate in the rows
			HashMap<Integer, List<Integer>> candidateRowPositions = new HashMap<Integer, List<Integer>>();
			possiblePositions = new LinkedList<Integer>();
			for (int rowID = 0 ; rowID < currentState.getField().getStructure().getHeight() ; rowID++) {
				//initiate position list
				candidateRowPositions.put(rowID, new LinkedList<Integer>());
				//iterate through all cells of the row and save candidate's positions
				for (int colID = 0 ; colID < currentState.getField().getStructure().getWidth() ; colID++) {
					if (currentState.getNoteManager().hasNote(currentState.getField().getCell(colID,rowID), candidate)) {
						candidateRowPositions.get(rowID).add(colID);
						if (!possiblePositions.contains(colID)) possiblePositions.add(colID);
					}
				}
			}
			
			DebugHelper.log(DebugHelper.PackageName.SolverStrategy, "rowPositionLists of candidate " + candidate);
			for (int rowID = 0 ; rowID < currentState.getField().getStructure().getHeight() ; rowID++) {
				DebugHelper.log(DebugHelper.PackageName.SolverStrategy, "--- row " + rowID + ": " + DebugHelper.getListString(candidateRowPositions.get(rowID)));
			}
			DebugHelper.log(DebugHelper.PackageName.SolverStrategy, "possible positions = " + DebugHelper.getListString(possiblePositions));
			
			//generate all subsets of the used positions
			subsets = this.getSubsets(possiblePositions);
			
			//search for n position lists that contain all n positions of the subset
			for (List<Integer> subset : subsets) {
				
				//just use subsets with at least 2 positions
				if (subset.size() < 2 || subset.size() == possiblePositions.size()) continue;
				
				//initiate the list to save the rows that contains the subset
				List<Integer> rowCandidateList = new LinkedList<Integer>();
				
				//find position lists that only contains the positions of the subset
				for (int rowID = 0 ; rowID < currentState.getField().getStructure().getWidth() ; rowID++) {	
					if (candidateRowPositions.get(rowID).size() > 0 && this.isSubset(candidateRowPositions.get(rowID), subset)) {
						rowCandidateList.add(rowID);
					}
				}
								
				//if there are as much rows as positions in the subset the candidate are within theses positions.
				//The candidates in the rows of the same position can be removed.
				if (rowCandidateList.size() == subset.size()) {
					
					DebugHelper.log(DebugHelper.PackageName.SolverStrategy, "nFish found in row " + DebugHelper.getListString(rowCandidateList) + " (subset = " + DebugHelper.getListString(subset) + ")");
					
					for (Integer colID : subset) {
						for (int rowID = 0 ; rowID < currentState.getField().getStructure().getHeight() ; rowID++) {
							if (!rowCandidateList.contains(rowID)) {
								if (currentState.getNoteManager().hasNote(currentState.getField().getCell(colID, rowID), candidate))
									DebugHelper.log(DebugHelper.PackageName.SolverStrategy, "remove candidate " + candidate + " from (" + colID + "," + rowID + ")");
								notesChanged = currentState.getNoteManager().removeNote(currentState.getField().getCell(colID, rowID), candidate) ? true : notesChanged;
							}
						}
					}
					
				} else {
					
					//DebugHelper.log(DebugHelper.PackageName.SolverStrategy, "subset did not match: " + DebugHelper.getListString(subset));
					
				}

			}
			
		}
		
		//if notes were changed add solveStep to the result list
		if (notesChanged) {
			result.add(new SolveStep(notesChanged));
		}
		
		DebugHelper.log(DebugHelper.PackageName.SolverStrategy,  "*** Finished nFish strategy (notes " + ((result.size() == 0) ? "NOT " : "") + "changed) ***");
		
		return result;
		
	}
	
	/**
	 * Initialises a new instance of the {@link NFishStrategy} class
	 */
	public NFishStrategy() {
		super();
	}

	/**
	 * Initialises a new instance of the {@link NFishStrategy} class
	 * @param strategyWeight the weight of the strategy
	 */
	public NFishStrategy(int strategyWeight) {
		super(strategyWeight);
	}

}


