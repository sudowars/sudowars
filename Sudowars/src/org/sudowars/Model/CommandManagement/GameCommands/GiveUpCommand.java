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

import org.sudowars.Model.CommandManagement.BaseCommand;
import org.sudowars.Model.Game.Game;
import org.sudowars.Model.Game.Player;

/**
 * The Class GiveUpCommand.
 */
public class GiveUpCommand extends BaseCommand implements GameCommand {


	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2551011344964712281L;

	/**
	 * Instantiates a new give up command.
	 */
	public GiveUpCommand() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.sudowars.Model.CommandManagement.GameCommand#execute(org.sudowars.Model.Game.Game, 
	 * org.sudowars.Model.Game.Player)
	 */
	@Override
	public boolean execute(Game game, Player executingPlayer)
			throws IllegalArgumentException {
		if (game == null) {
			throw new IllegalArgumentException("Game is null");
		}
		if (executingPlayer == null) {
			throw new IllegalArgumentException("executingPlayer is null");
		}
		
		game.abortGame(executingPlayer, 0);
		return true;
	}

	@Override
	public GameCommand getInvertedCommand(Game game) {
		return this;
	}

}
