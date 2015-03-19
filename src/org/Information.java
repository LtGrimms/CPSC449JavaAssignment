package org;

/*References
 * https://www.youtube.com/watch?v=agnblS47F18
 * http://stackoverflow.com/questions/194698/how-to-load-a-jar-file-at-runtime
 * Core Java Volume I Fundamentals Eigth Edition page 222-224*/
import java.util.*;
import java.lang.reflect.*;
import java.io.*;
import java.net.*;

public class Information {
	public static final int BIGINTEGER = 14, INTEGER = 6, BIGFLOAT = 12, FLOAT = 4, STRING = 1;
	
	
	public static Class cls;
	public Information (String jar, String methodsClass){
		try {
			File file = new File(jar);
			URL url = file.toURL();
			URL[] urls = new URL[] {url};
			ClassLoader cl = new URLClassLoader(urls);
			this.cls = cl.loadClass(methodsClass);
		}
		catch (NoClassDefFoundError e) {
			e.printStackTrace();
			System.exit(-6);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static boolean checkForFunction(String methodName){
		List<Method> ms = Arrays.asList(cls.getMethods());
		for (Method m : ms) {
			if (m.getName().equals(methodName)) {
				//System.out.println("The method exists");
				return true;
			}
		}
		//System.err.println("The method does not exists");
		return false;
	}//end method
	public static void printFunctions (){
		Method[] methods = cls.getDeclaredMethods();
		for (Method m : methods) {
			String name = m.getName();
			System.out.print("("+ name);
			// print parameter types   
			Class[] paramTypes = m.getParameterTypes(); 
			for (int j = 0; j < paramTypes.length; j++)   { 
				System.out.print(" "); 
				if (j > 0) {
					System.out.print(" ");
				}
				String parameter = paramTypes[j].getName();
				if (parameter.contains(".") ) {
					int start = parameter.lastIndexOf(".") + 1;
					int end = parameter.length();
					String formatted = parameter.substring(start,end).toLowerCase();
					System.out.print(formatted);
				}
				else {
					System.out.print(paramTypes[j].getName() );
				}//end else
			}//end inner-for
			String returnType = m.getReturnType().toString();
			if (returnType.contains(".")) {
				int start = returnType.lastIndexOf(".") + 1;
				int end = returnType.length();
				String formatted = returnType.substring(start,end).toLowerCase();
				System.out.println(")" + " " + ": " + formatted ); 
			}//end if
			else {
				System.out.println(")" + " " + ": " + returnType ); 
			}//end else
		}
		
	}
	public static ArrayList<Integer[]> properArguments(String methodName) {
		ArrayList<Integer[]> properArguments = new ArrayList<Integer[]>(); 
		List<Method> ms = Arrays.asList(cls.getMethods());
		for (Method m : ms) {
			if (m.getName().equals(methodName)) {
				//return type block
				//System.out.println("The method exists");
				Integer[] instanceOfProperArguments = new Integer[m.getParameterTypes().length + 1];
				String returnType = m.getReturnType().toString();
//				if (returnType.contains(".")) {
//					int start = returnType.lastIndexOf(".") + 1;
//					int end = returnType.length();
					String formatted = returnType; //.substring(start,end);
					//System.out.println("The formatted " + formatted);
					if (formatted.equals("String")) {
						//System.out.println("formatted reach");
						instanceOfProperArguments[0] = STRING;
					}
					else if (formatted.equals("Float")) {
						instanceOfProperArguments[0] = BIGFLOAT;
					}
					else if (formatted.equals("float")) {
						instanceOfProperArguments[0] = FLOAT;
					}
					else if (formatted.equals("Integer") ) {
						instanceOfProperArguments[0] = BIGINTEGER;
					}
					else if (formatted.equals("int") ) {
						instanceOfProperArguments[0] = INTEGER;
					}
//				}
				//will never be reach but just in case
//				else if (m.getReturnType().toString().equals("String")) {
//					System.out.println("formatted reach");
//					instanceOfProperArguments[0] = 1;
//				}
//				else if (m.getReturnType().toString().equals("float") ){
//					instanceOfProperArguments[0] = 4;
//				}
//				else if (m.getReturnType().toString().equals("int") ){
//					instanceOfProperArguments[0] = 6;
//				}
//				else if (m.getReturnType().toString().equals("Integer") ){
//					instanceOfProperArguments[0] = 6;
//				} 
				//parameter block
				Class[] paramTypes = m.getParameterTypes();
				for (int j = 1; j < instanceOfProperArguments.length; j++) {
					String parameter = paramTypes[j - 1].getName();
//					if (parameter.contains(".") ){
//						int start = parameter.lastIndexOf(".") + 1;
//						int end = parameter.length();
						formatted = parameter; //.substring(start,end).toLowerCase();
						if (formatted.equals("String")) {
							instanceOfProperArguments[j] = STRING;
						}
						else if (formatted.equals("Float")) {
							instanceOfProperArguments[j] = BIGFLOAT;
						}
						else if (formatted.equals("float")) {
							instanceOfProperArguments[j] = FLOAT;
						}
						else if (formatted.equals("Integer")) {
							instanceOfProperArguments[j] = BIGINTEGER;
						}
						else if (formatted.equals("int") ) {
							instanceOfProperArguments[j] = INTEGER;
						}
//						}//end parameter contains
					//never gonna reach it unless shit happens
//					else if (paramTypes[j].getName().equals("String")) {
//						instanceOfProperArguments[j] = 1;
//					}
//					else if (paramTypes[j].getName().equals("float") ){
//						instanceOfProperArguments[j] = 4;
//					}
//					else if (paramTypes[j].getName().equals("int") ){
//						instanceOfProperArguments[j] = 6;
//					}
//					else if (paramTypes[j].getName().equals("Integer") ){
//						instanceOfProperArguments[j] = 6;
//					}

					
					
				}//end for 
				properArguments.add(instanceOfProperArguments);
			}//end if 
			

		}//end for
		
		
		return properArguments;
	}//end method 
	public static Class getClassName() {
		return cls;
	}//end method
}
