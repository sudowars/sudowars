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
package org.sudowars;

import java.util.LinkedList;
import java.util.List;

import android.util.Log;

import org.sudowars.Model.Solver.SolverState;
import org.sudowars.Model.Sudoku.Sudoku;
import org.sudowars.Model.Sudoku.Field.*;
import org.sudowars.Model.SudokuUtil.NoteManager;

// TODO: Auto-generated Javadoc
/**
 * The Class DebugHelper.
 */
public class DebugHelper {
	
	/**
	 * The Enum PackageName.
	 */
	public static enum PackageName {
	    
    	Sudoku, SingleplayerSettings, MultiplayerSettings, Play, SingleplayerPlay, MultiplayerPlay
	    ,SingleplayerMenu, MultiplayerMenu, CreateMultiplayerGameObjectCommand, SudokuFilePool, TimeSyncer
	    ,BluetoothServer, BluetoothPacket, BluetoothConnection, BluetoothConnection_PacketHandler
	    , SudokuField, FileIO, Solver, Generator, SolverStrategy, Transformator
	}
	

	public static enum DebugState {
		
		PRINT_NOTHING, PRINT_SELECTED, PRINT_ALL
	}
	
	/** The DEBU g_ state. */
	private static DebugState DEBUG_STATE = DebugState.PRINT_NOTHING;
	
	/** The logs. (They are not saved in this list while DEBUG_STATE is PRINT_NONE */
	private static LinkedList<String> logs = new LinkedList<String>();
	
	/** The shown packages. */
	private static LinkedList<DebugHelper.PackageName> shownPackages= new LinkedList<DebugHelper.PackageName>(); 
	
	/**
	 * Prints the sudoku field.
	 *
	 * @param sudokuField the sudoku field
	 * @param justInitial the just initial
	 */
	public static void printSudokuField(Field<Cell> sudokuField, boolean justInitial) {
		
		if (DEBUG_STATE != DebugState.PRINT_ALL)
			return;
		
		String out;
		
		for (int y = 0; y < sudokuField.getStructure().getHeight(); y++) {
			out = "";
			for (int x = 0; x < sudokuField.getStructure().getWidth(); x++) {
				int val = sudokuField.getCell(x, y).getValue();
				out += (justInitial && sudokuField.getCell(x, y).isInitial() || !justInitial) ? val : "_";
				out += " ";
			}
			log(DebugHelper.PackageName.Sudoku, out);
		}
		
	}
	
	/**
	 * Prints the initial sudoku.
	 *
	 * @param sudoku the sudoku
	 */
	public static void printInitialSudoku(Sudoku<DataCell> sudoku) {
		DebugHelper.printSudokuField(sudoku.getField().convert(), true);
	}
	
	/**
	 * Prints the initial cell sudoku.
	 *
	 * @param sudoku the sudoku
	 */
	public static void printInitialCellSudoku(Sudoku<Cell> sudoku) {
		DebugHelper.printSudokuField(sudoku.getField(), true);
	}
	
	/**
	 * Log. It does what the name says
	 *
	 * @param part the PackageName
	 * @param message the message
	 */
	public static void log(DebugHelper.PackageName part, String message) {
		if (!(DEBUG_STATE == DebugState.PRINT_NOTHING)) {
			logs.addLast("Sudowars" + part + ": " + message);
			if (DEBUG_STATE == DebugState.PRINT_ALL) {
				Log.d("Sudowars", part + ": " + message);
			} else {
				if (shownPackages.contains(part)) {
					Log.d("Sudowars", part + ": " + message);
				}
			}
		}
	}
	
	/**
	 * Gets all logs for package.
	 *
	 * @param packageName the package name
	 * @return the all logs for package
	 */
	public static LinkedList<String> getAllLogsForPackage(DebugHelper.PackageName packageName) {
		LinkedList<String> returnList = new LinkedList<String>();
		for (String s : logs) {
			if (s.contains(packageName.toString())) {
				returnList.addLast(s);
			}
		}
		return returnList;
	}
	
	/**
	 * Prints the all logs for package. (Except logs which were written while DebugSate was PRINT_NONE)
	 *
	 * @param packageName the package name
	 */
	public static void printAllLogsForPackage(DebugHelper.PackageName packageName) {
		for (String s : getAllLogsForPackage(packageName)) {
			Log.d("org.sudowars", s);
		}
	}
	
	/**
	 * Adds the component to shown list. If the BebugState is PRINT_SELECTED only debug messages from packages in this list are printed.
	 *
	 * @param packageName the package name
	 */
	public static void addComponentToShownList(DebugHelper.PackageName packageName) {
		shownPackages.add(packageName);
	}
	
