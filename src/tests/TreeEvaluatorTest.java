package tests;

import static org.junit.Assert.*;

import org.ParseTree;
import org.TreeEvaluator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TreeEvaluatorTest {

	private ParseTree tree;
	private TreeEvaluator eval;
	
	public TreeEvaluatorTest() {
	}

	@Before
	public void setUp() throws Exception {
		tree = new ParseTree();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test_1_SimpleRootNode() {
		fail("not implemented");
	}

}
