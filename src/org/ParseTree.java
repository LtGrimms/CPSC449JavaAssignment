package org;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class ParseTree {
	
	private Node root;
	
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
	
	public String toString() {
		if (root == null)
			return "Empty Tree";
		
		String result = buildString(root);
		return result;
	}
	
	private String buildString(Node node) {
		String value = node.getValue();
		String append = "";
		
		Iterator<Node> iter = node.children.listIterator();
		while (iter.hasNext()) {
			append += buildString((Node) iter.next());
		}
		
		return value + append;
	}
	

	private class Node {
		private Node parent;
		private LinkedList<Node> children;
		private String value;
		private boolean complete = false;
		
		public Node(String st, Node parent) {
			this.parent = parent;
			value = st;
			children = new LinkedList<Node>();
			//this.checkCompleteness();
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
		
		private void checkCompleteness() {
			// Need to write this
			// depends on reflection data
			// check 2 things
			//  1. Does this node have the right number of children
			//  2. Are the children complete
			
			// the following is an ad-hoc implementation of checkCompleteness that allows each node to have only two children
			
			if (value == "2" || value == "5")
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
	
	public static void main(String[] args) {
		ParseTree tree = new ParseTree();
		
		tree.grow("'add'");
		tree.grow("5");
		System.out.println(tree.toString());
		System.out.println(tree.isComplete());
	}
}
