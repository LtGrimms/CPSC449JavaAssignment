package org;

/*References
 * https://www.youtube.com/watch?v=agnblS47F18
 * http://stackoverflow.com/questions/194698/how-to-load-a-jar-file-at-runtime
 * Core Java Volume I Fundamentals Eigth Edition page 222-224*/
import java.util.*;
import java.util.jar.JarFile;
import java.lang.reflect.*;
import java.io.*;
import java.net.*;

import utils.ErrorUtils;
import static utils.Types.BIGINTEGER;
import static utils.Types.INTEGER;
import static utils.Types.BIGFLOAT;
import static utils.Types.FLOAT;
import static utils.Types.STRING;

public class Information {
	
	private JarFile jarfile;
	
	public static Class cls;
	public Information (String jar, String methodsClass){
		try {
			File file = new File(jar);
			URL url = file.toURL();
			URL[] urls = new URL[] {url};
			ClassLoader cl = new URLClassLoader(urls);
			this.cls = cl.loadClass(methodsClass);
		}
		catch (ClassNotFoundException e) {
			ErrorUtils.couldNotFindMethodError();
		}
		catch (MalformedURLException e) {
		}
	}
	public static boolean checkForFunction(String methodName){
		List<Method> ms = Arrays.asList(cls.getMethods());
		for (Method m : ms) {
			if (m.getName().equals(methodName)) {
				return true;
			}
		}
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
				Integer[] instanceOfProperArguments = new Integer[m.getParameterTypes().length + 1];
				String returnType = m.getReturnType().toString();
					String formatted = returnType; 
					if (formatted.contains("String")) {
						instanceOfProperArguments[0] = STRING;
					}
					else if (formatted.contains("Float")) {
						instanceOfProperArguments[0] = BIGFLOAT;
					}
					else if (formatted.contains("float")) {
						instanceOfProperArguments[0] = BIGFLOAT;
					}
					else if (formatted.contains("Integer") ) {
						instanceOfProperArguments[0] = BIGINTEGER;
					}
					else if (formatted.contains("int") ) {
						instanceOfProperArguments[0] = BIGINTEGER;
					}

				Class[] paramTypes = m.getParameterTypes();
				for (int j = 1; j < instanceOfProperArguments.length; j++) {
					String parameter = paramTypes[j - 1].getName();
						formatted = parameter; 
						if (formatted.contains("String")) {
							instanceOfProperArguments[j] = STRING;
						}
						else if (formatted.contains("Float")) {
							instanceOfProperArguments[j] = BIGFLOAT;
						}
						else if (formatted.contains("float")) {
							instanceOfProperArguments[j] = FLOAT;
						}
						else if (formatted.contains("Integer")) {
							instanceOfProperArguments[j] = BIGINTEGER;
						}
						else if (formatted.contains("int") ) {
							instanceOfProperArguments[j] = INTEGER;
						}
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
