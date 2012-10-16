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

import java.io.Serializable;

/**
 * This class encapsulates the score of a player
 */
public class Score implements Serializable {

	private static final long serialVersionUID = 6023666139662549066L;
	private int score = 0;
	
	/**
	 * Initializes a new instance of the {@link Score} class with the default score 0.
	 */
	public Score() {
		
	}
	
	/**
	 * Initializes a new instance of the {@link Score} class with a given initial value.
	 *
	 * @param initialValue the initial score value
	 */
	public Score(int initialValue){
		this.score = initialValue;
	}
	
	/**
	 * Increments the current score about a given amount.
	 *
	 * @param n An integer indicating the increase amount:
	 * <li>n = 0: nothing changes</li>
	 * <li>n > 0: increment about the given value</li>
	 * <li>n < 0: decrement about -n
	 * @see Score#decrement(int)
	 */
	public void increment(int n) {
		this.score += n;
	}
	
	/**
	 * Decrements the current score about a given amount.
	 *
	 * @param n An integer indicating the decrease amount:
	 * <li>n = 0: nothing changes</li>
	 * <li>n > 0: decrement about the given value</li>
	 * <li>n < 0: increment about -n
	 * @see Score#increment(int)
	 */
	public void decrement(int n) {
		this.score -= n;
	}
	
	/**
	 * Gets the current score represented by an integer value.
	 *
	 * @return An integer value indicating the current score.
	 */
	public int getCurrentScore() {
		return this.score;
	}
	
	/**
	 * Gets the hashcode of the current instance.
	 * @return an integer value indicating the hashcode of the current instance, calculated as follows:<br>
	 * the XOR-combination of the hashcodes of all properties of the current instance
	 */
	@Override
	public int hashCode() {
		int hashCode = 37;		
		
		return 31 * hashCode + this.score;  
	}
	
	/**
	 * Indicates whether the current instance is equal to a given object.
	 * @param otherObject reference to an object to check for equality
	 * @return {@code true} if given obejct refers to the same instance,<br>
	 * or refers to another Score instance with equal score amount,<br>
	 * otherwise {@code false}
	 */
	@Override
	public boolean equals(Object otherObject) {
		boolean result = false;
		if (otherObject instanceof Score) {
			Score otherScore = (Score) otherObject;
			result = (this == otherScore || this.score == otherScore.score);
		}
		return result;
	}
}
