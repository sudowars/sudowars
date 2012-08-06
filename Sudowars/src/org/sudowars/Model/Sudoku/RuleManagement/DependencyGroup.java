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
package org.sudowars.Model.Sudoku.RuleManagement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.sudowars.Model.Sudoku.Field.Cell;
import org.sudowars.Model.Sudoku.Field.Field;

/**
 * The class {@link DependencyGroup} allows grouping {@link Field} slots in relation to a 
 * given {@link Rule}.
 * @see FieldStructure
 * @see FieldStructure#getIndex(int, int)
 */
public class DependencyGroup implements Serializable {
	
	/**
	 * The serial version UID for serialization
	 */
	private static final long serialVersionUID = -5880127787614777904L;
	
	private List<Integer> indices;
	private Rule rule;

	/**
	 * Initializes a new {@link DependencyGroup} instance with the given parameters.
	 *
	 * @param rule the {@link Rule} which defines the relation
	 * @param indices the grouped slots.
	 *
	 * @throws IllegalArgumentException if whether rule or indices are <code>null</code>
	 */
	public DependencyGroup(Rule rule, List<Integer> indices) throws IllegalArgumentException{
		
		if (rule == null || indices == null) {
			// throws all defined exceptions
			throw new IllegalArgumentException();
		}
		
		// a new list to disallow modifying the indices from outside via modifying the given indices object
		this.indices = new ArrayList<Integer>(indices);
		
		// rules are not meant to hold a status so they can be used without cloning
		this.rule = rule;		
	}
	
	/**
	 * Returns the inherited {@link Rule}
	 *
	 * @return the inherited {@link Rule}
	 */
	public Rule getRule() {
		return rule;
	}
	
	/**
	 * Returns a realization of grouped {@link Cell}s relative to the given {@link Field} 
	 *
	 * @param field the {@link Field} containing the {@link Cell}s.
	 *
	 * @return a {@link List} of {@link Cell}s.
	 *
	 * @throws IllegalArgumentException if field is <code>null</code>
	 * @throws IllegalArgumentException if at least one index contained in this {@link DependencyGroup} is not covered by the {@link Field} given. 
	 */
	public List<Cell> getCells(Field<Cell> field) throws IllegalArgumentException {
		
		if (field == null) {
			// thrown when field is null
			throw new IllegalArgumentException();
		}
		
		// ArrayList implementation of List, as the size is known and no modifications are expected 
		List<Cell> cells = new ArrayList<Cell>(indices.size());
		
		for (int index : indices) {
			try {
				cells.add(field.getCell(index));
			} catch(IllegalArgumentException e) {
				// thrown when the given index is not contained by field
				// re-throw as the same exception is specified in this case
				throw e;
			}
		}
		
		// no read-only view is needed, as this list is generated dynamically
		return cells;
	}
	
	/**
	 * Returns the inherited {@link List} of indices.
	 *
	 * @return the inherited {@link List} of indices.
	 */
	List<Integer> getIndices() {
		// forbids external indices modifications after object creation
		return Collections.unmodifiableList(indices);
	}
	
}


