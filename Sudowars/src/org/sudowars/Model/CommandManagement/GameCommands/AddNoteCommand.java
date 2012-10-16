package org.sudowars.Model.CommandManagement.GameCommands;

import java.io.Serializable;

import org.sudowars.Model.Game.Game;
import org.sudowars.Model.Game.GameCell;
import org.sudowars.Model.Game.Player;

/**
 * This class provides the possibility to add a note to a cell.
 */
public class AddNoteCommand extends NoteCommand implements Serializable {
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 494824075438793657L;
	
	/**
	 * Initializes a new instance of the {@link AddNoteCommand} class.
	 *
	 * @param cell The cell on which the command is executed.
   * @param noteValue The value of the note used during the command.
   *
	 * @throws IllegalArgumentException if given cell is
	 * <code>null</code> or noteValue < <code>1</code>
	 */
	public AddNoteCommand(GameCell cell, int noteValue)
			throws IllegalArgumentException {
		super(cell, noteValue);
	}

	/**
	* Executes the current command on the specified game in the name of the given player 
	* and adds the note to the given cell.
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
			throw new IllegalArgumentException("Game is null");
		}
		if (executingPlayer == null) {
			throw new IllegalArgumentException("executingPlayer is null");
		}
		GameCell cell = game.getSudoku().getField().getCell(cellIndex);
		return game.getNoteManagerOfPlayer(executingPlayer).addNote(cell, noteValue);
	}

	@Override
	public GameCommand getInvertedCommand(Game game) {
		return new RemoveNoteCommand(this.getCell(game), this.noteValue);
	}
}
