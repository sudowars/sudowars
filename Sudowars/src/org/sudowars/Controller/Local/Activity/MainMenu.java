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

import org.sudowars.R;
import org.sudowars.Controller.Local.Constants;
import org.sudowars.Model.SudokuManagement.IO.FileIO;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

/**
 * Shows the main menu.
 */
public class MainMenu extends PoolBinder {
	/**
	 * Intent request code
	 */
    private static final int REQUEST_ENABLE_BT = 3;
	
	/**
	 * Constants like the height of the status bar
	 */
	private Constants constants;
    
	/**
	 * the singleplayer button to start a new singeplayer game
	 */
	private Button btnSingleplayer;
	
	/**
	 * Button to continue the last unfinished singleplayer game
	 */
	private Button btnSingleplayerContinue;
	
	/**
	 * the multiplayer button to start a new multiplayer game
	 */
	private Button btnMultiplayer;
	
	/**
	 * the help button to show help for the game
	 */
	private Button btnManual;
	
	/**
	 * the singleplayer buttons including new game and commence button
	 */
	private LinearLayout layBtnSingleplayer;
	
	/**
	 * status of availability of a saved singleplayer game
	 */
	private FileIO savedGames;
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main_menu);
		
		SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
		
		if (preferences.getBoolean("firstStart", true)) {
			//Check, if this App is running in an emulator
			if (Build.PRODUCT.equals("sdk")) {
				PreferenceManager.setDefaultValues(getApplicationContext(), R.xml.preferences_emulator, true);
			}
			
			this.onBtnManualClick();
		}
		
		this.savedGames = new FileIO(this.getApplicationContext());
		this.constants = new Constants(this);
		
		this.setupButtons();
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	protected void onResume() {
		super.onResume();
		
		if (this.savedGames.hasSingleplayerGame() && this.layBtnSingleplayer.getChildCount() == 1) {
			// Get the screen's density scale
			final float scale = getResources().getDisplayMetrics().density;
			// Convert the dps to pixels, based on density scale
			int buttonWidth = (int) (60.0f * scale + 0.5f);
			
			this.layBtnSingleplayer.addView(this.btnSingleplayerContinue, 0);
			this.btnSingleplayer.setLayoutParams(new LinearLayout.LayoutParams(
					buttonWidth,
					LinearLayout.MarginLayoutParams.MATCH_PARENT));
			this.btnSingleplayer.setText(getString(R.string.button_singleplayer_symbol));
		} else if (!this.savedGames.hasSingleplayerGame() && this.layBtnSingleplayer.getChildCount() == 2) {
			this.layBtnSingleplayer.removeView(this.btnSingleplayerContinue);
			this.btnSingleplayer.setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.MarginLayoutParams.MATCH_PARENT,
					LinearLayout.MarginLayoutParams.MATCH_PARENT));
			this.btnSingleplayer.setText(getString(R.string.button_singleplayer));
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.sudowars.Controller.Local.PoolBinder#onStop()
	 */
	protected void onStop() {
		super.onStop();
		
		SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		
		editor.putBoolean("firstStart", false);
		
		editor.commit();
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu (Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
	    return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected (MenuItem item) {
		if (item.getItemId() == R.id.menu_settings) {
			Intent intent = new Intent(this, MainSettings.class);
			startActivity(intent);
			return true;
		} else if (item.getItemId() == R.id.menu_about) {
			onAboutDialog();
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}
	
	/*
	 * Create an about dialog
	 */
	private void onAboutDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		LayoutInflater inflater = LayoutInflater.from(MainMenu.this);
        View layout = inflater.inflate(R.layout.about, (ScrollView) findViewById(R.id.about));
    	
		builder.setTitle(R.string.about_title)
			.setView(layout)
			.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			}).show();
	}
	
	/*
	 * Create a dialog to allow bluetooth activation
	 */
	private void onBluetoothActivationDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		builder.setTitle(getString(R.string.warning))
    		.setMessage(getString(R.string.text_bluetooth_not_available))
    		.setNegativeButton(getString(R.string.button_ok), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					return;
				}
    		}).show(); 
    }
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
		
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
            	startMultiplayerMenu();
            } else {
            	Toast.makeText(getApplicationContext(),R.string.text_multiplayer_menu_cancled,Toast.LENGTH_LONG).show();
            }
        }
        
        this.btnMultiplayer.setClickable(true);
	}
	
	/**
	 * Running on a click on button {@link btnTraining}.
	 */
	private void onBtnSingleplayerClick () {
		Intent intent = new Intent(this, SingleplayerSettings.class);
		startActivity(intent);
	}

	/**
	 * Running on a click on button {@link btnTrainingCommence}.
	 */
	private void onBtnSingleplayerCommenceClick () {
		this.btnSingleplayerContinue.setClickable(false);
		
		if (!this.savedGames.hasSingleplayerGame()) {
			throw new IllegalAccessError("There is no singleplayer game to load.");
		}
		
		Intent intent = new Intent(this, SingleplayerPlay.class);
		startActivity(intent);
		this.btnSingleplayerContinue.setClickable(true);
	}
	
	/**
	 * Running on a click on button {@link btnMultiplayer}.
	 */
	private void onBtnMultiplayerClick () {
		if (BluetoothAdapter.getDefaultAdapter() != null) {
			if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
				this.btnMultiplayer.setClickable(false);
				Point size = new Point();
				getWindowManager().getDefaultDisplay().getSize(size);
				
				//workaround for Android bug: Dialog is showing multiply by rotating the device
				if (size.x < size.y) {
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				} else {
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
				}
				
			    startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), REQUEST_ENABLE_BT);
			} else {
				startMultiplayerMenu();
			}
		} else {
			onBluetoothActivationDialog();
		}
	}

	/**
	 * Running on a click on button {@link btnHelp}.
	 */
	private void onBtnManualClick () {
		Intent intent = new Intent(this, Manual.class);
		startActivity(intent);
	}
	
	/**
	 * Starts the multiplayer menu
	 */
	private void startMultiplayerMenu() {
		Intent intent = new Intent(this, MultiplayerMenu.class);
		startActivity(intent);
	}
	
	/**
	 * Setup buttons
	 */
	private void setupButtons() {
		this.layBtnSingleplayer = (LinearLayout) findViewById(R.id.layBtnSingleplayer);
		
		// Get the screen's density scale
		final float scale = getResources().getDisplayMetrics().density;
		
		// Convert the dps to pixels, based on density scale
		int buttonHeight = (int) (60.0f * scale + 0.5f);
		
		Display display = getWindowManager().getDefaultDisplay();
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(layBtnSingleplayer.getLayoutParams());
		
		int marginWidthValue = display.getWidth() * 1 / 8;
		int marginHeightValue = (display.getHeight() - 3 * buttonHeight - this.constants.getStatusBarHeight()) * 1 / 18;
		lp.setMargins(marginWidthValue, marginHeightValue, marginWidthValue, marginHeightValue);
		
		
		this.layBtnSingleplayer.setLayoutParams(lp);
		
		this.btnSingleplayer = (Button) findViewById(R.id.btnSingleplayer);
		this.btnSingleplayer.setOnClickListener(
                new OnClickListener() {
                	public void onClick(View v) {
                        onBtnSingleplayerClick();
                    }

                });
		
		this.btnSingleplayerContinue = (Button) findViewById(R.id.btnSingleplayerContinue);
		this.btnSingleplayerContinue.setOnClickListener(
                new OnClickListener() {
                	public void onClick(View v) {
                        onBtnSingleplayerCommenceClick();
                    }

                });
		
		this.btnMultiplayer = (Button) findViewById(R.id.btnMultiplayer);
		this.btnMultiplayer.setLayoutParams(lp);
		this.btnMultiplayer.setOnClickListener(
                new OnClickListener() {
                	public void onClick(View v) {
                        onBtnMultiplayerClick();
                    }

                });
		
		this.btnManual = (Button) findViewById(R.id.btnManual);
		this.btnManual.setLayoutParams(lp);
		this.btnManual.setOnClickListener(
                new OnClickListener() {
                	public void onClick(View v) {
                        onBtnManualClick();
                    }

                });
	}
}
