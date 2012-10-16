package org.sudowars.Model.SudokuUtil;

import java.io.Serializable;

import org.sudowars.Model.CommandManagement.DeltaManager;
import org.sudowars.Model.Difficulty.Difficulty;
import org.sudowars.Model.Game.SingleplayerGame;

/**
 * This class encapsulates a snapshot of a running {@link SingleplayerGame}.
 */
public class SingleplayerGameState extends GameState implements Serializable {
	
	private static final long serialVersionUID = 7807703339535102341L;
	private final boolean obviousMistakes;
	private final boolean solveCell;
	private final boolean bookmark;
	private final DeltaManager deltaManager;
	
	/**
	 * Initializes a new instance of the {@link SingleplayerGameState} class.
	 * @param game The running {@link SingleplayerGame}.
	 * @param difficulty The difficulty of the given game
	 * @param obviousMistakesShown Indicates if mistakes made by the player are displayed.
	 * @param solveCellAllowed Indicates the assistant can be triggered by the player to solve a cell. 
	 * @param bookmarkAllowed Indicates if the player can use the bookmark feature.
	 * @param deltaManager Reference to a {@link DeltaManager} containg all possible undo/redo operations.
	 * @throws IllegalArgumentException if either game, difficulty or deltaManager were {@code null}
	 */
	public SingleplayerGameState(SingleplayerGame game, Difficulty difficulty,
			boolean obviousMistakesShown, boolean solveCellAllowed, boolean bookmarkAllowed, DeltaManager deltaManager) 
					throws IllegalArgumentException{
		
		super(game, difficulty);
		if (deltaManager == null)  throw new IllegalArgumentException("deltaManager cannot be null.");
		
		this.obviousMistakes = obviousMistakesShown;
		this.solveCell = solveCellAllowed;
		this.bookmark = bookmarkAllowed;
		this.deltaManager = deltaManager;
	}
	
	/**
	 * Indicates if obvious mistakes are displayed by the application.
	 * @return {@code true} if so, otherwise {@code false}
	 */
	public boolean isShowObviousMistakesEnabled() {
		return this.obviousMistakes;
	}

	/**
	 * Indicates the assistant can be triggered by the player to solve a cell.
	 * @return {@code true} if so, otherwise {@code false}
	 */
	public boolean isSolveCellEnabled() {
		return this.solveCell;
	}
	
	/**
	 * Indicates if the player can use the bookmark feature.
	 * @return {@code true} if so, otherwise {@code false}
	 */
	public boolean isBookmarkEnabled() {
		return this.bookmark;
	}
	
	/**
	 * Gets the delta manager of the current game.
	 * @return Reference to the {@link DeltaManager} containg all possible undo/redo operations.
	 */
	public DeltaManager getDeltaManager() {
		return this.deltaManager;
	}
}
