package org;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.Scanner;

import utils.ErrorUtils;

public class Interpereter {
	
	/**
	 * Pretty self explanatory, this prints the synopsis for the program. This is
	 * run before the program enters interpreter mode.
	 */
	private static void printSynopsis() {
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
	private static void printHelp() {
		printSynopsis();
		System.out.println("This program interprets commands of the format '(<method> {arg}*)' on the command lind, finds cooresponding methods in <class-name>, and executes them, printing the result to sysout.");
	}
	
	/**
	 * This function is called inside the interpreter and prints out the interpreters 
	 * help text.
	 */
	private static void printInterpreterHelp() {
		System.out.println("q            : Quit the program.");
		System.out.println("v            : Toggle verbose mode (stack traces).");
		System.out.println("f            : List all know functions.");
		System.out.println("?            : Print this helpful text.");
		System.out.println("<expression> : Evaluate the expression.");
		System.out.println("Expressions can be integers, floats, strings (sourrounded in double quotes) or function calls of the form '(identifier {expression}*)'.");
	}
	
	/**
	 * This is to check the input arguments for the help or verbose qualifers.
	 * The method assumes that the string it is being passed has a '-' as the
	 * first character
	 * @param arg the string to be checked for qualifiers
	 * @param qualifiers a boolean array representing whether or not help and verbose have been found
	 * @return a boolean array of length two where the first element is help and the second is verbose and true values represent help/verbose have been activated by qualifiers
	 */
	private static boolean[] checkQualifiers(String arg, boolean[] qualifiers) {
		arg = arg.toLowerCase();
		boolean[] newQualifiers = qualifiers;
			if (arg.charAt(1) == '-') {
				if      (arg.substring(2) == "help") {
					newQualifiers[0] = true;
					return newQualifiers;
				}
				else if (arg.substring(2) == "verbose") {
					newQualifiers[1] = true;
					return newQualifiers;
				}
				else ErrorUtils.unRecognizedQualifierError();
			}
			for (int i = 1; i < arg.length(); i++) {
				if (!(arg.charAt(i) == 'h' 
				   || arg.charAt(i) == 'v' 
				   || arg.charAt(i) == '?')) {
					ErrorUtils.unRecognizedQualifierError();
				} else if (arg.charAt(i) == 'h' || arg.charAt(i) == '?') {
					newQualifiers[0] = true;					
				} else if (arg.charAt(i) == 'v') {
					newQualifiers[1] = true;
				}	
			} 
			return newQualifiers;
	}
	
	/**
	 * The main loop has two parts, the first considers the input parameters and determines if help and/or
	 * verbose has been called and tries to determine the jar file and method class that the arguments want
	 * loaded into the interpreter. The second part uses java reflection to interpret commands form an input 
	 * stream .
	 * @param args
	 */
	public static void main(String[] args) {

		//TODO Still need to ask about the error with exit code -4 since  I don't think 
		//TODO the loop below will catch it though it could with minor mods
		//TODO Create control structure that will load the jar.

		//__________________________________PART 1: check input params______________________________________
		String jar = null;
		String methodsClass = null;
		boolean[] qualifiers = {false, false};
		for (String arg : args) {
			if (arg.charAt(0) == '-') {
				qualifiers = checkQualifiers(arg, qualifiers);
				// continue;
			} else {
				if (jar == null) {
					if (/* arg is not a jar */false) {
						ErrorUtils.jarIsNotAJarError(); // How do we tell the diff between exit code -3 and -5?
					}
					jar = arg;
				} else if (jar != null && methodsClass == null) {
					methodsClass = arg;
				} else {
					ErrorUtils.toManyArgumentsError(); // Should exit with synopsis
				}
			}
		 }
		
		boolean help = qualifiers[0];
		boolean verb = qualifiers[1];
		
		if (jar == null && !help) {
			printSynopsis();
			System.exit(0); //Double check this
		} else if (help) {
			printHelp();
			if (jar == null)
				System.exit(0);
		}
		
		if (jar != null && methodsClass == null)
			methodsClass = "Commands";
		
		
		
		//___________________________________PART 2: Run an Interpreter_____________________________________
		
		Information info = new Information(jar, methodsClass); // Needs to exit with code -6 if method class cannot be found and exit code -5 if jar cannot be loaded
		Arborist arborist = new Arborist(info, verb);
		TreeEvaluator eval = new TreeEvaluator(info);
		
		while (true) {
			Scanner in = new Scanner(System.in);
			System.out.print("> ");
			String argument = in.nextLine();
			
			if (argument.length() == 0)
				continue;
			
			boolean lengthOne = argument.length() == 1;
			char firstChar = argument.charAt(0);
			
			if (lengthOne && firstChar == 'f') {
				info.printFunctions();
				continue;
			} 
			if (lengthOne && firstChar == 'v') {
				arborist.toggleVerbose();
				// If anything else needs to toggle verbose put it here
				continue;
			} 
			if (lengthOne && firstChar == '?') {
				printInterpreterHelp();
				continue;
			}
			if (lengthOne && firstChar == 'q') {
				System.out.print("bye.");
				System.exit(0);
			}
			
			ParseTree tree = null;
			try {
				tree = arborist.checkArgument(argument);
				tree.addReturnTypes();
				String output = eval.evaluate(tree.getRoot());
				System.out.println(output);
			} catch (ParseException ex) {
			} catch (Exception ex) {
				ex.printStackTrace(); //comment out for grading
			}
			
			//in.close(); //not sure why but this causes an error
		}
	}

}
