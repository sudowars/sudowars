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
package org.sudowars.Model.Sudoku.Field;

/**
 * {@link RectangleStructure} defines and implements the special case of a completly used and rectangled structure.
 */
public class RectangleStructure implements FieldStructure {

	/**
	 * Serial version UID for serialization.
	 */
	private static final long serialVersionUID = 1452106445176491505L;
	protected int width, height;
	
	/**
	 * Initializes a new instance of {@link RectangleStructure} with the given values for width and height.
	 *
	 * @param width the {@link FieldStructure}s width.
	 * @param height the {@link FieldStructure}s height.
	 *
	 * @throws IllegalArgumentException if at least one of the given integer values is less than 1.
	 */
	public RectangleStructure(int width, int height) throws IllegalArgumentException {
		if (width < 1 || height < 1) {
			throw new IllegalArgumentException("Illegal bounds");
		}
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Returns the amount of {@link Cell}s in a row of a field {@link Field} inhereting this {@link FieldStructure}.
	 *
	 * @return an integer value greater than 0.
	 */
	@Override
	public int getWidth() {
		return width;
	}

	/**
	 * Returns the amount of {@link Cell}s in a column of a field {@link Field} inhereting this {@link FieldStructure}.
	 *
	 * @return an integer value greater than 0.
	 */
	@Override
	public int getHeight() {
		return height;
	}
	
	
	private boolean inBounds(int x, int y) {
		return x >= 0 && x < width && y >= 0 && y < height;
	}

	
	/**
	 * Returns always true as all slots are used in this implementation.
	 *
	 * @param x horizontal position
	 * @param y vertical position
	 *
	 * @return <code>true</code>, if the exection was successfully
	 *
	 * @throws IllegalArgumentException if the position defined by x and y is not covered by this {@link RectangleStructure}.
	 * @see FieldStructure#isSlotUsed(int, int)
	 */
	@Override
	public boolean isSlotUsed(int x, int y) {
		if (!inBounds(x, y)) {
			throw new IllegalArgumentException("(x, y) not in bounds");
		}
		return true;
	}
	
	/**
	 * Returns the index of the given position as defined by {@link FieldStructure#getIndex(int, int)}.
	 * @return a positive integer between 0 and {@link RectangleStructure#getUsedSlotCount()}.
	 * @see FieldStructure#getIndex(int, int)
	 * @throws IllegalArgumentException if the position defined by x and y is not covered by this {@link RectangleStructure}.
	 */
	@Override
	public int getIndex(int x, int y) throws IllegalArgumentException {
		if (!inBounds(x, y)) {
			throw new IllegalArgumentException("(x, y) not in bounds");
		}
		return x + y * getWidth();
	}
	
	/**
	 * Returns the amount of used slots in this {@link RectangleStructure}.
	 * As all slots are used in this implementation the returned value equals 
	 * {@link RectangleStructure#getWidth()} multiplied with {@link RectangleStructure#getHeight()}
	 *
	 * @return a positive integer
	 * @see FieldStructure#getUsedSlotCount()
	 */
	@Override
	public int getUsedSlotCount() {
		return getWidth() * getHeight();
	}
}
