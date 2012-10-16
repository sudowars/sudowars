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