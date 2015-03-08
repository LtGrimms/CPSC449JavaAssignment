package tests;

import static org.junit.Assert.*;

import org.Arborist;
import org.ParseTree;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ArboristTest {
	
	private Arborist a;
	private ParseTree tree1;
	private ParseTree tree2;
	
	public ArboristTest() {
	}

	@Before
	public void setUp() throws Exception {
		a = new Arborist(/*input method data*/);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testBasicArgumentInteger() {
		tree1 = a.growTree("5");
		assertEquals("5", tree1.toString());
	}
	
	@Test
	public void testBasicArgumentFloat() {
		tree1 = a.growTree("5.");
		assertEquals("5.", tree1.toString());
	}
	
	@Test
	public void testBasicArgumentString() {
		tree1 = a.growTree("\"Hello\"");
		assertEquals("\"Hello\"", tree1.toString());
	}
	
	@Test
	public void testBasicArgumentStringTwoWords() {
		tree1 = a.growTree("\"Hello World\"");
		assertEquals("\"Hello World\"", tree1.toString());
	}
	
	@Test
	public void testSingleFunctionAddInt() {
		tree1 = a.growTree("(add 5 5)");
		assertEquals("add55", tree1.toString());
	}
	
	@Test
	public void testSingleFunctionAddFloat() {
		tree1 = a.growTree("(add 5 5.)");
		assertEquals("add55.", tree1.toString());
	}
	
	@Test
	public void testString() {
		tree1 = a.growTree("\"hello\"");
	}
	
	/* Wait until Information.java is finished to implement this
	@Test
	public void testSingleFunctionLength() {
		
	}
	*/

	@Test
	public void testCompositeFunctionAddMultMult() {
		tree1 = a.growTree("(add (mult 5 5) (mult 5 5))");
		assertEquals("addmult55mult55", tree1.toString());
	}
	
	@Test
	public void testCompositeFucntionAddMultAndArgs() {
		tree1 = a.growTree("(add (mult 5 5) 5)");
		assertEquals("addmult555", tree1.toString());
	}
	
	@Test
	public void testCompositeFunctionAddArgsAndMult() {
		tree1 = a.growTree("(add 5 (mult 5 5))");
		assertEquals("add5mult55", tree1.toString());
	}
	
	/* ERROR TESTING
	 */
	
	@Test(expected = Exception.class)
	public void testGiberish_01() {
		tree1 = a.growTree("afeafsd");
	}
	
	@Test(expected = Exception.class)
	public void testGiberish_02() {
		tree1 = a.growTree("5.asdfa");
	}
	
	@Test(expected = Exception.class)
	public void testAddWithNoArguments() {
		tree1 = a.growTree("(add)");
	}
	
	@Test(expected = Exception.class)
	public void testAddWithOneArgument() {
		tree1 = a.growTree("(add 5)");
	}
	
	@Test(expected = Exception.class)
	public void testAddWithThreeArguments() {
		tree1 = a.growTree("(add 5 5 5)");
	}
	
	@Test(expected = Exception.class)
	public void testMultipleArguments() {
		tree1 = a.growTree("5 5 5");
	}
	
	@Test(expected = Exception.class)
	public void testNoClosingBracketForSingleArgument() {
		tree1 = a.growTree("(add 5 5)");
	}
	
	@Test(expected = Exception.class)
	public void testNoClosingBracketForInnerArgument() {
		tree1 = a.growTree("(add (mult 5 5) 5");
	}
	
	@Test(expected = Exception.class)
	public void testNoOpeningBracketForAdd() {
		tree1 = a.growTree("add 5 5)");
	}
	
	@Test(expected = Exception.class)
	public void testNoOpeningBracketForInnerMult() {
		tree1 = a.growTree("(add mult 5 5) 5");
	}
	
	@Test(expected = Exception.class)
	public void testNoClosingQuote() {
		tree1 = a.growTree("\"hello");
	}
	
	@Test(expected = Exception.class)
	public void testNoOpeningQuote() {
		tree1 = a.growTree("hello\"");
	}
}
