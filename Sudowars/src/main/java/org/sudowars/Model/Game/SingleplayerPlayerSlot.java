/*
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
 * Contributors:
 * initial API and implementation:
 * Adrian Vielsack
 * Christof Urbaczek
 * Florian Rosenthal
 * Michael Hoff
 * Moritz Lüdecke
 * Philip Flohr
 */
package org.sudowars.Model.Game;


/**
 * This class is an extension of {@link PlayerSlot} used in single player games
 * @see SingleplayerGame
 */
public class SingleplayerPlayerSlot extends PlayerSlot {

	private static final long serialVersionUID = 7389208174636844121L;

	/**
	 * Indicates whether the player attached to this slot triggered a pause.
	 *
	 * @return <code>true</code> if pause was triggered, otherwise <code>false</code>
	 */
	public boolean hasPaused() {
		return super.hasPaused;
	}
	
	/**
	 * Gets the hashcode of the current instance.
	 * @return an integer value indicating the hashcode of the current instance
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.attachedPlayer == null) ? 0 : this.attachedPlayer.hashCode());
		result = prime * result + (this.hasPaused ? 1231 : 1237);
		//result = prime * result + ((notes == null) ? 0 : notes.hashCode());
		return result;
	}
	
	/**
	 * Indicates whether the current instance is equal to a given object.
	 * @param otherObject reference to an object to check for equality
	 * @return {@code true} if given obejct refers to the same instance,<br>
	 * or refers to another SingleplayerPlayerSlot instance with equal attributes/properties,<br>
	 * otherwise {@code false}
	 */
	@Override
	public boolean equals(Object otherObject) {
		boolean result = false;
		if (otherObject instanceof SingleplayerPlayerSlot) {
			SingleplayerPlayerSlot otherSlot = (SingleplayerPlayerSlot) otherObject;
			result = (this == otherSlot || attributesEqual(this, otherSlot));
		}
		return result;
	}
	
	private static boolean attributesEqual(SingleplayerPlayerSlot first, SingleplayerPlayerSlot second) {
		assert first != null && second != null;
		
		return (first.hasPaused == second.hasPaused && objectsEqual(first.attachedPlayer, second.attachedPlayer)
				&& objectsEqual(first.notes, second.notes));
		
	}
	
	/**Compares to objects taking into consideration that at least one of them may be null.
	 * 
	 * @param first reference to an object
	 * @param second reference to an object
	 * @return {@code true}, if both objects are {@code null} or {@code first.equals(second) == true},<br>
	 * otherwise {@code false}
	 */
	static boolean objectsEqual(Object first, Object second) {
		//taking into consideration that at least one of them may be null during runtime
		boolean result = true;
		if (first == null) {
			if (second != null) {
				result = false;
			}
		} else if(second == null) {
			if (first != null) {
				result = false;
			}
		}
		else {
			//none is null
			result = first.equals(second);
		}
		return result;
	}
}
