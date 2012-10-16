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
package org.sudowars.Model.Difficulty;

import java.util.ArrayList;
import java.util.List;

import org.sudowars.Model.Solver.SolverStrategy;
import org.sudowars.Model.Sudoku.Sudoku;
import org.sudowars.Model.Sudoku.Field.DataCell;

/**
 * Provides functionality to evaluate the {@link Difficulty} of a {@link Sudoku} for a human player
 * @see DifficultyEvaluator
 */
public class HumanDifficultyEvaluator extends DifficultyEvaluator {
		
	/**
	 * Evaluates the {@link Difficulty} of a given {@link Sudoku}
	 *
	 * @param sudoku the {@link Sudoku} to evaluate
	 * @param usedStrategies list of the used strategies to solve the sudoku
	 * @return the {@link Difficulty} of the {@link Sudoku}, <code>null</code> if no difficulty could be found
	 *
	 * @throws IllegalArgumentException if the given sudoku was <code>null</code>
	 */
	public Difficulty evaluateDifficulty(Sudoku<DataCell> sudoku, List<SolverStrategy> usedStrategies) throws IllegalArgumentException {
				
		//initiate difficulty list
		this.difficulties = new ArrayList<Difficulty>(3);
		this.difficulties.add(new DifficultyEasy());
		this.difficulties.add(new DifficultyMedium());
		this.difficulties.add(new DifficultyHard());
		
		//count unset cells
		int countUnsetCells = 0;
		for (DataCell cell : sudoku.getField().getCells()) {
			if (!cell.isInitial()) countUnsetCells++;
		}
		
		//evaluate difficulty by analysing the used strategies
		int weightSum = 0;
		for (SolverStrategy strategy : usedStrategies) weightSum += strategy.getStrategyWeight();
		double rating = (double)weightSum / (double)countUnsetCells;
				
		//find difficulty
		Difficulty resultDifficulty = null;
		for (Difficulty difficulty : this.difficulties) {
			try {
				difficulty.setValue(rating);
				resultDifficulty = difficulty;
				break;
			} catch (IllegalArgumentException ex) {
				
			}
		}
		
		return resultDifficulty;
		
	}

}


