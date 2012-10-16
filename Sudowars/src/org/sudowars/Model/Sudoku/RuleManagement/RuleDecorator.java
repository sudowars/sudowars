package org.sudowars.Model.Sudoku.RuleManagement;

import java.util.List;

import org.sudowars.Model.Sudoku.Field.Cell;
import org.sudowars.Model.Sudoku.Field.Field;

/**
 * The class {@link RuleDecorator} is used for building concrete RuleDecorators.
 * These classes filter the results of {@link Rule#getValidValues(Field<Cell>, DependencyGroup, Cell)} 
 * of the given decorated {@link Rule}.
 */
public abstract class RuleDecorator implements Rule {

	/**
	 * Serial version UID for serialization
	 */
	private static final long serialVersionUID = 1L;
	
	private Rule baseRule;
	
	/**
	 * Constructs a new instance with the given {@link Rule} to decorate.
	 * @param baseRule the {@link Rule} to be decorated.
	 */
	public RuleDecorator(Rule baseRule) {
		if (baseRule == null) {
			throw new IllegalArgumentException();
		}
		this.baseRule = baseRule;
	}
	
	/**
	 * Returns the {@link Rule} decorated by this instance.
	 * @return the {@link Rule} decorated by this instance.
	 */
	protected Rule getDecoratedRule() {
		return baseRule;
	}
	
	/**
	 * @see {@link Rule#getValidValues(Field, DependencyGroup, Cell)}
	 */
	@Override
	public abstract List<Integer> getValidValues(Field<Cell> field, DependencyGroup group, Cell cell);

}
