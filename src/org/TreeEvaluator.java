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
import java.util.List;

public class TreeEvaluator {
	
	private Class coms;
	private Information info;
	
	public TreeEvaluator(Information info) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException, IOException{
		this.coms = info.cls;
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

		Iterator<Node> iter = children.listIterator();
		if (!iter.hasNext()){
			return fun;
		}
		types= new Class[children.size()];
		args = new Object[children.size()];
		
		while (iter.hasNext()) {
			nxt = iter.next();
			fun = evaluate(nxt);
			switch ( nxt.getReturnType() & 7 ) {
				case 0b1:
					c=String.class;
					args[i]=fun.replace("\"","");
					break;
				case 0b100:
					c=float.class;
					args[i]=new Float(Float.parseFloat(fun));
					break;
				case 0b110:
					c=int.class;
					args[i]=new Integer(Integer.parseInt(fun));
					break;
				default:
					break;
			}
			
/*			if ((nxt.getReturnType() & 7) == 0b110) {
				args[i]=Integer.parseInt(fun);
			}
			else if ((nxt.getReturnType() & 7) == 0b100) {
				args[i]=Float.parseFloat(fun);
			}
			else{
				args[i]=fun.replace("\"","");
			}*/
			types[i]=c;
			i ++;
			
		}
		fun = root.getValue();
		

		fun = "" + coms.getMethod(fun, types).invoke(coms, args);

		
		return fun;
	}
	

	
	/*public static void main(String[] args) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException, IOException{
		
	}*/ 
	public static void main(String[] args) throws Exception{
		ParseTree tree = new ParseTree(new Information("C:\\Users\\Darkras\\Documents\\CPSC 449\\commands.jar", "Commands"));
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
		
		Information test = new Information("C:\\Users\\Darkras\\Documents\\CPSC 449\\commands.jar", "Commands");
		List<List<Integer>> testList = test.properArguments("add");
		
		System.out.println(testList);
		
		
		TreeEvaluator e = new TreeEvaluator(test);
		
		tree.grow("add",1);
		tree.grow("5",0);
		tree.grow("5",0);
		tree.addReturnTypes();
		System.out.println(tree.isComplete());
		System.out.print(e.evaluate(tree.getRoot()));
	}
}
