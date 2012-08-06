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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.sudowars.Model.Sudoku.Field.Cell;
import org.sudowars.Model.Sudoku.Field.Field;

/**
 * The class {@link UpperLimitRule} allows all values from 1 to a specified limit.
 */
public class UpperLimitRule implements Rule {
	
	/**
	 * Serial version UID for serialization
	 */
	private static final long serialVersionUID = -8736050171681078014L;
	
	private List<Integer> bufferedValidValueList;
	
	/**
	 * Constructs a new instance of {@link UpperLimitRule} with the given bound.
	 * This {@link Rule}-instance will allow every value from 1 to the given bound, including 1 and bound.
	 * @param upperLimit the given bound.
	 * @throws IllegalArgumentException if upperLimit is lower than {@code 1} 
	 */
	public UpperLimitRule(int upperLimit) {
		if (upperLimit < 1) {
			// if upperLimit is lower than 1...
			throw new IllegalArgumentException();
		}
		
		// as the List is constant, it is generated and stored on rule construction
		this.bufferedValidValueList = new ArrayList<Integer>(upperLimit);
		
		// bounds included
		for (int i = 1; i <= upperLimit; i++) {
			this.bufferedValidValueList.add(new Integer(i));
		}
		
		// forbid external modification
		this.bufferedValidValueList = Collections.unmodifiableList(this.bufferedValidValueList);
	}

	/**
	 * Returns all values from 1 to the bound given on {@link Rule}-construction (including bounds).
	 * @param field not needed => can be anything
	 * @param group not needed => can be anything
	 * @param cell not needed => can be anything
	 */
	@Override
	public List<Integer> getValidValues(Field<Cell> field, DependencyGroup group, Cell cell) {
		return this.bufferedValidValueList;
	}

}
