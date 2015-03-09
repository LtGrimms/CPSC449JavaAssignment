package org;

import java.nio.file.Path;
import java.util.Scanner;

public class Interpereter {
	
	private static void printSynopsis() {
		
	}
	
	private static void printHelp() {
		
	}
	
	private static void printInterpreterHelp() {
		
	}

	private static void unRecognizedQualifierError() {
		
		System.exit(-1);
	}
	
	private static void toManyArgumentsError() {
		
		System.exit(-2);
	}
	
	private static void jarIsNotAJarError() {
		
		System.exit(-3);
	}
	
	private static void helpInWrongLocError() {
		//NotYet Implemented
		System.exit(-4);
	}
	
	
	private static int[] checkQualifiers(String arg, int[] qualifiers) {
		arg = arg.toLowerCase();
		int[] newQualifiers = qualifiers;
			if (arg.charAt(1) == '-') {
				if      (arg.substring(2) == "help") {
					newQualifiers[0] = 1;
					return newQualifiers;
				}
				else if (arg.substring(2) == "verbose") {
					newQualifiers[1] = 1;
					return newQualifiers;
				}
				else unRecognizedQualifierError();
			}
			for (int i = 1; i < arg.length(); i++) {
				if (!(arg.charAt(i) == 'h' 
				   || arg.charAt(i) == 'v' 
				   || arg.charAt(i) == '?')) {
					unRecognizedQualifierError();
				} else if (arg.charAt(i) == 'h' || arg.charAt(i) == '?') {
					newQualifiers[0] = 1;					
				} else if (arg.charAt(i) == 'v') {
					newQualifiers[1] = 1;
				}	
			} 
			return newQualifiers;	
	}
	
	public static void main(String[] args) {

		//// Second try
		// Still need to ask about the error with exit code -4 since  I dont think 
		// the loop below will catch it though it could with minor mods

		String jar;
		String methodsClass;
		int[] qualifiers;
		for (String arg : args) {
			if (arg.charAt(0) == '-') {
				qualifiers = checkQualifiers(arg, qualifiers);
				// continue;
			} else {
				if (jar == null) {
					if (/* arg is not a jar */) {
						jarIsNotAJarError(); // How do we tell the diff between exit code -3 and -5?
					}
					jar = arg;
				} else if (methodsClass == null) {
					methodsClass = arg;
				} else {
					toManyArgumentsError(); // Should exit with synopsis
				}
			}
		 }
		
		
		
		
		
		// ------ Basic loop that runs program ----------
		// Assuming that the 'path' variable is correctly checked (is there two path variables?)
		// then we can build an information class and pass this into the arborist and evaluator.
		
		Information info = new Information(jar, methodsClass); // Needs to exit with code -6 if method class cannot be found and exit code -5 if jar cannot be loaded
		Arborist arborist = new Arborist(info);
		TreeEvaluator eval = new TreeEvaluator(info);
		
		while (true) {
			Scanner in = new Scanner(System.in);
			System.out.print("> ");
			String argument = in.next();
			
			if (argument == "f") {
				info.printFunctions();
				continue;
			} 
			if (argument == "v") {
				arborist.toggleVerbose();
				continue;
			} 
			if (argument == "?") {
				printInterpreterHelp();
				continue;
			}
			if (argument == "q") {
				System.out.print("bye.");
				System.exit(0);
			}
			
			ParseTree tree = arborist.growTree(argument);
			String output = eval.evaluate(tree);
			System.out.println(output);
		}
	}

}
