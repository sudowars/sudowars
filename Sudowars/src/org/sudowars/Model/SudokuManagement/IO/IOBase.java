/*******************************************************************************
 * Copyright (c) 2011 - 2012 Adrian Vielsack, Christof Urbaczek, Florian Rosenthal, Michael Hoff, Moritz Lüdecke, Philip Flohr.
 * 
 * This file is part of Sudowars.
 * 
 * Sudowars is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Sudowars is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Sudowars.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 * initial API and implementation:
 * Adrian Vielsack
 * Christof Urbaczek
 * Florian Rosenthal
 * Michael Hoff
 * Moritz Lüdecke
 * Philip Flohr 
 ******************************************************************************/
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


