package org.sudowars.Model.Game;

/**
 * Class to hold the event data of the GameChanged event.
 */
public class GameChangedEvent {
	
	private final Game game;
	private GameCell changedCell = null;
	
	/**
	 * Initializes a new instance of the {@link GameChangedEvent} class.
	 * @param game the event source
	 * @param changedCell Reference to the {@link GameCell} which changed, or {@code null} if no cell was affected.
	 * @throws IllegalArgumentException if the event source was {@code null}
	 */
	protected GameChangedEvent(Game source, GameCell changedCell) throws IllegalArgumentException {
		if (source == null) {
			throw new IllegalArgumentException("given game cannot be null.");
		}
		this.game = source;
		this.changedCell = changedCell;
	}
	
	/**
	 * Gets the {@link GameCell} which was affected.
	 * @return The affected cell, or {@code null} if not any was affected.
	 */
	public GameCell getChangedCell() {
		return this.changedCell;
	}
	
	/**
	 * Gets the source of the event.
	 * @return The game which triggered the event.
	 */
	public Game getGame() {
		return this.game;
	}
}
