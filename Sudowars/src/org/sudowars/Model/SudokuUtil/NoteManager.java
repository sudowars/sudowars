/*******************************************************************************
 * Copyright (c) 2011 - 2012 Adrian Vielsack, Christof Urbaczek, Florian Rosenthal, Michael Hoff, Moritz Lüdecke, Philip Flohr.
 * 
 * This file is part of Sudowars.
 * 
 * Sudowars is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Sudowars is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Sudowars.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * 
 * 
 * Diese Datei ist Teil von Sudowars.
 * 
 * Sudowars ist Freie Software: Sie können es unter den Bedingungen
 * der GNU General Public License, wie von der Free Software Foundation,
 * Version 3 der Lizenz oder (nach Ihrer Option) jeder späteren
 * veröffentlichten Version, weiterverbreiten und/oder modifizieren.
 * 
 * Sudowars wird in der Hoffnung, dass es nützlich sein wird, aber
 * OHNE JEDE GEWÄHELEISTUNG, bereitgestellt; sogar ohne die implizite
 * Gewährleistung der MARKTFÄHIGKEIT oder EIGNUNG FÜR EINEN BESTIMMTEN ZWECK.
 * Siehe die GNU General Public License für weitere Details.
 * 
 * Sie sollten eine Kopie der GNU General Public License zusammen mit diesem
 * Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 * initial API and implementation:
 * Adrian Vielsack
 * Christof Urbaczek
 * Florian Rosenthal
 * Michael Hoff
 * Moritz Lüdecke
 * Philip Flohr 
 ******************************************************************************/
package org.sudowars.Model.SudokuUtil;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.sudowars.Model.Sudoku.Field.Cell;
import org.sudowars.Model.Sudoku.Field.DataCell;

/**
 * This class manages the notes attached to {@link Cell}s.
 */
public class NoteManager implements Serializable {

	private static final long serialVersionUID = -4627325085946728592L;
	
	transient private List<NoteManagerChangedEventListener> onChangeListeners;
	
	private final HashMap<Cell, List<Integer>> notes;
	//private final HashMap<Cell, LinkedHashSet<Integer>> notes;
	
	/**
	 * Initializes a new instance of the {@link NoteManager} class.
	 */
	public NoteManager() {
		this.notes  = new HashMap<Cell, List<Integer>>();
		//this.notes = new HashMap<Cell, LinkedHashSet<Integer>>();
		initializeListenerLists();
	}
	
	private void initializeListenerLists() {
		this.onChangeListeners = new LinkedList<NoteManagerChangedEventListener>();
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		initializeListenerLists();
	}
	
	/**
	 * Gets the notes attached to a given cell.
	 *
	 * @param c Reference to a {@link Cell}.
	 *
	 * @return A READ-ONLY list of notes represented as int values attached to the given cell,<br>
	 * or an empty list if cell doesn't contain any notes.
	 *
	 * @throws IllegalArgumentException if given cell is <code>null</code>
	 */
	public List<Integer> getNotes(Cell c) throws IllegalArgumentException {
		if (c == null) {
			throw new IllegalArgumentException("given cell cannot be null.");
		}
		List<Integer> currentNotes = this.notes.get(c);
		if (currentNotes != null) {
			return Collections.unmodifiableList(currentNotes);
		}
		return Collections.unmodifiableList(new LinkedList<Integer>());
		//return currentNotes;
	}
	
	/**
	 * Adds a given value to a cell as a note.
	 *
	 * @param c Reference to a {@link Cell} to which to note shall be attached.
	 * @param value The value of the note to be set.
	 *
	 * @return <code>true</code>} if note was successfully attached,<br>
	 * <code>false</code> if given cell already contained the given value<br>
	 * If <code>true</code> is returned, the OnChange event is fired.
	 *
	 * @throws IllegalArgumentException if given cell was <code>null</code> or value <= {@link DataCell.NOT_SET}
	 * @see NoteManager#onNoteAdd()
	 */
	public boolean addNote(Cell c, int value) throws IllegalArgumentException {
		if (c == null || value <= DataCell.NOT_SET) {
			throw new IllegalArgumentException("cell was null or value invalid.");
		}
		boolean result = false;
		List<Integer> currentNotes = this.notes.get(c);
		if(currentNotes != null) {
			if (!currentNotes.contains((Object) value)) {
				if (currentNotes.add(value)) {
					result = true;
					onChange(new NoteManagerChangedEvent(this, c));
				}
			}
		} else {
			currentNotes = new LinkedList<Integer>();
			currentNotes.add(value);
			this.notes.put(c, currentNotes);
			onChange(new NoteManagerChangedEvent(this, c));
			result = true;
		}
		return result;
	}
	
