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

import java.text.DecimalFormat;
import java.util.List;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.sudowars.DebugHelper;
import org.sudowars.R;
import org.sudowars.Controller.Local.Constants;
import org.sudowars.Controller.Local.FontFitTextView;
import org.sudowars.Model.CommandManagement.*;
import org.sudowars.Model.CommandManagement.GameCommands.GiveUpCommand;
import org.sudowars.Model.Game.Game;
import org.sudowars.Model.Game.GameAbortedEvent;
import org.sudowars.Model.Game.GameAbortedEventListener;
import org.sudowars.Model.Game.GameCell;
import org.sudowars.Model.Game.GameChangedEvent;
import org.sudowars.Model.Game.GameChangedEventListener;
import org.sudowars.Model.Game.GameFinishedEvent;
import org.sudowars.Model.Game.GameFinishedEventListener;
import org.sudowars.Model.Game.Player;
import org.sudowars.Model.Game.StopWatchTickEventListener;
import org.sudowars.Model.Sudoku.Field.Cell;
import org.sudowars.Model.SudokuManagement.IO.FileIO;
import org.sudowars.Model.SudokuUtil.GameState;
import org.sudowars.Model.SudokuUtil.NoteManager;
import org.sudowars.Model.SudokuUtil.NoteManagerChangedEvent;
import org.sudowars.Model.SudokuUtil.NoteManagerChangedEventListener;
import org.sudowars.View.*;

/**
 * Shows a running Sudoku game.
 */
public abstract class Play extends PoolBinder {
	private final int[] keyIDs = {
			R.id.key1,
			R.id.key2,
			R.id.key3,
			R.id.key4,
			R.id.key5,
			R.id.key6,
			R.id.key7,
			R.id.key8,
			R.id.key9,
			R.id.key10,
			R.id.key11,
			R.id.key12,
			R.id.key13,
			R.id.key14,
			R.id.key15,
			R.id.key16
	};
	
	/**
	 * Constants like the height of the status bar
	 */
	protected Constants constants;

    /**
     * Workaround for Issue 11833 (https://code.google.com/p/android/issues/detail?id=11833)
     */
    private boolean firstMove = true;
	
	/**
	 * the sudoku field
	 */
	protected SudokuField sudokuField;
	
	/**
	 * the time of last touch
	 */
	protected long lastTouchTime;
	
	/**
	 * the button of last touch
	 * 
	 * if the value is bigger or equal than 0, it's a btnSymbols Button,
	 * -1 conforms no Button is selected,
	 * -2 conforms btnClear Button and
	 * -3 conforms btnInvert Button
	 */
	protected int lastUsedSymbolId;
	
	/**
	 * the symbol of the toggled button
	 * 
	 * if the value is bigger or equal than 0, it's a btnSymbols Button,
	 * -1 conforms no Button is selected,
	 * -2 conforms btnClear Button and
	 * -3 conforms btnInvert Button
	 */
	protected int toggledSymbolId;
	
	/**
	 * the played time
	 */
	protected TextView lblTime;

	/**
	 * the symbols for the Sudoku game
	 */
	private Button[] btnSymbols;

	/**
	 * the invert button to invert the symboles in a {@link Cell}
	 */
	private SudokuButton btnInvert;

	/**
	 * the delete button to delete the symboles in a {@link Cell}
	 */
	private ImageButton btnClear;
	
	/**
	 * status of availability of a saved singleplayer game
	 */
	protected FileIO savedGames;
	
	/**
	 * the game to commence or open new
	 */
	protected Game game;
	
	/**
	 * encapsulates the current game and its properties
	 */
	protected GameState gameState;
	
	/**
	 * the delta manager for undo and redo function in singleplayer
	 */
	protected DeltaManager deltaManager;

	/**
	 * the note manager of local player
	 */
	protected NoteManager noteManager;

	/**
	 * the local player
	 */
	protected Player localPlayer;
	
	/**
	 * the preferences
	 */
	protected SharedPreferences preferences;
	
	/**
	 * the vibrator
	 */
	protected Vibrator vibrator;
	
	/**
	 * the last notification
	 */
	private long lastNotificationTime;
	
	/**
	 * the root view
	 */
	protected LinearLayout rootView;
	
	/**
	 * the field size
	 */
	protected TableLayout keypadView;
	
	/**
	 * the field size
	 */
	private int size;
	
