package org.sudowars.Model.CommandManagement.GameCommands;

import org.sudowars.Model.Game.Game;

import org.sudowars.Model.Game.GameCell;
import org.sudowars.Model.Game.Player;
import org.sudowars.Model.Game.SingleplayerGame;

/**
 * The class encapsulates the ability to remove a value of a cell during a game.
 */
public class RemoveCellValueCommand extends CellCommand {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5148716031479180849L;
	
	private int value;

	/**
	 * Initializes a new instance of the RemoveCellValueCommand class.
	 *
	 * @param cell The cell on which the command is executed.
	 *
	 * @throws IllegalArgumentException if given cell is <code>null</code>
	 */
	public RemoveCellValueCommand(GameCell cell) throws IllegalArgumentException {
		super(cell);
	}

	/**
	 * Executes the command on the specified game in the name of the given player.
	 *
	 * @param game A reference to game on which to execute the current instance.
	 * @param executingPlayer the player in whose name to execute the command
	 *
	 * @return <code>true</code>, if the command was executed successfully, <code>false</code> otherwise
	 *
	 * @throws IllegalArgumentException if at least one of the given params is <code>null</code>
	 */
	public boolean execute(Game game, Player executingPlayer) throws IllegalArgumentException {

		if (game == null) {
			throw new IllegalArgumentException("game is NULL");
		}
		if (executingPlayer == null) {
			throw new IllegalArgumentException("executingPlayer is NULL");
		}

		GameCell cell = getCell(game);
		if (game instanceof SingleplayerGame) {
			value = cell.getValue();
			return ((SingleplayerGame) game).removeValue(cell);
		}
		return false;
	}
	
	/**
	 * Gets the removed value.
	 *
	 * @return the removed value
	 */
	public int getRemovedValue() {
		return value;
	}

	@Override
	public GameCommand getInvertedCommand(Game game) {
		GameCell cell = getCell(game);
		return new SetCellValueCommand(cell, getRemovedValue());
	}

}
