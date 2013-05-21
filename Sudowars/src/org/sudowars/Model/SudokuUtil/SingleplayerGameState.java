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
 * 
 * 
 * Diese Datei ist Teil von Sudowars.
 * 
 * Sudowars ist Freie Software: Sie können es unter den Bedingungen
 * der GNU General Public License, wie von der Free Software Foundation,
 * Version 3 der Lizenz oder (nach Ihrer Option) jeder späteren
 * veröffentlichten Version, weiterverbreiten und/oder modifizieren.
 * 
 * Sudowars wird in der Hoffnung, dass es nützlich sein wird, aber
 * OHNE JEDE GEWÄHELEISTUNG, bereitgestellt; sogar ohne die implizite
 * Gewährleistung der MARKTFÄHIGKEIT oder EIGNUNG FÜR EINEN BESTIMMTEN ZWECK.
 * Siehe die GNU General Public License für weitere Details.
 * 
 * Sie sollten eine Kopie der GNU General Public License zusammen mit diesem
 * Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>.
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
package org.sudowars.Model.SudokuUtil;

import java.io.Serializable;

import org.sudowars.Model.CommandManagement.DeltaManager;
import org.sudowars.Model.Difficulty.Difficulty;
import org.sudowars.Model.Game.SingleplayerGame;

/**
 * This class encapsulates a snapshot of a running {@link SingleplayerGame}.
 */
public class SingleplayerGameState extends GameState implements Serializable {
	
	private static final long serialVersionUID = 7807703339535102341L;
	private final boolean obviousMistakes;
	private final boolean solveCell;
	private final boolean bookmark;
	private final boolean backToFirstError;
	private final DeltaManager deltaManager;
	
	/**
	 * Initializes a new instance of the {@link SingleplayerGameState} class.
	 * @param game The running {@link SingleplayerGame}.
	 * @param difficulty The difficulty of the given game
	 * @param obviousMistakesShown Indicates if mistakes made by the player are displayed.
	 * @param solveCellAllowed Indicates the assistant can be triggered by the player to solve a cell. 
	 * @param bookmarkAllowed Indicates if the player can use the bookmark feature.
	 * @param deltaManager Reference to a {@link DeltaManager} containg all possible undo/redo operations.
	 * @throws IllegalArgumentException if either game, difficulty or deltaManager were {@code null}
	 */
	public SingleplayerGameState(SingleplayerGame game, Difficulty difficulty,
			boolean obviousMistakesShown, boolean solveCellAllowed, boolean bookmarkAllowed, boolean backToFirstError, DeltaManager deltaManager) 
					throws IllegalArgumentException{
		
		super(game, difficulty);
		if (deltaManager == null)  throw new IllegalArgumentException("deltaManager cannot be null.");
		
		this.obviousMistakes = obviousMistakesShown;
		this.solveCell = solveCellAllowed;
		this.bookmark = bookmarkAllowed;
		this.backToFirstError = backToFirstError;
		this.deltaManager = deltaManager;
	}
	
	/**
	 * Indicates if obvious mistakes are displayed by the application.
	 * @return {@code true} if so, otherwise {@code false}
	 */
	public boolean isShowObviousMistakesEnabled() {
		return this.obviousMistakes;
	}

	/**
	 * Indicates the assistant can be triggered by the player to solve a cell.
	 * @return {@code true} if so, otherwise {@code false}
	 */
	public boolean isSolveCellEnabled() {
		return this.solveCell;
	}
	
	/**
	 * Indicates if the player can use the bookmark feature.
	 * @return {@code true} if so, otherwise {@code false}
	 */
	public boolean isBookmarkEnabled() {
		return this.bookmark;
	}
	
	/**
	 * Gets the delta manager of the current game.
	 * @return Reference to the {@link DeltaManager} containg all possible undo/redo operations.
	 */
	public DeltaManager getDeltaManager() {
		return this.deltaManager;
	}
}
