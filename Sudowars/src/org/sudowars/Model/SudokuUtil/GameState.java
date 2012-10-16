package org.sudowars.Model.SudokuUtil;

import java.io.Serializable;

import org.sudowars.Model.Difficulty.Difficulty;
import org.sudowars.Model.Game.Game;
import org.sudowars.Model.Sudoku.Field.FieldStructure;

/**
 * This class encapsulates a snapshot of a running game.
 */
public class GameState implements Serializable {

	private static final long serialVersionUID = 1516464618042317279L;
	private final Game game;
	private final Difficulty difficulty;
	private boolean isFinished;
	
	/**
	 * Initializes a new instance of the {@link GameState} class.
	 * @param game The running game.
	 * @param difficulty The difficulty of the given game.
	 * @throws IllegalArgumentException if one of the given params was {@code null}
	 */
	public GameState(Game game, Difficulty difficulty) throws IllegalArgumentException {
		if (game == null || difficulty == null) {
			throw new IllegalArgumentException("invalid parameter (null) given.");
		}
		this.game = game;
		this.difficulty = difficulty;
		this.isFinished = false;
	}
	
	/**
	 * Gets the game.
	 * @return The {@link Game}.
	 */
	public Game getGame() {
		return this.game;
	}
	
	/**
	 * Gets the underlying {@link FieldStructure} of the game.
	 * @return The field structure.
	 */
	public FieldStructure getFieldStructure() {
		return this.game.getSudoku().getField().getStructure();
	}
	
	/**
	 * Gets the {@link Difficulty} of the game.
	 * @return The difficulty.
	 */
	public Difficulty getDifficulty() {
		return this.difficulty;
	}
	
	/**
	 * Marks the game as finished.
	 */
	public void gameFinished() {
		this.isFinished = true;
	}
	
	/**
	 * Indicates if the game is finished.
	 * @return {@code true} if the game is finished, otherwise {@code false}
	 * @see GameState#gameFinished()
	 */
	public boolean isFinished() {
		return this.isFinished;
	}
}
