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
