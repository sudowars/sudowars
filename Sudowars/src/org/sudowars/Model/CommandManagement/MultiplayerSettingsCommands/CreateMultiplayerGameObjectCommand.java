package org.sudowars.Model.CommandManagement.MultiplayerSettingsCommands;

import org.sudowars.DebugHelper;
import org.sudowars.Model.CommandManagement.BaseCommand;
import org.sudowars.Model.Game.MultiplayerGame;
import org.sudowars.Model.Sudoku.Sudoku;
import org.sudowars.Model.Sudoku.Field.Cell;
import org.sudowars.Model.Sudoku.Field.DataCell;

/**
 * The class CreateMultiplayerGameObjectCommand. It is used to create a new game 
 * in sp and mp mode and send it to the client in mp games.
 */
public class CreateMultiplayerGameObjectCommand extends BaseCommand {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5789251015668746424L;

	/**
	 * This variable holds a reference to the game.
	 */

	Sudoku<DataCell> sudoku = null;
	
	/** Initializes a new instance of the CreateMultiplayerGameObjectCommand class using the given Sudoku.
	 *
	 * @param sudoku The sudoku which shall be used by the multiplyer game to create.
	 *
	 * @throws IllegalArgumentException if the given sudoku is <code>null</code>
	 */
	public CreateMultiplayerGameObjectCommand(Sudoku<DataCell> sudoku) throws IllegalArgumentException {
		if (sudoku == null) {
			throw new IllegalArgumentException("Given sudoku is Null");
		}
		
		this.sudoku = sudoku;
		DebugHelper.log(DebugHelper.PackageName.CreateMultiplayerGameObjectCommand, "SetSudoku");
		DebugHelper.printInitialSudoku(sudoku);
	}


	/**
	 * This function returns the game which was created by the execute function if execute was called before.
	 * @return the created multiplyer game if execute was called before, <code>null</code> else
	 */
	public MultiplayerGame getGame() {
		Sudoku<Cell> gameSudoku = new Sudoku<Cell>(sudoku.getField().convert(), sudoku.getDependencyManager());
		DebugHelper.log(DebugHelper.PackageName.CreateMultiplayerGameObjectCommand, "First print in getSudoku");
		DebugHelper.printInitialSudoku(sudoku);
		DebugHelper.log(DebugHelper.PackageName.CreateMultiplayerGameObjectCommand, "First print in getSudoku");
		DebugHelper.printInitialCellSudoku(gameSudoku);
		
		return new MultiplayerGame(gameSudoku);
	}

}
