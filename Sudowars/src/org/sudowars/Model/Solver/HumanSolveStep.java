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

import java.util.List;

import org.sudowars.Model.Sudoku.Field.Cell;

/**
 * This class defines a solution step of the {@link HumanSolver}. It extends {@link SolveStep} by the strategies 
 * used to solve the {@link Cell}.
 */
public class HumanSolveStep extends SolveStep {

	private static final long serialVersionUID = -2401288797609366533L;
	private List<SolverStrategy> strategies;
	
	/**
	 * Initialises a new instance of the {@link HumanSolveStep} class.
	 *
	 * @param cell A reference to the solvedCell.
	 * @param notesChanged Defines if the notes were changed 
	 * @param strategies The strategies used to solve the cell.
	 * 
	 * @throws IllegalArgumentException given solution is smaller than zero
	 * 
	 */
	public HumanSolveStep(Cell cell, int solution, boolean notesChanged, List<SolverStrategy> strategies) throws IllegalArgumentException {
		super(cell, solution, notesChanged);
		this.strategies = strategies;		
	}
	
	/**
	 * Returns the strategies used to solve the {@link Cell}.
	 *
	 * @return A reference to a {@link SolverStrategy}, <code>null</code> if the step was not successful.
	 */
	public List<SolverStrategy> getUsedStrategies(){
		return this.strategies;
	}
	
}


