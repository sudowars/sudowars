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

import org.sudowars.Model.Sudoku.Field.Cell;

/**
 * This class defines a solution step of the {@link StrategyExecutor}. If a solution was found
 * it holds the solved {@link Cell}, its solution and the information if notes were changed.
 */
public class SolveStep implements Serializable {

	private static final long serialVersionUID = 4344624432671509197L;
	
	private Cell solvedCell;
	private boolean notesChanged;
	private int solution;
	
	/**
	 * Initialises a new instance of the {@link SolveStep} class
	 *
	 * @param cell Reference to the solved cell
	 * @param solution The calculated solution of the field
	 * @param notesChanged Defines if the notes were changed
	 * 
	 * @throws IllegalArgumentException given solution is smaller than zero
	 * 
	 */
	public SolveStep(Cell cell, int solution, boolean notesChanged) throws IllegalArgumentException {
		
		if (solution < 0) {
			throw new IllegalArgumentException("given solution can not be smaller than zero");
		}
		
		this.solvedCell = cell;
		this.notesChanged = notesChanged;
		this.solution = solution;
	}
	
	/**
	 * Initialises a new instance of the {@link SolveStep} class
	 * @param notesChanged Defines if the notes were changed
	 */
	public SolveStep(boolean notesChanged) {
		this.solvedCell = null;
		this.notesChanged = notesChanged;
		this.solution = 0;
	}
	
	/**
	 * Returns if the notes of one or more cells were changed.
	 * @return <code>true</code> if notes were changed, <code>false</code> otherwise
	 */
	public boolean hasChangedNotes() {
		return this.notesChanged;
	}
	
	/**
	 * Returns if the step found a new solution for a {@link Cell}
	 * @return <code>true</code> if a new solution was found, <code>false</code> otherwise
	 */
	public boolean hasSolvedCell() {
		return !(this.solvedCell == null);
	}
		
	/**
	 * Returns the solved cell.
	 * @return reference to a {@link Cell}, <code>null</code> if the step was not successful
	 */
	public Cell getSolvedCell() {
		return this.solvedCell;
	}
	
	/**
	 * Returns the solution of the solved cell.
	 * @return solution of the solved cell, <code>zero</code> if no cell was solved
	 */
	public int getSolution() {
		
		int result = 0;
		
		if (this.solvedCell != null) {
			result = (this.solution == 0) ? this.solvedCell.getValue() : this.solution;
		}
				
		return result;
		
	}
}


