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
package org.sudowars.Model.SudokuManagement.Generator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.sudowars.Model.Sudoku.Sudoku;
import org.sudowars.Model.Sudoku.Field.DataCell;
import org.sudowars.Model.Sudoku.Field.SquareStructure;
import org.sudowars.Model.Sudoku.RuleManagement.DependencyGroup;

/**
 * Provides functionality to transform a given {@link Sudoku} into a Sudoku with another {@link Field} arrangement
 */
public class Transformer extends TransformerBase {

	private static final long serialVersionUID = -4406759333321849281L;
	private static final int NUMBER_OF_TRANSFORMATORS = 5;
	private static final int NUMBER_OF_TRANSFORMATION_STEPS = 20;

	/**
	 * Transforms a given {@link Sudoku} into a Sudoku with another {@link Field} arrangement
	 *
	 * @param sudoku the {@link Sudoku} to transform
	 *
	 * @return the transformed {@link Sudoku}
	 */
	public Sudoku<DataCell> transformSudoku(Sudoku<DataCell> sudoku) {
		Random randGen = new Random();
		int currentRandomNumber;
		int tempRandomNumber1;
		int tempRandomNumber2;
		for (int i = 0; i < NUMBER_OF_TRANSFORMATION_STEPS; i++) {
			currentRandomNumber = randGen.nextInt(NUMBER_OF_TRANSFORMATORS) + 1;
			switch (currentRandomNumber + 1) {
			case 1:
				//Switch numbers
				tempRandomNumber1 = randGen.nextInt(sudoku.getField().getStructure().getWidth() - 1) + 1;
				do {
					tempRandomNumber2 = randGen.nextInt(sudoku.getField().getStructure().getWidth() - 1) + 1;
				} while (tempRandomNumber1 == tempRandomNumber2);
				swapValues(sudoku, tempRandomNumber1, tempRandomNumber2);
				break;
			case 2:
				//rotate
				randomRotate(sudoku);
				break;
			case 3:
				//swap single columns
				randomSwapColums(sudoku);
				break;
			case 4:
				//swap single columns
				randromSwapRows(sudoku);
				break;
			case 5:
				//mirror sudoku
				mirror(sudoku);
				break;
			default:
				break;
			}
		}
		return sudoku;
	}
	
	
	//Transformation methods
	
	
	
	private void randromSwapRows(Sudoku<DataCell> sudoku) {
		if (sudoku != null) {
			if (sudoku.getField().getStructure() instanceof SquareStructure) {
				Random randGen = new Random();
				int row1 = randGen.nextInt(sudoku.getField().getStructure().getHeight() - 1);
				int row2;
				do {
					row2 = randGen.nextInt(sudoku.getField().getStructure().getHeight());
					
				} while (row1 == row2 || (getBoxGroupForRow(sudoku, row1) != getBoxGroupForRow(sudoku, row2)));
				swapRows(sudoku, row1, row2);
			}
		}
	}
	
	private DependencyGroup getBoxGroupForRow(Sudoku<DataCell> sudoku, int row) {
		DependencyGroup currentRowGroup = getRowGroup(sudoku, row);
		DependencyGroup currentColumnGroup = getColumnGroup(sudoku, 0);
		List<DependencyGroup> groupsOfOneCellInBox = 
				sudoku.getDependencyManager().getDependencyGroupsOfCell(sudoku.getField().getCell(0, row));
		DependencyGroup returnGroup = null;
		for (DependencyGroup dg : groupsOfOneCellInBox) {
			if (dg != currentRowGroup && dg != currentColumnGroup) {
				returnGroup = dg;
			}
		}
		return returnGroup;
		
	}
	
	private DependencyGroup getBoxGroupForColumn(Sudoku<DataCell> sudoku, int col) {
		DependencyGroup currentRowGroup = getRowGroup(sudoku, 0);
		DependencyGroup currentColumnGroup = getColumnGroup(sudoku, col);
		List<DependencyGroup> groupsOfOneCellInBox = 
				sudoku.getDependencyManager().getDependencyGroupsOfCell(sudoku.getField().getCell(col, 0));
		DependencyGroup returnGroup = null;
		for (DependencyGroup dg : groupsOfOneCellInBox) {
			if (dg != currentRowGroup && dg != currentColumnGroup) {
				returnGroup = dg;
			}
		}
		return returnGroup;
		
	}
	
