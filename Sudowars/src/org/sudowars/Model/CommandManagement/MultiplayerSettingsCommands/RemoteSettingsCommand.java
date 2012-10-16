package org.sudowars.Model.CommandManagement.MultiplayerSettingsCommands;

import org.sudowars.Controller.Local.Activity.MultiplayerSettings;
import org.sudowars.Controller.Local.MultiplayerSudokuSettings;

/**
 * This class encapsulates the remote settings command.
 */
public class RemoteSettingsCommand extends MultiplayerSettingsCommand {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1381976426574608656L;
	/**
	 * the multiplayer settings of the remote
	 */
	private MultiplayerSudokuSettings multiplayerSudokuSettings;

	/**
	 * Creates a new {@link RemoteSettingsCommand}-Object
	 *
	 * @param multiplayerSudokuSettings the multiplayer settings of the remote
	 */
	public RemoteSettingsCommand(MultiplayerSudokuSettings multiplayerSudokuSettings) {
		if (multiplayerSudokuSettings == null) {
			throw new IllegalArgumentException("multiplayerSudokuSettings is null");
		}
		this.multiplayerSudokuSettings = multiplayerSudokuSettings;
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
			throw new IllegalArgumentException("Given settingsActivity is null");
		}
		settingsActivity.setSettings(this.multiplayerSudokuSettings);
		return true; //There is no way to check if this worked or not;(
	}
}
