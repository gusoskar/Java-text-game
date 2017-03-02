package kolanen;

public class Command {
	private String commandWord;
	private String word2;

	public Command(String word1, String word2) {
		commandWord = word1;
		this.word2 = word2;
	}

	public String getCommandWord() {
		return commandWord;
	}

	public String getWord2() {
		return word2;
	}

	public boolean isUnknown() {
		return (commandWord == null);
	}

	public boolean hasWord2() {
		return (word2 != null);
	}
}