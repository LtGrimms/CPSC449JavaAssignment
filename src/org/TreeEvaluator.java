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
		File f = new File("C:\\Users\\Desmond\\Documents\\School\\CPSC449\\JavaAssignment\\CPSC449JavaAssignment\\src\\org\\commands.jar");
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
	
	public String evaluate(Node root){
		String [] args;
		args = new String[5];
		int i= 0;
		String result;
		Node nxt;
		String fun = root.getValue();
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
		
		result = apply(fun, args);
		
		return result;
	}
	
	private String apply(String fun, String[] args){
		
		
		Method met = coms.getDeclaredMethod(fun, args)
		return "this function is not done yet! :(";
	}
	
	/*public static void main(String[] args) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException, IOException{
		
	}*/ 
	public static void main(String[] args) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException, IOException{
		ParseTree tree = new ParseTree();
		TreeEvaluator e = new TreeEvaluator();
		
		tree.grow("add");
		tree.grow("5");
		tree.grow("5");
		System.out.println(tree.isComplete());
		String c = e.evaluate(tree.getRoot());
	}
}
