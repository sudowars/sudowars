package org.sudowars.Model.CommandManagement.GameCommands;

import org.sudowars.Model.Game.Game;
import org.sudowars.Model.Game.GameCell;
import org.sudowars.Model.Game.MultiplayerGame;
import org.sudowars.Model.Game.Player;

public class MultiplayerGameSetCellValueCommand extends CellCommand {

	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3467974332713719607L;
	private int cellValue;
	private boolean prepare;
	private long timestamp;
	private Player creatingPlayer;
	private boolean executed;
	
	public MultiplayerGameSetCellValueCommand(GameCell cell, int cellValue, long timestamp, Player localPlayer)throws IllegalArgumentException {
		super(cell);
		assert (cellValue > 0 && cellValue < 17);		
		if (cellValue > 0 && cellValue < 17) {
			this.cellValue = cellValue;
		} else {
			throw new IllegalArgumentException("cellValue is out of range");
		}
		this.prepare = true;
		this.timestamp = timestamp;
		this.creatingPlayer = localPlayer;
		this.executed = false;
	}

	@Override
	public GameCommand getInvertedCommand(Game game) {
		throw new IllegalStateException();
	}
		
	@Override
	public boolean execute(Game game, Player executingPlayer) throws IllegalArgumentException {
		if (game == null || executingPlayer == null || !(game instanceof MultiplayerGame)) {
			throw new IllegalArgumentException();
		}
		boolean result = false;
		if (this.prepare) {
			// local preparation
			// make cell pending and set value while owner stays null
			
			GameCell cell = game.getSudoku().getField().getCell(this.cellIndex);
			
			if (cell.isOwnerPending()) {
				// if cell already pending drop request
				// as an earlier command has already started execution
				return false;
			}
			
			result = game.setValue(this.creatingPlayer, cell, this.cellValue, this.timestamp);
			this.prepare = false;
		} else {
			// local or remote execution
			if (!this.creatingPlayer.equals(executingPlayer)) {
				// local execution
				
				GameCell cell = game.getSudoku().getField().getCell(this.cellIndex);
				
				if (!cell.isSet()) {
					this.executed = true;
					return false;
				}
				
				if (this.timestamp == cell.getTimestamp()) {
					// this command made the cell pending
					if (((MultiplayerGame) game).attachCellToPlayer(this.creatingPlayer, cell, this.cellValue)) {
						result = true;					
					} else {
						// game already aborted: drop
					}
				} else {
					// otherwise drop command
				}
				this.executed = true;
			} else {
				// remote execution
				GameCell cell = game.getSudoku().getField().getCell(this.cellIndex);
				if(game.setValue(this.creatingPlayer, cell, this.cellValue, this.timestamp)) {
					result = ((MultiplayerGame)game).attachCellToPlayer(this.creatingPlayer, cell, this.cellValue);
				} else {
					this.executed = true;
				}
			}
			
			if (result) {
				// execution successful
				GameCell cell = game.getSudoku().getField().getCell(this.cellIndex);
				if (cell.isOwnerPending()) {
					throw new IllegalStateException();
				}
				if (cell.getOwningPlayer() == null) {
					throw new IllegalStateException();
				}
			}
			
		}
		
		return result;
	}

	public boolean wasExecuted() {
		return this.executed;
	}
	
	public boolean isCreatingPlayer(Player player) {
		return this.creatingPlayer.equals(player);
	}
}
