import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
	
/**
 * TextBuddy is used to manipulate text file using command lines.
 * You can add new line, delete a line, clear the whole file and 
 * display the lines in the file. The text file is stored on disk.
 * The command format is given by the example interaction below:
 
 Welcome to TextBuddy. mytextfile.txt is ready for use
 command: add little brown fox
 added to mytextfile.txt: "little brown fox"
 command: display
 1. little brown fox
 command: add jumped over the moon
 added to mytextfile.txt: "jumped over the moon"
 command: display
 1. little brown fox
 2. jumped over the moon
 command: delete 2
 deleted from mytextfile.txt: "jumped over the moon"
 command: display
 1. little brown fox
 command: clear
 all content deleted from mytextfile.txt
 command: display
 mytextfile.txt is empty
 command: exit
	
 * @author Nguyen Cao Nhat Linh
 */
public class TextBuddy {
	private static final String MSG_NO_ARGS = "No arguments!";
	private static final String MSG_WELCOME = "Welcome to TextBuddy. %s is ready for use";
	private static final String MSG_IO_OPERATION_FAILED = "I/O operation failed for %s. Please try again";
	private static final String MSG_CMD_PROMPT = "command: ";
	private static final String MSG_INVALID_CMD = "Invalid command";
	private static final String MSG_INVALID_PARAM = "Invalid parameters for %s command";
	private static final String MSG_ADD = "added to %s: \"%s\"";
	private static final String MSG_DELETE = "deleted from %s: \"%s\"";
	private static final String MSG_CLEAR = "all content deleted from %s";
	private static final String MSG_EMPTY_FILE = "%s is empty";
	private static final String MSG_SEARCH_NOT_FOUND = "%s not found";
	
	private static final String END_LINE = "\n";
	
	/*
	 *  This is used to indicate that the index for deleting the text line is invalid
	 *  (either not exist, less than 1 or is not a number) 
	 */
	private static final int INVALID_INDEX = -1;
	
	private static final String DISPLAY_FORMAT = "%d. %s";
	
	// This is used to indicate the position of parameter in the command line.
	private static final int PARAM_POSITION = 1;
	
	// This ArrayList is used to store the texts
	private static ArrayList<String> texts;
	// Scanner variable declared for the whole class
	private static Scanner scanner;
	// This File variable is used to represent the file on disk where texts are stored
	private static File file;
	
	// These are the possible command types
	private enum Command {
		ADD, DISPLAY, DELETE, CLEAR, SEARCH, EXIT, INVALID
	}
	
	public static void main(String args[]) {
		checkArguments(args);
		initializeEnvironment(args[0]);
		printWelcomeMessage(args[0]);
		acceptCommandsUntilExit(args);
	}
	
	private static void checkArguments(String args[]) {
		if (args.length == 0) {
			showToUser(MSG_NO_ARGS);
			System.exit(0);
		}
	}
	
	public static void initializeEnvironment(String filename) {
		initializeVariables(filename);
		readFromFile(file);
	}

	private static void initializeVariables(String filename) {
		texts = new ArrayList<String>();
		scanner = new Scanner(System.in);
		file = new File(filename);
	}
	
	private static void printWelcomeMessage(String filename) {
		showToUser(String.format(MSG_WELCOME, filename));
	}
	
	private static void acceptCommandsUntilExit(String args[]) {
		while (true) {
			System.out.print(MSG_CMD_PROMPT);
			String userCommand = getUserInput();
			String feedback = handleCommand(userCommand);
			if (feedback != null){
				showToUser(feedback);
			}
		}
	}

	private static void showToUser(String text){
		System.out.println(text);
	}

