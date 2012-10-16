package org.sudowars.Model.Sudoku.Field;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory for creating {@link Field}s.
 */
public class FieldBuilder<T extends Cell> {

	/**
	 * Creates a new {@link Field} based on the given {@link FieldStructure} filled with {@link Cell}s built by the given {@link CellBuilder}.
	 *
	 * @param structure {@link FieldStructure}-instance denoting the structure of the field
	 * @param cellBuilder {@link CellBuilder}-instance for creating the actual content, the {@link Cell}s.
	 *
	 * @return a {@link Field} inheriting the given {@link FieldStructure},<br> which was used to built it and holding {@link Cell}s built by the given {@link CellBuilder}
	 *
	 * @throws IllegalArgumentException if at least one of the given arguments is <code>null</code>
	 * @see FieldStructure
	 * @see CellBuilder
	 * @see Cell
	 * @see Field
	 */
	public Field<T> build(FieldStructure structure, CellBuilder<T> cellBuilder) throws IllegalArgumentException {
		if (structure == null || cellBuilder == null) {
			throw new IllegalArgumentException();
		}
		
		List<T> cells = new ArrayList<T>(structure.getUsedSlotCount());
		
		int index = 0;
		for (int i = 0; i < structure.getWidth(); i++) {
			for (int j = 0; j < structure.getHeight(); j++) {
				if (structure.isSlotUsed(i, j)) {
					cells.add(cellBuilder.buildCell(index++));
				}
			}
		}
		
		return new Field<T>(cells, structure);
	}
}
