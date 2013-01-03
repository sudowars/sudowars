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
package org.sudowars.Controller.Local.Activity;

import org.sudowars.DebugHelper;
import org.sudowars.R;
import org.sudowars.Model.CommandManagement.DeltaManager;
import org.sudowars.Model.Difficulty.Difficulty;
import org.sudowars.Model.Difficulty.DifficultyEasy;
import org.sudowars.Model.Difficulty.DifficultyHard;
import org.sudowars.Model.Difficulty.DifficultyMedium;
import org.sudowars.Model.Game.Player;
import org.sudowars.Model.Game.SingleplayerGame;
import org.sudowars.Model.Sudoku.Sudoku;
import org.sudowars.Model.Sudoku.Field.Cell;
import org.sudowars.Model.Sudoku.Field.DataCell;
import org.sudowars.Model.Sudoku.Field.SquareStructure;
import org.sudowars.Model.SudokuManagement.IO.FileIO;
import org.sudowars.Model.SudokuUtil.NoteManager;
import org.sudowars.Model.SudokuUtil.SingleplayerGameState;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;

/**
 * Shows the menu of a new Singleplayer game.
 */
public class SingleplayerSettings extends Settings {
	/**
	 * the start button to start a new Sudoku game
	 */
	private Button btnStart;
	
	/**
	 * the preferences
	 */
	protected SharedPreferences preferences;
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	    ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	    
		addPreferencesFromResource(R.xml.singleplayer_preferences);
		
		this.preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu (Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.singleplayer_settings, menu);
	    return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected (MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			this.onBackPressed();
			return true;
		} else if (item.getItemId() == R.id.btStart) {
			onBtnStartClick ();
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}
	
	/**
	 * Running on a click on button {@link btnStart}.
	 */
	private void onBtnStartClick () {
		if (this.btnStart != null)
			this.btnStart.setClickable(false);

		Difficulty difficulty;
		
		/* Ugly Android bug...
		 * see https://code.google.com/p/android/issues/detail?id=2096
		 */
		int size = Integer.parseInt(this.preferences.getString("singleplayer_field_size",
											this.getResources().getString(R.string.settings_game_field_size_default)));
		int diffValue = Integer.parseInt(this.preferences.getString("singleplayer_difficulty",
											this.getResources().getString(R.string.settings_game_difficulty_default)));
		if (diffValue == 0) {
			difficulty = new DifficultyEasy();
		} else if (diffValue == 1) {
			difficulty = new DifficultyMedium();
		} else {
			difficulty = new DifficultyHard();
		}
		
		Sudoku<DataCell> sudoku = this.pool.extractSudoku(new SquareStructure(size), difficulty);
		
		SingleplayerGame game = new SingleplayerGame(
				new Sudoku<Cell>(sudoku.getField().convert(), sudoku.getDependencyManager()));
		Player player = new Player("singleplayer");
		game.setPlayer(player);

		NoteManager noteManager = new NoteManager();
		game.setNoteManagerOfPlayer(player, noteManager);
		
		FileIO savedGames = new FileIO(this.getApplicationContext());
		savedGames.saveSingleplayerGame(new SingleplayerGameState(game,
				difficulty,
				this.preferences.getBoolean("singleplayer_assistant_obvious_mistakes", true),
				this.preferences.getBoolean("singleplayer_assistant_solve_cells", true),
				this.preferences.getBoolean("singleplayer_assistant_bookmark", true),
				new DeltaManager()));
		
		//Debug output
		int debugAssistants = this.preferences.getBoolean("singleplayer_assistant_obvious_mistakes", true)?4:0;
		debugAssistants += this.preferences.getBoolean("singleplayer_assistant_solve_cells", true)?2:0;
		debugAssistants += this.preferences.getBoolean("singleplayer_assistant_bookmark", true)?1:0;
		
		DebugHelper.log(DebugHelper.PackageName.SingleplayerSettings, "Size: " + size + "x" + size
				+ " Difficulty: " + difficulty.toString()
				+ " Assistants: " + debugAssistants);
		
		Intent intent = new Intent(this, SingleplayerPlay.class);
		startActivity(intent);
		finish();
	}
}
