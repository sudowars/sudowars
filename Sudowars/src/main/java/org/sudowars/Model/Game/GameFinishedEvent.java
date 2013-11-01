/*
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
 */
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
