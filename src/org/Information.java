/*References
 * https://www.youtube.com/watch?v=agnblS47F18
 * http://stackoverflow.com/questions/194698/how-to-load-a-jar-file-at-runtime
 * Core Java Volume I Fundamentals Eigth Edition page 222-224*/
import java.util.*;
import java.lang.reflect.*;
import java.io.*;
import java.net.*;

public class Information {
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
	public static boolean checkForFunction(String methodName) throws Exception{
		List<Method> ms = Arrays.asList(cls.getMethods());
		for (Method m : ms) {
			if (m.getName().equals(methodName)) {
				System.out.println("The method exists");
				return true;
			}
		}
		System.err.println("The method does not exists");
		return false;
	}//end method
	public static void printFunctions () throws Exception{
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
	public static List<List<Integer>> properArguments(String methodName) {
		List<List<Integer>> properArguments = new ArrayList<List<Integer>>(); 
		List<Method> ms = Arrays.asList(cls.getMethods());
		List<Integer> myList = new ArrayList<Integer>();
		for (Method m : ms) {
			if (m.getName().equals(methodName)) {
				//return type block
				//System.out.println("The method exists");
				String returnType = m.getReturnType().toString();
				if (returnType.contains(".")) {
					int start = returnType.lastIndexOf(".") + 1;
					int end = returnType.length();
					String formatted = returnType.substring(start,end).toLowerCase();
					//System.out.println("The formatted " + formatted);
					if (formatted.equals("string")) {
						//System.out.println("formatted reach");
						myList.add(001);
					}
					else if (formatted.equals("float")) {
						myList.add(100);
					}
					else if (formatted.equals("int") ) {
						myList.add(110);
					}
					else if (formatted.equals("integer") ) {
						myList.add(110);
					}
				}
				//will never be reach but just in case
				else if (m.getReturnType().toString().equals("String")) {
					System.out.println("formatted reach");
					myList.add(001);
				}
				else if (m.getReturnType().toString().equals("float") ){
					myList.add(100);
				}
				else if (m.getReturnType().toString().equals("int") ){
					myList.add(110);
				}
				else if (m.getReturnType().toString().equals("Integer") ){
					myList.add(110);
				} 
				//parameter block
				Class[] paramTypes = m.getParameterTypes();
				for (int j = 0; j < paramTypes.length; j++) {
					String parameter = paramTypes[j].getName();
					if (parameter.contains(".") ){
						int start = parameter.lastIndexOf(".") + 1;
						int end = parameter.length();
						String formatted = parameter.substring(start,end).toLowerCase();
						if (formatted.equals("string")) {
							myList.add(001);
						}
						else if (formatted.equals("float")) {
							myList.add(100);
						}
						else if (formatted.equals("int") ) {
							myList.add(110);
						}
						else if (formatted.equals("integer") ) {
							myList.add(110);
						}
						}//end parameter contains
					//never gonna reach it unless shit happens
					else if (paramTypes[j].getName().equals("String")) {
						myList.add(001);
					}
					else if (paramTypes[j].getName().equals("float") ){
						myList.add(100);
					}
					else if (paramTypes[j].getName().equals("int") ){
						myList.add(110);
					}
					else if (paramTypes[j].getName().equals("Integer") ){
						myList.add(110);
					}
					
				}//end for 

			}//end if 
			
		properArguments.add(myList);
		}//end for
		return properArguments;
	}//end method 
	public static Class getClassName() {
		return cls;
	}//end method
}
