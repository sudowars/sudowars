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
package org.sudowars.Model.CommandManagement;

import java.io.Serializable;
import java.util.LinkedList;

import org.sudowars.Model.CommandManagement.GameCommands.GameCommand;
import org.sudowars.Model.Game.Game;
import org.sudowars.Model.Game.Player;
import org.sudowars.Model.Game.SingleplayerGame;

/**
 * The class DeltaManager encapsulates the undo/redo functionality of the last command available in singleplayer games.
 */
public class DeltaManager implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 8818659252100577548L;
	private final CommandInverter inverter;
	private LinkedList<GameCommand> commands;
	private LinkedList<GameCommand> commandsToExecuteAfterBookmarkCounterIsZero;
	private int toBookmarkCounter;
	private int currentPosInList;
	private boolean bookmarkAvailable;
	private boolean backToFirstErrorAvailable;
	

	/**
	 * Instantiates a new delta manager.
	 */
	public DeltaManager() {
		inverter = CommandInverter.getInstance();
		commands = new LinkedList<GameCommand>();
		commandsToExecuteAfterBookmarkCounterIsZero = new LinkedList<GameCommand>();
		currentPosInList = commands.size() - 1;
		bookmarkAvailable = false;
		backToFirstErrorAvailable = false;
	}

	/**
	 * Checks if redo is possible.
	 *
	 * @return <code>true</code>, if redo is possible, otherwise <code>false</code>.
	 */
	public boolean hasForwardDelta() {
		if (!commands.isEmpty()) {
			if (currentPosInList != commands.size() - 1) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if undo is possible.
	 *
	 * @return <code>true</code>, if undo is possible, otherwise <code>false</code>.
	 */
	public boolean hasBackwardDelta() {
		if (currentPosInList >= 0) {
			return true;
		}
		return false;
	}

	/**
	 * Does the redo operation.
	 *
	 * @param game the game which is used
	 * @param executingPlayer the palyer in whose name to execute the command.
	 *
	 * @return <code>true</code>, if redo was successful, otherwise <code>false</code>.
	 *
	 * @throws IllegalArgumentException if at least one of the given params was <code>null</code>
	 */
	public boolean forward(Game game, Player executingPlayer) throws IllegalArgumentException {
		checkArgumentsForForwardAndBackward(game, executingPlayer);
		if (!hasForwardDelta()) {
			return false;
		}
		
		if (commands.get(currentPosInList + 1).execute(game, executingPlayer)) {
			if (bookmarkAvailable) {
				toBookmarkCounter++;
			}
			currentPosInList++;
			return true;
		}
		return false;
	}

	/**
	 * Does the undo operation.
	 * @param game the game which is used
	 * @param executingPlayer the palyer in whose name to execute the command.
	 * @return <code>true</code>, if undo was successful, otherwise <code>false</code>.
	 * @throws IllegalArgumentException if at least one of the given params was <code>null</code>
	 */
	public boolean backward(Game game, Player executingPlayer) throws IllegalArgumentException {
		checkArgumentsForForwardAndBackward(game, executingPlayer);
		if (!hasBackwardDelta()) {
			return false;
		}
		
		if (inverter.getInvertedCommand(commands.get(currentPosInList), game).execute(game, executingPlayer)) {
			currentPosInList--;
			if (bookmarkAvailable) {
				toBookmarkCounter--;
			}
			return true;
		}
		return false;
	}

	/*
	 * Adds a command to the delta management and creates the corresponding undo action, if available.
	 *
	 * @param game the game which is used
	 * @param c the GameCommand which can be redone.
	 *
	 * @throws IllegalArgumentException if given command was <code>null</code>
	 */
	public void addDelta(Game game, GameCommand c) throws IllegalArgumentException {
		backToFirstErrorAvailable = true;
		
		if (hasForwardDelta()) {
			if (bookmarkAvailable && toBookmarkCounter < 0) {
				toBookmarkCounter = 0;
				while (currentPosInList < commands.size() - 1) {
					commandsToExecuteAfterBookmarkCounterIsZero.addFirst(commands.get(commands.size() - 1));
					commands.remove(commands.size() - 1);
				}
			} else {
				while (currentPosInList < commands.size() - 1) {
					commands.remove(commands.size() - 1);
				}
			}
			
		}
		
		commands.addLast(c);
		currentPosInList = commands.size() - 1;
		
		if (bookmarkAvailable) {
			toBookmarkCounter++;
		}
	}
	
	private void checkArgumentsForForwardAndBackward(Game game, Player executingPlayer) 
			throws IllegalArgumentException {
		assert (game != null);
		assert (executingPlayer != null);
		
		if (game == null) {
			throw new IllegalArgumentException("Game is null");
		}
		if (executingPlayer == null) {
			throw new IllegalArgumentException("executingPlayer is null");
		}
	}
	
	/**
	 * Sets the bookmark. It's later possible to go back to the current state by calling backToBookmark function
	 */
	public void setBookmark() {
		bookmarkAvailable = true;
		toBookmarkCounter = 0;
		commandsToExecuteAfterBookmarkCounterIsZero.clear();
	}
	
	/**
	 * Back to the bookmark.
	 *
	 * @param game the game
	 * @param executingPlayer the executing player
	 * @return true, if successful
	 */
	public boolean backToBookmark(SingleplayerGame game, Player executingPlayer) {
		while (toBookmarkCounter > 0) {
			if (!backward(game, executingPlayer)) {
				return false;
			}
		}
		while (toBookmarkCounter < 0) {
			if (!forward(game, executingPlayer)) {
				return false;
			}
		}
		if (!commandsToExecuteAfterBookmarkCounterIsZero.isEmpty()) {
			GameCommand currentCommand;
			while (!commandsToExecuteAfterBookmarkCounterIsZero.isEmpty()) {
				currentCommand = commandsToExecuteAfterBookmarkCounterIsZero.getFirst();
				commandsToExecuteAfterBookmarkCounterIsZero.removeFirst();
				currentCommand.execute(game, executingPlayer);
				this.addDelta(game, currentCommand);
			}
		}
		return true;
	}
	
	/**
	 * Returns if a bookmark is available
	 * 
	 * @return if a bookmark is available
	 */
	public boolean isBookmarkAvailable() {
		return this.bookmarkAvailable;
	}
	
	/**
	 * Returns if Back to First Error feature is available
	 * 
	 * @return if Back to First Error feature is available
	 */
	public boolean isBackToFirstErrorAvailable() {
		return this.backToFirstErrorAvailable;
	}
	
	/**
	 * Back to the first error.
	 *
	 * @param game the game
	 * @param executingPlayer the executing player
	 * @return true, if successful, false if there was no error
	 */
	public boolean backToFirstError(SingleplayerGame game, Player executingPlayer) {
		backToFirstErrorAvailable = false;
		
		if (!game.hasIncorrectCells()) {
				return false;
		}
		
		while (game.hasIncorrectCells()) {
			if (!backward(game, executingPlayer)) {
				return true;
			}
		}
		
		return true;
	}
}
