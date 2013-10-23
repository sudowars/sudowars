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

import java.util.ArrayList;
import java.util.Set;

import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import org.sudowars.DebugHelper;
import org.sudowars.R;
import org.sudowars.Model.SudokuManagement.IO.FileIO;
import org.sudowars.Controller.Local.BluetoothDeviceList.BluetoothDeviceListAdapter;
import org.sudowars.Controller.Local.BluetoothDeviceList.BluetoothDeviceListItem;
import org.sudowars.Controller.Remote.BluetoothConnection;

/**
 * Shows the menu for the multiplayer.
 */
public class MultiplayerMenu extends PoolBinder {
	/**
	 * Intent request code
	 */
    private static final int REQUEST_ENABLE_BT = 3;
	
	/**
	 * List with all bluetooth devices, which lstBluetoothDevices uses
	 */
	private ArrayList<BluetoothDeviceListItem> btDeviceList;
	
	/**
	 * List of all bluetooth devices
	 */
	private ListView lstBluetoothDevices;
	
	/**
	 * the menu item to start or stop scanning
	 */
	private MenuItem btnScan;
	
	/**
	 * the saved Games for loading
	 */
	private FileIO savedGames;
	
	/**
	 * Bluetooth Adapter instance to work with
	 */
	private BluetoothAdapter bluetoothAdapter;
	
