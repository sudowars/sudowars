package org.sudowars.View;


import java.util.Iterator;
import java.util.List;

import org.sudowars.DebugHelper;
import org.sudowars.R;
import org.sudowars.Model.Game.Game;
import org.sudowars.Model.Game.GameCell;
import org.sudowars.Model.Game.GameChangedEvent;
import org.sudowars.Model.Game.GameChangedEventListener;
import org.sudowars.Model.Game.MultiplayerGame;
import org.sudowars.Model.Game.Player;
import org.sudowars.Model.Game.SingleplayerGame;
import org.sudowars.Model.Sudoku.Field.Cell;
import org.sudowars.Model.SudokuUtil.NoteManager;
import org.sudowars.Model.SudokuUtil.NoteManagerChangedEvent;
import org.sudowars.Model.SudokuUtil.NoteManagerChangedEventListener;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ZoomButtonsController;

public class SudokuField extends View {
	/**
	 * Paint to draw the normal lines
	 */
	private Paint linePaint;
	
	/**
	 * Paint to draw the invalid numbers when selected
	 */
	private Paint normalPaintInvalid;
	
	/**
	 * Paint to draw the bold lines 
	 */
	private Paint boldLinePaint;
	
	/**
	 * Paint to fill a marked Box
	 */
	private Paint fillBoxPaint;
	
	/**
	 * Paint to fill an invalid Box
	 */
	private Paint invalidBoxPaint;
	
	/**
	 * Paint for the notices
	 */
	private Paint noticePaint;
	
	/**
	 * Paint for the notices selected
	 */
	private Paint noticePaintSelected;
	
	/**
	 * Normal paint
	 */
	private Paint normalPaint;
	
	private Paint markedPaint;
	
	/**
	 * Normal selected paint
	 */
	private Paint normalPaintSelected;
	
	/**
	 * Normal paint for initial fields
	 */
	private Paint initialPaint;
	
	/**
	 * Paint for the symbols of the opponent
	 */
	private Paint opponentPaint;
	
	/**
	 * color for Disabled Fields
	 */
	private Paint fieldDisabledPaint;
	
	/**
	 * color for Pending Fields
	 */
	private Paint pendingPaint;
	
	/**
	 * actual selected field X
	 */
	private int selectedFieldX = -1;
	
	/**
	 * actual selected field Y
	 */
	private int selectedFieldY = -1;
	
	/**
	 * detects the zoom gesture
	 */
	private ScaleGestureDetector scaleGestureDetector;
	
	/**
	 * The scale factor
	 */
	private float scaleFactor = (float) 1.0;
	
	/**
	 * The scaled movement in X-position
	 */
	private int convertX = 0;
	
	/**
	 * Controlls the appereance of the Zoombuttons
	 */
	ZoomButtonsController zbc;
	
	/**
	 * The scaled movement in Y-position
	 */
	private int convertY = 0;
	
	private boolean cursorMoved = false;
	
	/**
	 * Last X and Yposition of the first finger on the Device
	 */
	float lastX;
	float lastY;
	
	/**
	 * Wether to show obvious mistakes or not
	 */
	private boolean showRedundant = true;

	/**
	 * For detecting the Convert amount
	 */
	
	private int activePointerID = -1;
	
	/**
	 * The onclick listener for events
	 */
	private OnClickListener onClickListener = null;
	
	/**
	 * The Sudoku symbol table
	 */
	private SymbolTable symbols;
	
	/**
	 * The game
	 */
	private Game game = null;
	
	/**
	 * The notes
	 */
	private NoteManager noteManager;

	/**
	 * If zoombuttons are enabled
	 */
	
	private boolean zoomButtons = false;
	
	/**
	 * The size of one Square
	 */
	private int squareSize;
	
	/**
	 * size of the Sudoku on the screen
	 */
	private int size = 0;
	
	/**
	 * defines if the Field is disable (not clickable)
	 */
	private boolean fieldDisabled = false;
	
	/**
	 * Marked Cells
	 */

