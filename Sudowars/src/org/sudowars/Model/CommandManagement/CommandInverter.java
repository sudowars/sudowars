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