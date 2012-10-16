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

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;

/**
 * Shows the menu of a new Singleplayer game.
 */
public class SingleplayerSettings extends PoolBinder {
	/**
	 * the RadioButton array for size
	 */
	private RadioButton[] rbtField_size;
	
	/**
	 * the RadioButton array for difficulty
	 */
	private RadioButton[] rbtDifficulty;
	
	/**
	 * state of showing obvious mistakes
	 */
	private CheckBox cbtAssistantObviousMistakes;
	
	/**
	 * show option menu entry to solving cells
	 */
	private CheckBox cbtAssistantSolveCells;
	
	/**
	 * show option menu entry to solving cells
	 */
	private CheckBox cbtAssistantBookmark;
	
	/**
	 * the start button to start a new Sudoku game
	 */
	private Button btnStart;
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.singleplayer_settings);
		setupButtons();
		
		SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
		int size = preferences.getInt("size", 0);
		int difficulty = preferences.getInt("difficulty", 1);
		
		this.cbtAssistantObviousMistakes.setChecked(preferences.getBoolean("assistantObviousMistakes", false));
		this.cbtAssistantSolveCells.setChecked(preferences.getBoolean("assistantSolveCells", false));
		this.cbtAssistantBookmark.setChecked(preferences.getBoolean("assistantBookmark", false));
		
		if (size < 0 || size > 1) {
			size = 0;
		}
		
		if (difficulty < 0 || difficulty > 2) {
			difficulty = 1;
		}
		
		this.rbtField_size[size].setChecked(true);
		this.rbtDifficulty[difficulty].setChecked(true);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.sudowars.Controller.Local.PoolBinder#onStop()
	 */
	protected void onStop() {
		super.onStop();
		
		SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		
		int difficulty;
		
		if (this.rbtDifficulty[0].isChecked() == true) {
			difficulty = 0;
		} else if (this.rbtDifficulty[1].isChecked() == true) {
			difficulty = 1;
		} else {
			difficulty = 2;
		}
		
		editor.putInt("size", (this.rbtField_size[0].isChecked() == true)?0:1);
		editor.putInt("difficulty", difficulty);
		editor.putBoolean("assistantObviousMistakes", this.cbtAssistantObviousMistakes.isChecked());
		editor.putBoolean("assistantSolveCells", this.cbtAssistantSolveCells.isChecked());
		editor.putBoolean("assistantBookmark", this.cbtAssistantBookmark.isChecked());
		
		editor.commit();
	}
	
	/**
	 * Running on a click on button {@link btnStart}.
	 */
	private void onBtnStartClick () {
		this.btnStart.setClickable(false);
		
		int size = (this.rbtField_size[0].isChecked() == true)?9:16;
		Difficulty difficulty;
		
		if (this.rbtDifficulty[0].isChecked() == true) {
			difficulty = new DifficultyEasy();
		} else if (this.rbtDifficulty[1].isChecked() == true) {
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
				this.cbtAssistantObviousMistakes.isChecked(),
				this.cbtAssistantSolveCells.isChecked(),
				this.cbtAssistantBookmark.isChecked(),
				new DeltaManager()));
		
		//Debug output
		int debugAssistants = this.cbtAssistantObviousMistakes.isChecked()?4:0;
		debugAssistants += this.cbtAssistantSolveCells.isChecked()?2:0;
		debugAssistants += this.cbtAssistantBookmark.isChecked()?1:0;
		
		DebugHelper.log(DebugHelper.PackageName.SingleplayerSettings, "Size: " + size + "x" + size
				+ " Difficulty: " + difficulty.toString()
				+ " Assistants: " + debugAssistants);
		
		Intent intent = new Intent(this, SingleplayerPlay.class);
		startActivity(intent);
		finish();
	}
	
	/**
	 * Setup buttons
	 */
	private void setupButtons() {
		this.rbtField_size = new RadioButton[2];
		this.rbtField_size[0] = (RadioButton) findViewById(R.id.rbtField_size_9x9);
		this.rbtField_size[1] = (RadioButton) findViewById(R.id.rbtField_size_16x16);
		
		this.rbtDifficulty = new RadioButton[3];
		this.rbtDifficulty[0] = (RadioButton) findViewById(R.id.rbtDifficulty_easy);
		this.rbtDifficulty[1] = (RadioButton) findViewById(R.id.rbtDifficulty_medium);
		this.rbtDifficulty[2] = (RadioButton) findViewById(R.id.rbtDifficulty_hard);
		this.cbtAssistantObviousMistakes = (CheckBox) findViewById(R.id.cbtAssistant_obvious_mistakes);
		this.cbtAssistantSolveCells = (CheckBox) findViewById(R.id.cbtAssistant_solve_cells);
		this.cbtAssistantBookmark = (CheckBox) findViewById(R.id.cbtAssistant_bookmark);
		
		this.btnStart = (Button) findViewById(R.id.btnStart);
		this.btnStart.setOnClickListener(
                new OnClickListener() {
                	public void onClick(View v) {
                		SingleplayerSettings.this.onBtnStartClick();
                    }
                });
	}
}