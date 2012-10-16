package org.sudowars.Model.CommandManagement;

public abstract class BaseCommand implements Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6653900865079706446L;
	
	private static int idCounter = 0;
	
	private int id;
	
	protected BaseCommand() {
		this.id = idCounter++;
	}
	
	@Override
	public int getId() {
		return this.id;
	}
	
	protected void setId(int id) {
		this.id = id;
	}

}
