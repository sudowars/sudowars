package org.sudowars.Model.Difficulty;

/**
 * This class defines the value range for the {@link Difficulty} <b>easy</b>
 */
public class DifficultyEasy extends Difficulty {

	private static final long serialVersionUID = -3272515475794785100L;
	
	/**
	 * Initialises a new instance of the class and defines the bounds
	 * of the {@link Difficulty}
	 */
	public DifficultyEasy() {
		this.lowerBound = 4.5;
		this.upperBound = 6.5;
	}
	
	/**
	 * Returns a string representation of the difficulty
	 */
	public String toString() {
		return "Easy";
	}
	
}


