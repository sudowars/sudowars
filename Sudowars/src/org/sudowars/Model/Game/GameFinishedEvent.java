package org.sudowars.Model.Game;

/**
 * Class to hold the event data of the GameSuccessfullyFinish event.
 */
public class GameFinishedEvent {

	private final Player winner;
	private final Game game;
	
	/**
	 * Initializes a new instance of the {@link GameFinishedEvent} class.
	 * @param game the event source
	 * @param winner Reference to the {@link Player} who won the given game.
	 * @throws IllegalArgumentException if one of the given params was {@code null}
	 */
	GameFinishedEvent(Game source, Player winner) throws IllegalArgumentException {
		if (winner == null || source == null) {
			throw new IllegalArgumentException("null is not an option.");
		}
		this.winner = winner;
		this.game = source;
	}

	/**
	 * Gets the winner of the game.
	 * @return Reference to the {@link Player} who won the game.
	 */
	public Player getWinner() {
		return this.winner;
	}
	
	/**
	 * Gets the source of the event.
	 * @return The game which triggered the event.
	 */
	public Game getGame() {
		return this.game;
	}
}
