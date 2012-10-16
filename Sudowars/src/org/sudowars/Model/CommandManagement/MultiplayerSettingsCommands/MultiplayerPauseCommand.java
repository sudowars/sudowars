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
