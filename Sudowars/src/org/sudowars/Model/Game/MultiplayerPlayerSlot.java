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
package org.sudowars.Model.Game;

/**
 * This class is an extension of the {@link SingleplayerPlayerSlot} optimized for multi player games
 */
public class MultiplayerPlayerSlot extends SingleplayerPlayerSlot {

	private static final long serialVersionUID = 1L;
	private Score score = new Score();
	
	/**
	 * Gets the current score of the player attached to this slot.
	 *
	 * @return The {@link Score} of the player.
	 */
	public Score getScore() {
		return this.score;
	}
	
	/**
	 * Gets the hashcode of the current instance.
	 * @return an integer value indicating the hashcode of the current instance
	 */
	@Override
	public int hashCode() {
		final int prime = 27;
		int result = super.hashCode();
		result = prime * result + ((this.score == null) ? 0 : this.score.hashCode());
		return result;
	}
	
	/**
	 * Indicates whether the current instance is equal to a given object.
	 * @param otherObject reference to an object to check for equality
	 * @return {@code true} if given obejct refers to the same instance,<br>
	 * or refers to another MultiplayerPlayerSlot instance with equal attributes/properties,<br>
	 * otherwise {@code false}
	 */
	@Override
	public boolean equals(Object otherObject) {
		boolean result = false;
		if (otherObject instanceof MultiplayerPlayerSlot) {
			MultiplayerPlayerSlot otherSlot = (MultiplayerPlayerSlot) otherObject;
			result = (this == otherSlot || attributesEqual(this, otherSlot));
		}
		return result;
	}

	private static boolean attributesEqual(MultiplayerPlayerSlot first, MultiplayerPlayerSlot second) {
		assert first != null && second != null;
		
		return (first.hasPaused == second.hasPaused && first.score.equals(second.score)
				&& objectsEqual(first.attachedPlayer, second.attachedPlayer)
				&& objectsEqual(first.notes, second.notes));
		
	}
}
