package org.sudowars.Model.CommandManagement.GameCommands;

import org.sudowars.Model.Game.GameCell;
import org.sudowars.Model.Game.Player;
import org.sudowars.Model.Game.Game;

/**
 * This class is the base of all classes executing commands with notes during a game.
 */
public abstract class NoteCommand extends CellCommand {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6995125752200561052L;
	/**
	 * The value of the note.
	 */
	protected int noteValue;

	/**
	 * Initializes a new instance of the NoteCommand class.
	 *
	 * @param cell The cell on which the command is executed.
   	 * @param noteValue The value of the note used during the command.
         *
	 * @throws IllegalArgumentException if given cell is <code>null</code> or noteValue < <code>1</code>
	 */
	protected NoteCommand(GameCell cell, int noteValue) throws IllegalArgumentException {
		super(cell);
		if (noteValue < 1 || noteValue > 16) {
			throw new IllegalArgumentException("NoteValue out of range");
		}
		this.noteValue = noteValue;
	}

	/**
	 * Gets the note value.
	 *
	 * @return the note value
	 */
	public int getNoteValue() {
		return noteValue;
	}

	/**
	 * Executes the current command on the specified game in the name of the given player.
	 *
	 * @param game A reference to game on which to execute the current instance.
	 * @param executingPlayer The player in whose name to execute the command
	 *
	 * @return <code>true</code>, if the command was executed successfully, <code>false</code> otherwise
	 *
	 * @throws IllegalArgumentException if at least one of the given params is <code>null</code>
	 */
	public abstract boolean execute(Game game, Player executingPlayer) throws IllegalArgumentException;	

}
