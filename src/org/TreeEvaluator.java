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
	
	public TreeEvaluator(Class<?> incoms) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException, IOException{
		this.coms = incoms;
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
	
	public String evaluate(Node root) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		Object[] args;
		Class[] types;
		int i= 0;
		Node nxt;
		String fun;
		fun = root.getValue();
		Class c=null;
		
		
		LinkedList<Node> children = root.getChildren();
		if (children == null){
			
		}
		Iterator<Node> iter = children.listIterator();
		types= new Class[children.size()];
		args = new Object[children.size()];
		while (iter.hasNext()) {
			nxt = iter.next();
			switch ( nxt.getReturnType() ) {
				case 1:
					c=String.class;
					break;
				case 100:
					c=Float.class;
					break;
				case 110:
					c=Integer.class;
					break;
				case 1000:
					c=Integer.class;
					break;
				default:
					break;
			}
			fun = evaluate(nxt);
			if (root.getReturnType() == 110) {
				args[i]=Integer.parseInt(fun);
			}
			else if (root.getReturnType() == 100) {
				args[i]=Float.parseFloat(fun);
			}
			else{
				args[i]=fun.replace("\"","");
			}
			types[i]=c;
			i ++;
			
		}
		
		fun = (String) coms.getMethod(fun,  types).invoke(coms, args); 
		
		return fun;
	}
	

	
	/*public static void main(String[] args) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException, IOException{
		
	}*/ 
	public static void main(String[] args) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException, IOException{
		ParseTree tree = new ParseTree();
		File f = new File("~\\Downloads\\commands.jar");
		Class[] parameterTypes = new Class[]{URL.class};
		URL url = (f.toURI()).toURL();
		URLClassLoader sysloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
		Class<?> sysclass = URLClassLoader.class;
		Method method = sysclass.getDeclaredMethod("addURL", parameterTypes);
		method.setAccessible(true);
		method.invoke(sysloader, new Object[]{ url });
		
		Class<?> c = Class.forName("Commands");
		Method[] methods = c.getDeclaredMethods();
		
		TreeEvaluator e = new TreeEvaluator(c);
		
		tree.grow("add");
		tree.grow("5");
		tree.grow("5");
		System.out.println(tree.isComplete());
		System.out.print(e.evaluate(tree.getRoot()));
	}
}