	private static void readFromFile(File file){
		BufferedReader br = null;
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			br = new BufferedReader(new FileReader(file));
			String newLine;
			while ((newLine = br.readLine()) != null) {
				texts.add(newLine);
			}
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
			showToUser(String.format(MSG_IO_OPERATION_FAILED,file.getName()));
			System.exit(-1);
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException ex) {
				showToUser(String.format(MSG_IO_OPERATION_FAILED,file.getName()));
				System.exit(-1);
			}
		}
		
	}
	
	private static String getUserInput() {
		//omit leading and trailing whitespace for later
		return scanner.nextLine().trim(); 
	}

	public static String handleCommand(String userCommand) {
		String commandString = getFirstWord(userCommand);
		Command command = parseInputForCommand(commandString);
		String parameter = extractParameter(userCommand);
		return executeCommand(command, parameter);
	}

	/**
	 * This operation executes the command with given parameter. 
	 * Returns the feedback string
	 * @param 	command		Command type
	 * @param	parameter	String type
	 * @return 				feedback message
	 */
	private static String executeCommand(Command command, String parameter) {
		switch (command) {
			case ADD:
				return addText(parameter);
			case DELETE:
				return deleteText(parameter);
			case DISPLAY:
				return displayTexts();
			case CLEAR:
				return clearTexts();
			case SEARCH:
				return searchTexts(parameter);
			case EXIT:
				System.exit(0);
			case INVALID:
				return MSG_INVALID_CMD;
			default:
				throw new Error("Unrecognized command");
				// this should never happen
		}
	}
	
	private static String searchTexts(String parameter) {
		String feedback;
		if (parameter == null) {
			feedback = String.format(MSG_INVALID_PARAM, "search");
		} else
		{
			feedback = String.format(MSG_SEARCH_NOT_FOUND, parameter);
		}
		return feedback;
	}

	private static void writeToFile() {
		try {
			BufferedWriter bw = new BufferedWriter(
					new FileWriter(file.getAbsoluteFile()));
			for (String line : texts) {
				bw.write(line);
				bw.newLine();
			}
			bw.close();
			
		} catch (IOException e) {
			showToUser(MSG_IO_OPERATION_FAILED);
			System.exit(-1);
		}
			
	}

	/**
	 * This operation clears all texts in the file and
	 *	@return	feedback message to inform texts have been clear
	 */
	private static String clearTexts() {
		texts.clear();
		writeToFile();
		return String.format(MSG_CLEAR, file.getName()); //feedback
	}

	/**
	 * This operation displays texts in texts ArrayList OR
	 * returns empty file message if file is empty. 
	 * @return	String type		feedback message if file empty
	 * 			String type		texts' content if file is not empty
	 */
	private static String displayTexts() {
		String textsContent = "";
		if (texts.isEmpty()) {
			return String.format(MSG_EMPTY_FILE,file.getName()); //feedback
		} else {
			for (int i = 0; i < texts.size(); i++) {
				String line = texts.get(i);
				textsContent = textsContent + String.format(DISPLAY_FORMAT,i+1,line);
				if (i != texts.size()-1) {
					textsContent += END_LINE;
				}
				//display texts line by line
			}
			return textsContent;
		}
	}
	
	/**
	 * This operation deletes selected line from texts and writes to file.
	 * @param 	parameter
	 * @return 	feedback message:
	 * 	could be deleted successful message,
	 *  or invalid parameter warning parameters are not number
	 *  in valid range [0..size-1]
	 */
	private static String deleteText(String parameter) {
		String feedback;
		int index = convertToIndex(parameter);
		if (index == INVALID_INDEX) {
			feedback = String.format(MSG_INVALID_PARAM, "delete");
		} else {
			String deletedLine = texts.remove(index);
			writeToFile();
			feedback = String.format(MSG_DELETE, file.getName(), deletedLine);
		}
		return feedback;
	}

	/**
	 * This operation converts parameter in String type to index in Integer type.
	 * Returns INVALID_INDEX if the index is out of bound or the parameter
	 * is not a number for deleting text line.
	 * @param 	parameter	String type
	 * @return 	index	integer type
	 */
	private static int convertToIndex(String parameter) {
		try {
			int index = Integer.parseInt(parameter)-1;
			return isValidIndex(index) ? index : INVALID_INDEX;
		} catch (NumberFormatException nfe) {
			return INVALID_INDEX;
		}
	}

	/**
	 * This operation checks if the index is inside range [0..size-1]
	 * @param 	index	the index to check
	 * @return			true or false
	 */
	private static boolean isValidIndex(int index) {
		return (index<texts.size()) && (index>=0);
	}

	/**
	 * This operation adds text line to texts ArrayList and writes to file
	
	 * @param 	parameter
	 * @return 	feedback message:
	 * 	could be added successful message,
	 * 	or invalid parameter warning if no parameters entered.	
	 */
	private static String addText(String parameter) {
		String feedback;
		if (parameter != null) {	
			texts.add(parameter);
			writeToFile();
			feedback = String.format(MSG_ADD,file.getName(),parameter);
		}
		else {
			feedback = String.format(MSG_INVALID_PARAM, "add");
		}
		return feedback;
	}

	private static String extractParameter(String userCommand) {
		// words[0] is the command and [1] is the parameter
		String words[] = splitParameterFromUserCommand(userCommand);
		if (words.length == 1) {
			return null;
		} else {
			return words[PARAM_POSITION];
		}
		
	}

	private static String[] splitParameterFromUserCommand(String userCommand) {
		return userCommand.split(" ", 2); // split into only two parts
	}

	private static String getFirstWord(String userCommand) {
		return userCommand.split(" ")[0]; 
	}
	
	/**
	 * This operation determines which of the supported command types the user
	 * wants to perform
	 * @param 	commandString	the first word of the user command
	 * @return	command			Command type
	 */
	private static Command parseInputForCommand(String commandString) {
		if (commandString.equalsIgnoreCase("add")) {
			return Command.ADD;
		} else if (commandString.equalsIgnoreCase("display")) {
			return Command.DISPLAY;
		} else if (commandString.equalsIgnoreCase("delete")) {
			return Command.DELETE;
		} else if (commandString.equalsIgnoreCase("clear")) {
			return Command.CLEAR;
		} else if (commandString.equalsIgnoreCase("search")) {
			return Command.SEARCH;
		} else if (commandString.equalsIgnoreCase("exit")) {
			return Command.EXIT;
		} else return Command.INVALID;
	}

	public static int getLineCount() {
		BufferedReader br = null;
		int lineCount = 0;
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			br = new BufferedReader(new FileReader(file));
			while (br.readLine() != null) {
				lineCount++;
			}
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
			showToUser(String.format(MSG_IO_OPERATION_FAILED,file.getName()));
			System.exit(-1);
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException ex) {
				showToUser(String.format(MSG_IO_OPERATION_FAILED,file.getName()));
				System.exit(-1);
			}
		}
		return lineCount;
	}
	
	public static String getCurrentFileContent() {
		BufferedReader br = null;
		String fileContent = "";
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			br = new BufferedReader(new FileReader(file));
			String lineRead;
			while ((lineRead = br.readLine()) != null) {
				fileContent+=lineRead+"\n";
			}
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
			showToUser(String.format(MSG_IO_OPERATION_FAILED,file.getName()));
			System.exit(-1);
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException ex) {
				showToUser(String.format(MSG_IO_OPERATION_FAILED,file.getName()));
				System.exit(-1);
			}
		}
		return fileContent;
	}
}
