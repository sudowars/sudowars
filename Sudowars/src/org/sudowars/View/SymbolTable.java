package org.sudowars.View;

/**
 * This Class is a SymbolTable for the View
 */
public class SymbolTable {
	/**
	 * Because this class is pretty much static, but we want to work with instances, we make it a singleton
	 */
	private static SymbolTable stable = null;
	
	/**
	 * SymbolTable
	 */
	private static char[] symbols = {
		' ',
		'1',
		'2',
		'3',
		'4',
		'5',
		'6',
		'7',
		'8',
		'9',
		'0',
		'a',
		'b',
		'c',
		'd',
		'e',
		'f'
	};
	
	/**
	 * The Constructor
	 */
	protected SymbolTable() {
		
	}
	
	/**
	 * Creates a Instance of Symbol Table if it does not exist and returns it
	 * 
	 * @return THE instance of SymbolTable
	 */
	public static SymbolTable getInstance() {
		if (stable == null) {
			stable = new SymbolTable();
		}
		
		return stable;
	}
	
	
	/**
	 * Gets the char for a value
	 * 
	 * @param value value in the field
	 * 
	 * @return char to draw
	 */
	public char getSymbol(int value) {
		if (value > -1 && value < SymbolTable.symbols.length) {
			return SymbolTable.symbols[value];
		} else {
			return ' ';
		}
	}
	
	/**
	 * returns the value to a char
	 * 
	 * @param symbol symbol to resolve
	 * 
	 * @return value of the symbol
	 */
	public int getValue(char symbol) {
		for (int n = 0; n < symbols.length; n++) {
			if (symbol == symbols[n])
				return n;
		}
		
		return -1;
	}
}
