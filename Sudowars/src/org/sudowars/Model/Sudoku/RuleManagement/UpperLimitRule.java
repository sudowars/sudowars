package org.sudowars.Model.Sudoku.RuleManagement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.sudowars.Model.Sudoku.Field.Cell;
import org.sudowars.Model.Sudoku.Field.Field;

/**
 * The class {@link UpperLimitRule} allows all values from 1 to a specified limit.
 */
public class UpperLimitRule implements Rule {
	
	/**
	 * Serial version UID for serialization
	 */
	private static final long serialVersionUID = -8736050171681078014L;
	
	private List<Integer> bufferedValidValueList;
	
	/**
	 * Constructs a new instance of {@link UpperLimitRule} with the given bound.
	 * This {@link Rule}-instance will allow every value from 1 to the given bound, including 1 and bound.
	 * @param upperLimit the given bound.
	 * @throws IllegalArgumentException if upperLimit is lower than {@code 1} 
	 */
	public UpperLimitRule(int upperLimit) {
		if (upperLimit < 1) {
			// if upperLimit is lower than 1...
			throw new IllegalArgumentException();
		}
		
		// as the List is constant, it is generated and stored on rule construction
		this.bufferedValidValueList = new ArrayList<Integer>(upperLimit);
		
		// bounds included
		for (int i = 1; i <= upperLimit; i++) {
			this.bufferedValidValueList.add(new Integer(i));
		}
		
		// forbid external modification
		this.bufferedValidValueList = Collections.unmodifiableList(this.bufferedValidValueList);
	}

	/**
	 * Returns all values from 1 to the bound given on {@link Rule}-construction (including bounds).
	 * @param field not needed => can be anything
	 * @param group not needed => can be anything
	 * @param cell not needed => can be anything
	 */
	@Override
	public List<Integer> getValidValues(Field<Cell> field, DependencyGroup group, Cell cell) {
		return this.bufferedValidValueList;
	}

}
