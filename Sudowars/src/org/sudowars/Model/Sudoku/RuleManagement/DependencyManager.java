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


