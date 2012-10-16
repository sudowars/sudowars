package org.sudowars.Model.CommandManagement.MultiplayerSettingsCommands;

import org.sudowars.Controller.Local.Activity.MultiplayerSettings;

public class KickMultiplayerClientCommand extends MultiplayerSettingsCommand {
	

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1754302014301198696L;

	public enum KickStatus {
	    KICK, KICKBAN
	}
	
	private KickStatus status;
	
	public KickMultiplayerClientCommand(KickStatus status) {
		if (status == null) {
			throw new IllegalArgumentException("Status can't be null");
		}
		this.status = status;
	}
	
	public boolean execute(MultiplayerSettings settingsActivity) {
		if (settingsActivity == null) {
			throw new IllegalArgumentException("settingsActivity is null");
		}
		if (this.status == KickStatus.KICK) {
			settingsActivity.onKick(false);
		} else {
			settingsActivity.onKick(true);
		}
		return true;
	}

}
