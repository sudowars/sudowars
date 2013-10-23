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
 * Contributors:
 * initial API and implementation:
 * Adrian Vielsack
 * Christof Urbaczek
 * Florian Rosenthal
 * Michael Hoff
 * Moritz Lüdecke
 * Philip Flohr 
 ******************************************************************************/
package org.sudowars.Model.Difficulty;

import java.util.List;

import org.sudowars.Model.Solver.SolverStrategy;
import org.sudowars.Model.Sudoku.Sudoku;
import org.sudowars.Model.Sudoku.Field.DataCell;

/**
 * Provides functionality to evaluate the {@link Difficulty} of a {@link Sudoku}. The
 * {@link Difficulty} will be calculated by interpreting the difficulty of the the 
 * strategies necessary to solve the given Sudoku.
 */
public abstract class DifficultyEvaluator {

	protected List<Difficulty> difficulties;
	
	/**
	 * Evaluates the {@link Difficulty} of a given {@link Sudoku}
	 *
	 * @param sudoku the {@link Sudoku} to evaluate
	 * @param usedStrategies list of the used strategies to solve the sudoku
	 * @return the {@link Difficulty} of the {@link Sudoku}, <code>null</code> if no difficulty could be found
	 *
	 * @throws IllegalArgumentException if the given sudoku was <code>null</code>
	 */
	public abstract Difficulty evaluateDifficulty(Sudoku<DataCell> sudoku, List<SolverStrategy> usedStrategies) throws IllegalArgumentException;
		
}


