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
import org.sudowars.Controller.Local.MultiplayerSudokuSettings;
import org.sudowars.Controller.Remote.BluetoothConnection;
import org.sudowars.Controller.Remote.BluetoothServer;
import org.sudowars.Model.Game.MultiplayerGame;
import org.sudowars.Model.Game.Player;
import org.sudowars.Model.Sudoku.Field.SquareStructure;
import org.sudowars.Model.SudokuManagement.IO.FileIO;
import org.sudowars.Model.SudokuUtil.GameState;
import org.sudowars.Model.SudokuUtil.NoteManager;
import org.sudowars.Model.CommandManagement.*;
import org.sudowars.Model.CommandManagement.MultiplayerSettingsCommands.CreateMultiplayerGameObjectCommand;
import org.sudowars.Model.CommandManagement.MultiplayerSettingsCommands.KickMultiplayerClientCommand;
import org.sudowars.Model.CommandManagement.MultiplayerSettingsCommands.MultiplayerSettingsCommand;
import org.sudowars.Model.CommandManagement.MultiplayerSettingsCommands.RemoteReadyCommand;
import org.sudowars.Model.CommandManagement.MultiplayerSettingsCommands.RemoteSettingsCommand;
import org.sudowars.Model.CommandManagement.MultiplayerSettingsCommands.ResumeMultiplayerGameCommand;
import org.sudowars.Model.CommandManagement.MultiplayerSettingsCommands.KickMultiplayerClientCommand.KickStatus;
import org.sudowars.Model.Difficulty.Difficulty;
import org.sudowars.Model.Difficulty.DifficultyEasy;
import org.sudowars.Model.Difficulty.DifficultyHard;
import org.sudowars.Model.Difficulty.DifficultyMedium;

import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.os.CountDownTimer;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceChangeListener;
import android.widget.ToggleButton;

/**
 * Shows the settings menu of a new multiplayer game.
 */
public class MultiplayerSettings extends Settings {
	/**
	 * Intent request code
	 */
    private static final int REQUEST_DISCOVERABLE = 3;
    
    /**
     * Counter how long the device is still visible
     */
    private int visibleCounter;
    
    /**
     * To be, or not to be
     */
    private boolean isClient;
    
	/**
	 * the preferences
	 */
	protected SharedPreferences preferences;
	
	/**
	 * the button to kick a connected player
	 */
	private MenuItem btKick;
	
	/**
	 * the button to ban a connected player
	 */
	private MenuItem btBan;

	/**
	 * the button to make the device visible
	 */
	private MenuItem btVisible;
	/**
	 * current connection status
	 */
	private Preference connectionStatus;
	
	/**
	 * the ready button of the local player
	 */
	private ToggleButton tglLocalReady;
	
	/**
	 * the ready button of the remote player
	 */
	private ToggleButton tglRemoteReady;
	
	/**
	 * the bluetooth API, to communicate between client and server
	 */
	private BluetoothConnection connection;
	
	/**
	 * the settings for the new game
	 */
	private MultiplayerSudokuSettings settings;
	
	/**
	 * the game to commence or open new
	 */
	private MultiplayerGame game;
	
	/**
	 * the game state
	 */
	private GameState gameState;
	
	/**
	 * the counter, which count down, when a bluetooth device is discoverable
	 */
	private Counter counter;
	
