package tests;

public class Methods04 {
  public static Integer add(int x, int y) {
    return x + y;
  }

  public static Float add(float x, float y) {
    return x + y;
  }

  public static int mult(Integer x, Integer y) {
    return x * y;
  }

  public static float mult(Float x, Float y) {
    return x * y;
  }

  public static int length(String arg) {
    return arg.length();
  }

  public static String charAt(String arg, int index) {
    return "" + arg.charAt(index);
  }
}
