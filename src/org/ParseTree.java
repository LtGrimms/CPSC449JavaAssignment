package org;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import utils.ParsingUtils;
/**
 * @author Anthony,Jihyun,Desdmond,Jason,Justin
 * class about ParseTree
 */
public class ParseTree {

	private Node root;
	private Information info;

	/**
	 * Constructor which initialize root of tree
	 */
	public ParseTree() {
		root = null;
	}

	/**
	 * Grow tree
	 * @param st String about user's command
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
	 * Grow tree if the tree has root and make node from user's command
	 * check if the node has two children
	 * @param st String about user's command
	 * @param node Root of parseTree
	 */
	private void grow(String st, int numberOfChildren, Node node) {
		if (node.isComplete())
			throw new IllegalArgumentException(); // too many arguments to root node

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
	 * check the root of tree is complete
	 * @return root is complete or not
	 */
	public boolean isComplete() {
		return root.isComplete();
	}
	
	public Node getRoot() {
		return root;		
	}
	/**
	 * Add return type of node on parseTree
	 * @throws Exception
	 */
	public void addReturnTypes() throws Exception {
		if (!isComplete())
			throw new Exception("tried to add return types to an incomplete tree");

		addReturnType(root);
	}
	/**
	 * Check the node is function or not 
	 * If the node is function, check children of the node
	 * @param node Node of parseTree
	 * @return Type of node
	 */
	private int addReturnType(Node node){

		int type = returnType(node.getValue());
		if (type == 1000) {
			ArrayList<Integer> arguments = new ArrayList<Integer>();
			for (Node child : node.children) {
				arguments.add(addReturnType(child));
			}
			type = ParsingUtils.checkForProperArguments(node.getValue(), arguments);
			node.setReturnType(type);
			return type;
		} else {
			node.setReturnType(type);
			return type;
		}
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
	 * Build string from parsTree
	 * @param node Root of tree
	 * @return user's command
	 */
	private String buildString(Node node) {
		String value;
		if (node.getReturnType() == 1000) {
			value = "(" + node.getValue() + "; " + node.getReturnType() + " ";
		} else {
			value = node.getValue() + ", " + node.getReturnType() + " ";
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
	 * Check whether argument is function or string or Integer or Float
	 * @param arg User's command
	 * @return Type of argument

	 */
	private static int returnType(String arg) {
		if (arg.charAt(0) == '\"')
			return 1;
		else if (/* info.checkForFunction(arg) */ arg == "add")  //check for function throws exception
			return 1000;
		else return ParsingUtils.intOrFloat(arg, 0);
	}

	/**
	 * 
	 * @author Anthony,Jihyun,Desdmond,Jason,Justin
	 *Class about node of parsTree
	 */
	protected class Node {
		private Node parent;
		private LinkedList<Node> children;
		private int numberOfChildren;
		private String value;
		private int returnType;
		private boolean complete = false;

		/**
		 * link the parent to children
		 * @param st
		 * @param parent
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
		 * @param type
		 */
		public void setReturnType(int type) {
			returnType = type;
		}
		/**
		 * get returnType
		 * @return Type of node
		 */
		public int getReturnType() {
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
		 * make children of the node
		 * @param st user's command
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
		 * check the node is right word and make variable 'complete' of node true
		 * check all nodes of parseTree are complete or not 
		 */
		public void checkCompleteness() {
			// Need to write this
			// depends on reflection data
			// check 2 things
			//  1. Does this node have the right number of children
			//  2. Are the children complete

			// the following is an ad-hoc implementation of checkCompleteness that allows each node to have only two children

			if (children.size() == numberOfChildren)
				complete = true;
			
			if (complete && parent != null) parent.checkCompleteness();
			
			
//			boolean digit = true;
//			for (int i = 0; i < value.length(); i++) {
//				if (!(Character.isDigit(value.charAt(i)) || value.charAt(i) == '.'))
//					digit = false;
//			}
//			if (digit || value == "\"hello there\"" || value == "\"world\"")
//				complete = true;
//
//			if (children.size() == 2) {
//				for (Node child : children){
//					if (!child.isComplete())
//						return;
//				}
//				complete = true;
//			}
//
//			if (complete && parent != null) parent.checkCompleteness();
		}
	}

	public static void main(String[] args) throws Exception {
		ParseTree tree = new ParseTree();

//		tree.grow("add", 2);
//		tree.grow("5", 0);
		tree.grow("5", 0);
		System.out.println(tree.toString());
		System.out.println(tree.isComplete());
	}
}
