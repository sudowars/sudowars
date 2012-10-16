package org.sudowars.Controller.Local;

import java.io.Serializable;

/**
 * This class saves the settings for the multiplayer game to transferred via bluetooth
 */
public class MultiplayerSudokuSettings implements Serializable {
	/**
	 * the Constant serialVersionUID
	 */
	private static final long serialVersionUID = -4276264869577864044L;
	
	/**
	 * The size of the Sudoku field
	 */
	private int size;
	
	/**
	 * The difficulty of the Sudoku game
	 */
	private int difficulty;
	
	/**
	 * Status, if a game is continue or new
	 */
	private boolean isNewGame;

	/**
	 * Constructs a new {@link MultiplayerSudokuSettings}-Object.
	 */
	public MultiplayerSudokuSettings() {
		this.size = 0;
		this.difficulty = 1;
		this.isNewGame = true;
	}

	/**
	 * Sets the difficulty of the Sudoku game
	 *
	 * @param difficulty the difficulty
	 */
	public void setDifficulty (int difficulty) {
		this.difficulty = difficulty;
	}

	/**
	 * Returns the difficulty of the Sudoku game
	 *
	 * @return the difficulty of the Sudoku game
	 */
	public int getDifficulty () {
		return this.difficulty;
	}

	/**
	 * Sets the size of the Sudoku field
	 *
	 * @param size the size
	 */
	public void setSize (int size) {
		this.size = size;
	}

	/**
	 * Returns the size of the Sudoku field
	 *
	 * @return the size of the Sudoku field
	 */
	public int getSize () {
		return this.size;
	}

	/**
	 * true if is new game is started. If this stae is set to true, it's unchangeable
	 *
	 * @param isNewGame the new checks if is new game
	 */
	public void setIsNewGame(boolean isNewGame) {
		this.isNewGame = isNewGame;
	}
	
	/**
	 * Checks if this is new game.
	 *
	 * @return true, if this is new game
	 */
	public boolean isNewGame() {
		return this.isNewGame;
	}
	
	/**
	 * Sets the settings
	 * 
	 * @param size the size
	 * @param difficulty the difficulty
	 */
	public void setSettings(int size, int difficulty) {
		this.setSize(size);
		this.setDifficulty(difficulty);
	}
	
	/**
	 * Sets the settings
	 * 
	 * @param size the size
	 * @param difficulty the difficulty
	 * @param isNewGame the new checks if is new game
	 */
	public void setSettings(int size, int difficulty, boolean isNewGame) {
		this.setSettings(size, difficulty);
		this.setIsNewGame(isNewGame);
	}
}
