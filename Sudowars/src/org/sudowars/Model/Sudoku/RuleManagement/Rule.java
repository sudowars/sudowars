package org.sudowars.Model.Sudoku.RuleManagement;

import java.io.Serializable;
import java.util.List;

import org.sudowars.Model.Sudoku.Field.Cell;
import org.sudowars.Model.Sudoku.Field.Field;

/**
 * The interface {@link Rule} defines how a {@link Rule} is used.
 */
public interface Rule extends Serializable {
	
	/**
	 * Returns a {@link List} of values the {@link Rule} would accept to be held by this {@link Cell} in this {@link DependencyGroup}.
	 * By definition 0 is not contained by this {@link List}.
	 * 
	 * @param field the {@link Field} containing the {@link Cell} and the {@link Cell}s linked by the {@link DependencyGroup}
	 * @param group the {@link DependencyGroup} containing the indices used to retrieve the {@link Cell}s from the {@link Field}
	 * @param cell the {@link Cell} whose possible values are to be determined.
	 * @return a {@link List} of possible valid values for the given {@link Cell}
	 */
	public List<Integer> getValidValues(Field<Cell> field, DependencyGroup group, Cell cell);
}


