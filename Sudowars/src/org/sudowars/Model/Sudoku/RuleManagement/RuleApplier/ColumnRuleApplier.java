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
package org.sudowars.Model.Sudoku.RuleManagement.RuleApplier;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.sudowars.Model.Sudoku.Field.FieldStructure;
import org.sudowars.Model.Sudoku.RuleManagement.DependencyGroup;
import org.sudowars.Model.Sudoku.RuleManagement.Rule;

/**
 * {@link RowRuleApplier} grants the possibility to apply a {@link Rule} to every column of a {@link FieldStructure}
 * @see RuleApplier
 * @see RuleApplier#applyRule(FieldStructure, Rule) 
 */
public class ColumnRuleApplier implements RuleApplier {

	/**
	 * Packs all used slots of column of {@link FieldStructure} in a {@link DependencyGroup} dependent on the given {@link Rule}.
	 * This way every column gets its own {@link DependencyGroup}.
	 * One can assert that the returned {@link DependencyGroup}s index sets are completely disjunct.
	 *
	 * @param FieldStructure the standard {@link FieldStructure} as defined in {@link RuleApplier#applyRule(FieldStructure, Rule)}
	 * @param rule the standard {@link Rule} as defined in {@link RuleApplier#applyRule(FieldStructure, Rule)}
	 *
	 * @returns a {@link List} of {@link DependencyGroup}s where at every {@link DependencyGroup} represents a column.
	 *
	 * @throws IllegalArgumentException if any parameter is <code>null</code>. 
	 */
	@Override
	public List<DependencyGroup> applyRule(FieldStructure fieldStructure,
			Rule rule) throws NullPointerException {
		
		if (fieldStructure == null || rule == null) {
			throw new IllegalArgumentException();
		}
		
		List<DependencyGroup> grps = new ArrayList<DependencyGroup>(fieldStructure.getWidth());
		
		for (int x = 0; x < fieldStructure.getWidth(); x++) {
			List<Integer> column = new LinkedList<Integer>();
			for (int y = 0; y < fieldStructure.getHeight(); y++) {
				if (fieldStructure.isSlotUsed(x, y)) {
					column.add(fieldStructure.getIndex(x, y));
				}
			}
			grps.add(new DependencyGroup(rule, column));
		}
		
		return grps;
	}
	
}


