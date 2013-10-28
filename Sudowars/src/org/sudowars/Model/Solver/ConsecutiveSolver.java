/*
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
 */
package org.sudowars.Model.Solver;

import java.io.Serializable;

/**
 * The interface defines the functionality of consecutive solvers
 */
public interface ConsecutiveSolver extends Serializable {
	
	/**
	 * Returns the {@link SolveStep} to solve the next {@link Cell}
	 *
	 * @param currentState the current field and notes of the solver
	 *
	 * @return {@link SolveStep} to solve the next {@link Cell}
	 */
	public SolveStep getCellToSolveNext(SolverState currentState);

}


