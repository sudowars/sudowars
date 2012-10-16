package org.sudowars.Model.SudokuManagement.IO;

import org.sudowars.Model.SudokuUtil.GameState;
import org.sudowars.Model.SudokuUtil.SingleplayerGameState;

/**
 * This interface provides functionality to save and load games.
 */
public interface IOBase {

	/**
	 * Saves the given singleplayer game and overwrites the last saved one.
	 *
	 * @param gameState {@link SingleplayerGameState} to save.
	 */
	public void saveSingleplayerGame(SingleplayerGameState gameState);
	
	/**
	 * Loads the last saved singleplayer game.
	 *
	 * @return A reference to a {@link SingleplayerGameState} or <code>null</code> if no game was found. 
	 */
	public SingleplayerGameState loadSingleplayerGame();

	/**
	 * Saves the given multiplayer game and overwrites the last saved one.
	 *
	 * @param gameState {@link GameState} to save.
	 */
	public void saveMultiplayerGame(GameState gameState);
	
	/**
	 * Loads the last saved multiplayer game.
	 *
	 * @return A reference to a {@link GameState} or <code>null</code> if no game was found. 
	 */
	public GameState loadMultiplayerGame();
}


