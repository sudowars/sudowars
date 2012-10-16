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
package org.sudowars.Model.CommandManagement;

import java.io.Serializable;

import org.sudowars.Model.CommandManagement.GameCommands.CompositeCommand;
import org.sudowars.Model.CommandManagement.GameCommands.GameCommand;
import org.sudowars.Model.Game.Game;

/**
 * The singleton class CommandInverter. It is used by the DeltaManager to create commands for undo/redo operations
 */
public final class CommandInverter implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7578243186550161168L;
	
	/**
	 * The instance of the CommandInverter
	 */
	private static CommandInverter instance = null;

	/** 
	 * Initializes a new instance of the CommandInverter class.
	 */
	private CommandInverter() {
	}

	/**
	 * This Method returns only the instance of this class.
	 *
	 * @return A reference to the only CommandInverter instance.
	 */
	public static CommandInverter getInstance() {
		if (instance == null) {
			instance = new CommandInverter();
		}
		return instance;
	}

	/**
	 * This method returns a command which undos the given command.
	 *
	 * @param command The command to be inverted.
	 *
	 * @return The inverted command in a compositeCommand or <code>null</code>, 
	 * if there is no inverted command to the specified one.
	 *
	 * @throws IllegalArgumentException if the given command was <code>null</code>
	 */
	public CompositeCommand getInvertedCommand(GameCommand command, Game game) throws IllegalArgumentException {
		assert (command != null);
		assert (game != null);
		if (command == null) {
			throw new IllegalArgumentException("Command is null");
		}
		if (game == null) {
			throw new IllegalArgumentException("given game is null");
		}
		
		CompositeCommand result = new CompositeCommand();
		result.pushCommand(command.getInvertedCommand(game));
		return result;
	}
}
