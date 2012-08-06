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

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.sudowars.Model.Sudoku.Field.FieldStructure;
import org.sudowars.Model.Sudoku.Field.SquareStructure;
import org.sudowars.Model.Sudoku.RuleManagement.DependencyGroup;
import org.sudowars.Model.Sudoku.RuleManagement.DependencyManager;
import org.sudowars.Model.Sudoku.RuleManagement.NoDuplicatesRule;
import org.sudowars.Model.Sudoku.RuleManagement.Rule;
import org.sudowars.Model.Sudoku.RuleManagement.UpperLimitRule;

/**
 * Factory for creating {@link Sudoku}s with the standard ruleset.
 */
public class StandardRulesetFactory implements Serializable {

	private static final long serialVersionUID = -2378028208799734897L;
	private static StandardRulesetFactory instance;
	
	/**
	 * Singleton
	 * @return the only instance of class {@link StandardRulesetFactory}
	 */
	public static StandardRulesetFactory getInstance() {
		if (instance == null) {
			instance = new StandardRulesetFactory();
		}
		return instance;
	}
	
	private StandardRulesetFactory() {}
	
	private DependencyManager buildRuleset(int minBlockSize) {
		FieldStructure fs = new SquareStructure(minBlockSize * minBlockSize);
		Rule r = new NoDuplicatesRule(new UpperLimitRule(minBlockSize * minBlockSize));
		
		List<DependencyGroup> grps = new LinkedList<DependencyGroup>();
		
		grps.addAll((new RowRuleApplier()).applyRule(fs, r));
		grps.addAll((new ColumnRuleApplier()).applyRule(fs, r));
		grps.addAll((new BlockRuleApplier(minBlockSize)).applyRule(fs, r));
		
		return new StandardDependencyManager(grps);	
	}
	
	/**
	 * Returns the standard ruleset for 9x9 {@link Sudoku}s.
	 * @return the standard ruleset for 9x9 {@link Sudoku}s.
	 */
	public DependencyManager build9x9Ruleset() {
		return buildRuleset(3);
	}
	
	/**
	 * Returns the standard ruleset for 16x16 {@link Sudoku}s.
	 * @return the standard ruleset for 16x16 {@link Sudoku}s.
	 */
	public DependencyManager build16x16Ruleset() {
		return buildRuleset(4);
	}
	
	/**
	 * Determines whether the given {@link DependencyManager} was produced by this factory.
	 * @param depManager the given {@link DependencyManager}
	 * @return {@code true} if the given {@link DependencyManager} was produced by this factory {@code false} otherwise.
	 */
	public boolean isStandardRuleset(DependencyManager depManager) {
		return depManager instanceof StandardDependencyManager;
	}
	
	private class StandardDependencyManager extends DependencyManager {

		/**
		 * 
		 */
		private static final long serialVersionUID = 594139637808668048L;

		private StandardDependencyManager(List<DependencyGroup> groups) {
			super(groups);
		}
		
	}
}
