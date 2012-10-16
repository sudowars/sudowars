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
