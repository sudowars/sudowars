package org.sudowars.Model.Sudoku.Field;

import java.io.Serializable;

/**
 * {@link Cell} defines the minimal functionality the smallest data structure inside a {@link Sudoku} has to be able to.
 */
public interface Cell extends Serializable, Cloneable {
	
	/**
	 * Returns the {@link Cell}s value.
	 *
	 * @returns the {@link Cell}s value.
	 */
	public int getValue();

	/**
	 * Returns the {@link Cell}s index.
	 *
	 * @returns the {@link Cell}s index
	 * @see FieldStructure#getIndex(int, int)
	 */
	public int getIndex();
	
	/**
	 * Determines whether this {@link Cell} was set since creation.
	 *
	 * @returns <code>true</code>, if the execution was successfully
	 */
	public boolean isInitial();

	/**
	 * Determines whether the {@link Cell} contains a legal value or not.
	 *
	 * @return <code>true</code>, if the execution was successfully
	 */
	public boolean isSet();
	
	/**
	 * Returns a copy of the calling {@link Cell}
	 * 
	 * @return a copy of the calling {@link Cell}
	 */
	public Object clone();
	
}
