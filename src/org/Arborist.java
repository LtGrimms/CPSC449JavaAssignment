package org;

import java.text.ParseException;
import java.util.ArrayList;

import utils.ErrorUtils;
import utils.ParsingUtils;

public class Arborist {

	private Information info;
	private boolean verbose;

	// De-Bugging Only
	public Arborist() {
	}

	/**
	 * Creates an arborist class with specific method data
	 * @param info The method data that this arborist will use
	 */
	public Arborist(Information info, boolean verbose) {
		this.info = info;
		this.verbose = verbose;
	}

	/**
	 * Before an Arborist grows a tree it will check the string for errors. This
	 * is the function that checks strings for errors
	 * @param arg The string to check for errors
	 * @throws Exception 
	 */
	public void checkArgument(String arg) throws Exception{
		checkArgument(arg, new int[] {0, Integer.MAX_VALUE - 1, 0});
	}

	/**
	 * This function is the guts of the error checking for the string inputs
	 * it will pass through the string looking for either functions, strings 
	 * or numbers. Upon seeing a function it will call itself to ensure that
	 * the function has the correct input parameters.
	 * @param arg The string to be error checked
	 * @param argumentData an array in the form {start-checking-here, end-checking-here, function-return-type}
	 * @return This will either print an error message or its return value is only usefull for recusive calls
	 * @throws Exception 
	 */
	public int[] checkArgument(String arg, int[] argumentData) throws Exception{
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
			String function = ParsingUtils.nextWord(arg, index + 1);		
			if(!info.checkForFunction(function)){
				ParseException ex=new ParseException("could not find function.",index);
				ErrorUtils.parseError(arg,"could not find function.",ex,verbose);
			}
			int nextIndex = index + function.length() + 1;									
			ArrayList<Integer> argTypes = new ArrayList<Integer>();
			while (nextIndex < end) {
				int[] argData = checkArgument(arg, new int[] {nextIndex, end, 0});
				nextIndex = argData[1] + 1;									
				argTypes.add(argData[2]);
			}
			type = ParsingUtils.checkForProperArguments(function, argTypes);
			if (type == 0){
				ParseException ex=new ParseException("Something had the wrong type",index);
				ErrorUtils.parseError(arg,"Something had the wrong type",ex,verbose);
			}

			return new int[] {index,end,type};

		} else if (arg.charAt(index) == '"') {
			end = ParsingUtils.findClosingQuote(arg, index);
			return new int[] {index, end, 1};

		} else {
			end = ParsingUtils.findNextSpace(arg, index);
			int end2 = ParsingUtils.findClosingBracket(arg, index);
			if (end2 < end) end = end2;
			type = ParsingUtils.intOrFloat(arg, index);
			return new int[] {index, end, type};
		}
	}

	/**
	 * This function will check a string for errors with checkArgument either grow
	 * the corresponding tree or throw an exception.
	 * @param arg The string that grows the tree
	 * @return returns a parse tree corresponding to the string
	 * @throws Exception
	 */
	public ParseTree growTree(String arg) throws Exception{
		checkArgument(arg);
		ParseTree tree = new ParseTree();
		ArrayList<String> splitArguments = new ArrayList<String>();
		for (int i = 0; i < arg.length(); i++) {
			if (arg.charAt(i) == '(' 
					|| arg.charAt(i) == ')'
					|| arg.charAt(i) == ' ') {
				continue;
			} else if (arg.charAt(i) == '\"') {
				int closingIndex = ParsingUtils.findClosingQuote(arg, i) + 1;
				splitArguments.add(arg.substring(i, closingIndex));
				i = closingIndex;
			} else {
				String word = ParsingUtils.nextWord(arg, i);
				splitArguments.add(word);
				i += word.length();
			}
		}

		for (String spurt : splitArguments) {
			tree.grow(spurt);
		}
		tree.addReturnTypes();

		return tree;
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

		amy.growTree(arg);
		ParseTree tree=amy.growTree(arg);
		System.out.println(tree.toString());
	}
}