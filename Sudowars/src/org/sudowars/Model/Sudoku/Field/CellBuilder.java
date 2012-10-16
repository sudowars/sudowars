package org.sudowars.Model.Sudoku.Field;

/**
 * Defines a factory-interface for {@link Cell}s.
 */
public interface CellBuilder<T extends Cell> {
	
	/**
	 * Builds a new object instance implementing {@link Cell}
	 *
	 * @param index the {@link Cell}s future index.
	 *
	 * @return the built {@link Cell}
	 *
	 * @throws IllegalArgumentException if given index was <= {@code 0} and therefore illegal
	 * @see Cell#getIndex(int, int)
	 * @see FieldStructure#getIndex(int, int)
	 */
	public T buildCell(int index) throws IllegalArgumentException;
	
}
