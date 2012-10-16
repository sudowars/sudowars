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
package org.sudowars.Model.CommandManagement.MultiplayerSettingsCommands;

import org.sudowars.Controller.Local.Activity.MultiplayerPlay;
import org.sudowars.Controller.Local.Activity.MultiplayerSettings;

/**
 * This class encapsulates the remote ready command, which must be sent to the client indicating<br>
 * indicating that the host player is ready to play.
 */
public class RemoteReadyCommand extends MultiplayerSettingsCommand {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5743826454904377836L;
	/**
	 * The tricker, if the remote is ready
	 */
	private boolean readyState;
	
	/**
	 * Creates a new {@link RemoteReadyCommand}-Object
	 *
	 * @param readyState the tricker, if the remote is ready
	 */
	public RemoteReadyCommand(boolean readyState) {
		this.readyState = readyState;		
	}
	
	/**
	 * Execute and set the settings for the game
	 *
	 * @param settingsActivity the Activity of the settings
	 *
	 * @return <code>true</code>, if the execution was successfully
	 */
	public boolean execute(MultiplayerSettings settingsActivity) {
		if (settingsActivity == null) {
			throw new IllegalArgumentException("activity is null");
		}
		settingsActivity.setRemoteReadyState(readyState);
		return true; // There is no way to check if this was successful
	}
	
	/**
	 * Execute and set the settings for the game
	 *
	 * @param settingsActivity the Activity of the settings
	 *
	 * @return <code>true</code>, if the execution was successfully
	 */
	public boolean execute(MultiplayerPlay playActivity) {
		if (playActivity == null) {
			throw new IllegalArgumentException("activity is null");
		}
		playActivity.setRemoteReadyState(readyState);
		return true; // There is no way to check if this was successful
	}
} 
