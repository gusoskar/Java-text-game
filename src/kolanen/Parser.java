package kolanen;

import java.util.*;

public class Parser {
	private CommandWords commands;
	private Scanner reader;
	private String enterWord = null;
	private boolean containPoison = false;

	public Parser() {
		commands = new CommandWords();
		reader = new Scanner(System.in);
	}

	public Command getCommand() {
		String inputLine;
		String word1 = null;
		String word2 = null;
		// String word3=null;
		System.out.print("\n> ");

		inputLine = reader.nextLine().toLowerCase();
		enterWord = inputLine;
		Scanner tokenizer = new Scanner(inputLine);
		if (tokenizer.hasNext()) {
			word1 = tokenizer.next(); // gets first word
			if (tokenizer.hasNext()) {
				word2 = tokenizer.next(); // gets second word if first is found
			}
			if (word1.equals("enter")) {
				word2 = inputLine.replaceAll("enter ", "").replaceAll("the ",
						"");
			}
			if (word1.equals("defuse")) {
				word2 = inputLine.replaceAll("defuse ", "").replaceAll("the ",
						"");
			}
			if (word1.equals("take")) {
				word2 = inputLine.replaceAll("take ", "")
						.replaceAll("the ", "");
			}
			if (word1.equals("use") && word2 != null) {
				word2 = inputLine.replaceAll("use ", "").replaceAll(" on ", "")
						.replaceAll("the ", "").replaceAll(" on", "")
						.replaceAll(" ", "");
			}

		}
		if (commands.isCommand(word1)) {
			return new Command(word1, word2); // if word is known create a
												// command with it

		} else {
			return new Command(null, word2); // if word not known create a
												// "null" command
		}
	}

	public String getEnterWord() {
		return this.enterWord;
	}

	public void showCommands() {
		System.out.println("COMMAND MENU:\n\n"
				+ "Move between locations	(enter X)\n"
				+ "Take an item		(take X)\n" + "Use an item		(use X on Y)\n"
				+ "Check inventory		(inventory)\n" + "Save game		(save)\n"
				+ "Load game		(load)\n" + "Quit game		(quit)");

	}
}
