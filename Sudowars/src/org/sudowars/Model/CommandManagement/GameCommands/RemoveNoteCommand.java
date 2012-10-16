package org.sudowars.Model.CommandManagement.GameCommands;

import org.sudowars.Model.Game.Game;
import org.sudowars.Model.Game.GameCell;
import org.sudowars.Model.Game.Player;

/**
 * The class RemoveNoteCommand. It provides the possibility to remove a note from a cell.
 */
public class RemoveNoteCommand extends NoteCommand {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6279070382750246165L;

	/**
	 * Initializes a new instance of the RemoveNoteCommand class.
	 *
	 * @param cell The cell on which the command is executed.
   * @param noteValue The value of the note used during the command.
   *
	 * @throws IllegalArgumentException if given cell is <code>null</code> or noteValue < <code>1</code>
	 */
	public RemoveNoteCommand(GameCell cell, int noteValue) throws IllegalArgumentException {
		super(cell, noteValue);
	}

	/**
	* Executes the current command on the specified game in the name of the given player and 
	* removes the note from the given cell.
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
		return  game.getNoteManagerOfPlayer(executingPlayer).removeNote(cell, noteValue);	
	}

	@Override
	public GameCommand getInvertedCommand(Game game) {
		return new AddNoteCommand(this.getCell(game), this.noteValue);
	}
}
