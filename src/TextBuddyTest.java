import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;


public class TextBuddyTest {
	private static final String TEST_FILE_NAME = "testfile.txt";
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		TextBuddy.initializeEnvironment(TEST_FILE_NAME);
	}

	@Test
	public void testAdd() {
		fail("Not yet implemented");
	}

}
