package org;

//package org;

import java.util.ArrayList;

import utils.ParsingUtils;

public class Arborist {
	
	private Information info;
	private boolean verbose;
	
	// De-Bugging Only
	public Arborist() {
	}
	
	/**
	 * Creates an Arborist class with desired class information
	 * @param info The method data that this arborist will use
	 */
	public Arborist(Information info) {
		this.info = info;
	}
	
	public void checkArgument(String arg) throws Exception {
		checkArgument(arg, new int[] {0, Integer.MAX_VALUE, 0});
	}
	
	public int[] checkArgument(String arg, int[] argumentData) throws Exception {
		int end;
		int index = argumentData[0];
		int type = argumentData[2];

		if (arg.charAt(index) == ' ') {
			return checkArgument(arg, new int[] {index+1, argumentData[1], type});
		}

		if (arg.charAt(index) == '(') {										
			end = ParsingUtils.findClosingBracket(arg, index);				
			if (end > argumentData[1]) 
				throw new Exception();

			String function = ParsingUtils.nextWord(arg, index + 1);		
			//checkForFunction(function);

			int nextIndex = index + function.length() + 1;									
			ArrayList<Integer> argTypes = new ArrayList<Integer>();
			while (nextIndex < end) {
				int[] argData = checkArgument(arg, new int[] {nextIndex, end, 0});
				nextIndex = argData[1] + 1;									
				argTypes.add(argData[2]);
			}
			type = ParsingUtils.checkForProperArguments(function, argTypes);
			if (type == 0)
				throw new Exception("Something had the wrong type");
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

	public ParseTree growTree(String arg) throws Exception{
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
	
	public void toggleVerbose() {
		verbose = !verbose;
	}

	public static void main(String[] args) throws Exception {
		
		Arborist amy=new Arborist();
		String arg = "(add (mult 5 5) (mult 5 5))";
		
		amy.checkArgument(arg, new int[] {0, Integer.MAX_VALUE, 0});
		ParseTree tree=amy.growTree(arg);
		System.out.println(tree.toString());
	}
}