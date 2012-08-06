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
