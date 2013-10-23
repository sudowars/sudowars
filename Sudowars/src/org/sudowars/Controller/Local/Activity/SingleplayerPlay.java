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

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import org.sudowars.DebugHelper;
import org.sudowars.R;
import org.sudowars.Model.CommandManagement.GameCommands.*;
import org.sudowars.Model.Game.GameCell;
import org.sudowars.Model.Game.SingleplayerGame;
import org.sudowars.Model.SudokuManagement.IO.FileIO;
import org.sudowars.Model.SudokuUtil.Assistant;
import org.sudowars.Model.SudokuUtil.SingleplayerGameState;

/**
 * Shows a running Sudoku game.
 */
public class SingleplayerPlay extends Play {
	
	private Handler assistantHandler = null;

	/*
	 * (non-Javadoc)
	 * @see org.sudowars.Controller.Local.Play#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		this.savedGames = new FileIO(this.getApplicationContext());
		this.gameState = this.savedGames.loadSingleplayerGame();
		
		super.onCreate(savedInstanceState);
		
		if (!(this.game instanceof SingleplayerGame)) {
			throw new IllegalStateException("Game is no instance of SingleplayerGame.");
		}
		
		this.deltaManager = ((SingleplayerGameState) this.gameState).getDeltaManager();

		//Debug output
		int debugAssistants = ((SingleplayerGameState) this.gameState).isShowObviousMistakesEnabled()?8:0;
		debugAssistants += ((SingleplayerGameState) this.gameState).isSolveCellEnabled()?4:0;
		debugAssistants += ((SingleplayerGameState) this.gameState).isBookmarkEnabled()?2:0;
		debugAssistants += ((SingleplayerGameState) this.gameState).isBackToFirstErrorEnabled()?1:0;
		
		DebugHelper.log(DebugHelper.PackageName.SingleplayerPlay, "Assistants: " + debugAssistants);
		assistantHandler = new Handler() {
			/*
	         * handleMessage() defines the operations to perform when
	         * the Handler receives a new Message to process.
	         */
	        @Override
	        public void handleMessage(Message inputMessage) {
	        	// Message Format: arg1 = cell number, negative = fail, arg2 = callvalue
	        	assistantRunning = false;
	        	if (inputMessage.arg1 >= 0) {
	        		SetCellValueCommand command = new SetCellValueCommand(gameState.getGame().getSudoku().getField().getCell(inputMessage.arg1), 
	        				inputMessage.arg2);
	        		if (command.execute(game, localPlayer)) {
						deltaManager.addDelta(gameState.getGame(), (GameCommand) command);
					}
	        	} else {
					Toast.makeText(getApplicationContext(), R.string.notification_assistant_failed, Toast.LENGTH_LONG).show();
	        	}
	        }
		};
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.sudowars.Controller.Local.Play#onResume()
	 */
	protected void onResume() {
		super.onResume();
		
		if (!this.gameState.isFinished()) {
			this.game.resumeGame(this.localPlayer);
			this.game.startGame();
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
		
		this.game.pauseGame(this.localPlayer);
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.singleplayer_play, menu);
		
		if (!((SingleplayerGameState) this.gameState).isSolveCellEnabled()) {
			menu.removeItem(R.id.menu_assistant);
		}
		
		if (!((SingleplayerGameState) this.gameState).isBookmarkEnabled()) {
			menu.removeItem(R.id.menu_get_bookmark);
			menu.removeItem(R.id.menu_set_bookmark);
		}
		
		if (!((SingleplayerGameState) this.gameState).isBackToFirstErrorEnabled()) {
			menu.removeItem(R.id.menu_go_back_to_first_error);
		}
		
		return super.onCreateOptionsMenu(menu);
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onConfigurationChanged(android.content.res.Configuration)
	 */
	@Override
    public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		
		this.refresh();
		
		if (!this.game.isPaused()) {
			this.game.startGame();
		}
    }
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onPrepareOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		
		if (!this.game.isPaused() && !this.gameState.isFinished()) {
			menu.findItem(R.id.menu_undo).setEnabled(this.deltaManager.hasBackwardDelta());
			menu.findItem(R.id.menu_redo).setEnabled(this.deltaManager.hasForwardDelta());
			
			if (((SingleplayerGameState) this.gameState).isSolveCellEnabled()) {
				menu.findItem(R.id.menu_assistant).setEnabled(true);
			}
			
			if (((SingleplayerGameState) this.gameState).isBookmarkEnabled()) {
                if (this.deltaManager.hasUnbookmarkedState() || !this.deltaManager.isBookmarkAvailable()) {
                    menu.findItem(R.id.menu_get_bookmark).setEnabled(this.deltaManager.isBookmarkAvailable());
                    menu.findItem(R.id.menu_set_bookmark).setEnabled(true);
                } else {
                    menu.findItem(R.id.menu_get_bookmark).setEnabled(false);
                    menu.findItem(R.id.menu_set_bookmark).setEnabled(false);
                }
			}
			
			if (((SingleplayerGameState) this.gameState).isBackToFirstErrorEnabled()) {
				menu.findItem(R.id.menu_go_back_to_first_error).setEnabled(this.deltaManager.isBackToFirstErrorAvailable());
			}
			
		} else {
			menu.findItem(R.id.menu_undo).setEnabled(false);
			menu.findItem(R.id.menu_redo).setEnabled(false);
			
			if (((SingleplayerGameState) this.gameState).isSolveCellEnabled()) {
				menu.findItem(R.id.menu_assistant).setEnabled(false);
			}
			
			if (((SingleplayerGameState) this.gameState).isBookmarkEnabled()) {
				menu.findItem(R.id.menu_get_bookmark).setEnabled(false);
				menu.findItem(R.id.menu_set_bookmark).setEnabled(false);
			}
			
			if (((SingleplayerGameState) this.gameState).isBackToFirstErrorEnabled()) {
				menu.findItem(R.id.menu_go_back_to_first_error).setEnabled(false);
			}
		}
		
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.sudowars.Controller.Local.Play#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {		
		if (item.getItemId() == R.id.menu_assistant) {
			return handleObjectItemAssistant();
		} else if (item.getItemId() == R.id.menu_get_bookmark) {
			if (((SingleplayerGameState) this.gameState).isBookmarkEnabled()
					&& !this.game.isPaused() && !this.gameState.isFinished()) {
				this.deltaManager.backToBookmark((SingleplayerGame) this.game, this.localPlayer);
			}
			return true;
		} else if (item.getItemId() == R.id.menu_set_bookmark) {
			if (((SingleplayerGameState) this.gameState).isBookmarkEnabled()&& !this.game.isPaused()
					&& !this.gameState.isFinished()) {
				this.deltaManager.setBookmark();
			}
			return true;
		} else if (item.getItemId() == R.id.menu_undo) {
			if (this.deltaManager.hasBackwardDelta() && !this.game.isPaused() && !this.gameState.isFinished()) {
				this.deltaManager.backward(this.game, this.localPlayer);
			}
			return true;
		} else if (item.getItemId() == R.id.menu_redo) {
			if (this.deltaManager.hasForwardDelta() && !this.game.isPaused() && !this.gameState.isFinished()) {
				this.deltaManager.forward(this.game, this.localPlayer);
			}
			return true;
		} else if (item.getItemId() == R.id.menu_go_back_to_first_error) {
			if (((SingleplayerGameState) this.gameState).isBackToFirstErrorEnabled()
					&& !this.game.isPaused() && !this.gameState.isFinished()) {
				if (this.deltaManager.backToFirstError((SingleplayerGame) this.game, this.localPlayer)) {
					//there was something to do
					notificate(R.string.notification_back_to_first_error_success, Toast.LENGTH_LONG);
					return true;
				} else {
					//there was no error
					notificate(R.string.notification_back_to_first_error_failed, Toast.LENGTH_LONG);
					return false;
				}
			}
			return true;
		}
		else {
			return super.onOptionsItemSelected(item);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.sudowars.Controller.Local.Play#onSymbolToggled(int)
	 */
	protected boolean onSymbolToggled(int symbolId) {
		boolean error = !super.onSymbolToggled(symbolId);
		
		if (!error) {
			GameCell selectedCell = this.sudokuField.getSelectedCell();			
			GameCommand command;
			if (selectedCell.isSet()) {
				command = new CompositeCommand();
				((CompositeCommand) command).pushCommand(new ClearCellCommand(selectedCell));
				((CompositeCommand) command).pushCommand(new AddNoteCommand(selectedCell, this.sudokuField.getSelectedCell().getValue()));
				
				if (selectedCell.getValue() != symbolId + 1) {
					((CompositeCommand) command).pushCommand(new AddNoteCommand(selectedCell, symbolId + 1));
				}
			} else {
				if (!this.game.getNoteManagerOfPlayer(this.localPlayer).hasNote(selectedCell, symbolId + 1)) {
					command = new AddNoteCommand(selectedCell, symbolId + 1);
				} else {
					command = new RemoveNoteCommand(selectedCell, symbolId + 1);
				}
			}
			if (command.execute(this.game, this.localPlayer)) {
				this.deltaManager.addDelta(this.game, command);
			} else {
				error = true;
			}
		}
		
		return !error;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.sudowars.Controller.Local.Play#onSymbolLongPress(int)
	 */
	protected boolean onSymbolLongPress(int symbolId) {
		boolean error = !super.onSymbolLongPress(symbolId);
		
		if (!error) {
			GameCell selectedCell = this.sudokuField.getSelectedCell();
			GameCommand command = null;
			
			if (!selectedCell.isSet() && this.game.getNoteManagerOfPlayer(localPlayer).getNotes(selectedCell).isEmpty()) {
				command = new CompositeCommand();
				((CompositeCommand) command).pushCommand(new ClearCellCommand(selectedCell));
				((CompositeCommand) command).pushCommand(new SetCellValueCommand(selectedCell, symbolId + 1));
			} else if (selectedCell.getValue() == symbolId + 1) {
				command = new ClearCellCommand(selectedCell);
			} else if (selectedCell.getValue() != symbolId + 1) {
				// handle the case here if new value is different than
				// current one
				command = new CompositeCommand();
				((CompositeCommand) command).pushCommand(new ClearCellCommand(selectedCell));
				((CompositeCommand) command).pushCommand(new SetCellValueCommand(selectedCell, symbolId + 1));
			} else {
				error = true;
			}
			
			if (!error && command.execute(this.game, this.localPlayer)) {
				this.deltaManager.addDelta(this.game, command);
			} else {
				error = true;
			}
		}
		
		return !error;
	}

    /*
     * (non-Javadoc)
     * @see org.sudowars.Controller.Local.Play#onBtnInvertClick()
     */
	protected boolean onBtnInvertClick() {
		boolean error = !super.onBtnInvertClick();
		
		if (!error) {
			InvertCellCommand command = new InvertCellCommand(this.sudokuField.getSelectedCell());
			
			if (command.execute(this.game, this.localPlayer)) {
				this.deltaManager.addDelta(this.game, command);
			} else {
				error = true;
			}
		}
		
		return !error;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.sudowars.Controller.Local.Play#onBtnClearClick()
	 */
	protected boolean onBtnClearClick() {
		boolean error = !super.onBtnInvertClick();
		GameCell selectedCell = this.sudokuField.getSelectedCell();
		
		if (!error && !(this.noteManager.hasNotes(selectedCell) && selectedCell.isSet())) {
			ClearCellCommand command = new ClearCellCommand(selectedCell);
			
			if (command.execute(this.game, this.localPlayer)) {
				this.deltaManager.addDelta(this.game, command);
			} else {
				error = true;
			}
		}
		
		return !error;
	}
	
	/**
	 * Execute the assistant
	 * 
	 * @return true, if assistant is finish
	 * 
	 * @throws IllegalArgumentException
	 */
	private boolean handleObjectItemAssistant() throws IllegalArgumentException {
		if (((SingleplayerGameState) this.gameState).isSolveCellEnabled() && !this.game.isPaused()
				&& !this.gameState.isFinished()) {
			if (!assistantRunning) {
				new Thread(new Assistant((SingleplayerGame) this.game, this.assistantHandler)).start();
				assistantRunning = true;
			} else {
				notificate(R.string.notification_assistant_running, Toast.LENGTH_SHORT);
			}
			
		}
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.sudowars.Controller.Local.Play#saveGame()
	 */
	protected void saveGame() {
		if (this.gameState.isFinished()) {
			this.savedGames.deleteSingleplayerGame();
		} else {
			this.savedGames.saveSingleplayerGame((SingleplayerGameState) this.gameState);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.sudowars.Controller.Local.Play#setupView()
	 */
	protected void setupView() {
		super.setupView();

		this.sudokuField.showInvalidValues(((SingleplayerGameState) this.gameState).isShowObviousMistakesEnabled());
	}
}