	private DependencyGroup getRowGroup(Sudoku<DataCell> sudoku, int rowNumber) {
		//find dependencyGroup "row"
		DependencyGroup rowGroup = null;
		List<DependencyGroup> dependencyGroupsOfFirstCellInRow = 
				sudoku.getDependencyManager().getDependencyGroupsOfCell(sudoku.getField().getCell(0, rowNumber));
		List<DependencyGroup> dependencyGroupsOfLastCellInRow = 
				sudoku.getDependencyManager().getDependencyGroupsOfCell(
						sudoku.getField().getCell(sudoku.getField().getStructure().getWidth() - 1, rowNumber));
		for (DependencyGroup d : dependencyGroupsOfFirstCellInRow) {
			if (dependencyGroupsOfLastCellInRow.contains(d)) {
				rowGroup = d;
			}
		}
		return rowGroup;
	}
	
	
	private DependencyGroup getColumnGroup(Sudoku<DataCell> sudoku, int columnNumber) {
		//find dependencyGroup "column"
		DependencyGroup columnGroup = null;
		List<DependencyGroup> dependencyGroupsOfFirstCellInColumn = 
				sudoku.getDependencyManager().getDependencyGroupsOfCell(sudoku.getField().getCell(columnNumber, 0));
		List<DependencyGroup> dependencyGroupsOfLastCellInColumn = 
				sudoku.getDependencyManager().getDependencyGroupsOfCell(
						sudoku.getField().getCell(columnNumber, sudoku.getField().getStructure().getHeight() - 1));
		for (DependencyGroup d : dependencyGroupsOfFirstCellInColumn) {
			if (dependencyGroupsOfLastCellInColumn.contains(d)) {
				columnGroup = d;
			}
		}
		return columnGroup;
	}
	
	private void swapRows(Sudoku<DataCell> sudoku, int row1, int row2) {
		assert (sudoku != null);
		assert (row1 >= 0 && row1 < sudoku.getField().getStructure().getWidth());
		assert (row2 >= 0 && row2 < sudoku.getField().getStructure().getWidth());
		int temp;
		boolean initialState;
		DataCell currentCell1;
		DataCell currentCell2;
		for (int i = 0; i < sudoku.getField().getStructure().getWidth(); i++) {
			currentCell1 = sudoku.getField().getCell(i, row1);
			currentCell2 = sudoku.getField().getCell(i, row2);
			temp = currentCell1.getValue();
			initialState = currentCell1.isInitial();
			currentCell1.setValue(currentCell2.getValue());
			currentCell1.setInitial(currentCell2.isInitial());
			currentCell2.setValue(temp);
			currentCell2.setInitial(initialState);
		}
	}
	
	private void randomSwapColums(Sudoku<DataCell> sudoku) {
		if (sudoku != null) {
			if (sudoku.getField().getStructure() instanceof SquareStructure) {
				Random randGen = new Random();
				int col1 = randGen.nextInt(sudoku.getField().getStructure().getWidth() - 1);
				int col2;
				do {
					col2 = randGen.nextInt(sudoku.getField().getStructure().getWidth() - 1);
					
				} while (col1 == col2 || (getBoxGroupForColumn(sudoku, col1) != getBoxGroupForColumn(sudoku, col2)));
				swapColumns(sudoku, col1, col2);
			}
		}
	}
	
