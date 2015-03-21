package org;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Scanner;
import java.util.jar.JarFile;

import utils.ErrorUtils;
import utils.Printing;

public class Interpereter {
	
	
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
	 * Used to determine if the inputed jar file is in-fact a jar
	 * taken from http://stackoverflow.com/questions/7085384/how-to-know-whether-a-zipped-file-is-jar-file-or-not 
	 * @param file the file to test for jar status
	 * @return true if the file is a jar
	 */
	 private static boolean detectJarFile(String file) {
	      try {
	        JarFile jarFile = new JarFile(file);

	      } catch (java.util.zip.ZipException e) {
	        // the uploaded file is NOT JAR file
	        return false;
	      } catch (IOException e) {
	    	  return false;
	      }

	      return true;
	    }
	 
	 private static boolean detectFile(String arg) {
		 File file = new File(arg);
		 return file.isFile();
	 }
	
	/**
	 * The main loop has two parts, the first considers the input parameters and determines if help and/or
	 * verbose has been called and tries to determine the jar file and method class that the arguments want
	 * loaded into the interpreter. The second part uses java reflection to interpret commands form an input 
	 * stream .
	 * @param args
	 */
	public static void main(String[] args) {

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
					jar = arg;
					if (!detectFile(jar)){
						ErrorUtils.jarIsNotAJarError();
					}
					if (!detectJarFile(jar)) {
						ErrorUtils.couldNotLoadJarError();
					}
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
			Printing.printSynopsis();
			System.exit(0); //Double check this
		} else if (help) {
			Printing.printHelp();
			if (jar == null)
				System.exit(0);
		}
		
		if (jar != null && methodsClass == null)
			methodsClass = "Commands";
		
		
		
		//___________________________________PART 2: Run an Interpreter_____________________________________
		
		Information info = new Information(jar, methodsClass); // Needs to exit with code -6 if method class cannot be found and exit code -5 if jar cannot be loaded
		Arborist arborist = new Arborist(info, verb);
		TreeEvaluator eval = new TreeEvaluator(info);
		
		Printing.printInterpreterHelp();
		while (true) {
			Scanner in = new Scanner(System.in);
			System.out.print("> ");
			String argument = in.nextLine();
			
			if (argument.length() == 0)
				continue;
			
			String noSpaces = argument.trim();
			boolean lengthOne = noSpaces.length() == 1;
			char firstChar = noSpaces.charAt(0);
			
			if (lengthOne && firstChar == 'f') {
				info.printFunctions();
				continue;
			} 
			if (lengthOne && firstChar == 'v') {
				arborist.toggleVerbose();
				continue;
			} 
			if (lengthOne && firstChar == '?') {
				Printing.printInterpreterHelp();
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
			}
			catch (Exception ex) {
				System.out.println("Someting in the reflection data threw an exception");
				ex.printStackTrace(); //catches "tried to addReturnTypes to incomplete tree"
									  //Leave this in for grading as it will catch calls to methods that throw exceptions in the reflection code
			}
			
			//in.close(); //not sure why but this causes an error
		}
	}

}
