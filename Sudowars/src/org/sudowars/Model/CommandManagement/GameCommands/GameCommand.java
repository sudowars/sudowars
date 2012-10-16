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
