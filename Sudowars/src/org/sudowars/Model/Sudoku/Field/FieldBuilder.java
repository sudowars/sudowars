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

import java.util.ArrayList;
import java.util.List;

/**
 * Factory for creating {@link Field}s.
 */
public class FieldBuilder<T extends Cell> {

	/**
	 * Creates a new {@link Field} based on the given {@link FieldStructure} filled with {@link Cell}s built by the given {@link CellBuilder}.
	 *
	 * @param structure {@link FieldStructure}-instance denoting the structure of the field
	 * @param cellBuilder {@link CellBuilder}-instance for creating the actual content, the {@link Cell}s.
	 *
	 * @return a {@link Field} inheriting the given {@link FieldStructure},<br> which was used to built it and holding {@link Cell}s built by the given {@link CellBuilder}
	 *
	 * @throws IllegalArgumentException if at least one of the given arguments is <code>null</code>
	 * @see FieldStructure
	 * @see CellBuilder
	 * @see Cell
	 * @see Field
	 */
	public Field<T> build(FieldStructure structure, CellBuilder<T> cellBuilder) throws IllegalArgumentException {
		if (structure == null || cellBuilder == null) {
			throw new IllegalArgumentException();
		}
		
		List<T> cells = new ArrayList<T>(structure.getUsedSlotCount());
		
		int index = 0;
		for (int i = 0; i < structure.getWidth(); i++) {
			for (int j = 0; j < structure.getHeight(); j++) {
				if (structure.isSlotUsed(i, j)) {
					cells.add(cellBuilder.buildCell(index++));
				}
			}
		}
		
		return new Field<T>(cells, structure);
	}
}
