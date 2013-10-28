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

import org.sudowars.Model.Sudoku.Field.DataCell;
import org.sudowars.Model.Sudoku.Field.Field;
import org.sudowars.Model.Sudoku.RuleManagement.DependencyManager;

/**
 * The interface defines the functionality to solve a {@link Field} completely
 */
public interface Solver extends Serializable {

	/**
	 * Solves a {@link Field} of {@link DataCell}s and returns the fully filled solution-{@link Field}
	 *
	 * @param initialField The initial field to solve
	 * @param dependencyManager The dependency manager of the field
	 *
	 * @return the fully filled solution-{@link Field}, <code>null</code> if given field is not solvable
	 *
	 * @throws IllegalArgumentException if no field or no dependencyManager was given
	 */
	public Field<DataCell> solve(Field<DataCell> initialField, DependencyManager dependencyManager);
	
}