	private char markedCells[][];
	
	
	Handler unmarker = new Handler();
	/**
	 * Constructs a new {@link SudokuField}
	 * Gets called by the view (Android system)
	 *
	 * @param context the context
	 * @param attrs the attributes
	 */
	public SudokuField(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		ZoomHandler zh = new ZoomHandler();
		this.scaleGestureDetector = new ScaleGestureDetector(context, zh);
		
		this.boldLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		this.boldLinePaint.setColor(this.getResources().getColor(R.color.sudoku_field_bold_line));
		this.boldLinePaint.setStrokeWidth(3);
		
		this.linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		this.linePaint.setColor(this.getResources().getColor(R.color.sudoku_field_normal_line));
		this.linePaint.setStrokeWidth(1);
		
		this.fillBoxPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		this.fillBoxPaint.setColor(this.getResources().getColor(R.color.sudoku_field_selected));
		this.fillBoxPaint.setStyle(Paint.Style.FILL);
		
		this.invalidBoxPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		this.invalidBoxPaint.setColor(this.getResources().getColor(R.color.sudoku_field_invalid));
		this.invalidBoxPaint.setStyle(Paint.Style.FILL);
		
		this.opponentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		this.opponentPaint.setColor(this.getResources().getColor(R.color.sudoku_field_remote));
		
		this.normalPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		this.normalPaint.setColor(this.getResources().getColor(R.color.sudoku_field_normal));
		
		this.normalPaintSelected = new Paint(Paint.ANTI_ALIAS_FLAG);
		this.normalPaintSelected.setColor(this.getResources().getColor(R.color.sudoku_field_normal_selected));
		
		this.initialPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		this.initialPaint.setColor(this.getResources().getColor(R.color.sudoku_field_initial));
		
		this.normalPaintInvalid = new Paint(Paint.ANTI_ALIAS_FLAG);
		this.normalPaintInvalid.setColor(this.getResources().getColor(R.color.sudoku_field_normal_invalid));
		
		this.fieldDisabledPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		this.fieldDisabledPaint.setColor(this.getResources().getColor(R.color.sudoku_field_field_disabled));
		this.fieldDisabledPaint.setStyle(Paint.Style.FILL);
		
		this.noticePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		this.noticePaint.setColor(this.getResources().getColor(R.color.sudoku_field_notice));
		
		this.markedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		this.markedPaint.setColor(this.getResources().getColor(R.color.sudoku_field_remote));
		
		this.pendingPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		this.pendingPaint.setColor(this.getResources().getColor(R.color.sudoku_field_pending_paint));
		
		this.noticePaintSelected = new Paint(Paint.ANTI_ALIAS_FLAG);
		this.noticePaintSelected.setColor(this.getResources().getColor(R.color.sudoku_field_notice_selected));
	}

	/**
	 * Draws a dummy Canvas for the android Editor
	 * @param canvas Canvas to draw on
	 */
	private void drawDummy(Canvas canvas) {
		
		for (int n = 0; n <= 3; n++) {
			canvas.drawLine(0, n * 3 * this.squareSize, this.getWidth(), n * 3 * this.squareSize, this.boldLinePaint);
			canvas.drawLine( n * 3 * this.squareSize, 0, n * 3 * this.squareSize, this.getHeight(), this.boldLinePaint);
		}
		
		//Drawing the normal Lines
		
		for (int n = 0; n < 9; n++) {
			if (n % 3 == 0)
				continue;
			canvas.drawLine(0, n * this.squareSize, this.getWidth(), n * this.squareSize, this.linePaint);
			canvas.drawLine( n * this.squareSize, 0, n * this.squareSize, this.getHeight(), this.linePaint);
		}
		
	}
	
	/**
	 * Private helber function to draw a centered Char
	 * @param c char to draw
	 * @param p font to draw with
	 * @param x destination center
	 * @param y destination center
	 * @param canvas canvas to draw on
	 */
	
	private static void drawCentered(char c, Paint p, int x, int y, Canvas canvas) {
		String text = Character.toString(c);
		Rect bounds = new Rect();
		p.getTextBounds(text, 0, text.length(), bounds);
		canvas.drawText(text, x - bounds.centerX(), y - bounds.centerY(), p);
		
		//DebugHelper.log("SudokuField", "Draw " + x + " " + y + " " + bounds.centerX() + " " + text);
		
	}
	