	/*
	 * indicates that the assistant is runing (in SPgame). nothing is allowed if this is true
	 */
	protected boolean assistantRunning = false;
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	    ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	    actionBar.setDisplayShowCustomEnabled(true);
	    actionBar.setDisplayShowTitleEnabled(false);
	    
		this.constants = new Constants(this);
		this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		this.lastNotificationTime = 0;
		this.lastTouchTime = 0;
		this.lastUsedSymbolId = -1;
		this.toggledSymbolId = -1;
		
		if (this.gameState == null) {
			throw new IllegalAccessError("Given gameState is null.");
		}
		
		if (this.gameState.getGame() != null) {
			this.game = this.gameState.getGame();
		} else {
			throw new IllegalAccessError("Given game is null.");
		}
		
		this.localPlayer = this.game.getPlayers().get(0);
		this.noteManager = this.game.getNoteManagerOfPlayer(this.localPlayer);
		this.size = this.game.getSudoku().getField().getStructure().getHeight();
		
		this.setupView();
		
		this.preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		
		if (preferences.getBoolean("vibrator", true)) {
			this.vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		}
		
		this.noteManager.addOnChangeListener(new NoteManagerChangedEventListener() {
					@Override
					public void onChange(NoteManagerChangedEvent event) {
						refresh();
					}
				});
		
		this.game.addOnChangeListener(new GameChangedEventListener() {
				@Override
				public void onGameChanged(GameChangedEvent event) {
					refresh();
				}
			});
		
		this.game.addOnSuccessfullyFinishListener(new GameFinishedEventListener() {
				@Override
				public void onGameSuccessfullyFinish(GameFinishedEvent eventData) {
					Play.this.vibrate(Play.this.getResources().getInteger(R.integer.vibrate_game_on_finish));
					onGameFinished(getString(R.string.text_game_over));
				}
			});
		
		this.game.addOnGameAbortListener(new GameAbortedEventListener() {
				@Override
				public void onGameAborted(GameAbortedEvent eventData) {
					Play.this.vibrate(Play.this.getResources().getInteger(R.integer.vibrate_game_on_aborted));
					Play.this.onGameAborted();
				};
			});
		
		this.game.addOnStopWatchTickListener(new StopWatchTickEventListener() {
				@SuppressWarnings("synthetic-access")
				@Override
				public void onTick(int tickCount, long elapsedMilliseconds) {
					refreshTime(elapsedMilliseconds);
				}
			});
		
