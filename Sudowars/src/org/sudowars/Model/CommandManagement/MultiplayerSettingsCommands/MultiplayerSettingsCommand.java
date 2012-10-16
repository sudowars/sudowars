package org.sudowars.Model.CommandManagement.MultiplayerSettingsCommands;

import org.sudowars.Controller.Local.Activity.MultiplayerSettings;
import org.sudowars.Model.CommandManagement.BaseCommand;

public abstract class MultiplayerSettingsCommand extends BaseCommand {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 820075323241139338L;

	public abstract boolean execute(MultiplayerSettings settings);

}
