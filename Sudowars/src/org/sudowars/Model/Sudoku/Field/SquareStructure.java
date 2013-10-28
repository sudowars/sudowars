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
package org.sudowars.Model.Sudoku.Field;

/** 
 * {@link SquareStructure} defines and implements the special case of a completly used and squared structure.
 */
public final class SquareStructure extends RectangleStructure {

	/**
	 * Serial version UID for serialization.
	 */
	private static final long serialVersionUID = -5237555608930090508L;

	/**
	 * Initializes a new Instance of {@link SquareStructure} with the given value.
	 *
	 * @param sideLength side length of the squared structure.
	 *
	 * @throws IllegalArgumentException if the given side length is lower than <code>1</code>.
	 *
	 * @see RectangleStructure#RectangleStructure(int, int)
	 * @see FieldStructure
	 */
	public SquareStructure(int sideLength) throws IllegalArgumentException {
		super(sideLength, sideLength);
	}
}
