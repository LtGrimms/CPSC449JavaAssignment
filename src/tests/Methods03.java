package tests;

public class Methods03 {
	public static int divide(int x, int y) {
		return x / y;
	}  // Divide by zero error
	
	public static float divide(float x, float y) {
		float fx = (float) x;
		float fy = (float) y;
		return fx / fy;
	}  // Divide by zero error
	
	public static void exception() throws Exception {
		throw new Exception("This is a malicios method");
	}
}
