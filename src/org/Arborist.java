package org;

import java.text.ParseException;
import java.util.ArrayList;

import utils.ErrorUtils;
import utils.ParsingUtils;
import static utils.Types.BIGINTEGER;
import static utils.Types.INTEGER;
import static utils.Types.BIGFLOAT;
import static utils.Types.FLOAT;
import static utils.Types.STRING;
import static utils.Types.ERROR;

public class Arborist {

	private ParseTree tree;
	private Information info;
	private boolean verbose;

	// De-Bugging Only
	public Arborist() {
	}

	/**
	 * Creates an arborist class with specific method data
	 * @param info The method data that this arborist will use
	 * @param verbose decide if the arborist starts in verbose mode (true) or not (false)
	 */
	public Arborist(Information info, boolean verbose) {
		this.info = info;
		this.verbose = verbose;
	}

	/**
	 * This function simultaneously checks a string for errors and grows an appropriate parse tree.
	 * The function will complete without error iff the input string was valid with respect to the
	 * reflection data. (double check that, seems to be true)
	 * @param arg The string to check for errors and attempt to grow
	 * @throws Exception parse exceptions are thrown if the string is invalid
	 */
	public ParseTree checkArgument(String arg) throws ParseException{
		tree = new ParseTree(info);
		checkArgument(arg, new int[] {0, Integer.MAX_VALUE - 1, ERROR});
		return tree;
	}

	/**
	 * This function is the guts of the error checking for the string inputs
	 * it will pass through the string looking for either functions, strings 
	 * or numbers. Upon seeing a function it will call itself to ensure that
	 * the function has the correct input parameters.
	 * @param arg The string to be error checked
	 * @param argumentData an array in the form {start-checking-here, end-checking-here, function-return-type}
	 * @return This will either print an error message or do nothing. Its return value is only usefull for recusive calls
	 * @throws Exception invalid strings will throw parse errors
	 */
	private int[] checkArgument(String arg, int[] argumentData) throws ParseException{
		int end;
		int index = argumentData[0];
		int type = argumentData[2];

		if (arg.charAt(index) == ' ') {
			return checkArgument(arg, new int[] {index+1, argumentData[1], type});
		}

		if (arg.charAt(index) == '(') {	
			end = ParsingUtils.findClosingBracket(arg, index);				
			if (end > argumentData[1]) {
				ParseException ex=new ParseException("could not find closing bracket",index);
				ErrorUtils.parseError(arg,"could not find closing bracket",ex,verbose);
			}
			
			return checkFunction(arg, new int[] {index, end, type});

		} else if (arg.charAt(index) == '"') {
			end = ParsingUtils.findClosingQuote(arg, index);
			String node = arg.substring(index, end + 1);
			
			if (end > argumentData[1]) {
				ParseException ex=new ParseException("could not find closing quote",index);
				ErrorUtils.parseError(arg,"could not find closing quote",ex,verbose);
			}
			
			tree.grow(node, 0);
			return new int[] {index, end, STRING};

		} else {
			end = ParsingUtils.findEndOfArgument(arg, index);
			type = ParsingUtils.intOrFloat(arg, index);
			if (type == ERROR) {
				ParseException ex = new ParseException("Could not identifiy argument", index);
				ErrorUtils.parseError(arg, "Could not identify argument" ,ex, verbose);
			} 
			
			String node = arg.substring(index, end);
			Float fl = Float.parseFloat(node);
//			else if (fl > Float.MAX_VALUE - 1); {
//				ParseException ex = new ParseException("Number is to large", index);
//				ErrorUtils.parseError(arg, "Number is to large", ex, verbose);
//			}

			tree.grow(node, 0);
			
			return new int[] {index, end, type};
		}
	}
	
	/**
	 * if checkArgument encounters a function it will use this to recursively call itself on the functions
	 * arguments.
	 * @param arg the string that is being checked for errors
	 * @param argumentData same as in checkArgument
	 * @return same as in checkArgument, only usefull for recursive calls
	 * @throws Exception parse exceptions are throw for invalid strings.
	 */
	private int[] checkFunction(String arg, int[] argumentData) throws ParseException {
		int index = argumentData[0];
		int end = argumentData[1];
		int type = argumentData[2];
		
		String function = ParsingUtils.nextWord(arg, index + 1);		
		if(!info.checkForFunction(function)){
			ParseException ex=new ParseException("could not find function " + function, index);
			ErrorUtils.parseError(arg,"could not find function " + function ,ex,verbose);
		}
		int numberOfArguments = 0;
		try{
			numberOfArguments = ParsingUtils.numberOfArg(arg, index, end);
		} catch (ParseException ex) {
			ErrorUtils.parseError(arg, "Could not find the closing index for string", ex, verbose);
		}
		tree.grow(function, numberOfArguments);
		
		type = ensureProperArguments(arg, function, index, end, numberOfArguments);
		
		if (type == ERROR){
			ParseException ex=new ParseException("Something had the wrong type",index);
			ErrorUtils.parseError(arg,"Something had the wrong type",ex,verbose);
		}

		return new int[] {index,end,type};
	}
	
	private int ensureProperArguments(String arg, String function, int index, int end, int numberOfArguments) throws ParseException {
		
		int type = 0;
		int nextIndex = index + function.length() + 1;		
		
		ArrayList<Integer> argTypes = new ArrayList<Integer>();
		while (argTypes.size() < numberOfArguments) {
			int[] argData = checkArgument(arg, new int[] {nextIndex, end, 0});
			nextIndex = argData[1] + 1;									
			argTypes.add(argData[2]);
		}
		
		Integer guard = ParsingUtils.checkForProperArguments(info.properArguments(function), argTypes)[0];
		if (guard == null) {
			ParseException ex = new ParseException("This function did not return a String, (I)int or (F)float", index);
			ErrorUtils.parseError(arg,"This function did not return a String, (I)int or (F)float", ex, verbose);
		}
		
		type = guard;
		
		if (type == ERROR){
			String errorMsg = "An argument for " + function + " was not of the right type";
			ParseException ex=new ParseException(errorMsg, index + 1);
			ErrorUtils.parseError(arg, errorMsg, ex, verbose);
		}
		return type;
	}

	/**
	 * Determines if the arborist prints stack traces or not
	 */
	public void toggleVerbose() {
		verbose = !verbose;
	}

	public static void main(String[] args) throws Exception {

		Arborist amy=new Arborist(new Information("/Users/alcridla/Documents/Methods.jar", "tests.Methods01"), false);
		String arg = "\"hello\"";

		System.out.println(1100 & 1000);
		ParseTree tree;
		tree = amy.checkArgument("(add (mult 5 5) (mult (add 5 5) 5))");
		tree.addReturnTypes();
		System.out.println(tree);
	}
}