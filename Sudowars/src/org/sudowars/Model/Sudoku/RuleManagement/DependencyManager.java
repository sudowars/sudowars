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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.sudowars.Model.Sudoku.Field.Cell;

/**
 * The {@link DependencyManager} manages {@link DependencyGroup}s.
 */
public class DependencyManager implements Serializable {
	
	/**
	 * Serial version UID for serialization
	 */
	private static final long serialVersionUID = -2881697546235744783L;
	
	private List<DependencyGroup> groups;
	
	/**
	 * Initializes a new {@link DependencyManager} instance with the given parameters.
	 *
	 * @param groups the {@link DependencyGroup}s the {@link DependencyManager} has to manage.
	 * 
	 * @throw IllegalArgumentException if groups is <code>null</code>
	 */
	public DependencyManager(List<DependencyGroup> groups) {
		if (groups == null) {
			throw new IllegalArgumentException();
		}
		this.groups = groups;
	}
	
	/**
	 * Returns all {@link DependencyGroup}s the {@link DependencyManager} instance knows.
	 *
	 * @return all {@link DependencyGroup}s the {@link DependencyManager} instance knows.
	 */
	public List<DependencyGroup> getDependencyGroups() {
		// forbids external modification
		return Collections.unmodifiableList(groups);
	}
	
	/**
	 * Returns all {@link DependencyGroup}s the {@link DependencyManager} knows which include the
	 * given {@link Cell}s index.
	 *
	 * @param cell the {@link Cell} which has to be included (via the its index) in the returned {@link DependencyGroup}s.
	 *
	 * @return a {@link List} of {@link DependencyGroup}s
	 *
	 * @throws IllegalArgumentException if the given {@link Cell} is <code>null</code>
	 */
	public List<DependencyGroup> getDependencyGroupsOfCell(Cell cell) throws IllegalArgumentException {
		if (cell == null) {
			// the only error case
			throw new IllegalArgumentException();
		}
		
		List<DependencyGroup> result = new LinkedList<DependencyGroup>();
		
		for (DependencyGroup group : groups) {
			// check group
			for (int index : group.getIndices()) {
				// check all indices IN the group being checked
				if (index == cell.getIndex()) {
					// wow, we found a match
					// group being checked therefore contains the cells index
					// => the translation of this group by using the field containing the given cell
					// 	  would return a list of cells which contains the given cell.
					result.add(group);
				}
			}
		}
		
		// no read-only view needed as the list isn't buffered
		// could be optimized by buffering the list
		return result;
	}
	
}


