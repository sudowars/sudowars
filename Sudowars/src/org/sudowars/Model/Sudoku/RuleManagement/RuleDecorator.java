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

import java.util.List;

import org.sudowars.Model.Sudoku.Field.Cell;
import org.sudowars.Model.Sudoku.Field.Field;

/**
 * The class {@link RuleDecorator} is used for building concrete RuleDecorators.
 * These classes filter the results of {@link Rule#getValidValues(Field<Cell>, DependencyGroup, Cell)} 
 * of the given decorated {@link Rule}.
 */
public abstract class RuleDecorator implements Rule {

	/**
	 * Serial version UID for serialization
	 */
	private static final long serialVersionUID = 1L;
	
	private Rule baseRule;
	
	/**
	 * Constructs a new instance with the given {@link Rule} to decorate.
	 * @param baseRule the {@link Rule} to be decorated.
	 */
	public RuleDecorator(Rule baseRule) {
		if (baseRule == null) {
			throw new IllegalArgumentException();
		}
		this.baseRule = baseRule;
	}
	
	/**
	 * Returns the {@link Rule} decorated by this instance.
	 * @return the {@link Rule} decorated by this instance.
	 */
	protected Rule getDecoratedRule() {
		return baseRule;
	}
	
	/**
	 * @see {@link Rule#getValidValues(Field, DependencyGroup, Cell)}
	 */
	@Override
	public abstract List<Integer> getValidValues(Field<Cell> field, DependencyGroup group, Cell cell);

}
