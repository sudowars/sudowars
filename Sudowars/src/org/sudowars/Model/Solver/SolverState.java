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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.sudowars.Model.Sudoku.Field.Cell;
import org.sudowars.Model.Sudoku.Field.Field;
import org.sudowars.Model.Sudoku.RuleManagement.DependencyGroup;
import org.sudowars.Model.Sudoku.RuleManagement.DependencyManager;
import org.sudowars.Model.SudokuUtil.NoteManager;

/**
 * This class defines the state of the {@link StrategyExecutor}. It holds all necessary information so that the 
 * {@link StrategyExecutor} can solve the next cell of the given field. The class can also be used to save and 
 * restore a current state.  
 */
public class SolverState implements Serializable {

	private static final long serialVersionUID = -2949216104314821472L;

	protected Field<Cell> field;
	private DependencyManager dependencyManager;
	private NoteManager noteManager;
	private SolveStep lastSolveStep;
	
	/**
	 * Sets the field of the SolverState
	 * @param field the field to set
	 * @throws IllegalArgumentException thrown if given field is <code>null</code>
	 */
	public void setField(Field<Cell> field) throws IllegalArgumentException{
		
		if (field == null) {
			throw new IllegalArgumentException("Given Field is null");
		}
		
		this.field = field;
		
	}
	
	/**
	 * Sets the DependencyManager of the SolverState
	 * @param DependencyManager the DependencyManager to set
	 * @throws IllegalArgumentException thrown if given DependencyManager is <code>null</code>
	 */
	public void setDependencyManager(DependencyManager dependencyManager) throws IllegalArgumentException{
		
		if (dependencyManager == null) {
			throw new IllegalArgumentException("Given DependencyManager is null");
		}
		
		this.dependencyManager = dependencyManager;
		
	}

	/**
	 * Sets the NoteManager of the SolverState
	 * @param NoteManager the NoteManager to set
	 * @throws IllegalArgumentException thrown if given NoteManager is <code>null</code>
	 */
	public void setNoteManager(NoteManager noteManager) throws IllegalArgumentException{
		
		if (noteManager == null) {
			throw new IllegalArgumentException("Given NoteManager is null");
		}
		
		this.noteManager = noteManager;
		
	}
	
	/**
	 * Generates a local instance of {@link NoteManager} and filter all possible values
	 */
	private void createNoteManager() {
		
		this.noteManager = new NoteManager();
		
		//iterate through all cells and search the candidates for the unsolved cells
		for (Cell cell : this.field.getCells()) {
		
			//set fields have no candidates and can be skipped
			if (cell.isSet()) continue;
			
			//collect all valid values of the cell
			List<List<Integer>> groupCandidates = new LinkedList<List<Integer>>();			
			for (DependencyGroup cellGroup : this.dependencyManager.getDependencyGroupsOfCell(cell)) {
				groupCandidates.add(cellGroup.getRule().getValidValues(this.field, cellGroup, cell));
			}
			
			//if there is no candidate for the cell it can not be solved
			if (groupCandidates.size() == 0) {
				//TODO Exception werfen?
				continue;
			}
			
			//candidates of the cell have to be found in all candidates lists
			List<Integer> cellCandidates = groupCandidates.get(0);
			for (int listIndex = 1; listIndex < groupCandidates.size(); listIndex++) {
				//compare the current and the group list and delete candidates that can not be found in both lists
				Iterator<Integer> it = cellCandidates.iterator();
				while (it.hasNext()) {
					if (!groupCandidates.get(listIndex).contains(it.next())) it.remove();
				}
			}
			
			//add candidates to NoteManager
			for (int candidate : cellCandidates) {
				this.noteManager.addNote(cell, candidate);
			}
			
		}
		
	}
	
	/**
	 * Initialises a new instance of the {@link SolverState} class.
	 *
	 * @param field The field to solve.
	 * @param dependencyManager The {@link DependencyManager} of the field.
	 *
	 * @throws IllegalArgumentException is thrown if at least one of the given 
	 * 				arguments is <code>null</code>
	 * 
	 */
	public SolverState(Field<Cell> field, DependencyManager dependencyManager) throws IllegalArgumentException {
				
		this.setField(field);
		this.setDependencyManager(dependencyManager);
		this.createNoteManager();
		
	}
	
	/**
	 * Returns the current {@link Field}.
	 * @return The current {@link Field}.
	 */
	public Field<Cell> getField() {
		return this.field;
	}

	/**
	 * Returns the {@link DependencyManager} of the current {@link Field}.
	 * @return The {@link DependencyManager} of the current {@link Field}.
	 */
	public DependencyManager getDependencyManager() {
		return this.dependencyManager;
	}

	/**
	 * Returns the current {@link NoteManager} of the {@link ConsecutiveSolver}.
	 * @return The current {@link NoteManager} of the {@link ConsecutiveSolver}.
	 */
	public NoteManager getNoteManager() {
		return this.noteManager;
	}

	/**
	 * Returns the last {@link SolveStep}.
	 * @return The last {@link SolveStep}, or <code>null</code> if there is no last step.
	 * @see SolverState#setLastSolveStep(SolveStep) 
	 */
	public SolveStep getLastSolveStep() {
		return this.lastSolveStep;
	}
	
	/**
	 * Sets the last {@link SolveStep}.
	 * @param solveStep The last {@link SolveStep}.
	 * @throws IllegalArgumentException if given solve step is <code>null</code>.
	 */
	public boolean setLastSolveStep(SolveStep solveStep) throws IllegalArgumentException {
		this.lastSolveStep = solveStep;
		return true;
	}
	
	/**
	 * Returns a copy of the state
	 * @return a copy of the state
	 */
	public SolverState clone() {
		
		//clone field
		Field<Cell> newField = this.field.clone();
		
		//create new SolverState
		SolverState newSolverState = new SolverState(newField, this.dependencyManager);
		
		//clone NoteManager
		NoteManager newNoteManager = newSolverState.getNoteManager();
		for (Cell cell : this.field.getCells()) {
			//remove all notes from the new NoteManager
			newNoteManager.removeAllNotes(newField.getCell(cell.getIndex()));
			//copy notes from old NoteManager
			if (this.noteManager.hasNotes(cell)) {
				for (int candidate : this.noteManager.getNotes(cell)) {
					newNoteManager.addNote(newField.getCell(cell.getIndex()), candidate);
				}
			}
		}
	
		//return new state
		return newSolverState;
		
	}
	
	/**
	 * Compares this instance with the specified object and indicates if they are equal.
	 * @param obj the object to compare this instance with.
	 * @return <code>true</code> if the specified object is equal to this object, <code>false</code> otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SolverState other = (SolverState) obj;
		if (dependencyManager == null) {
			if (other.dependencyManager != null)
				return false;
		} else if (!dependencyManager.equals(other.dependencyManager))
			return false;
		if (field == null) {
			if (other.field != null)
				return false;
		} else if (!field.equals(other.field))
			return false;
		if (lastSolveStep == null) {
			if (other.lastSolveStep != null)
				return false;
		} else if (!lastSolveStep.equals(other.lastSolveStep))
			return false;
		if (noteManager == null) {
			if (other.noteManager != null)
				return false;
		} else if (!noteManager.equals(other.noteManager))
			return false;
		return true;
	}	
	
	
}

