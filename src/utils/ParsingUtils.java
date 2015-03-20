package utils;

import java.text.ParseException;
import java.util.ArrayList;
import static utils.Types.BIGINTEGER;
import static utils.Types.INTEGER;
import static utils.Types.BIGFLOAT;
import static utils.Types.FLOAT;
import static utils.Types.STRING;
import static utils.Types.ERROR;

public final class ParsingUtils {
	
	
	/**
	 * This function compares the the argument types against all valid arguemnt types of a function 
	 * and returns the argument type that corresponds to the input parameters. For example if there is
	 * an add function with valid/proper argument types (int, int) and (float, float) and the argTypes
	 * variable has (int, int)  this function will retrun the variable type (as an integer) returned
	 * by add with input (int, int)
	 * @param function This should be an ArrayList<Integer[]> properArguments from an Information object
	 * @param argTypes The arrayList of arguments that 
	 * @return The type (as an integer) representing the return type of a function
	 */
	public static Integer[] checkForProperArguments(ArrayList<Integer[]> properArgumentTypes, ArrayList<Integer> argTypes) {
		//use method data to get parameter and return types for 'function'
		//put these return types in an ArrayList<Integer[]>
		// Example: if 'add' returns float and has float, float as parameters
		//          and also returns int with two int parameters
		//          then push {4, 4, 4} and {6, 6, 6} onto the list
		// Example: if 'length' returns int and takes a String and int
		//          then push {001, 001, 6} to the list
		// In all cases the elements added to the list should look like;
		//		{returnTypeSignature, parameter1TypeSignature, parameter2TypeSignature, ...}
		// call this array list properArguments
//		ArrayList<Integer[]> properArguments = new ArrayList<Integer[]>();
//		properArguments.add(new Integer[] {6, 6, 6});
//		properArguments.add(new Integer[] {4, 4, 4});
//		properArguments.add(new Integer[] {1, 1, 6});
//		properArguments.add(new Integer[] {6, 1});
		Integer[] returnType = {0};

		ArgumentChecking:
			for (Integer[] properFuncArguments : properArgumentTypes) {
				if (properFuncArguments.length != 1 + argTypes.size())
					continue;
				for (int i = 0; i < argTypes.size(); i++) {
					int argumentToMatch = properFuncArguments[i+1];
					if (((argTypes.get(i) & argumentToMatch) != (8 | argumentToMatch))
					  && (argTypes.get(i) & argumentToMatch) != (argumentToMatch)) {
						continue ArgumentChecking;
					}
				}
				returnType = properFuncArguments;
				break ArgumentChecking;
			}

		return returnType;
	}
	
	
	/**
	 * This method returns the word starting at index i from the string arg
	 * @param arg The string of words
	 * @param index The index that the word starts at
	 * @return The word between index i and the next bracket or space
	 */
	public static String nextWord(String arg, int index){
		int i=index;
		String functionName="";
		while(i < arg.length()
			&& arg.charAt(i)!=' '
			&& arg.charAt(i)!='('
			&& arg.charAt(i)!=')') 
			{
			functionName+=arg.charAt(i++);
		}
		return functionName;
	}
	
	/**
	 * This function returns the index of the next space in a string
	 * @param arg the string to find a space in
	 * @param index the place to start looking for spaces
	 * @return the index of the next space after index 'index', Integer.MAX_VALUE if none exist
	 */
	public static int findNextSpace(String arg, int index) {
		for (int i = index + 1; i < arg.length(); i ++) {
			if (arg.charAt(i) == ' ')
				return i;
		}

		return Integer.MAX_VALUE;
	}
	
	/**
	 * This function determines if the value of a substring of the input is an
	 * integer or a float or not a number
	 * @param arg The string that contains a number
	 * @param index The index of the start of the number
	 * @return 110 for integer, 100 for float, 0 for not a number
	 */
	public static int intOrFloat(String arg, int index) {
		//TODO this function needs to be able to recognize a two decimal error
		//     for example 5..5 will be recognized as a float
		int type = BIGINTEGER;
		boolean flag = false;
		for (int i = index; i < arg.length() 
				&& arg.charAt(i) != ' ' 
				&& arg.charAt(i) != ')'; i++) {
			Character c = arg.charAt(i);
			if (!(Character.isDigit(c) 
				 || c == '.' 
				 || c == '-'
				 || c == 'E'
				 || c == 'e'))
				return 0;
			if (c == '.' 
			 || c == 'e'
			 || c == 'E'){
				type = BIGFLOAT;
				if (flag) return 0;
				flag = true;
			}
		}
		return type;
	}
	
	
	/**
	 * This function finds the index of a particular closing bracket, the one
	 * that closes the bracket at index 'startBracket'
	 * @param arg the string to find a closing bracket in
	 * @param startBracet the index of the corresponding start bracket
	 * @return the index of the corresponding closing quote, Integer.MAX_VALUE if none exist
	 */
	public static int findClosingBracket(String arg, int startBracet){
		int count=1;
		int key=Integer.MAX_VALUE;
		for(int i=startBracet + 1;i<arg.length();i++){
			if(arg.charAt(i)=='('){
				count +=1;
			}
			else if(arg.charAt(i)==')'){
				count -=1;
			}
			if(count==0){
				key=i;
				break;
			}
		}
		return key;
	}

	/**
	 * This function returns the next quote character in the string from te given index
	 * @param arg The string to look for closing brackets in
	 * @param index The index of the start bracket
	 * @return The index of the next bracket, Integer.MAX_VALUE if none exist
	 */
	public static int findClosingQuote(String arg, int index){
		int count=1;
		int key=Integer.MAX_VALUE;
		for(int i=index+1;i<arg.length();i++){
			if(arg.charAt(i)=='"'){
				count +=1;
			}

			if(count%2 == 0){
				key=i;
				break;
			}
		}
		return key;
	}

	/**
	 * This function finds the end of the word in a string at the given index
	 * @param arg The string being parsed
	 * @param index The index of the start (or any part really) of the word
	 * @return the index of the character after the end of the word. IE if the word at index i is "mad" then arg.charAt(return - 1) will be 'd'
	 */
	public static int findEndOfArgument(String arg, int index) {
		for (int i = index; i < arg.length(); i++) {
			if (arg.charAt(i) == ' '
			 || arg.charAt(i) == ')') {
				return i;
			}
		}
		
		return arg.length();
	}
	
	/**
	 * This function will count the arguments in a function call after the function name. embedded function calls
	 * will count as one argument, as will strings.
	 * @param arg The string being parsed
	 * @param start The location of the bracket at the beginning of the function
	 * @param end The location of the bracket at the end of the function
	 * @return the number of arguments in a function call, Integer.MAX_VALUE if a string with no closing bracket is found
	 */
	 public static int numberOfArg(String arg, int start, int end){
		 int arguments = 0;
		 
		 int index = start + nextWord(arg, start + 1).length() + 1;
		 for (int i = index; i < end; i++) {
			 if (arg.charAt(i) == ' ' || arg.charAt(i) == ')')
				 continue;
			 if (arg.charAt(i) == '(') {
				 arguments++;
				 i = findClosingBracket(arg, i);
				 continue;
			 }
			 if (arg.charAt(i) == '\"') {
				 int closingBracket = findClosingBracket(arg, i);
				 if (closingBracket == Integer.MAX_VALUE) {
					 return Integer.MAX_VALUE;
				 } else {
					 arguments++;
					 i = closingBracket;
				 }
			 } else {
				 i += nextWord(arg, i).length();
				 arguments++;
			 }
		 }
		 return arguments;
	 }

}
