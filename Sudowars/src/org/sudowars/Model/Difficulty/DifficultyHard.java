package org.sudowars.Model.Difficulty;

/**
 * This class defines the value range for the {@link Difficulty} <b>hard</b>
 */
public class DifficultyHard extends Difficulty {

	private static final long serialVersionUID = 3379321694336314889L;
	
	/**
	 * Initialises a new instance of the class and defines the bounds
	 * of the {@link Difficulty}
	 */
	public DifficultyHard() {
		this.lowerBound = 8.5;
		this.upperBound = Double.MAX_VALUE;
		//TODO define upper bound : 11.6, 25.1, 17.1, 11.0 are some calculated values 
		//this.upperBound = 11.5; //possible max value which is not too hard, not verified
	}

	/**
	 * Returns a string representation of the difficulty
	 */
	public String toString() {
		return "Hard";
	}
	
}


