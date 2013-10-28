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
package org.sudowars.Model.CommandManagement.MultiplayerSettingsCommands;

import org.sudowars.Model.CommandManagement.BaseCommand;
import org.sudowars.Model.Game.MultiplayerGame;
import org.sudowars.Model.Game.Player;

/**
 * The Class ResumeMultiplayerGameCommand.
 */
public class MultiplayerPauseCommand extends BaseCommand {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1055894425990323699L;


	/**
	 * Instantiates a new HandleMultiplayerPauseCommand command.
	 *
	 * @param game the game
	 */
	public MultiplayerPauseCommand() {
	}
	
	
	public boolean execute(MultiplayerGame game, Player player) {
		if (game == null) {
			throw new IllegalArgumentException("given game is null");
		}
		if (player == null) {
			throw new IllegalArgumentException("given player is null");
		}
		//TODO
		game.pauseGame(game.getPlayers().get(0));
		return game.pauseGame(game.getPlayers().get(1));
	}
}
