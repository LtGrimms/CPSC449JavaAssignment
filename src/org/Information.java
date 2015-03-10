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
			System.out.println("The method name is " + name);
		}
		
	}
	public static ArrayList<Integer> properArguments(String methodName) {
		ArrayList<Integer> properArguments = new ArrayList<Integer>();
		List<Method> ms = Arrays.asList(cls.getMethods());
		for (Method m : ms) {
			if (m.getName().equals(methodName)) {
				//return type block
				if (m.getReturnType().toString() == "String") {
					properArguments.add(001);
				}
				else if (m.getReturnType().toString() == "float") {
					properArguments.add(100);
				}
				else if (m.getReturnType().toString() == "int") {
					properArguments.add(110);
				}
				//parameter block
				Class[] paramTypes = m.getParameterTypes();
				for (int j = 0; j < paramTypes.length; j++) {
					if (paramTypes[j].getName() == "String") {
						properArguments.add(001);
					}
					else if (paramTypes[j].getName() == "float") {
						properArguments.add(100);
					}
					else if (paramTypes[j].getName() == "int") {
						properArguments.add(110);
					}
				}//end for 

			}//end if 
			
		
		}//end for
		return properArguments;
	}//end method 
}
