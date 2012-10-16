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
