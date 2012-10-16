/*******************************************************************************
 * Copyright (c) 2011 - 2012 Adrian Vielsack, Christof Urbaczek, Florian Rosenthal, Michael Hoff, Moritz Lüdecke, Philip Flohr.
 * 
 * This file is part of Sudowars.
 * 
 * Sudowars is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Sudowars is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Sudowars.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * 
 * 
 * Diese Datei ist Teil von Sudowars.
 * 
 * Sudowars ist Freie Software: Sie können es unter den Bedingungen
 * der GNU General Public License, wie von der Free Software Foundation,
 * Version 3 der Lizenz oder (nach Ihrer Option) jeder späteren
 * veröffentlichten Version, weiterverbreiten und/oder modifizieren.
 * 
 * Sudowars wird in der Hoffnung, dass es nützlich sein wird, aber
 * OHNE JEDE GEWÄHELEISTUNG, bereitgestellt; sogar ohne die implizite
 * Gewährleistung der MARKTFÄHIGKEIT oder EIGNUNG FÜR EINEN BESTIMMTEN ZWECK.
 * Siehe die GNU General Public License für weitere Details.
 * 
 * Sie sollten eine Kopie der GNU General Public License zusammen mit diesem
 * Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 * initial API and implementation:
 * Adrian Vielsack
 * Christof Urbaczek
 * Florian Rosenthal
 * Michael Hoff
 * Moritz Lüdecke
 * Philip Flohr 
 ******************************************************************************/
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
