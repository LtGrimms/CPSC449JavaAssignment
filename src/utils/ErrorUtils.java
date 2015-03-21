package utils;

import java.text.ParseException;

public class ErrorUtils {

	/**
	 * Ends program if it recieved bad command line args
	 */
	public static void unRecognizedQualifierError() {
		System.out.println("unrecognized qualifier");
		Printing.printSynopsis();
		System.exit(-1);
	}
	
	/**
	 * ends program if it recived to many command line args
	 */
	public static void toManyArgumentsError() {
		System.out.println("to many arguments");
		Printing.printSynopsis();
		System.exit(-2);
	}
	
	/**
	 * Ends program if it cannot find the specified jar
	 */
	public static void jarIsNotAJarError() {
		System.out.println("jar is not a jar");
		Printing.printSynopsis();
		System.exit(-3);
	}
	
	/**
	 * Ends program if the help qualifier is not in the right place
	 */
	public static void helpInWrongLocError() {
		System.out.println("help in wrong location error");
		Printing.printSynopsis();
		System.exit(-4);
	}
	
	/**
	 * Ends the program if it could not load the selected jar file specified on the command line
	 */
	public static void couldNotLoadJarError() {
		System.out.println("could not load jar error");
		System.exit(-5);
	}
	
	/**
	 * Ends the program if it could not find the method class within the jar file specified on the command line
	 */
	public static void couldNotFindMethodError() {
		System.out.println("could not find method error");
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
