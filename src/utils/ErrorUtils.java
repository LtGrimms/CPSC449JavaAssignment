package utils;

import java.text.ParseException;

public class ErrorUtils {

	public static void unRecognizedQualifierError() {
		System.out.println("unrecognized qualifier");
		System.exit(-1);
	}
	
	public static void toManyArgumentsError() {
		System.out.println("Too many arguments");
		System.exit(-2);
	}
	
	public static void jarIsNotAJarError() {
		System.out.println("jar is not a jar");
		System.exit(-3);
	}
	
	public static void helpInWrongLocError() {
		//NotYet Implemented
		System.exit(-4);
	}
	
	public static void couldNotLoadJarError() {
		
		System.exit(-5);
	}
	
	public static void couldNotFindMethidError() {
		
		System.exit(-6);
	}
	
	/**
	 * All non-fatal errors should call this function.
	 * @param arg The string being parsed
	 * @param error An explanation of the error
	 * @param ex The ParseException with the index of the parse error
	 * @throws ParseException we have already printed a stack trace so don't put it in the catch block
	 */
	public static void parseError(String arg, String error,ParseException ex, boolean verbose) throws ParseException{
		System.out.println(error+" at offest "+ex.getErrorOffset());
		System.out.println(arg);
		for(int i=0;i<ex.getErrorOffset();i++){
			System.out.print("-");
		}
		System.out.print("^\n");
		if(verbose)
			ex.printStackTrace();
		throw ex;
	}
	
}
