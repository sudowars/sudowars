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
package org.sudowars.Model.SudokuManagement.IO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;

import org.sudowars.DebugHelper;
import org.sudowars.Model.Sudoku.Sudoku;
import org.sudowars.Model.Sudoku.Field.DataCell;
import org.sudowars.Model.Sudoku.Field.DataCellBuilder;
import org.sudowars.Model.Sudoku.Field.Field;
import org.sudowars.Model.Sudoku.Field.FieldBuilder;
import org.sudowars.Model.Sudoku.Field.SquareStructure;
import org.sudowars.Model.Sudoku.RuleManagement.RuleApplier.StandardRulesetFactory;
import org.sudowars.Model.SudokuUtil.GameState;
import org.sudowars.Model.SudokuUtil.SingleplayerGameState;

/**
 * This class provides functionality to save to and load games from files.
 * @see IOBase
 */
public class FileIO implements IOBase {

	private static final String SPGameFileName = "sp_game.ser";
	private static final String MPGameFileName = "mp_game.ser";
	private final File spGameFile;
	private final File mpGameFile;
	
	/**
	 * Initializes a new instance of the {@link FileIO} class.
	 * @param context the {@link Context} of the caller
	 * @throws IllegalArgumentException if given context was {@code null}
	 */
	public FileIO (Context context) throws IllegalArgumentException {
		if (context == null) {
			throw new IllegalArgumentException("context cannot be null.");
		}
		File f = context.getFileStreamPath("savedGames");
		if (!f.exists()) {
			f.mkdirs();
		}
		DebugHelper.log(DebugHelper.PackageName.FileIO,"f" + f.getAbsolutePath());
				
		this.spGameFile = new File(f, SPGameFileName);
		this.mpGameFile = new File(f, MPGameFileName);
		DebugHelper.log(DebugHelper.PackageName.FileIO,"SP_Location" + this.spGameFile.getAbsolutePath());
		DebugHelper.log(DebugHelper.PackageName.FileIO,"MP_Location" + this.mpGameFile.getAbsolutePath());
		DebugHelper.log(DebugHelper.PackageName.FileIO,"SP_exists" + String.valueOf(this.spGameFile.exists()));
		DebugHelper.log(DebugHelper.PackageName.FileIO,"MP_exists" + String.valueOf(this.mpGameFile.exists()));
	}
	
