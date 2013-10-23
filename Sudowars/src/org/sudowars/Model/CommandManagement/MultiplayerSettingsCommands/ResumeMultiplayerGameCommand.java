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
package org.sudowars.Model.CommandManagement.MultiplayerSettingsCommands;

import org.sudowars.Model.CommandManagement.BaseCommand;
import org.sudowars.Model.Game.MultiplayerGame;

/**
 * The Class ResumeMultiplayerGameCommand.
 */
public class ResumeMultiplayerGameCommand extends BaseCommand {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8724437015401155358L;
	
	/** The game. */
	private MultiplayerGame game;


	/**
	 * Instantiates a new resume multiplayer game command.
	 *
	 * @param game the game
	 */
	public ResumeMultiplayerGameCommand(MultiplayerGame game) {
		if (game == null) {
			throw new IllegalArgumentException("Given game is null");
		}
		this.game = game;
	}
	
	
	/**
	 * Gets the game.
	 *
	 * @return the game
	 */
	public MultiplayerGame getGame() {
		return game;
	}
}
