package org;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import utils.ParsingUtils;
/**
 * @author Anthony,Jihyun,Desmond,Jason,Justin
 * class about ParseTree
 */
public class ParseTree {

	private Node root;
	private static Information info;

	/**
	 * Constructor gives the tree the correct reflection data
	 */
	public ParseTree(Information info) {
		this.info = info;
		root = null;
	}

	/**
	 * Grows a tree by recursively calling the private grow function on successive nodes
	 * @param st The value to ultimately give the new node
	 * @param numberOfChildren the number of children allowed for the new node
	 */
	public void grow(String st, int numberOfChildren) {
		if (root == null)   {                    // add to empty tree
			root = new Node(st, numberOfChildren, null);
			root.checkCompleteness();
		}
		else
			grow(st, numberOfChildren, root);
	}
	/**
	 * Recursively grow a tree by finding the first incomplete node and adding a child.
	 * @param st The value to be given to the new node
	 * @param numberOfChildren the number of children allowed for the new node
	 * @param node the potential parent of the new node
	 */
	private void grow(String st, int numberOfChildren, Node node) {
//		For debugging
//		if (node.isComplete())
//			throw new IllegalArgumentException(); // too many arguments to root node

		boolean grown = false;

		if (node.children.isEmpty()) {
			node.addChild(st, numberOfChildren);
			grown = true;
		} else {
			Iterator<Node> iter = node.children.listIterator();
			while (iter.hasNext()) {
				Node child = (Node) iter.next();
				if (!child.isComplete()) {
					grow(st, numberOfChildren, child);
					grown = true;
				}
			}
		}

		if (!grown) {
			node.addChild(st, numberOfChildren);
		}

	}
	/**
	 * Once a tree is complete this function will add the return types to the nodes in the tree so
	 * we know the data types each node will return.
	 * @throws Exception when this is called on an incomplete tree
	 */
	public void addReturnTypes() /*throws Exception*/ {
//		very handy for debugging
//		if (!isComplete())
//			throw new Exception("tried to add return types to an incomplete tree"); //shouldn't happen?
	
		addReturnType(root);
	}

	/**
	 * Adds return types to nodes by first determining if they represent functions or values. If they
	 * are functions the return type is determined by the return types of their children and the reflection
	 * data. If they are values they are represented as integers
	 * @param node the node whose return type is being determined
	 * @return the return type of this node
	 * @throws Exception NOT NEEDED (JASONS)
	 */
	private Integer[] addReturnType(Node node) {
	
		Integer[] type = returnType(node.getValue());
		if (type[0] == 16) {
			ArrayList<Integer> arguments = new ArrayList<Integer>();
			for (Node child : node.children) {
				arguments.add(addReturnType(child)[0]);
			}
			type = ParsingUtils.checkForProperArguments(info.properArguments(node.getValue()), arguments);
			node.setReturnType(type);
			return type;
		} else {
			node.setReturnType(type);
			return type;
		}
	}

	/**
	 * Check whether a node is function call, String, Integer or Float
	 * @param arg the string representing a function call, Integer, String or Float
	 * @return Type of argument as an integer representation
	 * @throws Exception NOT NEEDED (JASONS)
	 */
	private static Integer[] returnType(String arg) {
		if (arg.charAt(0) == '\"'){
			Integer[] type =  {1};
			return type;
		}
		else if (info.checkForFunction(arg)) { //check for function throws exception
			Integer[] type = {16};
			return type;
		}
		else {
			Integer intOrFloat = ParsingUtils.intOrFloat(arg, 0);
			Integer[] type = {intOrFloat};
			return type;
		}
	}

	/**
	 * The tree is complete iff the root is complete
	 * @return root is complete or not
	 */
	public boolean isComplete() {
		return root.isComplete();
	}
	
	/**
	 * returns the trees root
	 * @return the root
	 */
	public Node getRoot() {
		return root;		
	}
	
	/**
	 * Print the parseTree
	 */
	public String toString() {
		if (root == null)
			return "Empty Tree";

		String result = buildString(root);
		return result;
	}
	/**
	 * Build string from parseTree by recursively looking through from the root down.
	 * This is mainly for debugging
	 * @param node current node being examined in recursive calls
	 * @return a string representation of the node
	 */
	private String buildString(Node node) {
		String value;
		if ((node.getReturnType()[0] & 16) == 16) {
			value = "(" + node.getValue() + ":" + node.getReturnType()[0] + " ";
		} else {
			value = node.getValue() + ":" + node.getReturnType()[0] + " ";
		}
		String append = "";

		Iterator<Node> iter = node.children.listIterator();
		if (iter.hasNext()) {
			while (iter.hasNext()) {
				append += buildString((Node) iter.next());
			}
			append += ")";
		}
		return value + append;
	}
	/**
	 * Parse Trees are nodes with pointers to their children and their parent. Each node also carries it's 
	 * returnType which is an integer that represents if the node is a function, Integer, Float or String. 
	 * They may also represent functions that return Integers, Floats or Strings. Each node is either 
	 * complete or not based on the number of children it has.
	 * @author Anthony,Jihyun,Desmond,Jason,Justin
	 */
	protected class Node {
		private Node parent;
		private LinkedList<Node> children;
		private int numberOfChildren;
		private String value;
		private Integer[] returnType;
		private boolean complete = false;

		/**
		 * Create a new node with no children and a pointer to the parent
		 * @param st The string value to be held by the node
		 * @param numberOfChildren The node will be complete once it has this many children
		 * @param parent a pointer to the nodes parent
		 */
		public Node(String st, int numberOfChildren, Node parent) {
			this.parent = parent;
			value = st;
			this.numberOfChildren = numberOfChildren;
			children = new LinkedList<Node>();
			//this.checkCompleteness();
		}
		/**
		 * set returnType of node to type
		 * @param type int representation of the return type
		 */
		public void setReturnType(Integer[] type) {
			returnType = type;
		}
		/**
		 * gets an integer representation of the returnType
		 * @return Type of node
		 */
		public Integer[] getReturnType() {
			return returnType;
		}
		
		/**
		 * get Children
		 * @return Linked list of children of node
		 */
		public LinkedList<Node> getChildren() {
			return children;
		}
		
		/**
		 * Add a child to the node. Each call to add child will determine if the new child is complete
		 * and, if so, determine if the parent is now complete and, if so, determine if the parent is
		 * complete and, if so....
		 * @param st The value held in the new node
		 * @param numberOfChildren the number of children the new node needs to be complete
		 */
		public void addChild(String st, int numberOfChildren) {
			Node child = new Node(st, numberOfChildren, this);
			children.add(child);
			child.checkCompleteness();
		}
		/**
		 * check node is complete or not
		 * @return node is complete or not
		 */
		public boolean isComplete() {
			return complete;
		}
		/**
		 * get value of node
		 * @return value of node
		 */
		public String getValue() {
			return value;
		}
		
		/**
		 * See if the node has the right number of children and set its completeness variable. If
		 * the node is complete check it will automatically check and set the parents completeness.
		 */
		public void checkCompleteness() {
			if (children.size() == numberOfChildren)
				complete = true;
			
			if (complete && parent != null) parent.checkCompleteness();
		}
	}

	public static void main(String[] args) throws Exception {
		ParseTree tree = new ParseTree(new Information("C:\\Users\\Darkras\\Documents\\CPSC 449\\commands.jar", "Commands"));

		tree.grow("test", 0);
		tree.grow("5", 0);
		tree.grow("5", 0);
		System.out.println(tree.toString());
		System.out.println(tree.isComplete());
	}
}