	/**
	 * Sets the debug state.
	 *
	 * @param state the new debug state
	 */
	public static void setDebugState(DebugHelper.DebugState state) {
		if (state == DebugHelper.DebugState.PRINT_ALL) {
			shownPackages.clear();
		}
		DEBUG_STATE = state;
	}
	
	/**
	 * Gets the list string.
	 *
	 * @param list the list
	 * @return the list string
	 */
	public static String getListString(List<Integer> list) {
		String listStr = "";
		for (Object element : list) listStr += (listStr.length() == 0 ? "" : ", ") + element.toString();
		return listStr;
	}
	
	// --- print complete Sudoku ------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Repeat.
	 *
	 * @param s the s
	 * @param n the n
	 * @return the string
	 */
	private static String repeat(String s, int n) {
	    if (s == null) return null;
	    StringBuilder sb = new StringBuilder();
	    for (int i = 0; i < n; i++) sb.append(s);
	    return sb.toString();
	}
	
	/**
	 * Gets the cell line.
	 *
	 * @param part the part
	 * @param lineNumber the line number
	 * @param totalLines the total lines
	 * @param cell the cell
	 * @param notes the notes
	 * @return the cell line
	 */
	private static String getCellLine(DebugHelper.PackageName part, int lineNumber, int totalLines, Cell cell, NoteManager notes) {
		
		String result = "";
		String emptyLine = repeat(" ", totalLines);
		//TODO: Use resource...
		//String[] symbols = Resources.getStringArray(R.array.symbols);
		String[] symbols = {
				" ",
				"1",
				"2",
				"3",
				"4",
				"5",
				"6",
				"7",
				"8",
				"9",
				"0",
				"a",
				"b",
				"c",
				"d",
				"e",
				"f"
		};
		
		if (cell.isInitial() || cell.isSet()) {
			String mark = (cell.isInitial()) ? "*" : "=";
			result = (lineNumber == 2) ? mark + symbols[cell.getValue()] + mark + ((totalLines == 4) ? " " : "") : emptyLine;			
		} else {
			//output notes
			for (int i = (lineNumber-1)*totalLines + 1; i <= lineNumber*totalLines; i++) {
				result += (notes.hasNote(cell, i)) ? symbols[i] : " "; 
			}
		}
		
		return result;
		
	}
	
	/**
	 * Prints the sudoku line.
	 *
	 * @param part the part
	 * @param lineNumber the line number
	 * @param field the field
	 * @param notes the notes
	 */
	private static void printSudokuLine(DebugHelper.PackageName part, int lineNumber, Field<Cell> field, NoteManager notes) {
		
		assert(field.getStructure() instanceof SquareStructure);		
		int fieldWidth = field.getStructure().getWidth(); 
		assert (fieldWidth == 16 || fieldWidth == 9);
		int linesPerCaree = (fieldWidth == 16) ? 4 : 3;
		
		for (int line = 1; line <= linesPerCaree; line++) {
			
			String lineStr = "||";
			for (int x = 0; x < fieldWidth; x++) {
				Cell current = field.getCell(x, lineNumber);
				lineStr += getCellLine(part, line, linesPerCaree, current, notes) + "|" + (((x+1)%linesPerCaree == 0) ? "|" : "");				
			}			
			
			log(part, lineStr);
			
		}
		
	}
	
	/**
	 * Prints the complete sudoku.
	 *
	 * @param part the part
	 * @param field the field
	 * @param notes the notes
	 */
	public static void printCompleteSudoku(DebugHelper.PackageName part, Field<Cell> field, NoteManager notes) {
		
		assert(field.getStructure() instanceof SquareStructure);
				
		int fieldWidth = field.getStructure().getWidth();
		int fieldHeight = field.getStructure().getHeight();
		
		assert (fieldWidth == 16 || fieldWidth == 9);
		
		int linesPerCaree = (fieldWidth == 16) ? 4 : 3;
		String borderLine = repeat("=", (fieldWidth == 16) ? 86 : 41);
		String fineLine = repeat("-", (fieldWidth == 16) ? 86 : 41);
		
		log(part, borderLine);		
		for (int y = 0; y < fieldHeight; y++) {
			printSudokuLine(part, y, field, notes);
			log(part, (((y+1)%linesPerCaree == 0) ? borderLine : fineLine));
		}
		
	}	
	
	/**
	 * Prints the complete sudoku.
	 *
	 * @param part the part
	 * @param currentState the current state
	 */
	public static void printCompleteSudoku(DebugHelper.PackageName part, SolverState currentState) {
		printCompleteSudoku(part, currentState.getField(), currentState.getNoteManager());
	}
	
	
}
