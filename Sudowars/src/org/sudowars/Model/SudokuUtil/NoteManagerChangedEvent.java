package org.sudowars.Model.SudokuUtil;

import org.sudowars.Model.Sudoku.Field.Cell;

/**
 * This class holds the data of the onChange event of the {@link NoteManager}. 
 */
public final class NoteManagerChangedEvent {

	private final Cell cell;
	private final NoteManager source;
	
	/**
	 * Initializes a new instance of the {@link NoteManagerChangedEvent}.
	 * @param source the source of the event.
	 * @param affectedCell The {@link Cell} which was affected by the event.
	 * @throws IllegalArgumentException if one of the arguments was {@code null}
	 */
	NoteManagerChangedEvent(NoteManager source, Cell affectedCell) throws IllegalArgumentException {
		if (source == null || affectedCell == null) {
			throw new IllegalArgumentException("invalid argument given: null");
		}
		this.source = source;
		this.cell = affectedCell;
	}

	/**
	 * Gets the cell which was affected by the event.
	 * @return Reference to the {@link Cell}.
	 */
	public Cell getCell() {
		return this.cell;
	}

	/**
	 * Gets the source of the event.
	 * @return The note manager which triggered the event.
	 */
	public NoteManager getSource() {
		return this.source;
	}
}
