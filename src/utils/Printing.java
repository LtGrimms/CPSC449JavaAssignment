package utils;

public class Printing {

	/**
	 * Pretty self explanatory, this prints the synopsis for the program. This is
	 * run before the program enters interpreter mode.
	 */
	public static void printSynopsis() {
		System.out.println("Synopsis:");
		System.out.println("  methods");
		System.out.println("  methods { -h | -? | --help }+");
		System.out.println("  methods {-v --verbose}* <jar-file> [<class-name>]");
		System.out.println("Arguments:");
		System.out.println("  <jar-file>:   The .jar file that contains the class to laod (see next line).");
		System.out.println("  <class-name>: The full qualified class name containing publc static command methods to call.");
		System.out.println("Qualifiers:");
		System.out.println("  -v --verbose: Print out detailed errors, warning, and tracking.");
		System.out.println("  -h -? --help: Pring out a detailed help message.");
		System.out.println("Single-char qualifiers may be grouped; long qualifiers may be truncated to unique prefixes and are not case sensitive.");
	}
	
	/**
	 * adds an extra line to the print synopsis function describing proper input
	 * for the program.
	 */
	public static void printHelp() {
		printSynopsis();
		System.out.println("This program interprets commands of the format '(<method> {arg}*)' on the command lind, finds cooresponding methods in <class-name>, and executes them, printing the result to sysout.");
	}
	
	/**
	 * This function is called inside the interpreter and prints out the interpreters 
	 * help text.
	 */
	public static void printInterpreterHelp() {
		System.out.println("q            : Quit the program.");
		System.out.println("v            : Toggle verbose mode (stack traces).");
		System.out.println("f            : List all know functions.");
		System.out.println("?            : Print this helpful text.");
		System.out.println("<expression> : Evaluate the expression.");
		System.out.println("Expressions can be integers, floats, strings (sourrounded in double quotes) or function calls of the form '(identifier {expression}*)'.");
	}

}
