/*References
 * https://www.youtube.com/watch?v=agnblS47F18
 * Core Java Volume I Fundamentals Eigth Edition page 222-224*/
import java.util.*;
import java.lang.reflect.*;

public class Information {
	public static void main(String[] args) {
		// read class name from command line args and if args is empty prompt
		// user input
		String name;
		if (args.length > 0)
			name = args[0];
		else {
			Scanner in = new Scanner(System.in);
			System.out.println("Enter your project prefix ");
			name = in.next();
		}

		try {
			// print class name and superclass name (if != Object)
			Class cl = Class.forName(name);
			System.out.println("The project prefix: " + name);
			ArrayList<String> methodList = new ArrayList<String>();
			methodList = methods(cl);
			/*
			 * for(String tmp: methodList) { System.out.println("method list " +
			 * tmp); }
			 */
			methodReturn(name, "getName");
			methodParameter(name, "equals");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.exit(0);
	} // end of main

	public static ArrayList<String> methods(Class cl) {
		ArrayList<String> methodList = new ArrayList<String>();
		Method[] methods = cl.getDeclaredMethods();
		for (Method m : methods) {
			Class retType = m.getReturnType();
			String name = m.getName();
			// System.out.println("The name of the method " + name);
			methodList.add(name);
			// print parameter types
			/*
			 * Class[] paramTypes = m.getParameterTypes(); for (int j = 0; j <
			 * paramTypes.length; j++)
			 * System.out.println(paramTypes[j].getName());
			 */
		}
		return methodList;
	}// end of method

	public static ArrayList<String> methodReturn(String className, String methodName) throws Exception {
		ArrayList<String> methodReturn = new ArrayList<String>();
		List<Method> ms = Arrays.asList(Class.forName(className).getMethods());
		for (Method m : ms) {
			if (m.getName().equals(methodName)) {
				methodReturn.add(m.getReturnType().toString());
				System.out.println("The return type is " + m.getReturnType().toString());
			}
		}
		return methodReturn;
	}// end method

	public static ArrayList<String> methodParameter(String className, String methodName) throws Exception {
		ArrayList<String> methodParameter = new ArrayList<String>();
		List<Method> ms = Arrays.asList(Class.forName(className).getMethods());
		for (Method m : ms) {
			if (m.getName().equals(methodName)) {
				Class[] paramTypes = m.getParameterTypes();
				for (int j = 0; j < paramTypes.length; j++) {
					if (j > 0) {
						System.out.print(" ");
					}
					methodParameter.add(paramTypes[j].getName());
					System.out.print("The parameter is " + paramTypes[j].getName());
				}
				System.out.println();
			}
		}
		System.out.println("There are " + methodParameter.size() + " number of parameter");
		return methodParameter;
	}
}
