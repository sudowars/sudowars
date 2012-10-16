package org.sudowars.Model.Sudoku.Field;

import java.io.Serializable;

/**
 * The interface {@link FieldStructure} defines how a 2 dimensional {@link Field} is structured. 
 */
public interface FieldStructure extends Serializable{
	
	/**
	 * Returns the width of the structure represented by {@link FieldStructure}.
	 *
	 * @return an integer value greater than 0.
	 */
	public int getWidth();
	
	/** 
	 * Returns the height of the structure represented by {@link FieldStructure}.
	 *
	 * @return an integer value greater than 0.
	 */
	public int getHeight();
	
	/**
	 * Determines whether the slot at row y and column x is used in this {@link FieldStructure}.
	 * 
	 * @param x horizontal position
	 * @param y vertical position
	 *
	 * @return <code>true</code> if the slot is used, <code>true</code> otherwise.
	 *
	 * @throws IllegalArgumentException if the position defined by x and y is not covered by this {@link FieldStructure}
	 */
	public boolean isSlotUsed(int x, int y) throws IllegalArgumentException;
	
	/**
	 * Translates the position given by row y and column x to an index.
	 * The returned index has the following properties:
	 * <ul>
	 *	<li>No 2 slots inside a {@link FieldStructure} have equals indices</li>
	 *	<li>Only used slots have indices</li>
	 *	<li>The value is bounded by 0 and {@link FieldStructure#getUsedSlotCount(int, int)}</li>
	 * </ul>
	 *
	 * @return a positive integer
	 *
	 * @throws IllegalArgumentException if the position defined by x and y is not covered by this {@link FieldStructure}
	 * @throws IllegalArgumentException if the position defined by x and y points to a unused slot inside this {@link FieldStructure}
	 */
	public int getIndex(int x, int y) throws IllegalArgumentException;
	
	/**
	 * Returns the amount of slots used in this {@link FieldStructure}.
	 *
	 * @return a positive integer
	 *
	 * @throws IllegalArgumentException if the position defined by x and y ist not covered by this {@link FieldStructure}
	 */
	public int getUsedSlotCount() throws IllegalArgumentException;
	
}
