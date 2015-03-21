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

import static utils.Types.BIGINTEGER;
import static utils.Types.INTEGER;
import static utils.Types.BIGFLOAT;
import static utils.Types.FLOAT;
import static utils.Types.STRING;
import static utils.Types.ERROR;
/**
 * 
 * @author Anthony,Jihyun,Desmond,Jason,Justin
 *
 */
public class TreeEvaluator {
	/**
	 * The class which contains the commands for the interpreter
	 */
	private Class coms;
	/**
	 *	An instance of information to handle all the reflection
	 */
	private Information info;
	
	/**
	 * 	Constructor for treeEvaluator, we need to be passed information
	 *  about the class we are reflecting. 
	 * @param info
	 */
	public TreeEvaluator(Information info) /*throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException, IOException*/{
		this.coms = info.cls;
	}
	
	/**
	 * Evaluates a complete and correct parsetree. 
	 * <p>
	 * evaluate is applied to a tree recursively. The evaluation of a tree
	 * is the function present at the root node applied to the result of all
	 * subtrees. The evaluation of a leaf is the value of that leaf.
	 * @param root
	 * @return
	 */
	public String evaluate(Node root){
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
		
		//populates types[] for getmethod and args[] for invoke
		while (iter.hasNext()) {
			nxt = iter.next();
			fun = evaluate(nxt);
			switch ( root.getReturnType()[i+1] & 0b1111 ) {
				//the argument should be a string
				case STRING:
					c=String.class;
					args[i]=fun.replace("\"","");
					break;
				//the argument should be a float primitive
				case FLOAT:
					c=float.class;
					args[i]=new Float(Float.parseFloat(fun));
					break;
				//the argument should be a int primitive
				case INTEGER:
					c=int.class;
					args[i]=new Integer(Integer.parseInt(fun));
					break;
				//the argument should be a Float class
				case BIGFLOAT:
					c=Float.class;
					args[i]=new Float(Float.parseFloat(fun));
					break;
				//the argument should be an Integer class
				case BIGINTEGER:
					c=Integer.class;
					args[i]=new Integer(Integer.parseInt(fun));
					break;
				default:
					break;
			}
			

			types[i]=c;
			i ++;
			
		}
		fun = root.getValue();
		
		try {
			//get the method at this node and apply it to the evaluation of its children
			fun = "" + coms.getMethod(fun, types).invoke(coms, args);
		}catch (Exception e){
			//this will never ever happen
		}
		
		return fun;
	}
	
	/**
	 * Main was used for testing purposes, and can safely be ignored. Just don't call it.
	 * @param args
	 * @throws Exception
	 */
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
		List<Integer[]> testList = test.properArguments("add");
		
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
