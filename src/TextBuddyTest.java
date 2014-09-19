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
		assertEquals("Found 2 item(s):\n"
				+ "1. a quick brown fox\n"
				+ "3. how now brown cow",
				TextBuddy.handleCommand("search brown"));
	}
	@Test
	public void testSort(){
		assertEquals("simple sort with empty list", String.format("%s is empty",TEST_FILE_NAME),
				TextBuddy.handleCommand("sort"));
		TextBuddy.handleCommand("add a quick brown fox");
		TextBuddy.handleCommand("add jumps over a lazy dog");
		TextBuddy.handleCommand("add how now brown cow");
		TextBuddy.handleCommand("add black sheep wall");
		TextBuddy.handleCommand("add grass of green");
		TextBuddy.handleCommand("add grass of different colors");
		TextBuddy.handleCommand("add B for bee");
		assertEquals("simple sort with entries", "Sorted alphabetically",
				TextBuddy.handleCommand("sort"));
		assertEquals(7, TextBuddy.getLineCount());
		assertEquals("display test after sort", 
				"1. a quick brown fox\n"
				+ "2. B for bee\n"
				+ "3. black sheep wall\n"
				+ "4. grass of different colors\n"
				+ "5. grass of green\n"
				+ "6. how now brown cow\n"
				+ "7. jumps over a lazy dog",
				TextBuddy.handleCommand("display"));
		assertEquals("a quick brown fox\n"
				+ "B for bee\n"
				+ "black sheep wall\n"
				+ "grass of different colors\n"
				+ "grass of green\n"
				+ "how now brown cow\n"
				+ "jumps over a lazy dog\n",
				TextBuddy.getCurrentFileContent());
	}
}