		DebugHelper.log(DebugHelper.PackageName.Play, "Size: " + this.gameState.getFieldStructure().toString()
				+ " Difficulty: " + this.gameState.getDifficulty().toString());
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		
		if (this.gameState.isFinished()) {
			refreshTime(this.game.getGameTime());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		super.onPause();
		
		this.saveGame();
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onConfigurationChanged(android.content.res.Configuration)
	 */
	@Override
    public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		
		this.constants = new Constants(this);
		this.setupView();
		
		if (this.gameState.isFinished()) {
			this.onGameFinished(getString(R.string.text_game_over));
		}
    }
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
    public boolean onCreateOptionsMenu(Menu menu) {
        View view = (View) menu.findItem(R.id.menu_time).getActionView();
		this.lblTime = (TextView) view.findViewById(R.id.time);
		this.refreshTime(this.game.getGameTime());
		
        return super.onCreateOptionsMenu(menu);
    }
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onPrepareOptionsMenu(android.view.Menu)
	 */
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		
		if (!this.game.isPaused() && !this.gameState.isFinished()) {
			menu.findItem(R.id.menu_give_up).setEnabled(true);
		} else {
			menu.findItem(R.id.menu_give_up).setEnabled(false);
		}
		
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			this.onBackPressed();
			return true;
		} else if (item.getItemId() == R.id.menu_give_up) {
			if (!this.game.isPaused() && !this.gameState.isFinished()) {
				showDialog(1);
			}
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreateDialog(int)
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		AlertDialog alert;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
        switch(id) { 
	        //query for solving all cells
	        case 1:
	        	builder.setIcon(R.drawable.ic_menu_myplaces);
	        	builder.setTitle(getString(R.string.give_up));
	        	builder.setMessage(getString(R.string.text_give_up));
	        	builder.setNegativeButton(getString(R.string.button_no), new DialogInterface.OnClickListener() {
	        			public void onClick(DialogInterface dialog, int id) {
	        				return;
	        			}
	       	    	});
	        	builder.setPositiveButton(getString(R.string.button_yes), new DialogInterface.OnClickListener() {
	 	           		public void onClick(DialogInterface dialog, int id) {
	 	           			onGivingUp();
	 	           			
	 	           			return;
	 	           		}
	        	});
	        	alert = builder.create();
	        	break;
	        	 
	        default:
				alert = null;
        }
        
        return alert;
    }
	
	/**
	 * Refresh the settings of buttons
	 */
	protected void refresh() {
		if (this.btnSymbols != null && this.btnClear != null || this.btnInvert != null) {
			GameCell selectedCell = this.sudokuField.getSelectedCell();
		
			for (int i = 0; i < this.btnSymbols.length; i++) {
				this.btnSymbols[i].setTextColor(this.getResources().getColor(R.color.button_symbols_normal_foreground));
				this.btnSymbols[i].setBackgroundColor(this.getResources().getColor(R.color.button_symbols_normal_background));
			}

            this.btnClear.setColorFilter(this.getResources().getColor(R.color.button_clear_normal_foreground), PorterDuff.Mode.MULTIPLY);
			this.btnClear.setBackgroundColor(this.getResources().getColor(R.color.button_clear_normal_background));
			this.btnInvert.setTextColor(this.getResources().getColor(R.color.button_invert_normal_foreground));
			this.btnInvert.setBackgroundColor(this.getResources().getColor(R.color.button_invert_normal_background));
	
			if (selectedCell != null) {
				if (selectedCell.isSet()) {
					this.btnSymbols[selectedCell.getValue() - 1].setTextColor(this.getResources().getColor(R.color.button_symbols_cell_value_foreground));
					this.btnSymbols[selectedCell.getValue() - 1].setBackgroundColor(this.getResources().getColor(R.color.button_symbols_cell_value_background));
				} else {
					List<Integer> selectedNotes = this.noteManager.getNotes(selectedCell);
		
					if (selectedNotes != null) {
						for (Integer note : selectedNotes) {
							this.btnSymbols[note - 1].setTextColor(this.getResources().getColor(R.color.button_symbols_notice_foreground));
							this.btnSymbols[note - 1].setBackgroundColor(this.getResources().getColor(R.color.button_symbols_notice_background));
						}
					}
				}
			}
			
			if (this.toggledSymbolId > -1) {
				if (selectedCell != null && toggledSymbolId == selectedCell.getValue() - 1) {
					this.btnSymbols[this.toggledSymbolId].setTextColor(this.getResources().getColor(R.color.button_symbols_toggled_and_cell_value_foreground));
					this.btnSymbols[this.toggledSymbolId].setBackgroundColor(this.getResources().getColor(R.color.button_symbols_toggled_and_cell_value_background));
				} else {
					this.btnSymbols[this.toggledSymbolId].setTextColor(this.getResources().getColor(R.color.button_symbols_toggled_foreground));
					this.btnSymbols[this.toggledSymbolId].setBackgroundColor(this.getResources().getColor(R.color.button_symbols_toggled_background));
				}
			} else if (this.toggledSymbolId == -2) {
                this.btnClear.setColorFilter(this.getResources().getColor(R.color.button_clear_toggled_foreground), PorterDuff.Mode.MULTIPLY);
				this.btnClear.setBackgroundColor(this.getResources().getColor(R.color.button_clear_toggled_background));
			} else if (this.toggledSymbolId == -3) {
				this.btnInvert.setTextColor(this.getResources().getColor(R.color.button_invert_toggled_foreground));
				this.btnInvert.setBackgroundColor(this.getResources().getColor(R.color.button_invert_toggled_background));
			}
		}
	}
	
	/**
	 * Refresh the timer
	 * 
	 * @param elapsedMilliseconds the play time in milliseconds
	 */
	private void refreshTime(long elapsedMilliseconds) {
		if (this.lblTime != null) {
			final DecimalFormat format = new DecimalFormat("00");
			
			byte seconds = (byte) ((elapsedMilliseconds / 1000) % 60);
			byte minutes = (byte) ((elapsedMilliseconds / 60000) % 60);
			//show only unit position of hour
			byte hours = (byte) ((elapsedMilliseconds / 3600000) % 10);
			String text = "";
			boolean leadingZero = false;
			
			if (hours > 0) {
				text += ""+hours+":";
				leadingZero = true;
			}
			
			if (leadingZero) {
				text += String.format("%s:", format.format(minutes));
			} else {
				text += minutes+":";
				leadingZero = true;
			}
			
			text += String.format("%s", format.format(seconds));
			
			this.lblTime.setText(text);
		}
	}
	
	/**
	 * Running on a click on one button of the symbol buttons.
	 * 
	 * @param symbolId the id of the symbol
	 * 
	 * @return true, if the execution was successful
	 */
	protected boolean onSymbolToggled(int symbolId) {
		return this.isEditable(this.sudokuField.getSelectedCell());
	}
	
	/**
	 * Running on a long click on one button of the symbol buttons.
	 * 
	 * @param symbolId the id of the symbol
	 */
	protected boolean onSymbolLongPress(int symbolId) {
		return this.isEditable(this.sudokuField.getSelectedCell());
	}

	/**
	 * Running on a click on button {@link btnInvert}.
	 */
	protected boolean onBtnInvertClick() {
		return this.isEditable(this.sudokuField.getSelectedCell());
	}
	
	/**
	 * Running on a click on button {@link btnClear}.
	 */
	protected boolean onBtnClearClick() {
		return this.isEditable(this.sudokuField.getSelectedCell());
	}
	
	/**
	 * Running on finished game
	 */
	protected void onGameFinished(String text) {
		sudokuField.setDisabled(true);
		this.gameState.gameFinished();
		
		FontFitTextView lblText = new FontFitTextView(this);
		lblText.setText(text);
		
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		lp.weight = 1;
		
		lblText.setLayoutParams(lp);
		lblText.setPadding(16, 16, 16, 16);
		lblText.setTextColor(this.getResources().getColor(R.color.text_game_over));
		lblText.setGravity(Gravity.CENTER);
		lblText.setAlpha(0f);
		
		final TextView goodbye = lblText;
		this.keypadView.animate()
			.alpha(0f)
			.setDuration(this.getResources().getInteger(R.integer.fade_in_game_finish_text))
			.setListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
						rootView.removeView(Play.this.keypadView);
						rootView.addView(goodbye);
						goodbye.animate()
				    			.alpha(1f)
				    			.setDuration(Play.this.getResources().getInteger(R.integer.fade_in_game_finish_text))
				    			.setListener(null);
				}
		});
	}
	
	/**
	 * Runs, when a player give up the game
	 */
	protected void onGivingUp() {
		GiveUpCommand command = new GiveUpCommand();
		command.execute(game, localPlayer);
	}
	
	/**
	 * Running on finished game
	 */
	protected void onGameAborted() {
		this.onGameFinished(getString(R.string.text_game_over));
	}
	
	/**
	 * Returns true, if this cell is editable, otherwise false
	 * 
	 * @param selectedCell the symbol id
	 * 
	 * @return true, if this cell is editable 
	 */
	protected boolean isEditable(GameCell selectedCell) {
		boolean error = false;
		
		if (selectedCell == null) {
			this.notificate(R.string.notification_unselected_field, Toast.LENGTH_SHORT);
			error = true;
		}

		if (!error && selectedCell.isInitial()) {
			this.notificate(R.string.notification_initial_field, Toast.LENGTH_SHORT);
			error = true;
		}
		
		if (!error && assistantRunning) {
			this.notificate(R.string.notification_assistant_running, Toast.LENGTH_SHORT);
			error = true;
		}
		
		return !error;
	}
	
	/**
	 * Save the game, if the game is not finished,
	 * otherwise delete the game
	 */
	abstract protected void saveGame();
	
	/**
	 * Setup view
	 */
	protected void setupView() {
		if (size == 9) {
			setContentView(R.layout.play_9);
		} else {
			setContentView(R.layout.play_16);
		}
		
		this.keypadView = (TableLayout) findViewById(R.id.keypad);
		this.sudokuField = (SudokuField) findViewById(R.id.sudoku_field);
		this.sudokuField.showInvalidValues(false);
		this.sudokuField.setGame(this.game);
		this.sudokuField.setNoteManager(this.noteManager);
		this.sudokuField.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (Play.this.toggledSymbolId > -1) {
					Play.this.onSymbolToggled(Play.this.toggledSymbolId);
				} else if (Play.this.toggledSymbolId == -2) {
					Play.this.onBtnClearClick();
				} else if (Play.this.toggledSymbolId == -3) {
					Play.this.onBtnInvertClick();
				}
				
				refresh();
			}
		});
		
		this.rootView = (LinearLayout) this.findViewById(R.id.root);
		
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		this.sudokuField.setZoomButtonsEnable(settings.getBoolean("zoom_buttons", false));
		
		setupButtons();
	}
	
	/**
	 * Setup buttons
	 */
	protected void setupButtons() {
		this.btnClear = (ImageButton) this.findViewById(R.id.key_clear);
		this.btnClear.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				long now = SystemClock.uptimeMillis();
				
				if (now - Play.this.lastTouchTime < Play.this.getResources().getInteger(R.integer.toggle_button_interval) && Play.this.lastUsedSymbolId == -2) {
					if (Play.this.toggledSymbolId == -2) {
						Play.this.toggledSymbolId = -1;
					} else {
						Play.this.toggledSymbolId = -2;
					}
					
					Play.this.refresh();
					Play.this.lastUsedSymbolId = -1;
				} else {
					if (Play.this.toggledSymbolId != -2) {
						onBtnClearClick();
					}
					
					Play.this.lastUsedSymbolId = -2;
					Play.this.toggledSymbolId = -1;
					Play.this.refresh();
				}
				
				Play.this.lastTouchTime = now;
			}
		});
		
		this.btnInvert = (SudokuButton) this.findViewById(R.id.key_invert);
		this.btnInvert.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				long now = SystemClock.uptimeMillis();
				
				if (now - Play.this.lastTouchTime < Play.this.getResources().getInteger(R.integer.toggle_button_interval) && Play.this.lastUsedSymbolId == -3) {
					if (Play.this.toggledSymbolId == -3) {
						Play.this.toggledSymbolId = -1;
					} else {
						Play.this.toggledSymbolId = -3;
					}
					
					Play.this.refresh();
					Play.this.lastUsedSymbolId = -1;
				} else {
					if (Play.this.toggledSymbolId != -3) {
						onBtnInvertClick();
					}
					
					Play.this.lastUsedSymbolId = -3;
					Play.this.toggledSymbolId = -1;
					Play.this.refresh();
				}
				
				Play.this.lastTouchTime = now;
			}
		});
		
		this.btnSymbols = new SudokuButton[size];
		
		for (int i = 0; i < size; i++) {
			this.btnSymbols[i] = (SudokuButton) this.findViewById(this.keyIDs[i]);
			this.btnSymbols[i].setText(this.getResources().getStringArray(R.array.symbols)[i+1]);

			final int symbolId = i;
			this.btnSymbols[i].setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					long now = SystemClock.uptimeMillis();
					
					if (now - Play.this.lastTouchTime < Play.this.getResources().getInteger(R.integer.toggle_button_interval) && Play.this.lastUsedSymbolId == symbolId) {
						if (Play.this.toggledSymbolId == symbolId) {
							Play.this.toggledSymbolId = -1;
						} else {
							Play.this.toggledSymbolId = symbolId;
						}
						
						Play.this.refresh();
						Play.this.lastUsedSymbolId = -1;
					} else {
						if (Play.this.toggledSymbolId != symbolId) {
							onSymbolToggled(symbolId);
						}
						
						Play.this.lastUsedSymbolId = symbolId;
						Play.this.toggledSymbolId = -1;
						Play.this.refresh();
					}
					
					Play.this.lastTouchTime = now;
				}
			});

			this.btnSymbols[i].setOnLongClickListener(new OnLongClickListener() {
				public boolean onLongClick(View v) {
					Play.this.toggledSymbolId = -1;
					onSymbolLongPress(symbolId);

					return true;
				}
			});
		}
	}
	
	/**
	 * Turn the vibrator on, if it is activate in settings
	 * 
	 * @param milliseconds The number of milliseconds to vibrate.
	 */
	protected void vibrate(long milliseconds) {
		if (this.vibrator != null) {
			this.vibrator.vibrate(milliseconds);
		}
	}
	
	/**
	 * Make a standard toast that just contains a text view with the text from a resource
	 * 
	 * @param resId The resource id of the string resource to use
	 * @param duration How long to display the message. Either LENGTH_SHORT or LENGTH_LONG
	 */
	protected void notificate(int resId, int duration) {
		long now = SystemClock.uptimeMillis();
		
		if (this.preferences.getBoolean("notifications", true) && now - this.lastNotificationTime > this.getResources().getInteger(R.integer.notification_interval)) {
			Toast.makeText(getApplicationContext(), resId, duration).show();
		}
		
		this.lastNotificationTime = now;
	}

    /**
     * Workaround for Issue 11833 (https://code.google.com/p/android/issues/detail?id=11833)
     */
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (this.firstMove) {
            this.firstMove = false;

            if (menu != null) {
                onPrepareOptionsMenu(menu);
            }
        }

        return super.onMenuOpened(featureId, menu);
    }
}
