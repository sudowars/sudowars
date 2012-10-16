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

package org.sudowars.Controller.Remote;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

import org.sudowars.DebugHelper;
import org.sudowars.Model.SudokuManagement.Pool.SudokuPool;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.graphics.SweepGradient;

public class SudowarsBluetoothSocket implements SudowarsSocket, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8611765134721761230L;

	enum INTERNAL_STATE {
		STATE_CONNECTED,
		STATE_NONE,
		STATE_LISTENING
	};
	
	public final static int maxFragmentSize = 500;
	
	/**
	 * Internal state
	 */
	
	private INTERNAL_STATE internalState = INTERNAL_STATE.STATE_NONE;
	
	/**
	 * Event Handler for the BluetoothConnection and -Server Class (DO NOT USE IN ANY ACTIVITY!!!)
	 */
	SocketEvent socketEventHandler;
	
	/**
	 * BluetoothSocket
	 */
	
	static BluetoothSocket btSocket;
	
	/**
	 * BluetoothServer
	 */
	
	static BluetoothServerSocket btServer;
	
	/**
	 * BluetoothConnectionUUID
	 */
	static final UUID uuid_secure = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66"); 
	
	/**
	 * BluetoothAdapter
	 */
	
	BluetoothAdapter btAdapter;
	
	/**
	 * BannedBluetoothDevices
	 */
	ArrayList<String> blockedMAC;
	
	/**
	 * Input-/Output-Streams
	 */
	
	InputStream inp;
	OutputStream out;
	
	/**
	 * Connecting Thread
	 */
	
	ConnectThread cntThread;
	
	Object closeSync = new Object();
	
	/**
	 * {@link ServerThread} for listening
	 */
	ServerThread srvThread;
	
	private Object sendLock = new Object();
	
	private boolean wasListening = false;
	
	public SudowarsBluetoothSocket() {
		this.btAdapter = BluetoothAdapter.getDefaultAdapter();
		blockedMAC = new ArrayList<String>();
	}
	
	
	public void setEventHandler(SocketEvent evtHandler) {
		this.socketEventHandler = evtHandler;
	}
	
	public boolean listen() {
		try {
			this.btServer = this.btAdapter.listenUsingRfcommWithServiceRecord("Sudowars Server Service", uuid_secure);
		} catch (IOException e) {
			return false;
		}
		this.internalState = INTERNAL_STATE.STATE_LISTENING;
		this.srvThread = new ServerThread();
		this.srvThread.start();
		wasListening = true;
		this.socketEventHandler.onListening();
	
		
		return true;
	}
	
	public void kick() {
		if (this.internalState != INTERNAL_STATE.STATE_CONNECTED)
			return;
		close();
		if (wasListening)
			listen();
	}
	
	public void ban() {
		if (this.internalState != INTERNAL_STATE.STATE_CONNECTED)
			return;
		this.blockedMAC.add(this.btSocket.getRemoteDevice().getAddress());
		kick();
	}
	
	private boolean prepareConnected () {
		try {
			out = this.btSocket.getOutputStream();
			inp = this.btSocket.getInputStream();
			return true;
		} catch (IOException e) {
			DebugHelper.log(DebugHelper.PackageName.BluetoothConnection, "Could not get input/Output Stream");
			return false;
		}
		
	}
	
	public void stop() {
		if (btServer != null) {
			try {
				btServer.close();
			} catch (IOException e) {
				// ignore
			}
		}
		if (btSocket != null) {
			try {
				btSocket.close();
			} catch (IOException e) {
				// ignore
			}
		}
		if (this.cntThread != null)
			this.cntThread.stopThread();
		if (this.srvThread != null)
			this.srvThread.stopThread();
	}
	
	private boolean accept() {
		if (this.internalState != INTERNAL_STATE.STATE_LISTENING)
			return false;
		try {
			if (btServer == null)
				return false;
			this.btSocket = btServer.accept(3000);
			
			if (blockedMAC.contains(this.btSocket.getRemoteDevice().getAddress())) {
				return false;
			}
			
			if (!prepareConnected()){
				return false;
			}
			DebugHelper.log(DebugHelper.PackageName.BluetoothConnection, "Got a Connection");
			this.internalState = INTERNAL_STATE.STATE_CONNECTED;
			
			this.socketEventHandler.onConnected();
			
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	
	public void close() {
		synchronized (closeSync) {
			this.internalState = INTERNAL_STATE.STATE_NONE; //Avoiding Errors, we first should set our internal State to nothing
			StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
	
			String o = "";
			for (StackTraceElement a : stackTraceElements)
				o += a.getClassName() + " -> ";
			
			DebugHelper.log(DebugHelper.PackageName.BluetoothConnection, "close called by " + o);	
			if (this.btServer != null) {
				try {
					
					this.btServer.close();
				} catch (IOException e) {
					DebugHelper.log(DebugHelper.PackageName.BluetoothConnection, "Could not close Server Socket, probably bad ...");
					
				}
			}
			if (this.btSocket != null) {
				try {
					this.btSocket.close();
				} catch (IOException e) {
					DebugHelper.log(DebugHelper.PackageName.BluetoothConnection, "Could not close Socket, probably bad ...");
				}
			}
			DebugHelper.log(DebugHelper.PackageName.BluetoothConnection, "Connection has been closed");
			this.socketEventHandler.onClose();
		}
	}
	
	public boolean recv(byte[] data) {
		if (this.internalState != INTERNAL_STATE.STATE_CONNECTED) 
			return false;
		if (this.inp == null)
			return false;
		if (data.length + 10 <= maxFragmentSize) {
			_recv(data);
		}else{
			byte[] tmp = new byte[maxFragmentSize - 10];
			_recv(tmp);
			System.arraycopy(tmp, 0, data, 0, maxFragmentSize - 10);
			tmp = new byte[maxFragmentSize];
			for (int n = maxFragmentSize - 10; n < data.length; n+= maxFragmentSize) {
				tmp = new byte[Math.min(maxFragmentSize, data.length - n)];
				_recv(tmp);
				System.arraycopy(tmp, 0, data, n, Math.min(maxFragmentSize, data.length - n));
			}
		}

		return true;
	}
	
	private boolean _recv(byte[] data) {
		if (this.internalState != INTERNAL_STATE.STATE_CONNECTED) 
			return false;
		if (this.inp == null)
			return false;
		try {
			this.inp.read(data);
			DebugHelper.log(DebugHelper.PackageName.BluetoothConnection, "Read " + data.length + " Bytes");
		} catch (IOException e) {
			if (data.length < 30)
				close();
			return false;
		}
		return true;
	}
	
	public boolean sendData(byte[] data) {
		if (internalState != INTERNAL_STATE.STATE_CONNECTED)
			return false;
		synchronized (sendLock) {
			
		
			try {
				if (data.length <= maxFragmentSize)
					out.write(data);
				else{
					byte[] tmp;
					for (int n = 0; n < data.length - 1; n+= maxFragmentSize){
						tmp = new byte[Math.min(maxFragmentSize, data.length - n)];
						System.arraycopy(data, n, tmp, 0, Math.min(maxFragmentSize, data.length - n));
						
						out.write(tmp);
					}
						
				}
				
				DebugHelper.log(DebugHelper.PackageName.BluetoothConnection, "Written " + data.length + " Bytes");
				
			} catch (IOException e) {
				DebugHelper.log(DebugHelper.PackageName.BluetoothConnection, "Could not send data!!");
				close();
				return false;
			}

		}
		return true;
		
	}
	
	public boolean isConnected() {
		return (this.internalState == INTERNAL_STATE.STATE_CONNECTED);
	}
	
	public boolean connect(String deviceAddress) {
		BluetoothDevice btDev = this.btAdapter.getRemoteDevice(deviceAddress);
		try {
			this.cntThread = new ConnectThread(btDev);
		} catch (IOException e) {
			return false;
		}
		cntThread.start();
		this.socketEventHandler.onConnecting();
		return true;
	}
	
	
	/**
	 * Accepting Thread
	 * @author adrian
	 *
	 */
	private class ConnectThread extends Thread {
		
		
		public ConnectThread(BluetoothDevice btDev) throws IOException {
			if (btSocket != null) {
				try {
					btSocket.close();
				} catch (IOException e) {
					btSocket = null;
				}
			}
			btSocket = btDev.createRfcommSocketToServiceRecord(uuid_secure);
		}
		
		public void run() {
			btAdapter.cancelDiscovery();
			
			try {
				btSocket.connect();
				if (!prepareConnected()){
					close();
					return;
				}
				internalState = INTERNAL_STATE.STATE_CONNECTED;
				socketEventHandler.onConnected();
				
			} catch (IOException e) {
				//Close the Socket to be able to connect again
				close();
				return;
			}
			
			
		}
		
		public void stopThread() {
			this.interrupt();
		
			
		}
		
	}
	
	
	class ServerThread extends Thread {
		
		
		volatile boolean exit = false;
		
		public ServerThread() {
			
		}
		
		public void run() {
			
			while (internalState == INTERNAL_STATE.STATE_LISTENING && exit == false) {
				accept();
			}
			
			try {
				btServer.close();
				
			} catch (IOException e) {
				
			}
			btServer = null;
		}

		public void stopThread() {
			exit = true;
		}
	}
	
	public String getRemoteHost() {
		if (internalState != INTERNAL_STATE.STATE_CONNECTED)
			return "";
		return this.btSocket.getRemoteDevice().getAddress() == null ? "" : this.btSocket.getRemoteDevice().getAddress();
	}
}
