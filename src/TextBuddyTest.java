import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class TextBuddyTest {
	private static final String TEST_FILE_NAME = "testfile.txt";
	
	@Before
	public void setUpBefore() throws Exception {
		TextBuddy.initializeEnvironment(TEST_FILE_NAME);
		TextBuddy.handleCommand("clear");
	}

	@Test
	public void testAdd() {
		assertEquals("simple add", String.format("added to %s: \"abc\"", TEST_FILE_NAME),
				TextBuddy.handleCommand("add abc"));
		assertEquals("simple add without text", "Invalid parameters for add command",
				TextBuddy.handleCommand("add"));
		assertEquals(1, TextBuddy.getLineCount());
		assertEquals("abc\n",TextBuddy.getCurrentFileContent());
	}
	
	@Test
	public void testDelete() {
		TextBuddy.handleCommand("add abc");
		assertEquals("simple delete", String.format("deleted from %s: \"abc\"",TEST_FILE_NAME),
				TextBuddy.handleCommand("delete 1"));
		assertEquals("simple delete with non-existent index", "Invalid parameters for delete command",
				TextBuddy.handleCommand("delete 1"));
		assertEquals(0, TextBuddy.getLineCount());
		assertEquals("",TextBuddy.getCurrentFileContent());
	}
	
	@Test
	public void testDisplay() {
		//empty list
		assertEquals("simple display with empty list", String.format("%s is empty",TEST_FILE_NAME),
				TextBuddy.handleCommand("display"));
		//list with entries
		TextBuddy.handleCommand("add a quick brown fox");
		TextBuddy.handleCommand("add jumps over a lazy dog");
		assertEquals(2, TextBuddy.getLineCount());
		assertEquals("a quick brown fox\n"
				+ "jumps over a lazy dog\n",
				TextBuddy.getCurrentFileContent());
		assertEquals("simple display", 
				  "1. a quick brown fox\n"
				+ "2. jumps over a lazy dog",
				TextBuddy.handleCommand("display"));
		TextBuddy.handleCommand("add how now brown cow");
	}
	
	@Test
	public void testSearch() {
		TextBuddy.handleCommand("add a quick brown fox");
		TextBuddy.handleCommand("add jumps over a lazy dog");
		TextBuddy.handleCommand("add how now brown cow");
		//invalid parameter
		assertEquals("Invalid parameters for search command",TextBuddy.handleCommand("search"));
		assertEquals("abc not found",TextBuddy.handleCommand("search abc"));
	}
}