	/**
	 * Receive events when a new Device was found
	 */
	private final BroadcastReceiver bluetoothEvent = new BroadcastReceiver() {
		@Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            
            MultiplayerMenu.this.activateBluetooth();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                
                String name = getString(R.string.unknown);
                
                if (device.getName() != null) {
                	name = device.getName();
                }
                
                BluetoothDeviceListItem item = new BluetoothDeviceListItem(name, device.getAddress(), false);
                
                if (device.getBondState() != BluetoothDevice.BOND_BONDED && !MultiplayerMenu.this.btDeviceList.contains(item)) {
                    MultiplayerMenu.this.btDeviceList.add(item);
                }
        	} else if ((BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action) || BluetoothAdapter.ACTION_STATE_CHANGED.equals(action))) {
                setProgressBarVisibility(false);
        		MultiplayerMenu.this.btnScan.setTitle(getString(R.string.button_bluetooth_scan));
            }
        }
    };
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate (Bundle savedInstanceState) {
		if (BluetoothAdapter.getDefaultAdapter() == null) {
			throw new IllegalStateException("The device has no Bluetooth, so this Activity should not be loaded...");
		}

        requestWindowFeature(getWindow().FEATURE_PROGRESS);
		super.onCreate(savedInstanceState);
		
	    ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);

        setProgressBarIndeterminate(true);
		setContentView(R.layout.multiplayer_menu);
		
		try {
			this.savedGames = new FileIO(this.getApplicationContext());
		} catch (IllegalArgumentException e) {
			DebugHelper.log(DebugHelper.PackageName.MultiplayerMenu, "Initialization of a FileIO object failed!");
		}
		
		this.lstBluetoothDevices = (ListView) findViewById(R.id.bluetooth_devices);
		this.btDeviceList = new ArrayList<BluetoothDeviceListItem>();

		this.lstBluetoothDevices.setAdapter(new BluetoothDeviceListAdapter(this, R.layout.device_list_item, this.btDeviceList));
		this.lstBluetoothDevices.setOnItemClickListener(
				new OnItemClickListener() {
		    		@Override
		    		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		    				long arg3) {
		    			if (arg2 < MultiplayerMenu.this.btDeviceList.size()) {
		    				onLstBluetoothClick(MultiplayerMenu.this.btDeviceList.get(arg2).getMac());
		    			}
		    		}
				});

        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	protected void onResume() {
		super.onResume();
		
		this.activateBluetooth();
		
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(bluetoothEvent, filter);

        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(bluetoothEvent, filter);
        
        filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(bluetoothEvent, filter);
        
        if (BluetoothConnection.getActiveBluetoothConnection() != null) {
        	BluetoothConnection.getActiveBluetoothConnection().closeConnection();
        }
		
		Set<BluetoothDevice> pairedDevices = this.bluetoothAdapter.getBondedDevices();

		if (pairedDevices != null) {
            for (BluetoothDevice device : pairedDevices) {
                this.btDeviceList.add(new BluetoothDeviceListItem(device.getName(), device.getAddress(), true));
            }
		} else {
			DebugHelper.log(DebugHelper.PackageName.MultiplayerMenu, "Error occured when retreiving bonded devices of bluetooth adapter");
		}
	}
	
	/*
     * (non-Javadoc)
     * @see android.app.Activity#onPause()
     */
    @Override
	protected void onPause() {
		super.onPause();
	    
		this.btDeviceList.clear();
		
		if (BluetoothAdapter.getDefaultAdapter().isEnabled()) {
			stopScan();
		}
		
		if (bluetoothEvent != null) {
			unregisterReceiver(bluetoothEvent);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu (Menu menu) {
	    MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.multiplayer_menu, menu);
	    
		if (!this.savedGames.hasMultiplayerGame()) {
			menu.removeItem(R.id.menu_multiplayer_continue);
		}
		
	    return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onPrepareOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		
	    this.btnScan = (MenuItem) menu.findItem(R.id.menu_scan);
	    this.btnScan.setEnabled(BluetoothAdapter.getDefaultAdapter().isEnabled());
	    
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
		} else if (item.getItemId() == R.id.menu_multiplayer_new) {
			onBtnMultiplayerNewClick();
			return true;
		} else if (item.getItemId() == R.id.menu_multiplayer_continue) {
			onBtnMultiplayerContinueClick();
			return true;
		} else if (item.getItemId() == R.id.menu_scan) {
			if (this.bluetoothAdapter.isDiscovering()) {
    			stopScan();
            } else {
            	startScan();
            }
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
		
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_CANCELED) {
            	finish();
            }
        }
	}
	
	/**
	 * Running on a click on button {@link btnMultiplayerNew}
	 */
	private void onBtnMultiplayerNewClick () {
		stopScan();
		
		Intent intent = new Intent(this, MultiplayerSettings.class);
		startActivity(intent);
	}
	
	/**
	 * Running on a click on button {@link btnMultiplayerCommence}.
	 */
	private void onBtnMultiplayerContinueClick () {
		if (!this.savedGames.hasMultiplayerGame()) {
			throw new IllegalAccessError("There is no multiplayer game to load.");
		}
		
		stopScan();
		
		Intent intent = new Intent(this, MultiplayerSettings.class);
		Bundle bundle = new Bundle();
		
		bundle.putSerializable("gameState", this.savedGames.loadMultiplayerGame());
		intent.putExtras(bundle);
		
		startActivity(intent);
	}
	
	/**
	 * Run on click on a entry in the bluetooth list
	 *
	 * @param mac the mac address of the bluetooth device in the list
	 */
	private void onLstBluetoothClick(String mac) {
		BluetoothConnection connection = new BluetoothConnection();
		
		if (connection.connect(mac)) {
			stopScan();
			
			Intent intent = new Intent(this, MultiplayerSettings.class);
			Bundle bundle = new Bundle();
			
			bundle.putSerializable("connection", true);
			intent.putExtras(bundle);
			
			startActivity(intent);
		} else {
			Toast.makeText(getApplicationContext(), R.string.text_bluetooth_could_not_connect, Toast.LENGTH_LONG).show();
		}
	}
	
	/**
	 * Initiate Bluetooth Scan
	 */
	private void startScan() {
		if (bluetoothAdapter != null && !this.bluetoothAdapter.isDiscovering()) {
	    	// Request discover from BluetoothAdapter
	    	this.bluetoothAdapter.startDiscovery();
	    	this.btnScan.setTitle(getString(R.string.button_bluetooth_scan_stop));
            setProgressBarVisibility(true);
		}
	}
	
	/**
	 * Stop Scanning
	 */
	private void stopScan() {
		if (bluetoothAdapter != null && bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
            this.btnScan.setTitle(getString(R.string.button_bluetooth_scan));
            setProgressBarVisibility(false);
        }
	}
    
	/**
	 * Ask for activating Bluetooth
	 */
	private void activateBluetooth() {
		if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
			int rotation = MultiplayerMenu.this.getWindowManager().getDefaultDisplay().getRotation();
			
			//workaround for Android bug: Dialog is showing multiply by rotating the device
			if (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) {
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			} else {
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			}
			
		    MultiplayerMenu.this.startActivityForResult(
		    		new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), REQUEST_ENABLE_BT);
        }
	}
}
