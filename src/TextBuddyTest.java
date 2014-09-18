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
	}
}