	/**
	 * Receive events when a new Device was found
	 */
	private final BroadcastReceiver bluetoothEvent = new BroadcastReceiver() {
		@Override
        public void onReceive(Context context, Intent intent) {
            if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            	MultiplayerSettings.this.finish();
            }
        }
    };
	
	/**
	 * the Bluetooth handler
	 */
	private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        	switch (msg.what) {
        	case BluetoothConnection.MESSAGE_BT_STATE_CHANGE:
        		//send settings to client
    			if (MultiplayerSettings.this.connection.getState() == BluetoothConnection.STATE_CONNECTED) {
    				if (MultiplayerSettings.this.connection instanceof BluetoothServer) {
    					RemoteSettingsCommand command = new RemoteSettingsCommand(MultiplayerSettings.this.settings);
    					MultiplayerSettings.this.connection.sendCommand((Command) command);
    					if (MultiplayerSettings.this.btKick != null && MultiplayerSettings.this.btBan != null) {
	    					MultiplayerSettings.this.btKick.setEnabled(true);
	    					MultiplayerSettings.this.btBan.setEnabled(true);
    					}
    				}
    				
        			tglLocalReady.setEnabled(true);
        		} else {
        			if (MultiplayerSettings.this.btKick != null && MultiplayerSettings.this.btBan != null) {
	        			MultiplayerSettings.this.btKick.setEnabled(false);
						MultiplayerSettings.this.btBan.setEnabled(false);
        			}
        			MultiplayerSettings.this.tglLocalReady.setChecked(false);
        			MultiplayerSettings.this.tglLocalReady.setEnabled(false);
        		}
    			
    			if (MultiplayerSettings.this.connection instanceof BluetoothServer
    					&& MultiplayerSettings.this.connection.getState() == BluetoothConnection.STATE_NONE) {
    					((BluetoothServer) MultiplayerSettings.this.connection).listen();
    			}

        		String states[] = getResources().getStringArray(R.array.bluetooth_states);
        		MultiplayerSettings.this.connectionStatus.setTitle(states[MultiplayerSettings.this.connection.getState()]);
        		
        		break;
        	case BluetoothConnection.MESSAGE_NEW_DATA:
        		Command command = MultiplayerSettings.this.connection.getCurrentCommand();
        		if (command != null) {
        			if (command instanceof CreateMultiplayerGameObjectCommand) {
        				MultiplayerSettings.this.game = ((CreateMultiplayerGameObjectCommand) command).getGame();
        				MultiplayerSettings.this.startGame();
        			} else if (command instanceof ResumeMultiplayerGameCommand) {
        				MultiplayerSettings.this.game = ((ResumeMultiplayerGameCommand) command).getGame();
        				MultiplayerSettings.this.game.swapSlots();
        				MultiplayerSettings.this.startGame();
        			} else if (command instanceof MultiplayerSettingsCommand) {
        				((MultiplayerSettingsCommand) command).execute(MultiplayerSettings.this);
        				MultiplayerSettings.this.refresh();
        				
        				if (command instanceof KickMultiplayerClientCommand) {
        					MultiplayerSettings.this.finish();
        				}
        			}
        		}
        	
        		break;
        	case BluetoothConnection.MESSAGE_BT_PACKET_DELIVERED:
    			Command deliveredCommand = MultiplayerSettings.this.connection.getDeliveredCommand();
    			if (deliveredCommand != null) {
    				if (deliveredCommand instanceof CreateMultiplayerGameObjectCommand
    						|| deliveredCommand instanceof ResumeMultiplayerGameCommand) {
        				MultiplayerSettings.this.startGame();
        			}
    			}
    			
    			break;
        	}
        }
    };
	
    /*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	    ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
		
		setContentView(R.layout.multiplayer_settings);
		addPreferencesFromResource(R.xml.multiplayer_preferences);

		this.preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		this.visibleCounter = -1;
		this.setupButtons();
		this.settings = new MultiplayerSudokuSettings();
		
		Intent intent = getIntent();
		isClient = intent.hasExtra("connection");
		
		if (isClient) {
			this.connection = (BluetoothConnection) BluetoothConnection.getActiveBluetoothConnection();
			this.connectionStatus.setTitle(getResources().getStringArray(R.array.bluetooth_states)[1]);
			
			this.connection.setBluetoothEventHandler(mHandler);
			disableButtons();
		} else {
			this.connection = new BluetoothServer();
			
			//commence game
			if (intent.hasExtra("gameState")) {
				disableButtons();
				this.gameState = (GameState) intent.getExtras().getSerializable("gameState");
				this.game = (MultiplayerGame) this.gameState.getGame();
				
				if (this.gameState == null) {
					throw new IllegalStateException("Given gameState is null.");
				} else if (this.game == null) {
					throw new IllegalStateException("Given game is null.");
				}
								
				this.settings.setSettings(
						(this.gameState.getFieldStructure().getHeight() == 9)?0:1,
						encodeDifficulty(this.gameState.getDifficulty()),
						false);
			//new game
			} else {
				this.settings.setIsNewGame(true);
				int size = Integer.parseInt(preferences.getString("multiplayer_field_size", "9"));
            	int difficulty = Integer.parseInt(preferences.getString("multiplayer_difficulty", "0"));
            	
            	size = (size == 9)?0:1;
				if (difficulty < 0 || difficulty > 2)
					difficulty = 1;
				
				this.settings.setSettings(size, difficulty, true);
			}
			
			this.connection.setBluetoothEventHandler(mHandler);
			
			this.connectionStatus.setTitle(getResources().getStringArray(R.array.bluetooth_states)[0]);
	    	((BluetoothServer) this.connection).listen();
			
			if (this.connection.getState() == BluetoothConnection.STATE_CONNECTED) {
				RemoteSettingsCommand command = new RemoteSettingsCommand(this.settings);
				this.connection.sendCommand((Command) command);
			}
		}
		
		if (this.settings.isNewGame())
			this.setTitle(String.format(getString(R.string.button_multiplayer_new)));
		else
			this.setTitle(String.format(getString(R.string.button_multiplayer_continue)));
	}
    
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	protected void onResume() {
		super.onResume();
		
		if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
			this.finish();
		}

		IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(bluetoothEvent, filter);
	}
	
	/*
     * (non-Javadoc)
     * @see android.app.Activity#onPause()
     */
    @Override
	protected void onPause() {
		super.onPause();
		
		if (bluetoothEvent != null) {
			unregisterReceiver(bluetoothEvent);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		super.onStop();
		
	    if (this.connection instanceof BluetoothServer) {
	    	((BluetoothServer)this.connection).stopListening();
	    }
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
		
		if (requestCode == REQUEST_DISCOVERABLE) {
	        if (resultCode == Activity.RESULT_CANCELED) {
	        	if (this.btVisible != null) {
	        		this.btVisible.setEnabled(true);
	        	}
				Toast.makeText(getApplicationContext(), R.string.text_bluetooth_discoverable_failed, Toast.LENGTH_SHORT).show();
			} else {
				this.counter = new Counter(resultCode * 1000, 1000);
				this.counter.start();
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu (Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.multiplayer_settings, menu);
		
		if (isClient) {
			menu.removeItem(R.id.btKick);
			menu.removeItem(R.id.btBan);
			menu.removeItem(R.id.btVisible);
		} else {
			this.btKick = (MenuItem) menu.findItem(R.id.btKick);
			this.btBan = (MenuItem) menu.findItem(R.id.btBan);
			this.btVisible = (MenuItem) menu.findItem(R.id.btVisible);
		}
		
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
		} else if (item.getItemId() == R.id.btKick) {
			onBtKickClick();
			return true;
		} else if (item.getItemId() == R.id.btBan) {
			onBtBanClick();
			return true;
		} else if (item.getItemId() == R.id.btVisible) {
			onBtVisibleClick();
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Refresh the radio bottons
	 */
	private void refresh() {
		if (!(this.connection instanceof BluetoothServer)) {
			ListPreference size = (ListPreference) findPreference("multiplayer_field_size");
			size.setValueIndex(this.settings.getSize());
			size.setSummary(size.getEntries()[this.settings.getSize()]);
			
			ListPreference diff = (ListPreference) findPreference("multiplayer_difficulty");
			diff.setValueIndex(this.settings.getDifficulty());
			diff.setSummary(diff.getEntries()[this.settings.getDifficulty()]);
		}
		
		if (this.connection != null && this.connection instanceof BluetoothServer && this.connection.getState() == BluetoothConnection.STATE_CONNECTED) {
			RemoteSettingsCommand command = new RemoteSettingsCommand(this.settings);
			this.connection.sendCommand((Command) command);
		}
		
		int debugSize = (this.settings.getSize() == 0)?9:16;
		DebugHelper.log(DebugHelper.PackageName.MultiplayerSettings, "Size: " + debugSize + "x" + debugSize
				+ " Difficulty: " + this.decodeDifficulty().toString());
	}
	
	/**
	 * Running on a click on button {@link tglLocalReady}.
	 */
	private void onTglLocalReadyToggle() {
		if (connection.getState() == BluetoothConnection.STATE_CONNECTED) {
			RemoteReadyCommand command = new RemoteReadyCommand(this.tglLocalReady.isChecked());
			this.connection.sendCommandAsync((Command) command);
			
			DebugHelper.log(DebugHelper.PackageName.MultiplayerSettings, "Set local ready: " + this.tglLocalReady.isChecked());
			
			this.prepareGame();
		} else {
			this.tglLocalReady.setChecked(false);
		}
	}

	/**
	 * Running on a click on button {@link BtnVisible}.
	 */
	private void onBtVisibleClick() {
		if (this.visibleCounter <= 0
				&& this.connection instanceof BluetoothServer
				&& BluetoothAdapter.getDefaultAdapter().isEnabled()) {	
			DebugHelper.log(DebugHelper.PackageName.MultiplayerSettings, "Make bluetooth device discoverable.");
			
			Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,
					this.getResources().getInteger(R.integer.bluetooth_discoverable_duration));
			startActivityForResult(discoverableIntent, REQUEST_DISCOVERABLE);
		} else {
			Toast.makeText(getApplicationContext(), this.getString(R.string.button_bluetooth_is_visible_before)+" "
									+this.visibleCounter+" "+this.getString(R.string.button_bluetooth_is_visible_after),
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Running on a click on button {@link btnKick}.
	 */
	private void onBtKickClick() {
		if (this.connection instanceof BluetoothServer && connection.getState() == BluetoothConnection.STATE_CONNECTED) {
			KickMultiplayerClientCommand command = new KickMultiplayerClientCommand(KickStatus.KICK);
			((BluetoothServer) this.connection).sendCommandAsync((Command) command);
			((BluetoothServer) this.connection).kick();
			this.tglRemoteReady.setChecked(false);
			this.tglLocalReady.setChecked(false);
			Toast.makeText(getApplicationContext(), R.string.text_kick, Toast.LENGTH_SHORT).show();
			
			DebugHelper.log(DebugHelper.PackageName.MultiplayerSettings, "Remote kicked.");
		}
	}

	/**
	 * Running on a click on button {@link btnBan}.
	 */
	private void onBtBanClick() {
		if (this.connection instanceof BluetoothServer && connection.getState() == BluetoothConnection.STATE_CONNECTED) {
			KickMultiplayerClientCommand command = new KickMultiplayerClientCommand(KickStatus.KICKBAN);
			((BluetoothServer) this.connection).sendCommandAsync((Command) command);
			((BluetoothServer) this.connection).ban();
			this.tglRemoteReady.setChecked(false);
			Toast.makeText(getApplicationContext(), R.string.text_ban, Toast.LENGTH_SHORT).show();
			
			DebugHelper.log(DebugHelper.PackageName.MultiplayerSettings, "Remote banned.");
		}
	}
	
	/**
	 * Kicked the user
	 * 
	 * @param ban the status, if a user was kicked
	 */
	public void onKick(boolean ban) {
		if (ban) {
			Toast.makeText(getApplicationContext(), R.string.text_banned, Toast.LENGTH_LONG).show();
			DebugHelper.log(DebugHelper.PackageName.MultiplayerSettings, "Local was banned.");
		} else {
			Toast.makeText(getApplicationContext(), R.string.text_kicked, Toast.LENGTH_LONG).show();
			DebugHelper.log(DebugHelper.PackageName.MultiplayerSettings, "Local was kicked.");
		}
		
		this.finish();
	}
	
	/**
	 * Sets the settings
	 * 
	 * @param multiplayerSudokuSettings the settings
	 */
	public void setSettings(MultiplayerSudokuSettings multiplayerSudokuSettings) {
		if (multiplayerSudokuSettings == null) {
			throw new IllegalArgumentException("Given multiplayerSudokuSettings is null.");
		}
		
		this.settings = multiplayerSudokuSettings;
		this.refresh();
	}
	
	/**
	 * Function used to set the RemoteReady state via remote command object
	 * 
	 * @param state the state of the remote player
	 */
	public void setRemoteReadyState(boolean state) {
		this.tglRemoteReady.setChecked(state);
		
		DebugHelper.log(DebugHelper.PackageName.MultiplayerSettings, "Set remote ready: " + this.tglRemoteReady.isChecked());
		
		this.prepareGame();
	}
	
	/**
	 * Prepare the game
	 */
	private void prepareGame() {
		if (this.connection instanceof BluetoothServer
				&& this.tglLocalReady.isChecked() && this.tglRemoteReady.isChecked()) {
			FileIO savedGames = new FileIO(this.getApplicationContext());
			
			this.tglLocalReady.setClickable(false);
				
			if (this.connection instanceof BluetoothServer) {
				Command command;
				
				if (this.settings.isNewGame()) {
					int size = (this.settings.getSize() == 0)?9:16;
					
					command = new CreateMultiplayerGameObjectCommand(
							this.pool.extractSudoku(new SquareStructure(size), decodeDifficulty()));
					this.game = ((CreateMultiplayerGameObjectCommand) command).getGame();					
					savedGames.saveMultiplayerGame(new GameState(this.game, this.decodeDifficulty()));
				} else {
					this.game = (MultiplayerGame) savedGames.loadMultiplayerGame().getGame();
					command = new ResumeMultiplayerGameCommand(this.game);
				}
				
				this.connection.sendCommandAsync(command);
			}
		}
		
	}
	
	/**
	 * Starts the Game, if local and remote are ready
	 */
	private void startGame() {
		if (this.tglLocalReady.isChecked() && this.tglRemoteReady.isChecked()) {
			this.tglLocalReady.setClickable(false);
			
			FileIO savedGames = new FileIO(this.getApplicationContext());
			Intent intent = new Intent(this, MultiplayerPlay.class);
			
			Player localPlayer = new Player(BluetoothAdapter.getDefaultAdapter().getAddress() == null ?
							"Local":BluetoothAdapter.getDefaultAdapter().getAddress());
			Player remotePlayer = new Player(this.connection.getRemoteDeviceName() == null ?
							"Remote":this.connection.getRemoteDeviceName());
			
			this.game.setFirstPlayer(localPlayer);
			this.game.setSecondPlayer(remotePlayer);
			
			if (this.settings.isNewGame()) {
				this.game.setNoteManagerOfPlayer(localPlayer, new NoteManager());
				this.game.setNoteManagerOfPlayer(remotePlayer, new NoteManager());
			}
			
			savedGames.saveMultiplayerGame(new GameState(this.game, this.decodeDifficulty()));
			
			DebugHelper.log(DebugHelper.PackageName.MultiplayerSettings, "Size: "
					+ this.game.getSudoku().getField().getStructure().toString()
					+ " Difficulty: " + this.decodeDifficulty().toString());
			
			startActivity(intent);
			finish();
		}
	}
	
	/**
	 * Returns the difficulty code
	 * 
	 * @param difficulty
	 * @return the difficulty code
	 */
	private int encodeDifficulty(Difficulty difficulty) {		
		int code;
		
		if (difficulty instanceof DifficultyEasy) {
			code = 0;
		} else if (difficulty instanceof DifficultyMedium) {
			code = 1;
		} else {
			code = 2;
		}
		
		return code;
	}
	
	/**
	 * Returns the difficulty
	 * 
	 * @return the difficulty
	 */
	private Difficulty decodeDifficulty() {
		Difficulty difficulty;
		
		if (this.settings.getDifficulty() == 0) {
			difficulty = new DifficultyEasy();
		} else if (this.settings.getDifficulty() == 1){
			difficulty = new DifficultyMedium();
		} else {
			difficulty = new DifficultyHard();
		}
		
		return difficulty;
	}
	
	/**
	 * Setup buttons
	 */
	private void setupButtons() {
		ListPreference size = (ListPreference) findPreference("multiplayer_field_size");
		size.setSummary(size.getEntry());
		size.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference pref, Object obj) {
            	if (obj instanceof String) {
	            	int size = Integer.parseInt((String) obj);
	            	size = (size == 9)?0:1;
	            	pref.setSummary(((ListPreference) pref).getEntries()[size]);
					MultiplayerSettings.this.settings.setSize(size);
					refresh();
            	}
                return true;
            }
        });

		ListPreference diff = (ListPreference) findPreference("multiplayer_difficulty");
		diff.setSummary(diff.getEntry());
		diff.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference pref, Object obj) {
            	if (obj instanceof String) {
	            	int difficulty = Integer.parseInt((String) obj);
	            	pref.setSummary(((ListPreference) pref).getEntries()[difficulty]);
					MultiplayerSettings.this.settings.setDifficulty(difficulty);
					refresh();
            	}
				return true;
			}
        });
		
		this.connectionStatus = findPreference("multiplayer_connection_status");
		
		this.tglLocalReady = (ToggleButton) findViewById(R.id.tglLocalReady);
		this.tglLocalReady.setOnClickListener(
                new OnClickListener() {
                	public void onClick(View v) {
                		onTglLocalReadyToggle();
                    }
                });
		
		this.tglRemoteReady = (ToggleButton) findViewById(R.id.tglRemoteReady);
		this.tglRemoteReady.setClickable(false);
	}
	
	
	@Override
	public void onBackPressed() {
		// kill all network stuff
		MultiplayerSettings.this.connection.stop();
		this.finish();
	}
	/**
	 * The Counter to display a countdown before the game begin
	 */
	private class Counter extends CountDownTimer {
		/*
		 * The constructor		
		 */
		public Counter(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
			MultiplayerSettings.this.btVisible.setTitle(R.string.button_bluetooth_make_visible_active);
		}
		
		/*
		 * (non-Javadoc)
		 * @see android.os.CountDownTimer#onFinish()
		 */
		@Override
		public void onFinish() {
			MultiplayerSettings.this.visibleCounter = -1;
			if (MultiplayerSettings.this.btVisible != null) {
				MultiplayerSettings.this.btVisible.setTitle(R.string.button_bluetooth_make_visible);
			}
		}
		
		/*
		 * (non-Javadoc)
		 * @see android.os.CountDownTimer#onTick(long)
		 */
		@Override
		public void onTick(long millisUntilFinished) {
			MultiplayerSettings.this.visibleCounter = (int) (millisUntilFinished / 1000);
		}
	}
	
	private void disableButtons() {
		findPreference("multiplayer_field_size").setEnabled(false);
		findPreference("multiplayer_field_size").setSelectable(false);
		findPreference("multiplayer_difficulty").setEnabled(false);
		findPreference("multiplayer_difficulty").setSelectable(false);
	}
}