	public static Sudoku<DataCell> createTestSudoku16x16Easy() {
		FieldBuilder<DataCell> fb = new FieldBuilder<DataCell>();
		Field<DataCell> field = fb.build(new SquareStructure(16), new DataCellBuilder());
		int row = 0;
		field.getCell(row, 0).setValue(10);
		field.getCell(row, 1).setValue(1);
		field.getCell(row, 2).setValue(14);
		field.getCell(row, 3).setValue(7);
		field.getCell(row, 4).setValue(13);
		field.getCell(row, 5).setValue(8);
		field.getCell(row, 6).setValue(4);
		field.getCell(row, 7).setValue(15);
		field.getCell(row, 8).setValue(2);
		field.getCell(row, 9).setValue(9);
		field.getCell(row, 10).setValue(12);
		field.getCell(row, 11).setValue(3);
		field.getCell(row, 12).setValue(5);
		field.getCell(row, 13).setValue(16);
		field.getCell(row, 14).setValue(6);
		field.getCell(row, 15).setValue(11);
		row++;
		field.getCell(row, 0).setValue(6);
		field.getCell(row, 1).setValue(13);
		field.getCell(row, 2).setValue(16);
		field.getCell(row, 3).setValue(15);
		field.getCell(row, 4).setValue(3);
		field.getCell(row, 5).setValue(11);
		field.getCell(row, 6).setValue(1);
		field.getCell(row, 7).setValue(14);
		field.getCell(row, 8).setValue(4);
		field.getCell(row, 9).setValue(10);
		field.getCell(row, 10).setValue(7);
		field.getCell(row, 11).setValue(5);
		field.getCell(row, 12).setValue(12);
		field.getCell(row, 13).setValue(9);
		field.getCell(row, 14).setValue(8);
		field.getCell(row, 15).setValue(2);
		row++;
		field.getCell(row, 0).setValue(3);
		field.getCell(row, 1).setValue(12);
		field.getCell(row, 2).setValue(4);
		field.getCell(row, 3).setValue(11);
		field.getCell(row, 4).setValue(2);
		field.getCell(row, 5).setValue(9);
		field.getCell(row, 6).setValue(7);
		field.getCell(row, 7).setValue(5);
		field.getCell(row, 8).setValue(16);
		field.getCell(row, 9).setValue(6);
		field.getCell(row, 10).setValue(8);
		field.getCell(row, 11).setValue(1);
		field.getCell(row, 12).setValue(10);
		field.getCell(row, 13).setValue(13);
		field.getCell(row, 14).setValue(14);
		field.getCell(row, 15).setValue(15);
		row++;
		field.getCell(row, 0).setValue(5);
		field.getCell(row, 1).setValue(9);
		field.getCell(row, 2).setValue(8);
		field.getCell(row, 3).setValue(2);
		field.getCell(row, 4).setValue(10);
		field.getCell(row, 5).setValue(6);
		field.getCell(row, 6).setValue(12);
		field.getCell(row, 7).setValue(16);
		field.getCell(row, 8).setValue(15);
		field.getCell(row, 9).setValue(13);
		field.getCell(row, 10).setValue(11);
		field.getCell(row, 11).setValue(14);
		field.getCell(row, 12).setValue(1);
		field.getCell(row, 13).setValue(7);
		field.getCell(row, 14).setValue(3);
		field.getCell(row, 15).setValue(4);
		row++;
		field.getCell(row, 0).setValue(4);
		field.getCell(row, 1).setValue(14);
		field.getCell(row, 2).setValue(3);
		field.getCell(row, 3).setValue(12);
		field.getCell(row, 4).setValue(5);
		field.getCell(row, 5).setValue(2);
		field.getCell(row, 6).setValue(6);
		field.getCell(row, 7).setValue(7);
		field.getCell(row, 8).setValue(8);
		field.getCell(row, 9).setValue(16);
		field.getCell(row, 10).setValue(1);
		field.getCell(row, 11).setValue(10);
		field.getCell(row, 12).setValue(13);
		field.getCell(row, 13).setValue(15);
		field.getCell(row, 14).setValue(11);
		field.getCell(row, 15).setValue(9);
		row++;
		field.getCell(row, 0).setValue(8);
		field.getCell(row, 1).setValue(10);
		field.getCell(row, 2).setValue(13);
		field.getCell(row, 3).setValue(5);
		field.getCell(row, 4).setValue(11);
		field.getCell(row, 5).setValue(15);
		field.getCell(row, 6).setValue(16);
		field.getCell(row, 7).setValue(1);
		field.getCell(row, 8).setValue(3);
		field.getCell(row, 9).setValue(2);
		field.getCell(row, 10).setValue(9);
		field.getCell(row, 11).setValue(4);
		field.getCell(row, 12).setValue(14);
		field.getCell(row, 13).setValue(6);
		field.getCell(row, 14).setValue(7);
		field.getCell(row, 15).setValue(12);
		row++;
		field.getCell(row, 0).setValue(11);
		field.getCell(row, 1).setValue(7);
		field.getCell(row, 2).setValue(6);
		field.getCell(row, 3).setValue(16);
		field.getCell(row, 4).setValue(4);
		field.getCell(row, 5).setValue(12);
		field.getCell(row, 6).setValue(3);
		field.getCell(row, 7).setValue(9);
		field.getCell(row, 8).setValue(14);
		field.getCell(row, 9).setValue(5);
		field.getCell(row, 10).setValue(15);
		field.getCell(row, 11).setValue(13);
		field.getCell(row, 12).setValue(2);
		field.getCell(row, 13).setValue(8);
		field.getCell(row, 14).setValue(10);
		field.getCell(row, 15).setValue(1);
		row++;
		field.getCell(row, 0).setValue(2);
		field.getCell(row, 1).setValue(15);
		field.getCell(row, 2).setValue(9);
		field.getCell(row, 3).setValue(1);
		field.getCell(row, 4).setValue(8);
		field.getCell(row, 5).setValue(10);
		field.getCell(row, 6).setValue(14);
		field.getCell(row, 7).setValue(13);
		field.getCell(row, 8).setValue(11);
		field.getCell(row, 9).setValue(7);
		field.getCell(row, 10).setValue(6);
		field.getCell(row, 11).setValue(12);
		field.getCell(row, 12).setValue(4);
		field.getCell(row, 13).setValue(3);
		field.getCell(row, 14).setValue(16);
		field.getCell(row, 15).setValue(5);
		row++;
		field.getCell(row, 0).setValue(7);
		field.getCell(row, 1).setValue(5);
		field.getCell(row, 2).setValue(10);
		field.getCell(row, 3).setValue(9);
		field.getCell(row, 4).setValue(1);
		field.getCell(row, 5).setValue(16);
		field.getCell(row, 6).setValue(13);
		field.getCell(row, 7).setValue(4);
		field.getCell(row, 8).setValue(12);
		field.getCell(row, 9).setValue(14);
		field.getCell(row, 10).setValue(3);
		field.getCell(row, 11).setValue(6);
		field.getCell(row, 12).setValue(11);
		field.getCell(row, 13).setValue(2);
		field.getCell(row, 14).setValue(15);
		field.getCell(row, 15).setValue(8);
		row++;
		field.getCell(row, 0).setValue(13);
		field.getCell(row, 1).setValue(6);
		field.getCell(row, 2).setValue(1);
		field.getCell(row, 3).setValue(4);
		field.getCell(row, 4).setValue(14);
		field.getCell(row, 5).setValue(3);
		field.getCell(row, 6).setValue(9);
		field.getCell(row, 7).setValue(8);
		field.getCell(row, 8).setValue(10);
		field.getCell(row, 9).setValue(11);
		field.getCell(row, 10).setValue(2);
		field.getCell(row, 11).setValue(15);
		field.getCell(row, 12).setValue(16);
		field.getCell(row, 13).setValue(12);
		field.getCell(row, 14).setValue(5);
		field.getCell(row, 15).setValue(7);
		row++;
		field.getCell(row, 0).setValue(14);
		field.getCell(row, 1).setValue(11);
		field.getCell(row, 2).setValue(12);
		field.getCell(row, 3).setValue(3);
		field.getCell(row, 4).setValue(15);
		field.getCell(row, 5).setValue(5);
		field.getCell(row, 6).setValue(2);
		field.getCell(row, 7).setValue(10);
		field.getCell(row, 8).setValue(13);
		field.getCell(row, 9).setValue(8);
		field.getCell(row, 10).setValue(16);
		field.getCell(row, 11).setValue(7);
		field.getCell(row, 12).setValue(9);
		field.getCell(row, 13).setValue(1);
		field.getCell(row, 14).setValue(4);
		field.getCell(row, 15).setValue(6);
		row++;
		field.getCell(row, 0).setValue(15);
		field.getCell(row, 1).setValue(16);
		field.getCell(row, 2).setValue(2);
		field.getCell(row, 3).setValue(8);
		field.getCell(row, 4).setValue(6);
		field.getCell(row, 5).setValue(7);
		field.getCell(row, 6).setValue(11);
		field.getCell(row, 7).setValue(12);
		field.getCell(row, 8).setValue(5);
		field.getCell(row, 9).setValue(1);
		field.getCell(row, 10).setValue(4);
		field.getCell(row, 11).setValue(9);
		field.getCell(row, 12).setValue(3);
		field.getCell(row, 13).setValue(14);
		field.getCell(row, 14).setValue(13);
		field.getCell(row, 15).setValue(10);
		row++;
		field.getCell(row, 0).setValue(16);
		field.getCell(row, 1).setValue(2);
		field.getCell(row, 2).setValue(7);
		field.getCell(row, 3).setValue(10);
		field.getCell(row, 4).setValue(12);
		field.getCell(row, 5).setValue(13);
		field.getCell(row, 6).setValue(5);
		field.getCell(row, 7).setValue(11);
		field.getCell(row, 8).setValue(9);
		field.getCell(row, 9).setValue(15);
		field.getCell(row, 10).setValue(14);
		field.getCell(row, 11).setValue(8);
		field.getCell(row, 12).setValue(6);
		field.getCell(row, 13).setValue(4);
		field.getCell(row, 14).setValue(1);
		field.getCell(row, 15).setValue(3);
		row++;
		field.getCell(row, 0).setValue(12);
		field.getCell(row, 1).setValue(8);
		field.getCell(row, 2).setValue(15);
		field.getCell(row, 3).setValue(13);
		field.getCell(row, 4).setValue(16);
		field.getCell(row, 5).setValue(1);
		field.getCell(row, 6).setValue(10);
		field.getCell(row, 7).setValue(3);
		field.getCell(row, 8).setValue(6);
		field.getCell(row, 9).setValue(4);
		field.getCell(row, 10).setValue(5);
		field.getCell(row, 11).setValue(2);
		field.getCell(row, 12).setValue(7);
		field.getCell(row, 13).setValue(11);
		field.getCell(row, 14).setValue(9);
		field.getCell(row, 15).setValue(14);
		row++;
		field.getCell(row, 0).setValue(1);
		field.getCell(row, 1).setValue(3);
		field.getCell(row, 2).setValue(5);
		field.getCell(row, 3).setValue(14);
		field.getCell(row, 4).setValue(9);
		field.getCell(row, 5).setValue(4);
		field.getCell(row, 6).setValue(15);
		field.getCell(row, 7).setValue(6);
		field.getCell(row, 8).setValue(7);
		field.getCell(row, 9).setValue(12);
		field.getCell(row, 10).setValue(13);
		field.getCell(row, 11).setValue(11);
		field.getCell(row, 12).setValue(8);
		field.getCell(row, 13).setValue(10);
		field.getCell(row, 14).setValue(2);
		field.getCell(row, 15).setValue(16);
		row++;
		field.getCell(row, 0).setValue(9);
		field.getCell(row, 1).setValue(4);
		field.getCell(row, 2).setValue(11);
		field.getCell(row, 3).setValue(6);
		field.getCell(row, 4).setValue(7);
		field.getCell(row, 5).setValue(14);
		field.getCell(row, 6).setValue(8);
		field.getCell(row, 7).setValue(2);
		field.getCell(row, 8).setValue(1);
		field.getCell(row, 9).setValue(3);
		field.getCell(row, 10).setValue(10);
		field.getCell(row, 11).setValue(16);
		field.getCell(row, 12).setValue(15);
		field.getCell(row, 13).setValue(5);
		field.getCell(row, 14).setValue(12);
		field.getCell(row, 15).setValue(13);
		
		field.getCell(0, 0).setInitial(true);
		field.getCell(0, 1).setInitial(true);
		field.getCell(0, 2).setInitial(true);
		field.getCell(0, 3).setInitial(true);
		field.getCell(0, 6).setInitial(true);
		field.getCell(0, 7).setInitial(true);
		field.getCell(0, 8).setInitial(true);
		field.getCell(0, 9).setInitial(true);
		field.getCell(0, 11).setInitial(true);
		field.getCell(0, 14).setInitial(true);
		
		field.getCell(1, 0).setInitial(true);
		field.getCell(1, 1).setInitial(true);
		field.getCell(1, 2).setInitial(true);
		field.getCell(1, 3).setInitial(true);
		field.getCell(1, 4).setInitial(true);
		//field.getCell(0, 7).setInitial(true);
		field.getCell(1, 8).setInitial(true);
		field.getCell(1, 9).setInitial(true);
		field.getCell(1, 13).setInitial(true);
		
		field.getCell(2, 0).setInitial(true);
		field.getCell(2, 1).setInitial(true);
		field.getCell(2, 2).setInitial(true);
		field.getCell(2, 3).setInitial(true);
		field.getCell(2, 4).setInitial(true);
		field.getCell(2, 5).setInitial(true);
		field.getCell(2, 7).setInitial(true);
		field.getCell(2, 8).setInitial(true);
		field.getCell(2, 10).setInitial(true);
		field.getCell(2, 11).setInitial(true);
		field.getCell(2, 12).setInitial(true);
		field.getCell(2, 14).setInitial(true);
		
		field.getCell(3, 0).setInitial(true);
		field.getCell(3, 3).setInitial(true);
		field.getCell(3, 4).setInitial(true);
		field.getCell(3, 5).setInitial(true);
		field.getCell(3, 6).setInitial(true);
		field.getCell(3, 9).setInitial(true);
		field.getCell(3, 11).setInitial(true);
		field.getCell(3, 13).setInitial(true);
		field.getCell(3, 14).setInitial(true);
		field.getCell(3, 15).setInitial(true);
				
		field.getCell(4, 0).setInitial(true);
		field.getCell(4, 4).setInitial(true);
		field.getCell(4, 5).setInitial(true);
		field.getCell(4, 6).setInitial(true);
		field.getCell(4, 7).setInitial(true);
		field.getCell(4, 8).setInitial(true);
		field.getCell(4, 11).setInitial(true);
		field.getCell(4, 15).setInitial(true);
		
		for (int i = 1; i < 16; ++i) field.getCell(5, i).setInitial(true);
		
		for (int i = 0; i < 16; ++i) {
			if (i != 1 && i != 3 && i != 5 && i != 11 && i != 14 && i != 15)
				field.getCell(6, i).setInitial(true);
		}
		
		for (int i = 0; i < 16; ++i) {
			if (i != 3 && i != 4 && i != 5 && i != 10 && i != 11 && i != 14)
				field.getCell(7, i).setInitial(true);
		}
		
		for (int i = 0; i < 16; ++i) {
			if (i != 3 && i != 4 && i != 5 && i != 10 && i != 12)
				field.getCell(8, i).setInitial(true);
		}
		
		for (int i = 0; i < 16; ++i) {
			if (i != 0 && i != 3 && i != 4 && i != 7 && i != 8 && i != 9 && i != 12 && i != 13 && i != 15) 
				field.getCell(9, i).setInitial(true);
		}
		
		for (int i = 0; i < 16; ++i) {
			if (i != 0 && i != 1 && i != 3 && i != 6 && i != 7 && i != 8 && i != 11 && i != 12 && i != 13 && i != 14 && i != 15) 
				field.getCell(10, i).setInitial(true);
		}
		
		for (int i = 0; i < 16; ++i) {
			if (i != 3 && i != 4 && i != 7) 
				field.getCell(11, i).setInitial(true);
		}
		
		for (int i = 0; i < 16; ++i) {
			if (i != 2 && i != 5 && i != 15) 
				field.getCell(12, i).setInitial(true);
		}
		
		for (int i = 0; i < 16; ++i) {
			if (i != 0 && i != 2 && i != 7 && i != 9 && i != 10 && i != 13 && i != 15) 
				field.getCell(13, i).setInitial(true);
		}
		
		for (int i = 0; i < 16; ++i) {
			if (i != 2 && i != 6 && i != 10 && i != 12 && i != 14) 
				field.getCell(14, i).setInitial(true);
		}
		
		for (int i = 0; i < 16; ++i) {
			if (i != 0 && i != 1 && i != 2 && i != 8 && i != 11 && i != 13 && i != 15) 
				field.getCell(15, i).setInitial(true);
		}
		
		Sudoku<DataCell> sudoku = new Sudoku<DataCell>(field, StandardRulesetFactory.getInstance().build16x16Ruleset());
		return sudoku;
		
	}
	
