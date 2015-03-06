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
			methodReturn(name,"getName");
			methodParameter(name,"getName");
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

	public static void methodReturn(String className, String methodName) throws Exception{
		List<Method> ms = Arrays.asList(Class.forName(className).getMethods());
		for (Method m : ms) {
			if (m.getName().equals(methodName)) {
				System.out.println(m.getReturnType().toString());
			}
		}	
	}// end method
	public static void methodParameter(String className, String methodName) throws Exception{
		List<Method> ms = Arrays.asList(Class.forName(className).getMethods());
		for (Method m : ms) {
			if (m.getName().equals(methodName)) {
				Class[] paramTypes = m.getParameterTypes(); 
				for (int j = 0; j < paramTypes.length; j++)   {   
					if (j > 0) {
						System.out.print(" ");  
					} 
                System.out.print(paramTypes[j].getName());   
				}   
			}
		}	
	}
}
