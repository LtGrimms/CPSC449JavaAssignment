import java.util.*;   
import java.lang.reflect.*;   

public class Information {
	public static void main(String[] args) { 
	/*read the project prefix in 
	* This part will be hardcored after the testing is completed
	*/
		String name;   
		if (args.length > 0)
			name = args[0];   
		else  {   
			Scanner in = new Scanner(System.in);   
			System.out.println("Enter my.project.prefix ");   
			name = in.next();   
		}
		try {
			Class cl = Class.forName(name);  
			System.out.println("The project prefix: " + name);
			printMethods(cl);
		}
		catch (ClassNotFoundException e)  {   
			e.printStackTrace();   
		}
		catch (Exception e) {
			e.printStackTrace();  
		}  
		System.exit(0);
	}//end main
	public static void printMethods(Class cl)   {   
		Method[] methods = cl.getDeclaredMethods();   
		for (Method m : methods)   {   
			Class retType = m.getReturnType();   
            String name = m.getName();   
     
            System.out.print("   ");   
            // print modifiers, return type and method name   
            String modifiers = Modifier.toString(m.getModifiers());   
            if (modifiers.length() > 0) System.out.print(modifiers + " ");            
            System.out.print(retType.getName() + " " + name + "(");   
       
            // print parameter types   
            Class[] paramTypes = m.getParameterTypes();   
            for (int j = 0; j < paramTypes.length; j++)   {   
				if (j > 0) System.out.print(", ");   
                System.out.print(paramTypes[j].getName());   
             }   
             System.out.println(");");   
          }   
       } //end of method
}
