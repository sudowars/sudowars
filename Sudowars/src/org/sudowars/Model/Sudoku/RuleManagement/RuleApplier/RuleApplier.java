package org.sudowars.Model.Sudoku.RuleManagement.RuleApplier;

import java.util.List;

import org.sudowars.Model.Sudoku.Field.FieldStructure;
import org.sudowars.Model.Sudoku.RuleManagement.DependencyGroup;
import org.sudowars.Model.Sudoku.RuleManagement.Rule;

/**
 * {@link RuleApplier} defines an interface for grouping slots of a {@link FieldStructure} in {@link DependencyGroup}s dependent on a specific {@link Rule}.
 */
public interface RuleApplier {
	
	/**
	 * Picks slots of a given {@link FieldStructure} and packs them into {@link DependencyGroup}s inheriting the given {@link Rule}. 
	 *
	 * @param fieldStructure the {@link FieldStructure} providing the indices for the {@link DependencyGroup}s.
	 * @param rule the {@link Rule} the {@link DependencyGroup} will depend on.
	 *
	 * @return a {@link List} of {@link DependencyGroup}s
	 */
	public List<DependencyGroup> applyRule(FieldStructure fieldStructure, Rule rule);
	
}