	public static Sudoku<DataCell> createTestSudoku9x9Easy() {
		FieldBuilder<DataCell> fb = new FieldBuilder<DataCell>();
		Field<DataCell> field = fb.build(new SquareStructure(9), new DataCellBuilder());
		int row = 0;
		field.getCell(row, 0).setValue(3);
		field.getCell(row, 1).setValue(1);
		field.getCell(row, 2).setValue(2);
		field.getCell(row, 3).setValue(4);
		field.getCell(row, 4).setValue(9);
		field.getCell(row, 5).setValue(8);
		field.getCell(row, 6).setValue(5);
		field.getCell(row, 7).setValue(6);
		field.getCell(row, 8).setValue(7);
		row++;
		field.getCell(row, 0).setValue(7);
		field.getCell(row, 1).setValue(8);
		field.getCell(row, 2).setValue(9);
		field.getCell(row, 3).setValue(1);
		field.getCell(row, 4).setValue(6);
		field.getCell(row, 5).setValue(5);
		field.getCell(row, 6).setValue(2);
		field.getCell(row, 7).setValue(3);
		field.getCell(row, 8).setValue(4);
		row++;
		field.getCell(row, 0).setValue(5);
		field.getCell(row, 1).setValue(6);
		field.getCell(row, 2).setValue(4);
		field.getCell(row, 3).setValue(3);
		field.getCell(row, 4).setValue(7);
		field.getCell(row, 5).setValue(2);
		field.getCell(row, 6).setValue(1);
		field.getCell(row, 7).setValue(9);
		field.getCell(row, 8).setValue(8);
		row++;
		field.getCell(row, 0).setValue(8);
		field.getCell(row, 1).setValue(4);
		field.getCell(row, 2).setValue(7);
		field.getCell(row, 3).setValue(2);
		field.getCell(row, 4).setValue(3);
		field.getCell(row, 5).setValue(9);
		field.getCell(row, 6).setValue(6);
		field.getCell(row, 7).setValue(1);
		field.getCell(row, 8).setValue(5);
		row++;
		field.getCell(row, 0).setValue(1);
		field.getCell(row, 1).setValue(5);
		field.getCell(row, 2).setValue(3);
		field.getCell(row, 3).setValue(6);
		field.getCell(row, 4).setValue(8);
		field.getCell(row, 5).setValue(4);
		field.getCell(row, 6).setValue(9);
		field.getCell(row, 7).setValue(7);
		field.getCell(row, 8).setValue(2);
		row++;
		field.getCell(row, 0).setValue(9);
		field.getCell(row, 1).setValue(2);
		field.getCell(row, 2).setValue(6);
		field.getCell(row, 3).setValue(7);
		field.getCell(row, 4).setValue(5);
		field.getCell(row, 5).setValue(1);
		field.getCell(row, 6).setValue(4);
		field.getCell(row, 7).setValue(8);
		field.getCell(row, 8).setValue(3);
		row++;
		field.getCell(row, 0).setValue(2);
		field.getCell(row, 1).setValue(7);
		field.getCell(row, 2).setValue(5);
		field.getCell(row, 3).setValue(9);
		field.getCell(row, 4).setValue(1);
		field.getCell(row, 5).setValue(3);
		field.getCell(row, 6).setValue(8);
		field.getCell(row, 7).setValue(4);
		field.getCell(row, 8).setValue(6);
		row++;
		field.getCell(row, 0).setValue(6);
		field.getCell(row, 1).setValue(9);
		field.getCell(row, 2).setValue(8);
		field.getCell(row, 3).setValue(5);
		field.getCell(row, 4).setValue(4);
		field.getCell(row, 5).setValue(7);
		field.getCell(row, 6).setValue(3);
		field.getCell(row, 7).setValue(2);
		field.getCell(row, 8).setValue(1);
		row++;
		field.getCell(row, 0).setValue(4);
		field.getCell(row, 1).setValue(3);
		field.getCell(row, 2).setValue(1);
		field.getCell(row, 3).setValue(8);
		field.getCell(row, 4).setValue(2);
		field.getCell(row, 5).setValue(6);
		field.getCell(row, 6).setValue(7);
		field.getCell(row, 7).setValue(5);
		field.getCell(row, 8).setValue(9);
		
		field.getCell(0, 1).setInitial(true);
		field.getCell(0, 3).setInitial(true);
		field.getCell(0, 6).setInitial(true);
		
		field.getCell(1, 1).setInitial(true);
		field.getCell(1, 7).setInitial(true);
		
		field.getCell(2, 5).setInitial(true);
		field.getCell(2, 6).setInitial(true);
		field.getCell(2, 8).setInitial(true);
		
		field.getCell(3, 1).setInitial(true);
		field.getCell(3, 2).setInitial(true);
		field.getCell(3, 3).setInitial(true);
		
		field.getCell(4, 3).setInitial(true);
		field.getCell(4, 5).setInitial(true);
		field.getCell(4, 6).setInitial(true);
		
		field.getCell(5, 0).setInitial(true);
		field.getCell(5, 5).setInitial(true);
		field.getCell(5, 7).setInitial(true);
		field.getCell(5, 8).setInitial(true);
		
		field.getCell(6, 0).setInitial(true);
		field.getCell(6, 6).setInitial(true);
		field.getCell(6, 8).setInitial(true);
		
		field.getCell(7, 3).setInitial(true);
		field.getCell(7, 5).setInitial(true);
		field.getCell(7, 7).setInitial(true);
		
		field.getCell(8, 0).setInitial(true);
		field.getCell(8, 6).setInitial(true);
		field.getCell(8, 8).setInitial(true);
		
		Sudoku<DataCell> sudoku = new Sudoku<DataCell>(field, StandardRulesetFactory.getInstance().build9x9Ruleset());
		return sudoku;
		
	}
	
