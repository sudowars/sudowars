package org.sudowars.Model.Game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.os.SystemClock;

import org.sudowars.Model.Sudoku.Sudoku;
import org.sudowars.Model.Sudoku.Field.Cell;
import org.sudowars.Model.Sudoku.Field.Field;
import org.sudowars.Model.Sudoku.Field.FieldBuilder;
import org.sudowars.Model.Sudoku.RuleManagement.DependencyManager;
import org.sudowars.Model.SudokuUtil.NoteManager;

/**
 * This class provides fundamental game functionality.
 */
public abstract class Game implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5181586542160404692L;
	
	transient private List<GameChangedEventListener> registeredOnChangeObservers;
	transient private List<GameFinishedEventListener> registeredOnFinishObservers;
	transient private List<StopWatchTickEventListener> registeredOnStopWatchTickObservers;
	transient private List<GameAbortedEventListener> registeredOnGameAbortObservers;
	
	protected final Sudoku<GameCell> sudoku;
	protected List<PlayerSlot> participatingPlayers;
	
	private final StopWatch stopwatch;
	
	private boolean isPaused = true;
	private boolean isStarted = false;
	private boolean isAborted = false;
	private PlayerSlot abortingPlayerSlot = null;
	
	/**
	 * Initializes a new instance of the {@link Game} class.
	 *
	 * @param sudoku A reference to a {@link Sudoku} to use during the game.
	 *
	 * @throws IllegalArgumentException if the given sudoku is <code>null</code>
	 */
	public Game(Sudoku<Cell> sudoku) throws IllegalArgumentException {
		if (sudoku == null) {
			throw new IllegalArgumentException("given sudoku cannot be null.");
		}
		this.sudoku = createGameSudoku(sudoku.getField(), sudoku.getDependencyManager());
		this.stopwatch = new GameStopWatch(this);
		initializeObserverLists();
	}
		
	private void initializeObserverLists() {
		this.registeredOnChangeObservers = new LinkedList<GameChangedEventListener>();
		this.registeredOnFinishObservers = new LinkedList<GameFinishedEventListener>();
		this.registeredOnStopWatchTickObservers = new LinkedList<StopWatchTickEventListener>();
		this.registeredOnGameAbortObservers = new LinkedList<GameAbortedEventListener>();
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		initializeObserverLists();
	}
	
	private static Sudoku<GameCell> createGameSudoku(Field<Cell> field, DependencyManager dependencies) {
		assert field != null && dependencies != null;
		
		GameCellBuilder gameCellBuilder = new GameCellBuilder(field);
		Field<GameCell> sudokuField = new FieldBuilder<GameCell>().build(field.getStructure(), gameCellBuilder);
		return new Sudoku<GameCell>(sudokuField, dependencies);
	}
	
	/**
	 * Creates a new, empty {@link PlayerSlot}
	 *
	 * @return A reference to a {@link PlayerSlot} which has not yet been attached to a player.
	 */
	protected abstract PlayerSlot createPlayerSlot();
	
	/**
	 * Method to distribute the onTick event of the game's stopwatch to all attached listeners.
	 */
	private void onStopWatchTick(int tickCount, long elapsedMilliseconds) {
		for (StopWatchTickEventListener listener : this.registeredOnStopWatchTickObservers) {
			listener.onTick(tickCount, elapsedMilliseconds);
		}
	}
	
	/**
	 * Method to distribute the onGameAborted event to all attached listeners.
	 */
	protected final void onGameAborted(PlayerSlot abortingPlayerSlot) {
		assert abortingPlayerSlot != null && abortingPlayerSlot.getPlayer() != null && this.participatingPlayers.contains(abortingPlayerSlot);
		
		Player abortingPlayer = abortingPlayerSlot.getPlayer();
		this.pauseGame(abortingPlayer);
		this.isAborted = true;
		this.abortingPlayerSlot = abortingPlayerSlot;
		GameAbortedEvent eventData = new GameAbortedEvent(this, abortingPlayer);
		for (GameAbortedEventListener listener : this.registeredOnGameAbortObservers) {
			listener.onGameAborted(eventData);
		}
	}	
	/**
	 * Method to distribute the onChange event to all attached listeners.
	 */
	protected final void onChange(GameCell changedCell) {
		GameChangedEvent eventData = new GameChangedEvent(this, changedCell);
		//inform all attached listeners here
		for (GameChangedEventListener listener : this.registeredOnChangeObservers) {
			listener.onGameChanged(eventData);
		}
	}
	
	/**
	 * Method to distribute the onSuccessfullyFinish event to all attached listeners.
	 */
	protected final void onSuccessfullyFinish(Player winner) {
		assert winner != null;
		this.pauseGame(winner);
		GameFinishedEvent eventData = new GameFinishedEvent(this, winner);
		//inform all attached listeners here
		for (GameFinishedEventListener listener : this.registeredOnFinishObservers) {
			listener.onGameSuccessfullyFinish(eventData);
		}
	}
	
	/**
	 * Gets the sudoku of the game
	 *
	 * @return A reference to a {@link Sudoku}.
	 */
	public Sudoku<GameCell> getSudoku() {
		return this.sudoku;
	}
	

	/**
	 * Gets the elapsed time of the current game.
	 *
	 * @return The elapsed time, in milliseconds.
	 * @see Game#addOnStopWatchTickListener(StopWatchTickEventListener)
	 */
	public long getGameTime() {
		return this.stopwatch.getElapsedTime();
	}
	
	/**
	 * Gets a list containing the {@link Player}s participating at the game.
	 *
	 * @return A READ-ONLY list containing the {@link Player}s participating at the game,<br>
	 * which is empty if no players have joined yet  
	 */
	public final List<Player> getPlayers() {
		ArrayList<Player> players = new ArrayList<Player>(this.participatingPlayers.size());
		Player player = null;
		for (PlayerSlot slot : this.participatingPlayers) {
			player = slot.getPlayer();
			if (player != null) {
				players.add(player);
			}
		}
		return Collections.unmodifiableList(players);
	}
	
	/**
	 * Adds the given observer to the onGameAborted observer list of the current instance.
	 *
	 * @param actionListener A reference to an {@link GameAbortedEventListener} instance.
	 *
	 * @return <code>true</code>, if the execution was successfully
	 * @see List#add(Object)
	 */
	public final boolean addOnGameAbortListener(GameAbortedEventListener actionListener) {
		return this.registeredOnGameAbortObservers.add(actionListener);
	}
	
	/**
	 * Removes the first occurrence of the specified element from the onGameAborted observer list of the current instance.
	 *
	 * @param listener The {@link GameAbortedEventListener} to remove.
	 *
	 * @return <code>true</code>, if element was contained in the list, otherwise <code>false</code>
	 * @see List#remove(Object)
	 */
	public final boolean removeOnGameAbortListener(GameFinishedEventListener listener) {
		return this.registeredOnGameAbortObservers.remove(listener);
	}
	
	/**
	 * Adds the given observer to the onChange observer list of the current instance.
	 *
	 * @param actionListener A reference to an {@link GameChangedEventListener} instance.
	 *
	 * @return <code>true</code>, if the execution was successfully
	 * @see List#add(Object)
	 */
	public final boolean addOnChangeListener(GameChangedEventListener actionListener) {
		return this.registeredOnChangeObservers.add(actionListener);
	}
	
	/**
	 * Adds the given observer to the onSuccessfullyFinish observer list of the current instance, <br>
	 * which is fired, if the game's sudoku was successfully solved.
	 *
	 * @param actionListener A reference to an {@link GameFinishedEventListener} instance.
	 *
	 * @return <code>true</code>, if the execution was successfully
	 * @see List#add(Object)
	 */
	public final boolean addOnSuccessfullyFinishListener(GameFinishedEventListener actionListener) {
		return this.registeredOnFinishObservers.add(actionListener);
	}
	
	/**
	 * Adds the given observer to the onStopWatchTick observer list of the stop watch of current instance.
	 *
	 * @param actionListener A reference to an {@link StopWatchTickEventListener} instance.
	 *
	 * @return <code>true</code>, if the execution was successfully
	 * @see List#add(Object)
	 */
	public final boolean addOnStopWatchTickListener(StopWatchTickEventListener actionListener) {
		return this.registeredOnStopWatchTickObservers.add(actionListener);
	}
	
	/**
	 * Removes the first occurrence of the specified element from the onSuccessfullyFinish observer list of the current instance.
	 *
	 * @param listener The {@link GameFinishedEventListener} to remove.
	 *
	 * @return <code>true</code>, if element was contained in the list, otherwise <code>false</code>
	 * @see List#remove(Object)
	 */
	public final boolean removeOnSuccessfullyFinishListener(GameFinishedEventListener listener) {
		return this.registeredOnFinishObservers.remove(listener);
	}
	
	/**
	 * Removes the first occurrence of the specified element from the onChange observer list of the current instance.
	 *
	 * @param listener The {@link GameChangedEventListener} to remove.
	 *
	 * @return <code>true</code>, if element was contained in the list, otherwise <code>false</code>
	 * @see List#remove(Object)
	 */
	public final boolean removeOnChangeListener(GameChangedEventListener listener) {
		return this.registeredOnChangeObservers.remove(listener);
	}
	
	/**
	 * Removes the first occurrence of the specified element from the onStopWatchTick observer list of stop watch of the current instance.
	 *
	 * @param listener The {@link StopWatchTickEventListener} to remove.
	 *
	 * @return <code>true</code>, if element was contained in the list, otherwise <code>false</code>
	 * @see List#remove(Object)
	 */
	public final boolean removeOnStopWatchTickListener(StopWatchTickEventListener listener) {
		return this.registeredOnStopWatchTickObservers.remove(listener);
	}
	
	/**
	 * Indicates whether the given player triggered the current game pause. 
	 * @param player A reference to a {@link Player}.
	 * @return {@code true}, if the player triggered the current game pause, <br>
	 * {@code false}, if the given player didn't trigger the pause, or the game is not paused
	 * @throws IllegalArgumentException if given player was {@code null} or doesn't participate
	 */
	public boolean hasPaused(Player player) throws IllegalArgumentException {
		PlayerSlot slot = getPlayerSlotOfPlayer(player);
		
		if (isPaused()) {
			return slot.hasPaused;
		}
		return false;
	}
	
	/**
	 * Pauses the current game
	 *
	 * @param player A reference to the {@link Player} who triggered the pause.
	 *
	 * @return <code>true</code> if game is now paused, otherwise <code>false</code>
	 * @throws IllegalArgumentException if given player was {@code null} or doesn't participate
	 * @see Game#resumeGame(Player)
	 */
	public boolean pauseGame(Player player) throws IllegalArgumentException {
		PlayerSlot slot = getPlayerSlotOfPlayer(player);
		boolean result = false;
		if (this.isStarted && !this.isAborted) {
			slot.setPausedState(true);
			result = true;
			if (!isPaused()) {
				this.isPaused = true;
				this.stopwatch.stop();
			}
		}
		return result;
	}
	
	/**
	 * Resumes the current game.
	 *
	 * @param player A reference to the player who triggered the resume.
	 *
	 * @return <code>true</code> if game is now not paused,
	 * <br><code>false</code> if given player didn't trigger the pause preliminarily, or
	 * game wasn't paused
	 * @throws IllegalArgumentException if given player was {@code null} or doesn't participate
	 * @see Game#pauseGame(Player)
	 */
	public boolean resumeGame(Player player) throws IllegalArgumentException {
		PlayerSlot slot = getPlayerSlotOfPlayer(player);
		
		if (!this.isStarted || !this.isPaused || this.isAborted) {
			return false;
		} else {
			slot.setPausedState(false);
			boolean isPaused = false;
			
			for (PlayerSlot p : this.participatingPlayers) {
				if (p.hasPaused) {
					isPaused = true;
					break;
				}
			}
			
			this.isPaused = isPaused;
			
			return !this.isPaused;
		}
	}
	
	/**Starts the game.
	*/
	public final void startGame() {
		if (!this.isAborted) {
			this.isPaused = false;
			this.isStarted = true;
			this.stopwatch.start();
		}
	}
	
	/**
	* Aborts the current game and exposes all cells which have not been solved yet.<br>
	* The call of this method should trigger the onChange event followed by the onGameAborted event.
	* @param abortingPlayer reference to the {@link Player} who triggered the abandonment. 
	*  @param timestamp A Long value indicating the abandonment time. 
	* @throws IllegalArgumentException if no player has yet been attached to the game,<br>
	* or the given player is not equal to one of the players attached 
	*/
	public abstract void abortGame(Player abortingPlayer, long timestamp) throws IllegalArgumentException; 
	
	/**
	 * Gets the player slot of a specified player.
	 * @param player reference to a {@link Player}
	 * @return the underlying {@link PlayerSlot} of the given player
	 * @throws IllegalArgumentException if given player was {@code null}<br>
	 * or no players participate at the game,<br>
	 * or the given player doesn't participate
	 */
	protected PlayerSlot getPlayerSlotOfPlayer (Player player)	throws IllegalArgumentException {
		if (player == null) {
			throw new IllegalArgumentException("given player was null.");
		}
		if (this.participatingPlayers == null || this.participatingPlayers.isEmpty()) {
			throw new IllegalArgumentException("no players participating.");
		}
		PlayerSlot slot = getPlayerSlotOfPlayer(player, this.participatingPlayers);
		if (slot == null) {
			throw new IllegalArgumentException("given player doesn't participate.");
		}
		return slot;
	}
	
	private static PlayerSlot getPlayerSlotOfPlayer(Player player, List<PlayerSlot> participatingPlayers) {
		assert player != null && participatingPlayers != null;
		
		for (PlayerSlot playerSlot : participatingPlayers) {
			if (player.equals(playerSlot.getPlayer())) {
				return playerSlot;
			}
		}
		return null;
	}
	
	/**
	 * Gets the {@link NoteManager} of the specified player.
	 * @param player reference to a {@link Player}
	 * @return the {@link NoteManager} of the specified player
	 * @throws IllegalArgumentException if given player was {@code null},<br>
	 * or doesn't participate at the current game
	 */
	public NoteManager getNoteManagerOfPlayer(Player player) throws IllegalArgumentException {
		//the following call either throws an IllegalArgumentException or returns a value != null
		PlayerSlot slot = getPlayerSlotOfPlayer(player);
		return slot.getNoteManager();
	}
	
	/**
	 * Sets the {@link NoteManager} for a given player.
	 * @param player reference to a {@link Player}
	 * @param noteManager reference to a {@link NoteManager}
	 * @throws IllegalArgumentException if given player was {@code null},<br>
	 * or doesn't participate at the current game,<br>
	 * or the given note manager was {@code null}
	 */
	public void setNoteManagerOfPlayer(Player player, NoteManager noteManager) throws IllegalArgumentException {
		//the following call either throws an IllegalArgumentException or returns a value != null
		PlayerSlot slot = getPlayerSlotOfPlayer(player);
		slot.setNoteManager(noteManager);
	}
	
	/**
	 * Indicates whether the current game is aborted, i.e. not finished successfully
	 * @return {@code true} if game was aborted, otherwise {@code false}
	 */
	public boolean isAborted() {
		return this.isAborted;
	}
	
	/**
	 * Gets the player who aborted the game, if exists.
	 * @return reference to the {@link Player} who aborted the game, {@code null}, if game was not yet aborted
	 */
	public Player getAbortingPlayer() {
		if (this.isAborted) return this.abortingPlayerSlot.getPlayer();
		return null;
	}
	
	/**
	 * Indicates whether the game is currently paused.
	 *
	 * @return <code>true</code> if game is paused or not yet started, otherwise <code>false</code>
	 */
	public boolean isPaused() {
		return this.isPaused || !this.isStarted;
	}
	
	/**
	 * Indicates whether the game is currently started.
	 * @return <code>true</code> if game has been started
	 */
	public boolean isStarted() {
		return this.isStarted;
	}
	/**
	 * Sets the value of the given cell and attaches it to the given player.
	 *
	 * @param player A reference to the <code>Player</code> who sets the value.
	 * @param cell A reference to the <code>null</code> whose value is to be set.
	 * @param value The new value of the cell.
	 *
	 * @return <code>true</code> if value was successfully set, otherwise <code>false</code>
	 * @throws IllegalArgumentException if value is equal or less than {@link DataCell#NOT_SET}
	 * @throws IllegalArgumentException if player or cell is {@code null}
	 */
	public abstract boolean setValue(Player player, GameCell cell, int value, long timestamp) throws IllegalArgumentException;
	
	/**
	 * Gets the {@link GameCell} of a field with the given index.
	 * @param index an integer value indicating the cell's index
	 * @param field a field containing {@code GameCell}s
	 * @return reference to the {@link GameCell} with the given index
	 * @throws IllegalArgumentException  if the given index is out of bounds
	 * defined by the underlying field structure
	 */
	protected static GameCell getGameCellByIndex(int index, Field<GameCell> field) throws IllegalArgumentException {
		assert index >= 0 && field != null;
		
		return field.getCell(index);
	}
	
	protected static boolean successfullySolved(Sudoku<GameCell> sudoku) {
		assert sudoku != null;
		
		boolean result = false;
		Field<GameCell> field = sudoku.getField();
		if (field.isFilled()) {
			result = true;
			for (GameCell cell : field.getCells()) {
				if (cell.getValue() != cell.getSolution()) {
					result = false;
					break;
				}
			}
		}
		return result;
	}
	
	/**
	 * Exposes all cells which have not been solved by any of the players yet.
	 * @param sudoku the game's sudoku
	 * @param playerSlot the player who is expected to own all cells to be exposed
	 */
	protected static void exposeAllCells(Sudoku<GameCell> sudoku, PlayerSlot playerSlot, long timestamp) {
		assert sudoku != null && playerSlot != null;
		for (GameCell currentCell : sudoku.getField().getCells()) {
			if (currentCell != null && !currentCell.isInitial() && currentCell.getValue() != currentCell.getSolution()) {
				currentCell.setValue(currentCell.getSolution(), timestamp);
				currentCell.attachToPlayer(playerSlot);
			}
		}
	}
	
	private final class GameStopWatch extends StopWatch implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -5802064952119912158L;
		private final Game game;
		
		GameStopWatch(Game game) {
			super();
			this.game = game;
		}
		
		@SuppressWarnings("synthetic-access")
		@Override
		public void step() {
			if (this.game != null && !this.game.isPaused() && !this.game.isAborted()) {
				this.game.onStopWatchTick(this.tickCount++, this.elapsedMilliseconds);
			}
		}
		
		private void writeObject(ObjectOutputStream out) throws IOException {
			// TODO clone!
			if (this.running) {
				//ensure elapsed time is up to date
				this.elapsedMilliseconds += (SystemClock.uptimeMillis() - this.lastLogTime);
			}
			out.defaultWriteObject();
			//out.writeLong(this.elapsedMilliseconds);
			//out.writeLong(this.tickInterval);
		}
		
		private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
			in.defaultReadObject();
			this.lastLogTime = SystemClock.uptimeMillis();
			this.running = false;
			this.tickCount--;
		}
	}
}
