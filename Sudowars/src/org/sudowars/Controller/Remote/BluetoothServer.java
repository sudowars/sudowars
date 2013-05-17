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

import java.io.Serializable;

/**
 * This class represents a bluetooth server for a client to connect to
 *
 */

public class BluetoothServer extends BluetoothConnection implements Serializable {
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
