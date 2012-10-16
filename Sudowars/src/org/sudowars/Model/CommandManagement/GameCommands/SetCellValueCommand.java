package org.sudowars.Model.CommandManagement.GameCommands;

import org.sudowars.Model.Game.Game;
import org.sudowars.Model.Game.GameCell;
import org.sudowars.Model.Game.Player;

/**
 *This class encapsulates the command to set the value of a cell during a game.
 */
public class SetCellValueCommand extends CellCommand {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3467974332713719607L;
	private int cellValue;
	
	/**
	 * Initializes a new instance of the SetCellValueCommand class.
	 *
	 * @param cell The cell on which the command is executed.
	 * @param cellValue the value to be set.
	 *
	 * @throws IllegalArgumentException if given cell is <code>null</code> or the given value < <code>1</code>
	 */
	public SetCellValueCommand(GameCell cell, int cellValue) throws IllegalArgumentException {
		super(cell);
		assert (cellValue > 0 && cellValue < 17);		
		if (cellValue > 0 && cellValue < 17) {
			this.cellValue = cellValue;
		} else {
			throw new IllegalArgumentException("cellValue is out of range");
		}
	}

	/** Executes the current command on the specified game in the name of the given player.
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

		GameCell cell = game.getSudoku().getField().getCell(cellIndex);
		if (cell instanceof GameCell) {
			return game.setValue(executingPlayer, (GameCell) cell, cellValue, 0);
		} else {
			//This should not happen...
			return false;
		}
	}

	@Override
	public GameCommand getInvertedCommand(Game game) {
		return new RemoveCellValueCommand(this.getCell(game));
	}

}