	private void swapColumns(Sudoku<DataCell> sudoku, int col1, int col2) {
		assert (sudoku != null);
		assert (col1 >= 0 && col1 < sudoku.getField().getStructure().getWidth());
		assert (col2 >= 0 && col2 < sudoku.getField().getStructure().getWidth());
		int temp;
		boolean initialState;
		DataCell currentCell1;
		DataCell currentCell2;
		for (int i = 0; i < sudoku.getField().getStructure().getHeight(); i++) {
			currentCell1 = sudoku.getField().getCell(col1, i);
			currentCell2 = sudoku.getField().getCell(col2, i);
			temp = currentCell1.getValue();
			initialState = currentCell1.isInitial();
			currentCell1.setValue(currentCell2.getValue());
			currentCell1.setInitial(currentCell2.isInitial());
			currentCell2.setValue(temp);
			currentCell2.setInitial(initialState);
		}
	}
	
	
	private void mirror(Sudoku<DataCell> sudoku) {
		int[][][] currentValues = 
				new int[sudoku.getField().getStructure().getWidth()] [sudoku.getField().getStructure().getHeight()][2];
		for (int i = 0; i < sudoku.getField().getStructure().getWidth(); i++) {
			for (int j = 0; j < sudoku.getField().getStructure().getHeight(); j++) {
				currentValues[i][j][0] = sudoku.getField().getCell(i, j).getValue();
				if (sudoku.getField().getCell(i, j).isInitial()) {
					currentValues[i][j][1] = 1;
				} else {
					currentValues[i][j][1] = 0;
				}
			}
		}
		
		
		for (int i = 0; i < sudoku.getField().getStructure().getWidth(); i++) {
			for (int j = 0; j < sudoku.getField().getStructure().getHeight(); j++) {
				sudoku.getField().getCell(i, j).setValue(
						currentValues[sudoku.getField().getStructure().getWidth() - 1 - i]
								[sudoku.getField().getStructure().getHeight() - 1 - j][0]);
				if (currentValues[sudoku.getField().getStructure().getWidth() - 1 - i]
								[sudoku.getField().getStructure().getHeight() - 1 - j][1] == 1) {
					sudoku.getField().getCell(i, j).setInitial(true);
				} else {
					sudoku.getField().getCell(i, j).setInitial(false);
				}
			}
		}
	}
	
	
	private void randomRotate(Sudoku<DataCell> sudoku) {
		assert (sudoku != null);
		//This method does nothing if the structure is not a square
		if (sudoku.getField().getStructure() instanceof SquareStructure) {	
			Random randGen = new Random();
			//RotationLevel describes the rotation in 90 degree steps between 90 and 270 deg
			int rotationLevel = randGen.nextInt(2) + 1;
			int arrayHeight = sudoku.getField().getStructure().getHeight();
			int arrayWidth = sudoku.getField().getStructure().getWidth();
			int[][][] oldStructure = new int[arrayWidth][arrayHeight][2];
			int[][][] newStructure = new int[arrayWidth][arrayHeight][2];
			int numberOfCellsInSudoku = 
					sudoku.getField().getStructure().getHeight() * sudoku.getField().getStructure().getWidth();
			
			//save old structure in array
			for (int i = 0; i < numberOfCellsInSudoku; i++) {
				oldStructure[i % arrayWidth][i / arrayWidth] [0] = sudoku.getField().getCell(i).getValue();
				if (sudoku.getField().getCell(i).isInitial()) {
					oldStructure[i % arrayWidth][i / arrayWidth] [1] = 1;
				} else {
					oldStructure[i % arrayWidth][i / arrayWidth] [1] = 0;
				}
			}
			
			//change arrayWidth/height for loops
			arrayWidth--;
			arrayHeight--;
			
			//set all cells to non-initial
			//for (int i = 0; i < arrayWidth; i++) {
			//	for (int j = 0; j < arrayHeight; j++) {
			//		sudoku.getField().getCell(i, j).setInitial(false);
			//	}
			//}
			switch (rotationLevel) {
				case 1:
					//90 degrees
					for (int currentWidth = 0; currentWidth <= arrayWidth; currentWidth++) {
						for (int currentHeight = 0; currentHeight <= arrayHeight; currentHeight++) {
							newStructure[arrayWidth - currentHeight][currentWidth][0] = 
									oldStructure[currentWidth][currentHeight][0];
							if (oldStructure[currentWidth][currentHeight][1] == 1) {
								newStructure[arrayWidth - currentHeight][currentWidth][1] = 1;
							}
						}
					}
					break;
				case 2:
					//180 degrees
					for (int currentWidth = 0; currentWidth <= arrayWidth; currentWidth++) {
						for (int currentHeight = 0; currentHeight <= arrayHeight; currentHeight++) {
							newStructure[currentWidth][arrayHeight - currentHeight][0] = 
									oldStructure[currentWidth][currentHeight][0];
							if (oldStructure[currentWidth][currentHeight][1] == 1) {
								newStructure[currentWidth][arrayHeight - currentHeight][1] = 1;
							}
						}
					}
					break;
				default:
					//This should never happen
					break;
			}
			
			//Write changes to sudoku
			int currentCellValue;
			for (int currentWidth = 0; currentWidth <= arrayWidth; currentWidth++) {
				for (int currentHeight = 0; currentHeight <= arrayHeight; currentHeight++) {
					currentCellValue = newStructure[currentWidth][currentHeight][0];
					sudoku.getField().getCell(currentWidth, currentHeight).setValue(currentCellValue);
					if (newStructure[currentWidth][currentHeight][1] == 1) {
						sudoku.getField().getCell(currentWidth, currentHeight).setInitial(true);
					} else {
						sudoku.getField().getCell(currentWidth, currentHeight).setInitial(false);
					}
				}
			}
		}
	}
	
	
	private void swapValues(Sudoku<DataCell> sudoku, int value1, int value2) {
		assert (value1 > 0 && value1 <= 16);
		assert (value2 > 0 && value2 <= 16);
		//If this happens in non-debug mode, this function will not crash but simply do nothing
		assert (value1 != value2); 
		if (value1 != value2) {
			LinkedList<DataCell> cellsWithValue1 = new LinkedList<DataCell>();
			LinkedList<DataCell> cellsWithValue2 = new LinkedList<DataCell>();
			int numberOfCellsInSudoku = 
					sudoku.getField().getStructure().getHeight() * sudoku.getField().getStructure().getWidth();
			DataCell currentCell;
			for (int i = 0; i < numberOfCellsInSudoku; i++) {
				currentCell = sudoku.getField().getCell(i);
				if (currentCell.getValue() == value1) {
					cellsWithValue1.add(currentCell);
				} else if (currentCell.getValue() == value2) {
					cellsWithValue2.add(currentCell);
				}
			}
			for (DataCell c : cellsWithValue1) {
				c.setValue(value2);
			}
			for (DataCell c : cellsWithValue2) {
				c.setValue(value1);
			}
		}
	}
	
}


