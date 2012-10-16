package org.sudowars.Controller.Remote;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import org.sudowars.DebugHelper;

import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.graphics.SweepGradient;

/**
 * This class represents a bluetooth server for a client to connect to
 *
 */

public class BluetoothServer extends BluetoothConnection implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9091603361363056738L;

	
	
	/**
	 * Creates a new {@link BluetoothServer}
	 */
	public BluetoothServer() {
		super();
		
	}
	
	/**
	 * This function starts the listening process and waits for clients
	 * This call will be ignored, if already listening
	 *
	 * @return returns <code>true</code> if the listening process was started, otherwise it returns <code>false</code>
	 */
	public boolean listen() {
		return swSocket.listen();
	}
	
	/**
	 * Disconnects the actual connected player and starts listening again
	 */
	public void kick() {
		swSocket.kick();
	}
	
	/**
	 * Disconnects the actual connected player, starts listening again and stops the player to connect again
	 */
	public void ban() {
		swSocket.ban();
	}
	
	/**
	 * {@link Deprecated} use closeConnection instead
	 */
	
	public void stopListening() {
		
		//closeConnection();
		
	}
}
