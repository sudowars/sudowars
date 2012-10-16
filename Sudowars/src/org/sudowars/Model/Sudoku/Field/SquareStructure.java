package org.sudowars.Model.Sudoku.Field;

/** 
 * {@link SquareStructure} defines and implements the special case of a completly used and squared structure.
 */
public final class SquareStructure extends RectangleStructure {

	/**
	 * Serial version UID for serialization.
	 */
	private static final long serialVersionUID = -5237555608930090508L;

	/**
	 * Initializes a new Instance of {@link SquareStructure} with the given value.
	 *
	 * @param sideLength side length of the squared structure.
	 *
	 * @throws IllegalArgumentException if the given side length is lower than <code>1</code>.
	 *
	 * @see RectangleStructure#RectangleStructure(int, int)
	 * @see FieldStructure
	 */
	public SquareStructure(int sideLength) throws IllegalArgumentException {
		super(sideLength, sideLength);
	}
}
