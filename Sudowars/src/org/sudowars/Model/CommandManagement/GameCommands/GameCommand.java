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
package org.sudowars.Model.CommandManagement.GameCommands;

import org.sudowars.Model.CommandManagement.Command;
import org.sudowars.Model.Game.Player;
import org.sudowars.Model.Game.Game;

/**
 * This interface allows implementing classes to execute a command on a game in the name of a player.
 */
public interface GameCommand extends Command {
	
	/**
	 * Executes the current command on the specified game in the name of the given player.
	 *
	 * @param game A reference to game on which to execute the current instance.
	 * @param executingPlayer the player in whose name to execute the command
	 *
	 * @return <code>true</code>if the command was executed successfully, <code>false</code> otherwise
	 *
	 * @throws IllegalArgumentException if at least one of the given params is <code>null</code>
	 */
	boolean execute(Game game, Player executingPlayer) throws IllegalArgumentException;
	
	/**
	 * Get the corresponding complementary command which nullifies the action(s) of the current instance affecting the given game.
	 * @param game reference to a {@link Game}
	 * @return the corresponding complementary {@link GameCommand}
	 */
	GameCommand getInvertedCommand(Game game);
}
