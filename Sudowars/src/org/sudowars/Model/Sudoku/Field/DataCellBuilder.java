package org.sudowars.Model.Sudoku.Field;

/**
 * Defines and implements a factory for not initial {@link DataCell}s.
 */
public class DataCellBuilder implements CellBuilder<DataCell> {
	
	/**
	 * Builds a {@link DataCell} with the given index and initial state {@code false}.
	 *
	 * @param index the {@link DataCell}s future index.
	 *
	 * @return the new {@link DataCell} instance
	 *
	 * @throws IllegalArgumentException if given index was < <code>0</code>
	 * @see CellBuilder#buildCell(int) 
	 */
	public DataCell buildCell(int index) throws IllegalArgumentException {
		return new DataCell(index, false);
	}
	
}
