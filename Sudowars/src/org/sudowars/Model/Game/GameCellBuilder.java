package org.sudowars.Model.Game;

import org.sudowars.Model.Sudoku.Field.Cell;
import org.sudowars.Model.Sudoku.Field.CellBuilder;
import org.sudowars.Model.Sudoku.Field.Field;

/**
 * Defines and implements a factory for {@link GameCell}s.
 */
public class GameCellBuilder implements CellBuilder<GameCell> {
	
	private final Field<Cell> field;
		
	/**
	 * Initializes a new instance of the {@link GameCellBuilder} class with to decorate the cells<br>
	 * of a given {@link Field}.
	 * @param field reference to a {@link Field} to relate.
	 * @throws IllegalArgumentException if the given field was {@code null}
	 * @see GameCell
	 */
	GameCellBuilder(Field<Cell> field) throws IllegalArgumentException {
		if (field == null) {
			throw new IllegalArgumentException("given field cannot be null.");
		}
		this.field = field;
	}
	
	/**
	 * Builds a {@link GameCell} with the given index.
	 *
	 * @param index the {@link GameCell}s future index.
	 *
	 * @return the new {@link GameCell} instance
	 *
	 * @throws IllegalArgumentException if given index was <= <code>0</code>
	 * @see CellBuilder#buildCell(int) 
	 */
	public GameCell buildCell(int index) throws IllegalArgumentException {
		if (index < 0) {
			throw new IllegalArgumentException("given cell index must be >= 0.");
		}
		GameCell result = null;
		Cell core = this.field.getCell(index);
		if (core != null) {
			result = new GameCell(core);
		}
		return result;
	}
}
