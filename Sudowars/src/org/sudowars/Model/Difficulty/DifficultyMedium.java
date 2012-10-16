package org.sudowars.Model.Difficulty;

/**
 * This class defines the value range for the {@link Difficulty} <b>medium</b>
 */
public class DifficultyMedium extends Difficulty {

	private static final long serialVersionUID = -5794420238572126548L;
	
	/**
	 * Initialises a new instance of the class and defines the bounds
	 * of the {@link Difficulty}
	 */
	public DifficultyMedium() {
		this.lowerBound = 6.5;
		this.upperBound = 8.5;
	}
	
	/**
	 * Returns a string representation of the difficulty
	 */
	public String toString() {
		return "Medium";
	}
	
}


