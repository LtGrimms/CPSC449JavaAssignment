//jar tvf JFLAP.jar
import java.util.*;

public class Test {
	public static void main(String[] args) throws Exception{
		Information information = new Information("/home/mr-fool/Documents/JFLAP.jar","org.apache.batik.bridge.PaintBridge");
		information.printFunctions();
		information.checkForFunction("fuck");
		ArrayList<Integer> methodList = new ArrayList<Integer>();
		methodList = information.properArguments("createPaint");
		for(Integer tmp: methodList) { 
			System.out.println(tmp);
		}//end-for
	}//end main
}