	private Paint getPaintForCell(GameCell gc, int x, int y, boolean activeCellInvalid) {
		Player localPlayer = this.game.getPlayers().get(0);
		
		if (gc.isInitial())
			return this.initialPaint;
		if (this.game instanceof MultiplayerGame) {
			if (gc.isOwnerPending())
				return this.pendingPaint;
			if (gc.getOwningPlayer() == null)
				return this.pendingPaint;
			if (gc.getOwningPlayer().equals(localPlayer)) {
				if (this.selectedFieldX == x && this.selectedFieldY == y) 
					return this.normalPaintSelected;
				else
					return this.normalPaint;
			}else {
				if (this.markedCells[x][y] != 0)
					return this.normalPaintSelected;
				
				return this.opponentPaint;
			}
		}else {
			if (this.selectedFieldX == x && this.selectedFieldY == y) {
				if (activeCellInvalid)
					return this.normalPaintInvalid;
				return this.normalPaintSelected;
			}else {
				return this.normalPaint;
			}	
		}
	}
	
	/**
	 * This method is called when the view is marked invalid, this means the data has been changed and the View needs to be redrawn
	 * This View is only Valid for normal Sudokus
	 * @param canvas the canvas to draw on
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		
		List<Integer> notes = null;
		Paint tmpPaint;
		
		if (this.game == null) {
			this.drawDummy(canvas);
			return;
		}
		
		boolean activeCellInvalid = false;
		
		canvas.save();
        canvas.scale(this.scaleFactor, this.scaleFactor);
        canvas.translate(this.convertX, convertY);
		
      //This is not a solution for all Blocks Problem, tough we only support 9x9 and 16x16 Sudokus, this one is valid
  		
        
		int height = this.game.getSudoku().getField().getStructure().getHeight(), width = this.game.getSudoku().getField().getStructure().getHeight();
		int realHeight = height * squareSize;
		int realWidth = width * squareSize;
		
	      //This is not a solution for all Blocks Problem, tough we only support 9x9 and 16x16 Sudokus, this one is valid
		int blockSize = (int) Math.sqrt(height);
		
		if (this.showRedundant && (!this.fieldDisabled)) {
			if (this.game instanceof SingleplayerGame) {
				Iterator<GameCell> invalidCells = ((SingleplayerGame) this.game).getCellsContainingInvalidValues().iterator();
				Cell invalidCell;
				
				while (invalidCells.hasNext()) {
					invalidCell = invalidCells.next();
					DebugHelper.log(DebugHelper.PackageName.SudokuField, "Highlighting illegalValues " + invalidCell.getIndex());
					if (this.selectedFieldX != -1 && this.selectedFieldY != -1){
						if (((GameCell) this.game.getSudoku().getField().getCell(this.selectedFieldX, this.selectedFieldY)).equals(invalidCell))
							activeCellInvalid = true;
					}
					int invalidx = invalidCell.getIndex() % width;
					int invalidy = Math.round( invalidCell.getIndex() / height);
					canvas.drawRect(new Rect(invalidx * this.squareSize, invalidy * this.squareSize, (invalidx + 1) * this.squareSize, (invalidy + 1) * this.squareSize), this.invalidBoxPaint);
				}
			}
		}
		
		
		if (this.selectedFieldX != -1 && this.selectedFieldY != -1) {
			canvas.drawRect(new Rect(this.selectedFieldX * this.squareSize, this.selectedFieldY * this.squareSize, (this.selectedFieldX + 1) * this.squareSize, (this.selectedFieldY + 1) * this.squareSize), this.fillBoxPaint);
		}
		
		
		
		//First we Fill the Fields
		
		for (int x = 0; x < this.game.getSudoku().getField().getStructure().getWidth(); x++) {
			for (int y = 0; y < this.game.getSudoku().getField().getStructure().getHeight(); y++) {
				if (! this.game.getSudoku().getField().getStructure().isSlotUsed(x, y)) {
					canvas.drawRect(new Rect(x * this.squareSize, y * this.squareSize, (x + 1) * this.squareSize, (y + 1) * this.squareSize), this.fieldDisabledPaint);
					continue;
				} 
				if (this.markedCells[x][y] == 1)
					canvas.drawRect(new Rect(x * this.squareSize, y * this.squareSize, (x + 1) * this.squareSize, (y + 1) * this.squareSize), this.markedPaint);
				if (this.markedCells[x][y] == 2)
					canvas.drawRect(new Rect(x * this.squareSize, y * this.squareSize, (x + 1) * this.squareSize, (y + 1) * this.squareSize), this.invalidBoxPaint);
				
				
				GameCell gc = (GameCell) this.game.getSudoku().getField().getCell(x, y);
				Player localPlayer = this.game.getPlayers().get(0);
				if (gc.getValue() != 0) {
					tmpPaint = getPaintForCell(gc, x, y, activeCellInvalid);
					drawCentered(this.symbols.getSymbol(gc.getValue()), tmpPaint, x * this.squareSize + (this.squareSize / 2), y * this.squareSize + (this.squareSize / 2), canvas);
					
				}else {	//Not set yet, check for notices
					if (this.noteManager != null) {
						try {
							notes = this.noteManager.getNotes(gc);
						}catch (IllegalArgumentException e) {
							// TODO: handle exception
							continue;
						}
						if (notes == null) {
							continue;
						}
						if (!notes.isEmpty()) {
							Iterator<Integer> iter= notes.iterator();
							while (iter.hasNext()) {
								int note = iter.next();
								if (x == this.selectedFieldX && y == this.selectedFieldY)
									drawCentered(symbols.getSymbol(note), this.noticePaintSelected, 
											x * this.squareSize + (int)((this.squareSize / (blockSize * 2)) * 1) + ((note - 1) % blockSize) * ((this.squareSize) / blockSize), 
											y * this.squareSize + 1 + (int)((this.squareSize / (blockSize * 2)) * 1) + (Math.round((note - 1) / blockSize)) * ((this.squareSize - blockSize) / blockSize), 
											canvas);
								else
									drawCentered(symbols.getSymbol(note), this.noticePaint, 
											x * this.squareSize + (int)((this.squareSize / (blockSize * 2))) + ((note - 1) % blockSize) * ((this.squareSize) / blockSize), 
											y * this.squareSize + 1 + (int)((this.squareSize / (blockSize * 2)) * 1) + (Math.round((note - 1) / blockSize)) * ((this.squareSize - blockSize) / blockSize), 
											canvas);
								
							}
						}
					}
				}
			}
		}
		
		
		//Drawing the bold lines
		
		for (int n = 0; n <= (height / blockSize); n++) {
			canvas.drawLine(0, n * blockSize * this.squareSize, realWidth, n * blockSize * this.squareSize, this.boldLinePaint);
			canvas.drawLine( n * blockSize * this.squareSize, 0, n * blockSize * this.squareSize, realHeight, this.boldLinePaint);
		}
		
		//Drawing the normal Lines
		
		for (int n = 0; n < height; n++) {
			if (n % blockSize == 0)
				continue;
			canvas.drawLine(0, n * this.squareSize, realWidth, n * this.squareSize, this.linePaint);
			canvas.drawLine( n * this.squareSize, 0, n * this.squareSize, realHeight, this.linePaint);
		}
		
		
		
		canvas.restore();
		
		
	}


	/**
	 * Determine the size of the view
	 */
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);
        int d = w == 0 ? h : h == 0 ? w : w < h ? w : h;
        setMeasuredDimension(d, d);
        refreshPaintSize();
	}

	/**
	 * Called when the view size changes
	 */
	protected void onSizeChanged(int newWidth, int newHeight, int oldWidth, int oldHeight) {
		super.onSizeChanged(newWidth, newWidth, oldWidth, oldHeight);

		if (newHeight < newWidth) {
			setMeasuredDimension(newHeight, newHeight);
			this.size = newHeight;
		}else{
			setMeasuredDimension(newWidth, newWidth);
			this.size = newWidth;
		}
		if (this.game != null) 
			this.squareSize = Math.min(this.getWidth() / this.game.getSudoku().getField().getStructure().getWidth(), this.getHeight() / this.game.getSudoku().getField().getStructure().getHeight());
		else
			this.squareSize = this.getHeight() / 9;
		refreshPaintSize();
	}

	/**
	 * Sets the on click listener for this view
	 *
	 * @param listener the listener
	 */
	public void setOnClickListener(OnClickListener listener) {
		this.onClickListener = listener;
	}
	
	/**
	 * Sets the notes
	 *
	 * @param noteManager the notes
	 */
	public void setNoteManager(NoteManager noteManager) {
		this.noteManager = noteManager;
	}

	/**
	 * Returns the selected cell.
	 *
	 * @return the selected cell
	 */
	public GameCell getSelectedCell() {
		if (this.selectedFieldX != -1 && this.selectedFieldY != -1)
			return this.game.getSudoku().getField().getCell(this.selectedFieldX, this.selectedFieldY);
		
		return null;
	}
	
	private void refreshPaintSize() {
		
		this.squareSize = Math.min(this.getWidth() / this.game.getSudoku().getField().getStructure().getWidth(), this.getHeight() / this.game.getSudoku().getField().getStructure().getHeight());
		float normalPaintSize = (float) (this.squareSize * 0.8);
		float noteSize = (float) ((this.squareSize / (Math.sqrt(this.game.getSudoku().getField().getStructure().getWidth())) ) * 0.9);
		
		this.normalPaint.setTextSize(normalPaintSize);
		this.normalPaintSelected.setTextSize(normalPaintSize);
		this.normalPaintInvalid.setTextSize(normalPaintSize);
		this.opponentPaint.setTextSize(normalPaintSize);
		this.initialPaint.setTextSize(normalPaintSize);
		this.pendingPaint.setTextSize(normalPaintSize);
		
		/*
		if (this.game.getSudoku().getField().getStructure().getWidth() > 9) 
			this.noticePaint.setTextSize(6);
		else
			this.noticePaint.setTextSize((float) (19 / this.game.getSudoku().getField().getStructure().getWidth() * 9 * displayFactor));*/
		this.noticePaint.setTextSize(noteSize);
		this.noticePaintSelected.setTextSize(this.noticePaint.getTextSize());
		
		invalidate();
	}
	
	/**
	 * Sets the game, so the view knows what to draw
	 *
	 * @param game the game
	 */
	public void setGame (Game game) {
		if (game != null) {
			this.game = game;
			
			final Game eventGame = game;
			this.game.addOnChangeListener(new GameChangedEventListener() {
				
				@Override
				public void onGameChanged(GameChangedEvent event) {
					
					if (event.getChangedCell() != null && event.getChangedCell().getOwningPlayer() != null && !event.getChangedCell().getOwningPlayer().equals(eventGame.getPlayers().get(0))) {
						highlightCell(event.getChangedCell(), 3000);
					}
					invalidate();
					
				}
			});
			
			this.game.getNoteManagerOfPlayer(this.game.getPlayers().get(0)).addOnChangeListener(new NoteManagerChangedEventListener() {
				
				@Override
				public void onChange(NoteManagerChangedEvent event) {
					invalidate();
					
				}
			});
			
			invalidate();
			this.markedCells = new char[this.game.getSudoku().getField().getStructure().getWidth()][this.game.getSudoku().getField().getStructure().getWidth()];
			for (int x = 0; x < this.game.getSudoku().getField().getStructure().getWidth(); x++){
				for (int y = 0; y < this.game.getSudoku().getField().getStructure().getHeight(); y++) {
					markedCells[x][y] = 0;
				}
				
			}
				
		 
		DebugHelper.log(DebugHelper.PackageName.SudokuField, "" + ViewConfiguration.getZoomControlsTimeout());
		zbc = new ZoomButtonsController(this);
		zbc.setOnZoomListener(new ZoomButtonHandler());
		zbc.setAutoDismissed(true);
		this.zbc.setVisible(false);
		zbc.setZoomInEnabled(true);
		zbc.setZoomOutEnabled(true);
		}
		
	}

	/**
	 * This function is calld by the Android system, if a touchevent occurs on the view
	 *
	 * @param event the event
	 */
	public boolean onTouchEvent(MotionEvent event) {
		
		int action = event.getAction();
		
		if (this.game == null) {
			DebugHelper.log(DebugHelper.PackageName.SudokuField, "Game is null!");
			return true;
		}
		
		//Handle Zoom Events
		this.scaleGestureDetector.onTouchEvent(event);
		
		
		
		int x = Math.max((int) (((event.getX() / this.scaleFactor) - this.convertX) / this.squareSize), 0);
		int y = Math.max((int) (((event.getY() / this.scaleFactor) - this.convertY) / this.squareSize), 0);
		
		//Finger on the Display
		if (action == MotionEvent.ACTION_DOWN) {
			
			// do not move selector when gesture zooming 
			if (!this.scaleGestureDetector.isInProgress()) {
			
				if (this.activePointerID == -1) {
					this.lastX = event.getX();
					this.lastY = event.getY();
					
					if (x < this.game.getSudoku().getField().getStructure().getWidth() && y < this.game.getSudoku().getField().getStructure().getHeight()){
						DebugHelper.log(DebugHelper.PackageName.SudokuField, "X: " + x + " Y: " + y + " SF: " + this.scaleFactor + " " + this.convertX + " " + this.convertY);
						if (this.game.getSudoku().getField().getStructure().isSlotUsed(x, y)) {
							if (!this.fieldDisabled){
								this.selectedFieldX = x;
								this.selectedFieldY = y;
								if (this.onClickListener != null)
					            	this.onClickListener.onClick(this);
							}
						}
					}
					this.activePointerID = event.getPointerId(0); //To detect movement we only use the first finger on the display
				}
			
			}
		} else if (action == MotionEvent.ACTION_MOVE) { //I like to Move IT (we move our first finger
			if (this.activePointerID != -1) {
				if (! this.scaleGestureDetector.isInProgress()) {
					this.convertX += (event.getX() - this.lastX) / this.scaleFactor;
					this.convertY += (event.getY() - this.lastY) / this.scaleFactor;
					
					if (x < this.game.getSudoku().getField().getStructure().getWidth() && y < this.game.getSudoku().getField().getStructure().getHeight()){
						DebugHelper.log(DebugHelper.PackageName.SudokuField, "X: " + x + " Y: " + y + " SF: " + this.scaleFactor + " " + this.convertX + " " + this.convertY);
						if (this.game.getSudoku().getField().getStructure().isSlotUsed(x, y)) {
							/*if (!this.fieldDisabled){
								this.selectedFieldX = x;
								this.selectedFieldY = y;
								if (this.onClickListener != null)
					            	this.onClickListener.onClick(this);
							}*/
						}
					}
				}
				this.convertX = (int) Math.max(Math.min(this.convertX, 0), - this.size + (this.size / this.scaleFactor));
	        	this.convertY = (int) Math.max(Math.min(this.convertY, 0), - this.size + (this.size / this.scaleFactor));
				if (Math.abs(this.lastX - event.getX()) + Math.abs(this.lastY - event.getY()) >  20) {
					if ((!this.zbc.isVisible()) && this.zoomButtons)
						this.zbc.setVisible(true);
					cursorMoved = true;
				}
	        	this.lastX = event.getX();
				this.lastY = event.getY();
				
				
			}
		} else if (action == MotionEvent.ACTION_UP | action == MotionEvent.ACTION_CANCEL | action == MotionEvent.ACTION_POINTER_UP) {
			
			// do not move selector when gesture zooming!
			if (!this.scaleGestureDetector.isInProgress()) {
			
				if (x < this.game.getSudoku().getField().getStructure().getWidth() && y < this.game.getSudoku().getField().getStructure().getHeight()){
					DebugHelper.log(DebugHelper.PackageName.SudokuField, "X: " + x + " Y: " + y + " SF: " + this.scaleFactor + " " + this.convertX + " " + this.convertY);
					if (this.game.getSudoku().getField().getStructure().isSlotUsed(x, y) && !cursorMoved) {
						/*if (!this.fieldDisabled){
							this.selectedFieldX = x;
							this.selectedFieldY = y;
							if (this.onClickListener != null)
				            	this.onClickListener.onClick(this);
						}*/
					}
				}
				this.activePointerID = -1;
			
			}
			cursorMoved = false;
				
            
		}
		
		invalidate();
		
		return true;
	}
	
	
	/**
	 * Marks the View invalid so it redraws itself.
	 */
	public void refresh() {
		//invalidate();
	}
	
	/**
	 * Set the symboltable
	 * @param st instance of symbolTable
	 */
	
	public void setSymbolTable(SymbolTable st) {
		this.symbols = st;
	}
	
	/**
	 * This class handles the zoom gestures
	 */
	private class ZoomHandler extends ScaleGestureDetector.SimpleOnScaleGestureListener {
	    @Override
	    public boolean onScale(ScaleGestureDetector detector) {
	        scaleFactor *= detector.getScaleFactor();
	        // Prevent to much zoom but also less
	        scaleFactor = Math.max(1.0f, Math.min(scaleFactor, 2.7f));
	        
	        int dirX = (int) (((size * 0.5) - detector.getFocusX()) / scaleFactor);
	        int dirY = (int) (((size * 0.5) - detector.getFocusY()) / scaleFactor);
	        
	        convertX = (int) (convertX + dirX * Math.abs(detector.getCurrentSpan() - detector.getPreviousSpan()) * Math.max(detector.getScaleFactor() - 1, 0));
	        convertY = (int) (convertY + dirY * Math.abs(detector.getCurrentSpan() - detector.getPreviousSpan()) * Math.max(detector.getScaleFactor() - 1, 0));
	        
	        convertX = (int) Math.max(Math.min(convertX, 0), - size + (size / scaleFactor));
        	convertY = (int) Math.max(Math.min(convertY, 0), - size + (size / scaleFactor));
	        
        	invalidate();
	        return true;
	    }
	}
	
	
	
	
	/**
	 * Manually zoom into the field
	 */
	
	private void zoomIn() {
		this.scaleFactor *= 1.1;
		scaleFactor = Math.max(1.0f, Math.min(scaleFactor, 2.7f));
		this.convertX = (int) Math.max(Math.min(this.convertX, 0), - this.size + (this.size / this.scaleFactor));
    	this.convertY = (int) Math.max(Math.min(this.convertY, 0), - this.size + (this.size / this.scaleFactor));
    	invalidate();
	}
	
	/**
	 * Manually zoom out of the field
	 */
	
	private void zoomOut() {
		this.scaleFactor /= 1.1;
		scaleFactor = Math.max(1.0f, Math.min(scaleFactor, 2.7f));
		this.convertX = (int) Math.max(Math.min(this.convertX, 0), - this.size + (this.size / this.scaleFactor));
    	this.convertY = (int) Math.max(Math.min(this.convertY, 0), - this.size + (this.size / this.scaleFactor));
    	invalidate();
	}
	
	/**
	 * Defines if invalid values are to be hilighted
	 * @param enable enables the hilighting
	 */
	public void showInvalidValues (boolean enable) {
		this.showRedundant = enable;
	}
	
	/**
	 * Handles the Zoombuttons (hiding and events)
	 * @author adrian
	 *
	 */
	private class ZoomButtonHandler implements ZoomButtonsController.OnZoomListener {

		@Override
		public void onVisibilityChanged(boolean visible) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onZoom(boolean zoomInEnabled) {
			if (zoomInEnabled)
				zoomIn();
			else
				zoomOut();
			
		}
		
	}
	
	/**
	 * Enables the Zoombuttons
	 * @param enable
	 */
	public void setZoomButtonsEnable(boolean enable) {
		this.zoomButtons = enable;
	}
	
	/**
	 * needed to disable the Zoombuttons when not visible
	 */
	@Override
	protected void onDetachedFromWindow (){
		this.zbc.setVisible(false);
	}
	
	/**
	 * Disables the Field
	 * @param disabled is true field is disabled
	 */
	public void setDisabled(boolean disabled) {
		if (disabled) {
			this.selectedFieldX = -1;
			this.selectedFieldY = -1;
			invalidate();
		}
		
		this.fieldDisabled = disabled;
	}
	
	/**
	 * Method to test Multitouch
	 */
	public float getScaleFactor() {
		return this.scaleFactor;
	}
	
	
	/**
	 * Mark a cell for a duration
	 * @param cell Cell to Mark
	 * @param duration duration in milisec
	 */
	public void highlightCell(Cell cell, int duration) {
		final int x = cell.getIndex() % this.game.getSudoku().getField().getStructure().getWidth();
		final int y = cell.getIndex() / this.game.getSudoku().getField().getStructure().getHeight();
		if (this.markedCells[x][y] != 0)
			return;
		this.markedCells[x][y] = 1;
		
		Runnable unmarkCell = new Runnable() {
			
			@Override
			public void run() {
				markedCells[x][y] = 0;
				invalidate();
			}
		};
		
		invalidate();
		unmarker.postDelayed(unmarkCell, duration);
	}
	
	public void highlightWrongInput(Cell cell, int duration) {
		final int x = cell.getIndex() % this.game.getSudoku().getField().getStructure().getWidth();
		final int y = cell.getIndex() / this.game.getSudoku().getField().getStructure().getHeight();
		if (this.markedCells[x][y] != 0)
			return;
		this.markedCells[x][y] = 2;
		
		Runnable unmarkCell = new Runnable() {
			
			@Override
			public void run() {
				markedCells[x][y] = 0;
				invalidate();
			}
		};
		
		invalidate();
		unmarker.postDelayed(unmarkCell, duration);
	}
}

