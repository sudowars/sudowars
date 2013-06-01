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
package org.sudowars.Model.SudokuUtil;

import org.sudowars.DebugHelper;
import org.sudowars.Model.Game.SingleplayerGame;
import org.sudowars.Model.Solver.ConsecutiveSolver;
import org.sudowars.Model.Solver.HumanSolveStep;
import org.sudowars.Model.Solver.HumanSolver;
import org.sudowars.Model.Solver.SolveStep;
import org.sudowars.Model.Solver.SolverState;
import org.sudowars.Model.Solver.SolverStrategy;
import org.sudowars.Model.Sudoku.Field.Cell;
import org.sudowars.Model.Sudoku.Field.Field;
import org.sudowars.Model.Sudoku.RuleManagement.DependencyManager;
import android.os.Handler;
import android.os.Message;

/**
 * This class is used to assist the player during game.
 */
public class Assistant implements Runnable {

	private final SingleplayerGame game;
	private final ConsecutiveSolver solver;
	private final Field<Cell> convertedGameField;
	private final DependencyManager dependencies;
	private final Handler targetHandler;
			
	/**
	 * Initializes a new instance of the {@link Assistant} class.
	 *
	 * @param game The {@link SingleplayerGame} to work on.
	 *
	 * @throws IllegalArgumentException if the given game is <code>null</code>
	 */
	public Assistant(SingleplayerGame game, Handler targetHandler) throws IllegalArgumentException {
		if (game == null) {
			throw new IllegalArgumentException("given game cannot be null.");
		}
		this.game = game;
		this.solver = new HumanSolver();
		this.convertedGameField = this.game.getSudoku().getField().convert();
		this.dependencies = this.game.getSudoku().getDependencyManager();
		this.targetHandler = targetHandler;
	}
	
	/**
	 * Makes the current assistant solve a game cell whose value has not been set.
	 *
	 * @return Reference to a {@link SolveStep} holding the cell whose value has been found, if any,
	 * or <code>null</code> if no cell to solve was found.
	 */
	private SolveStep solveNext(){
		return this.solver.getCellToSolveNext(new SolverState(this.convertedGameField, this.dependencies));
	}

	@Override
	public void run() {
		Message message = new Message(); // The massege to the handler
		message.setTarget(targetHandler);
		HumanSolveStep assistantResult = (HumanSolveStep)solveNext();	
		
		if (assistantResult != null && assistantResult.hasSolvedCell()) {
					DebugHelper.log(DebugHelper.PackageName.SingleplayerPlay, "Cell #"
					+ assistantResult.getSolvedCell().getIndex() + " solved : " + assistantResult.getSolution());
			
			if (assistantResult.getUsedStrategies().size() == 0) {
				DebugHelper.log(DebugHelper.PackageName.SingleplayerPlay, "---" + "Cell \"advised\"");
			} else {
				DebugHelper.log(DebugHelper.PackageName.SingleplayerPlay, "Use strategy");
				
				for (SolverStrategy strategy : assistantResult.getUsedStrategies()) {
					DebugHelper.log(DebugHelper.PackageName.SingleplayerPlay, "---" + strategy.toString());
				}
			}
			message.arg1 = assistantResult.getSolvedCell().getIndex();
			message.arg2 = assistantResult.getSolution();			
		} else {
			message.arg1 = -1;
		}
		message.sendToTarget();
	}
}


