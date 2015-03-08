//package org;

import java.util.ArrayList;

public class Arborist {
	// private MethodData methodData;
	
	// this thing needs to be able to take in a string and reflection data and either grow a 
	// parse tree of throw an appropriate error.
	
	// Integer[] argumentData = {start, finish, type}
	
	public Arborist(/*MethodData methodData*/) {
		//this.methodData = methodData;
	}
	
	public int[] checkArgument(String arg, int[] argumentData) throws Exception {
		//A few pre parsing things will need to be done before this will work
		//they include
		// 1. ensure even number of quotes
		// 2. 
		int end;
		int index = argumentData[0];
		int type = argumentData[2];
		
		if (arg.charAt(index) == ' ') {
			return checkArgument(arg, new int[] {index+1, argumentData[1], type});
		}
		
		if (arg.charAt(index) == '(') {										// if this is a function call
			end = findClosingBracket(arg, index);							// find end of function
			if (end > argumentData[1]) 
				throw new Exception();
			
			String function = nextWord(arg, index);							// find and test function name
			//checkForFunction(function);
			
			int nextIndex = index + function.length() + 1;					// Possible errors with multiple spaces				
			ArrayList<Integer> argTypes = new ArrayList<Integer>();
			while (nextIndex < end) {										// get return types from input 'arg'
				int[] argData = checkArgument(arg, new int[] {nextIndex, end, 0});
				nextIndex = argData[1] + 1;									// Possible errors with multiple spaces
				argTypes.add(argData[2]);
			}
			type = checkForProperArguments(function, argTypes);
			if (type == 0)
				throw new Exception("Something had the wrong type");
			return new int[] {index,end,type};
			
		} else if (arg.charAt(index) == '"') {
			end = findClosingQuote(arg, index);
			return new int[] {index, end, 1};
			
		} else {
			end = findNextSpace(arg, index);
			int end2 = findClosingBracket(arg, index);
			if (end2 < end) end = end2;
			type = intOrFloat(arg, index);
			return new int[] {index, end, type};
		}
	}
	
	private int intOrFloat(String arg, int index) {
		int type = 110;
		for (int i = index; i < arg.length() 
				             && arg.charAt(i) != ' ' 
				             && arg.charAt(i) != '\n'
				             && arg.charAt(i) != ')'; i++) {
			Character c = arg.charAt(i);
			if (!(Character.isDigit(c) || c == '.'))
				return 0;
			if (c == '.')
				type = 100&type;
		}
		return type;
	}
	
	private static int findNextSpace(String arg, int index) {
		for (int i = index + 1; i < arg.length(); i ++) {
			if (arg.charAt(i) == ' ' || arg.charAt(i) == '\n')
				return i;
		}
		
		return Integer.MAX_VALUE;
	}
	
	private static int findClosingBracket(String arg, int startBracet){
		 int count=1;
		 int key=Integer.MAX_VALUE;;
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

	private static int findClosingQuote(String arg, int index){
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

	private static String nextWord(String arg, int index){
		int i=index;
		String functionName="";
		while(arg.charAt(i)!=' '){
		 functionName+=arg.charAt(++i);
		}
		return functionName;
	}


		
	private static void checkForFunction(String function) {
		
	}
	
	private int checkForProperArguments(String function, ArrayList<Integer> argTypes) {
		//use method data to get parameter and return types for 'function'
		//put these return types in an ArrayList<Integer[]>
		// Example: if 'add' returns float and has float, float as parameters
		//          and also returns int with two int parameters
		//          then push {100, 100, 100} and {110, 110, 110} onto the list
		// Example: if 'length' returns int and takes a String and int
		//          then push {001, 001, 110} to the list
		// In all cases the elements added to the list should look like;
		//		{returnTypeSignature, parameter1TypeSignature, parameter2TypeSignature, ...}
		// call this array list properArguments
		ArrayList<Integer[]> properArguments = new ArrayList<Integer[]>();
		properArguments.add(new Integer[] {110, 110, 110});
		properArguments.add(new Integer[] {100, 100, 100});
		properArguments.add(new Integer[] {001, 001, 110});
		properArguments.add(new Integer[] {110, 001});
		int returnType = 0;
		
		ArgumentChecking:
		for (Integer[] properFuncArguments : properArguments) {
			if (properFuncArguments.length != 1 + argTypes.size())
				continue;
			for (int i = 0; i < argTypes.size(); i++) {
				int argumentToMatch = properFuncArguments[i+1];
				if ((argTypes.get(i) & argumentToMatch) != argumentToMatch) {
					continue ArgumentChecking;
				}
			}
			returnType = properFuncArguments[0];
			break ArgumentChecking;
		}
		
		return returnType;
	}
	
	public static void main(String[] args) {
		
		Arborist a = new Arborist();
		try {
			//tried the following 
			//  1. 5  PASS
			//  2. 5. PASS
			//  3. (add (mult 5 5) (mult 5 5)) PASS
			//  4. (add (mult 5 5) (mult 5. 5)) PASS
			//  5. "Hello"  PASS
			//  6. (charAT "Hello" 5) PASS
			//  7.  (add 5 (length \"hello\")) PASS
			
			int[] result = a.checkArgument("(add 5 (length \"hello\"))", new int[] {0, Integer.MAX_VALUE, 0});
			for (int i= 0; i < result.length; i++)
				System.out.println(result[i] + ", ");
		} catch (Exception e) {
			System.out.println("something went wrong hommie, go fix it.");
			e.printStackTrace();
		}
	}
}