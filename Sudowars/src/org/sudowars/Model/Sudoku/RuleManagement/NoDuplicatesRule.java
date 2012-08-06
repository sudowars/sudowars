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

import java.util.LinkedList;
import java.util.List;

import org.sudowars.Model.Sudoku.Field.Cell;
import org.sudowars.Model.Sudoku.Field.Field;
import org.sudowars.Model.Sudoku.RuleManagement.Rule;

/**
 * The {@link Rule} {@link NoDuplicatesRule} allows a value only to exist once in the {@link Cell}s 
 * referenced via the {@link DependencyGroup} indices, mapped on the {@link Field}s actual {@link Cell}s.
 */
public final class NoDuplicatesRule extends RuleDecorator {
	
	/**
	 * Serial version UID for serialization
	 */
	private static final long serialVersionUID = 4092324830031527579L;
	
	public NoDuplicatesRule(Rule baseRule) {
		super(baseRule);
	}
	
	/**
	 * Returns a {@link List} of valid values for this {@link Cell} according to that {@link Rule}, {@link DependencyGroup} combination.
	 * This concrete implementation returns all values allowed by the decorated {@link Rule} except those which are already held by other {@link Cell}s
	 * linked to by the {@link DependencyGroup}
	 *
	 * @param field the {@link Field} holding the {@link Cell}s.
	 * @param group the {@link DependencyGroup} knowing which {@link Cell}s of {@link Field} should be taken into account.
	 * @param cell the {@link Cell} the cell whose possible values are to be retrieved
	 *
	 * @return a {@link List} of valid integer values for the given {@link Cell}
	 *
	 * @throws IllegalArgumentExecption if any parameter is <code>null</code>
	 * @throws IllegalArgumentExecption if ... 
	 * 	<ul>
	 * 		<li>
	 * 			the given {@link Cell} is not contained by the given {@link Field}
	 * 		</li>
	 * 		<li>
	 * 			at least one of the given {@link DependencyGroup}s indices is not covered by the {@link Field}s {@link FieldStructure}
	 *		</li>
	 * 	</ul> 
	 */
	@Override
	public List<Integer> getValidValues(Field<Cell> field, DependencyGroup group, Cell cell) {
		
		if (field == null || group == null || cell == null || !field.getCells().contains(cell)) {
			// if any parameter is null
			// or
			// if the given Cell is not contained by the given Field
			throw new IllegalArgumentException();
		}
		
		// using LinkedList as multiple removals are expected
		List<Integer> validValues = new LinkedList<Integer>(this.getDecoratedRule().getValidValues(field, group, cell));
		
		List<Cell> cells = null;
		
		try {
			cells = group.getCells(field);
		} catch (IllegalArgumentException e) {
			// field != null
			// => at least one given index in group is not covered by field
			// => throw IllegalArgumentException
			throw e;
		}
		
		for (Cell c : cells) {
			if (c != cell && c.isSet()) {
				// use of Integer needed to call the right remove(...) variant
				// remove(int) removes the object with the given index
				// remove(Object) removes the given object
				validValues.remove(new Integer(c.getValue()));
			}
		}	

		// no read-only view needed as the list is generated each time
		return validValues;
	}
		
}


