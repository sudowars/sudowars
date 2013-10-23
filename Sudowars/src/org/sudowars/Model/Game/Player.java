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

import java.io.Serializable;

/**
 * This class is used to describe a player.
 * @see PlayerSlot
 */
public class Player implements Serializable {

	private static final long serialVersionUID = -8500359963346360261L;
	private final String nickname;
	
	/**
	 * Initializes a new instance of the {@link Player} class with a given nickname,<br>
	 * which is the bluetooth name of the user's device.
	 *
	 * @param nickname The nickname of the player.
	 *
	 * @throws IllegalArgumentException if the given nickname is <code>null</code> or empty
	 */
	public Player(String nickname) throws IllegalArgumentException {
		if (isNicknameNullOrEmpty(nickname)) {
			throw new IllegalArgumentException("given nickname was null.");
		}
		this.nickname = nickname;
	}
	
	/**
	 * Gets the nickname of the current instance.
	 *
	 * @return The nickname of the player.
	 */
	public String getNickname() {
		return this.nickname;
	}
	
	private static boolean isNicknameNullOrEmpty(String nickname) {
		return (nickname == null || nickname.length() == 0);
	}
	
	/**
	 * Indicates whether the current instance is equal to a given object.
	 * @param otherObject reference to an object to check for equality
	 * @return {@code true} if given obejct is a valid player instance with same nickname,<br>
	 * otherwise {@code false}
	 */
	@Override
	public boolean equals(Object otherObject) {
		boolean result = false;
		if (otherObject instanceof Player) {
			Player otherPlayer = (Player) otherObject;
			if (this == otherPlayer || this.nickname.equals(otherPlayer.nickname)) {
				result = true;
			}
		}
		return result;
	}
	
	/**
	 * Gets the hashcode of the current instance.
	 * @return an integer value indicating the hashcode of the current instance
	 */
	@Override
	public int hashCode() {
		int hashCode = 47;
		
		return 31 * hashCode + this.nickname.hashCode();
	}
}
