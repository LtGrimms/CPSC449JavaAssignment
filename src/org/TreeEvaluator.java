package org;

import org.ParseTree.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarFile;
import java.util.Iterator;
import java.util.LinkedList;

public class TreeEvaluator {
	
	private Class coms;
	
	public TreeEvaluator() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException, IOException{
		File f = new File("C:\\Users\\Darkras\\Documents\\CPSC 449\\commands.jar");
		Class[] parameterTypes = new Class[]{URL.class};
		URL url = (f.toURI()).toURL();
		URLClassLoader sysloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
		Class<?> sysclass = URLClassLoader.class;
		Method method = sysclass.getDeclaredMethod("addURL", parameterTypes);
		method.setAccessible(true);
		method.invoke(sysloader, new Object[]{ url });
		
		Class<?> c = Class.forName("Commands");
		Method[] methods = c.getDeclaredMethods();
		/*for (Method m : methods)   {   
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
		}*/
		
		
		
	}
	
	public Object evaluate(Node root) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		Object [] args;
		args = new Object[5];
		int i= 0;
		Object result;
		Node nxt;
		String fun;
		fun = root.getValue();
		if (root.getReturnType() == 0) {
				//fun = Integer.parseInt(root.getValue());
			}
		else if (root.getReturnType() == 1) {
				//fun = Float.parseFloat(root.getValue());
			}
		else{
				//fun = root.getValue().replace("\"","");
			}
		
		
		LinkedList<Node> children = root.getChildren();
		if (children == null){
			return fun;
		}
		Iterator<Node> iter = children.listIterator();
		
		while (iter.hasNext()) {
			nxt = iter.next();
			args[i] = evaluate(nxt);
			i ++;
			
		}
		
	
		
		result = coms.getMethod(fun, (Class[]) args).invoke(coms, args); 

		result = null;
		return result;
	}
	

	
	/*public static void main(String[] args) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException, IOException{
		
	}*/ 
	public static void main(String[] args) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException, IOException{
		ParseTree tree = new ParseTree(new Information("C:\\Users\\Darkras\\Documents\\CPSC 449\\commands.jar", "tests.Methods01"));
		TreeEvaluator e = new TreeEvaluator();
		
		tree.grow("add",2);
		tree.grow("5",0);
		tree.grow("5",0);
		System.out.println(tree.isComplete());
		Object c = e.evaluate(tree.getRoot());
	}
}