	/**
	 * Removes the note represented by the given value from the given cell.
	 *
	 * @param c Reference to a {@link Cell}.
	 * @param value The value of the note to be removed.
	 *
	 * @return <code>true</code> if note was successfully removed,<br>
	 * <code>false</code> if given cell already did not contain the given value.<br>
	 * If <code>true</code> is returned, the OnChange event is fired.
	 * <br>NOTE: If this method is invoked iteratively, the event is fired at each successful execution.
	 * @throws IllegalArgumentException if given cell was <code>null</code> or value <= {@link DataCell.NOT_SET}
	 * @see NoteManager#OnNoteRemove()
	 */
	public boolean removeNote(Cell c, int value) throws IllegalArgumentException {
		if (c == null || value <= DataCell.NOT_SET) {
			throw new IllegalArgumentException("cell was null or value invalid.");
		}
		boolean result = false;
		List<Integer> currentNotes = this.notes.get(c);
		//LinkedHashSet<Integer> currentNotes = this.notes.get(c);
		if(currentNotes != null) {
			if (currentNotes.remove((Object) value)) {
				result = true;
				//if all notes of the cell were removed, delete item from hash map
				if (currentNotes.isEmpty()) {
					this.notes.remove(c);
				}
				onChange(new NoteManagerChangedEvent(this, c));
			} 
		} 
		return result;
	}
	
	/**
	 * Removes all notes from the given cell.
	 *
	 * @param cell Reference to a {@link Cell}.
	 * @return <code>true</code> if all notes were successfully removed,<br>
	 * <code>false</code> if given cell already did not contain any notes.<br>
	 * If <code>true</code> is returned, the OnChange event is fired.
	 *
	 * @throws IllegalArgumentException if given cell was <code>null</code>
	 * @see NoteManager#OnNoteRemove()
	 */
	public boolean removeAllNotes(Cell cell) throws IllegalArgumentException {
		if (cell == null) {
			throw new IllegalArgumentException("given cell cannot be null.");
		}
		boolean result = false;
		List<Integer> currentNotes = this.notes.get(cell);
		if (currentNotes != null) {
			currentNotes.clear();
			result = true;
			// all notes of the cell were removed, delete item from hash map
			this.notes.remove(cell);
			onChange(new NoteManagerChangedEvent(this, cell));
		}
		return result;
	}
	
	/**
	 * Indicates whether a note represented by the given value is attached to the given cell.
	 *
	 * @param c Reference to a {@link Cell}.
	 * @param value The value of the note.
	 *
	 * @return <code>true</code> if given note is attached to the cell, otherwise <code>false</code>
	 *
	 * @throws IllegalArgumentException if given cell was <code>null</code> or value <= {@link DataCell.NOT_SET}
	 */
	public boolean hasNote(Cell c, int value) throws IllegalArgumentException {
		if (c == null || value <= DataCell.NOT_SET) {
			throw new IllegalArgumentException("cell was null or value invalid.");
		}
		boolean result = false;
		List<Integer> currentNotes = this.notes.get(c);
		if(currentNotes != null && currentNotes.contains((Object) value)) {
			result = true; 
		} 
		return result;
	}
	
	/**
	 * Indicates whether one or more notes are attached to the given cell.
	 *
	 * @param cell Reference to a {@link Cell}.
	 * @return <code>true</code> if given cell contains notes, otherwise <code>false</code>
	 *
	 * @throws IllegalArgumentException if given cell was <code>null</code>
	 */
	public boolean hasNotes(Cell cell) throws IllegalArgumentException {
		if (cell == null) {
			throw new IllegalArgumentException("given cell was null.");
		}
		return this.notes.containsKey(cell);
	}
	
	/**
	 * Adds the given listener to the onChange observer list of the current instance.
	 * @param actionListener A reference to an <code>NoteManagerChangedEventListener</code> instance.
	 * @return <code>true</code>
	 * @throws IllegalArgumentException if given listener is <code>null</code>
	 * @see List#add(Object)
	 */
	public boolean addOnChangeListener(NoteManagerChangedEventListener listener) throws IllegalArgumentException {
		return this.onChangeListeners.add(listener);
	}
	
	
	/**
	 * Removes the first occurrence of the specified element from the onChange observer list of the current instance.
	 *
	 * @param listener the {@link NoteManagerChangedEventListener} to remove
	 *
	 * @return <code>true</code>, if element was contained in the list, otherwise <code>false</code>
	 * @see List#remove(Object)
	 */
	public boolean removeOnChangeListener(NoteManagerChangedEventListener listener) {
		return this.onChangeListeners.remove(listener);
	}
	
	/**
	 * Distributes the onChange event to all attached listeners.
	 */
	private void onChange(NoteManagerChangedEvent eventData) {
		for (NoteManagerChangedEventListener listener : this.onChangeListeners) {
			listener.onChange(eventData);
		}
	}
	
	@Override
	public int hashCode() {
		//it is recommended not to include transient fields into hashcode calculation
		final int prime = 31;
		int result = 1;
		result = prime * result + this.notes.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		//it is recommended not to include transient fields into equality calculation
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof NoteManager))
			return false;
		NoteManager other = (NoteManager) obj;
		return attributesEqual(this, other);
	}

	private static boolean attributesEqual(NoteManager first, NoteManager second) {
		assert first != null && second != null;
		
		return (first.notes.equals(second.notes));
	}
}