	private static void saveSingleplayerGame(SingleplayerGameState game, File saveLocation) throws IOException {
		assert game != null && saveLocation != null && saveLocation.exists() && saveLocation.canWrite();
		
		FileOutputStream fileOutputStream = null;
		ObjectOutputStream outputStream = null;
		try {
			fileOutputStream = new FileOutputStream(saveLocation);
			outputStream = new ObjectOutputStream(fileOutputStream);
			outputStream.writeObject(game);
		} catch (IOException e) {
			throw e;
		}
		finally {
			if (outputStream != null) outputStream.close();
			if (fileOutputStream != null) fileOutputStream.close();
		}
	}
	
	private static void saveMultiplayerGame(GameState game, File saveLocation) throws IOException {
		assert game != null && saveLocation != null && saveLocation.exists() && saveLocation.canWrite();
		
		FileOutputStream fileOutputStream = null;
		ObjectOutputStream outputStream = null;
		try {
			fileOutputStream = new FileOutputStream(saveLocation);
			outputStream = new ObjectOutputStream(fileOutputStream);
			outputStream.writeObject(game);
		} catch (IOException e) {
			throw e;
		}
		finally {
			if (outputStream != null) outputStream.close();
			if (fileOutputStream != null) fileOutputStream.close();
		}
	}
	
