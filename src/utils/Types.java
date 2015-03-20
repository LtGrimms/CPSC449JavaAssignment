package utils;

public class Types {

	/**
	 * These are the numerical representations of the different types that are readable by the
	 * interpreter. Type conversion can be made possible by changing the values of integer to
	 * 0b1110 and integer to 0b0110
	 */
	public static final int BIGINTEGER = 0b1100, 
							INTEGER = 0b0100, 
							BIGFLOAT = 0b1010, 	
							FLOAT = 0b0010, 	
							STRING = 0b1, 
							ERROR = 0;
}
