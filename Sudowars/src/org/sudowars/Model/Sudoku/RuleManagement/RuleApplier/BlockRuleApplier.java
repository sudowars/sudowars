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
import java.util.List;

import org.sudowars.Model.Sudoku.Field.FieldStructure;
import org.sudowars.Model.Sudoku.RuleManagement.DependencyGroup;
import org.sudowars.Model.Sudoku.RuleManagement.Rule;

/**
 * {@link BlockRuleApplier} grants the possibility to apply {@link Rule}s to rectangles inside a {@link FieldStrucutre}.
 */
public class BlockRuleApplier implements RuleApplier {

	private int blockWidth;
	private int blockHeight;
	
	/**
	 * Initializes a new {@link BlockRuleApplier} instance with the given values.
	 *
	 * @param width the width of the produced blocks
	 * @param height the height of the produced blocks
	 * @throws IllegalArgumentException if any parameter is less than 1
	 */
	public BlockRuleApplier(int width, int height) {
		if (width < 1 || height < 1) {
			throw new IllegalArgumentException();
		}
		this.blockWidth = width;
		this.blockHeight = height;
	}
	
	/**
	 * Initializes a new {@link BlockRuleApplier} instance with the given values.
	 *
	 * @param edge the width and the height of the produced blocks
	 * @throws IllegalArgumentException if edge is less than 1
	 */
	public BlockRuleApplier(int edge) {
		this(edge, edge);
	}
	
	/**
	 * Packs the given {@link FieldStructure} in rectangle-formed blocks as defined in {@link RuleApplier#applyRule(FieldStructure, Rule)}
	 *
	 * @param fieldStructure the given {@link FieldStructure}
	 * @param rule the given {@link Rule}
	 *
	 * @return {@link List} of {@link DependencyGroup}
	 *
	 * @throws IllegalArgumentException if the given {@link FieldStructure} can not be completely transformed in a set of uniform blocks.
	 * @throws IllegalArgumentException if any parameter is <code>null</code>
	 */
	@Override
	public List<DependencyGroup> applyRule(FieldStructure fieldStructure,
			Rule rule) throws NullPointerException, IllegalArgumentException {
		
		if (fieldStructure == null || rule == null 
								   || fieldStructure.getWidth()  % blockWidth  != 0 
								   || fieldStructure.getHeight() % blockHeight != 0) {
			throw new IllegalArgumentException();
		}
		
		int xBound = fieldStructure.getWidth() - 1;
		int yBound = fieldStructure.getHeight() - 1;
		
		List<DependencyGroup> grps = new ArrayList<DependencyGroup>((fieldStructure.getWidth() / blockWidth) * (fieldStructure.getHeight() / blockHeight));
		for (int x = 0; x < xBound; x += blockWidth) {
			for (int y = 0; y < yBound; y += blockHeight) {
				List<Integer> indices = new ArrayList<Integer>(blockWidth * blockHeight);
				for (int i = 0; i < blockWidth; i++) {
					for (int j = 0; j < blockHeight; j++) {
						if (fieldStructure.isSlotUsed(x + i, y + j)) {
							indices.add(fieldStructure.getIndex(x + i, y + j));
						}
					}
				}
				grps.add(new DependencyGroup(rule, indices));
			}
		}
		
		return grps;
	}
	
}