	/** Indicates whether a saved singleplayer game exists.
	 * @return {@code true} if a save game exists, otherwise {@code false}
	 */
	public boolean hasSingleplayerGame() {
		return this.spGameFile.exists();
	}
	
	/** Indicates whether a saved singleplayer game exists.
	 * @return {@code true} if a save game exists, otherwise {@code false}
	 */
	public boolean hasMultiplayerGame() {
		return this.mpGameFile.exists();
	}
	
	/**
	 * Saves the given singleplayer game to a file and overwrites the last saved one.
	 *
	 * @param gameState {@link SingleplayerGameState} to save.
	 * @throws IllegalArgumentException if given game was {@code null}
	 * @see FileIO#loadSingleplayerGame()
	 */
	@Override
	public void saveSingleplayerGame(SingleplayerGameState gameState) throws IllegalArgumentException {
		if (gameState == null) {
			throw new IllegalArgumentException("game to save cannot be null.");
		}
		try {
			saveSingleplayerGame(gameState, this.spGameFile);
		} catch (IOException e) {
			DebugHelper.log(DebugHelper.PackageName.FileIO,"error during saveSingleplayerGame: " + e.getMessage());
		}
	}

	/**
	 * Loads the last saved singleplayer game from a file.
	 *
	 * @return A reference to a {@link SingleplayerGameState} or <code>null</code> if no game was found. 
	 * @see FileIO#saveSingleplayerGame(SingleplayerGameState)
	 */
	@Override
	public SingleplayerGameState loadSingleplayerGame() {
		SingleplayerGameState result = null;
		if (hasSingleplayerGame()) {
			FileInputStream fileInputStream = null;
			ObjectInputStream inputStream = null;
			try {
				fileInputStream = new FileInputStream(this.spGameFile);
				inputStream = new ObjectInputStream(fileInputStream);
				result = (SingleplayerGameState) inputStream.readObject();
			} catch (IOException e) {
				DebugHelper.log(DebugHelper.PackageName.FileIO,"IOException during loadSingleplayerGame: " + e.getMessage());
				result = null;
			} catch (ClassNotFoundException ex) {
				DebugHelper.log(DebugHelper.PackageName.FileIO,"ClassNotFoundException during loadSingleplayerGame: " + ex.getMessage());
				result = null;
			} finally {
				try {
					if (inputStream != null) inputStream.close();
				} catch (IOException e) {
					DebugHelper.log(DebugHelper.PackageName.FileIO,"error during stream close of loadSingleplayerGame: " + e.getMessage());
				}
				try {
					if (fileInputStream != null) fileInputStream.close();
				} catch (IOException e) {
					DebugHelper.log(DebugHelper.PackageName.FileIO,"error during stream close of loadSingleplayerGame: " + e.getMessage());
				}
			}
		}
		return result;
	}

