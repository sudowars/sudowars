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


