package org;

//jar tvf JFLAP.jar
import java.util.*;

public class Test {
	public static void main(String[] args) {
		String argument = "     ?     ";
		String noSpaces = argument.replace(' ', '\u0000');
		System.out.println(noSpaces + " " + noSpaces.length());
		boolean lengthOne = noSpaces.length() == 1;
		char firstChar = noSpaces.charAt(0);
	}//end main
}
