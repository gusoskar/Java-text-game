package kolanen;
public class CommandWords {
	private static final String[] validCommands = {
		"enter", "quit", "help", "take", "use", "defuse","inventory","save","load"
	};

	public CommandWords(){

	}
	public boolean isCommand(String aString)
	{
		for(int i = 0; i < validCommands.length; i++) {
			if(validCommands[i].equals(aString))
				return true;
		}

		return false;
	}
	public String showAll()
	{
		System.out.println("Here are all valid commands: ");
		 {
		        String allCommands = "Commands: ";
		        for(String command : validCommands) {
		            allCommands += command + " ";
		            System.out.println(command + " ");
		        }return allCommands;
	}
}
	}