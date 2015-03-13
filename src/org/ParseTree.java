package org;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import utils.ParsingUtils;

public class ParseTree {
	
	private Node root;
	private Information info;
	
	public ParseTree() {
		root = null;
	}
	
	public void grow(String st) {
		if (root == null)   {                    // add to empty tree
			root = new Node(st, null);
			root.checkCompleteness();
		}
		else
			grow(st, root);
	}
	
	private void grow(String st, Node node) {
		if (node.isComplete())
			throw new IllegalArgumentException(); // too many arguments to root node
		
		boolean grown = false;
		
		if (node.children.isEmpty()) {
			node.addChild(st);
			grown = true;
		} else {
			Iterator<Node> iter = node.children.listIterator();
			while (iter.hasNext()) {
				Node child = (Node) iter.next();
				if (!child.isComplete()) {
					grow(st, child);
					grown = true;
				}
			}
		}
		
		if (!grown) {
			node.addChild(st);
		}
		
	}
	
	public boolean isComplete() {
		return root.isComplete();
	}
	
	public void addReturnTypes() throws Exception {
		if (!isComplete())
			throw new Exception("tried to add return types to an incomplete tree");
	
		addReturnType(root);
	}
	
	private int addReturnType(Node node) throws Exception {
		
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
	
	public String toString() {
		if (root == null)
			return "Empty Tree";
		
		String result = buildString(root);
		return result;
	}
	
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
	
	private static int returnType(String arg) throws Exception {
		if (arg.charAt(0) == '\"')
			return 1;
		else if (/* info.checkForFunction(arg) */ arg == "add")  //check for function throws exception
			return 1000;
		else return ParsingUtils.intOrFloat(arg, 0);
	}
	

	protected class Node {
		private Node parent;
		private LinkedList<Node> children;
		private String value;
		private int returnType;
		private boolean complete = false;
		
		public Node(String st, Node parent) {
			this.parent = parent;
			value = st;
			children = new LinkedList<Node>();
			//this.checkCompleteness();
		}
		
		public void setReturnType(int type) {
			returnType = type;
		}
		
		public int getReturnType() {
			return returnType;
		}
		
		public void addChild(String st) {
			Node child = new Node(st, this);
			children.add(child);
			child.checkCompleteness();
		}
		
		public boolean isComplete() {
			return complete;
		}
		
		public String getValue() {
			return value;
		}
		
		public void checkCompleteness() {
			// Need to write this
			// depends on reflection data
			// check 2 things
			//  1. Does this node have the right number of children
			//  2. Are the children complete
			
			// the following is an ad-hoc implementation of checkCompleteness that allows each node to have only two children
			
			boolean digit = true;
			for (int i = 0; i < value.length(); i++) {
				if (!(Character.isDigit(value.charAt(i)) || value.charAt(i) == '.'))
						digit = false;
			}
			if (digit || value == "\"hello there\"" || value == "\"world\"")
				complete = true;
			
			if (children.size() == 2) {
				for (Node child : children){
					if (!child.isComplete())
						return;
				}
				complete = true;
			}
			
			if (complete && parent != null) parent.checkCompleteness();
		}
	}
	
	public static void main(String[] args) throws Exception {
		ParseTree tree = new ParseTree();
		
		String s1 = "add";
		String s2 = "5";
		String s3 = "5";
		String[] array = {s1, s2, s3};
		for (int i = 0; i < array.length; i++)
			tree.grow(array[i]);
//		tree.grow("add");
//		tree.grow("5");
//		tree.grow("5");
		tree.addReturnTypes();
		System.out.println(tree.toString());
	}
}