	/**
	 * Deletes the saved singleplayer stored in a file.
	 * @return {@code true} if no singleplayer game was found to delete or
	 * was found and deleted successfully,<br>
	 * {@code false} if the delete operation was not successful
	 * @see FileIO#saveSingleplayerGame(SingleplayerGameState)
	 * @see FileIO#loadSingleplayerGame()
	 */
	public boolean deleteSingleplayerGame() {
		boolean result = true;
		if (hasSingleplayerGame()) {
			result = this.spGameFile.delete();
		}
		return result;
	}
	
	/**
	 * Deletes the saved multiplayer stored in a file.
	 * @return {@code true} if no multiplayer game was found to delete or
	 * was found and deleted successfully,<br>
	 * {@code false} if the delete operation was not successful
	 * @see FileIO#saveMultiplayerGame(GameState)
	 * @see FileIO#loadMultiplayerGame()
	 */
	public boolean deleteMultiplayerGame() {
		boolean result = true;
		if (hasMultiplayerGame()) {
			result = this.mpGameFile.delete();
		}
		return result;
	}
		
	/**
	 * Saves the given multiplayer game to a file and overwrites the last saved one.
	 *
	 * @param gameState {@link GameState} to save.
	 * @throws IllegalArgumentException if given game was {@code null}
	 * @see FileIO#loadMultiplayerGame()
	 */
	@Override
	public void saveMultiplayerGame(GameState gameState) throws IllegalArgumentException {
		if (gameState == null) {
			throw new IllegalArgumentException("game to save cannot be null.");
		}
		try {
			saveMultiplayerGame(gameState, this.mpGameFile);
		} catch (IOException e) {
			DebugHelper.log(DebugHelper.PackageName.FileIO,"error during saveSingleplayerGame: " + e.getMessage());
		}
	}

	/**
	 * Loads the last saved multiplayer game from a file.
	 *
	 * @return A reference to a {@link GameState} or <code>null</code> if no game was found. 
	 * @see FileIO#saveMultiplayerGame(GameState)
	 */
	@Override
	public GameState loadMultiplayerGame() {
		GameState result = null;
		if (hasMultiplayerGame()) {
			FileInputStream fileInputStream = null;
			ObjectInputStream inputStream = null;
			try {
				fileInputStream = new FileInputStream(this.mpGameFile);
				inputStream = new ObjectInputStream(fileInputStream);
				result = (GameState) inputStream.readObject();
			} catch (IOException e) {
				result = null;
			} catch (ClassNotFoundException ex) {
				result = null;
			} finally {
				try {
					if (inputStream != null) inputStream.close();
				} catch (IOException e) {
					DebugHelper.log(DebugHelper.PackageName.FileIO,"error during stream close of loadMultiplayerGame: " + e.getMessage());
				}
				try {
					if (fileInputStream != null) fileInputStream.close();
				} catch (IOException e) {
					DebugHelper.log(DebugHelper.PackageName.FileIO,"error during stream close of loadMultiplayerGame: " + e.getMessage());
				}
			}
		}
		return result;
	}
}
