//jar tvf JFLAP.jar
import java.util.*;

public class Test {
	public static void main(String[] args) throws Exception{
		int maxSize = 0;
		Information information = new Information("/home/mr-fool/Documents/JFLAP.jar","org.apache.batik.transcoder.TranscoderInput");
		information.printFunctions();
		//information.checkForFunction("attributeIsFalse");
		List<List<Integer>> methodList = new ArrayList<List<Integer>>(); 
		methodList = information.properArguments("getURI");
		/*for(Integer tmp: methodList) { 
			System.out.println(tmp);
		}*///end-for
		for (int i = 0; i < maxSize; i++) {
			for (List<Integer> innerList : methodList) {
        //You don't need this if all lists are the same length.
			if (i > innerList.size() - 1) { 
				System.out.print("x ");
		} else {
			System.out.print(innerList.get(i) + " ");
        }
    }
    System.out.println(); //new line for the next row
}
	}//end main
}
