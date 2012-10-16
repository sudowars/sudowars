package org.sudowars.Model.Game;


/**
 * Class to hold the event data of the GameAborted event.
 */
public final class GameAbortedEvent {

	private final Player abortingPlayer;
	private final Game game;
	
	/**
	 * Initializes a new instance of the {@link GameAbortedEvent} class.
	 * @param game the event source
	 * @param abortingPlayer Reference to the {@link Player} who aborted the given game.
	 * @throws IllegalArgumentException if one of the given params was {@code null}
	 */
	GameAbortedEvent(Game game, Player abortingPlayer) throws IllegalArgumentException {
		if (abortingPlayer == null || game == null) {
			throw new IllegalArgumentException("null is not an option.");
		}
		this.abortingPlayer = abortingPlayer;
		this.game = game;
	}

	/**
	 * Gets the player who aborted the game.
	 * @return Reference to the {@link Player} who aborted the game.
	 */
	public Player getAbortingPlayer() {
		return this.abortingPlayer;
	}
	
	/**
	 * Gets the source of the event.
	 * @return The game which triggered the event.
	 */
	public Game getGame() {
		return this.game;
	}
}
