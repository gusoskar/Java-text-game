package kolanen;

import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

public class GameController implements Serializable {
	private static final long serialVersionUID = 1L;

	private Parser parser;
	private Player player1;
	private HashMap<String, Car> cars;
	private Scanner reader;
	private Car currentLocation;
	private Art art = new Art();
	private int i;
	private WallOfText textWall = new WallOfText();
	private boolean dead = false;
	private boolean bombDefuse = false;
	private boolean bombDefuseFail = false;
	private boolean gameStart = false;
	private boolean addedMedicine = false;
	private boolean medicineDelivered = false;
	private boolean poisonUsed = false;

	public GameController() {
		parser = new Parser();
		cars = new HashMap<String, Car>();
		cars = createCars();
	}

	public void play() throws Exception {

		printWelcome();

		player1 = new Player("JANUS", getCar("Station"));

		while (gameStart == false) {

			Command commandMenu = parser.getCommand();

			if (parser.getEnterWord().equals("start")) {
				gameStart = true;
				currentLocation = player1.getLocation();
				System.out.println(player1.getLocation().getDesc());
				Item stationItems = getCar("Station").getItemValue("ticket");
				player1.addToInventoryStation(stationItems);
				i--;
				// System.out.println(player1.getLocation().getName())
			} else if (parser.getEnterWord().equals("load")) {
				gameStart = true;
				loadGame();
				System.out.println(player1.getLocation().getDesc());
				String carItems = player1.getLocation().getItems();
			} else if (parser.getEnterWord().equals("quit")) {
				System.out.println("Thanks for playing our game.");
				System.exit(0);
			} else if (parser.getEnterWord().equals("help")) {
				printHelp();
			} else if (parser.getEnterWord().equals("credits")) {
				printCredits();
			} else {
				System.out.println("Can't do that in the game menu.");
			}
		}
		boolean finished = false;

		while (finished == false && bombDefuse == false
				&& bombDefuseFail == false && dead == false) {

			i++;
			Command command = parser.getCommand();
			finished = processCommand(command);

		}

		if (finished == true) {
			System.out.println("Bye Bye!!");
			printCredits();
		}

		else if (bombDefuse == true) {
			System.out
					.println("Congratulation!\n"
							+ "You've defused the bomb and saved the people on this train!\n"
							+ "The heroism of your deeds shall be celebrated for a thousand years!");
		} else if (bombDefuseFail == true) {
			System.out
					.println("That was a good quess but not the right answer.\n"
							+ "You've just chosen the wrong wire and blown all the passangers into OBLIVION.\n"
							+ "GAME OVER");
		}
	}

	private boolean processCommand(Command command) {
		boolean quits = false;

		if (command.isUnknown()) {
			System.out.println("I don't know how to " + parser.getEnterWord());
			i--;
			return false;
		}
		String commandWord = command.getCommandWord();

		if (commandWord.equals("help")) {
			printHelp();
			i--;
		} else if (commandWord.equals("enter")) {
			enterCar(command);
		} else if (commandWord.equals("use")) {
			useObject(command);
		} else if (commandWord.equals("inventory")) {
			System.out.println(player1.getInventory());
			i--;
		} else if (commandWord.equals("take")) {
			takeItem(command);
		} else if (commandWord.equals("quit")) {
			System.out.println("You have entered " + i
					+ " commands during this game.");
			i--;
			saveGame();
			quits = quit(command);
		} else if (commandWord.equals("save")) {

			System.out.println("saving...");
			saveGame();
			System.out.println("game saved");
			i--;

		} else if (commandWord.equals("load")) {

			System.out.println("loading...");
			loadGame();
			System.out.println("game loaded");
			System.out.println(player1.getLocation().getDesc());
			String carItems = player1.getLocation().getItems();
			i--;
		} else if (commandWord.equals("defuse")) {

			defuse(command);
		}

		return quits;

	}

	private boolean defuse(Command command) {

		bombDefuse = false;
		bombDefuseFail = false;
		String item = command.getWord2();
		// System.out.println(item);
		// System.out.println(command.getCommandWord());

		if (!command.hasWord2() || command.getWord2().equals("")
				|| !command.getWord2().equals("bomb")) {
			// command.getWord2().equals("defuse")||
			System.out.println("Defuse what?");
			i--;
			// return;
		}

		else if (item.equals("bomb")
				&& player1.getLocation().getName().equals("CarXa")) {

			System.out.println(textWall.defuse());

			// && player1.getLocation().equals(getCar("CarX"))

			while (bombDefuse == false || bombDefuseFail == false) {
				// player1.setLocation(getCar("CarXa"));
				// System.out.println(player1.getLocation().getDesc());
				System.out.println("Pick a wire, the options are:\n"
						+ "RED, GREEN, and BLUE\n");
				Command commandDefuse = parser.getCommand();

				if (parser.getEnterWord().equals("red")) {
					// System.out.println("You have cut the red wire");
					bombDefuseFail = true;
					return bombDefuseFail;
					// boolean red=red(command);
					// return red;
				} else if (parser.getEnterWord().equals("green")) {
					// System.out.println("You have cut the green wire");
					bombDefuse = true;
					return bombDefuse;
					// boolean green=green(command);
					// return green;
				} else if (parser.getEnterWord().equals("blue")) {
					// System.out.println("You have cut the blue wire");
					bombDefuseFail = true;
					return bombDefuseFail;
					// boolean blue=blue(command);
					// return blue;
				} else {
					System.out.println("Pick a wire to defuse the bomb!\n");
					i--;
				}
			}
		} else {
			System.out.println("Sorry, no bomb here.");
		}
		return false;
	}

	private void saveGame() {
		try {
			FileOutputStream out = new FileOutputStream("gamesave.ser");
			ObjectOutputStream obout = new ObjectOutputStream(out);
			obout.writeObject(player1);
			obout.close();
		} catch (FileNotFoundException e) {
			System.out.println("Could not open gamesave.ser");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error writing into file");
			e.printStackTrace();
		}
	}

	private void loadGame() {
		try {
			FileInputStream in = new FileInputStream("gamesave.ser");
			ObjectInputStream obin = new ObjectInputStream(in);
			player1 = (Player) obin.readObject();
			obin.close();
		} catch (FileNotFoundException e) {
			System.out.println("Could not open gamesave.ser");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error reading file");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("Error reading object");
			e.printStackTrace();
		}
	}

	private void useObject(Command command) {

		boolean containsItem = false;
		String useLocation = null;
		String item = "";
		if (command.hasWord2()) {
			item = command.getWord2();
		}

		// System.out.println(item);
		String item2 = "";

		if (item.equals("poisonvent")) {
			item2 = "poison";
		}
		if (item.equals("chairdoor") || item.equals("chairtoilet")) {
			item2 = "chair";
		}
		if (item.equals("gunwindow")) {
			item2 = "gun";
		}
		if (item.equals("inventionmecenaries") || item.equals("invention")) {
			item2 = "invention";
		}
		boolean containsItem2 = player1.getFromInventory(item);
		boolean containsItemReal = player1.getFromInventory(item2);

		if (!command.hasWord2() || command.getWord2().equals("use")
				|| command.getWord2().equals("")) {
			System.out.println("Use what?");
			i--;
			return;
		} else if (containsItemReal == false) {
			System.out
					.println("You don't have the correct item for that action!");
		} else if (item.equals("ticket") || item.equals("poison")
				|| item.equals("chair") || item.equals("gun")) {
			System.out.println("Use " + item + " on what?");
		} else {

			useLocation = player1.getLocation().getName();

			// THIS IS FOR CAR 2 POISON

			if (command.getWord2().equals("poisonvent")
					&& useLocation.equals("Car2b")) {

				containsItem = player1.getFromInventory("poison");

				if (containsItem == true) {
					Car useNextCar = getCar("Car2c");
					Item itemValue = getCar("Cabin5").getItemValue(item2);
					player1.removeFromInventory(itemValue);
					player1.setLocation(useNextCar);
					System.out.println("Hopefully that did the job!");
					poisonUsed = true;
				}
			}
			// THIS IS FOR CAR 4 CHAIR
			if (command.getWord2().equals("chairdoor")
					|| command.getWord2().equals("chairtoilet")
					&& useLocation.equals("Car4")) {
				containsItem = player1.getFromInventory("chair");
				if (containsItem == true) {
					Car useNextCar = getCar("Car4a");
					Item itemValue = getCar("Car4").getItemValue(item2);
					player1.removeFromInventory(itemValue);
					player1.setLocation(useNextCar);
					System.out.println(useNextCar.getDesc());
				}
			}
			// THIS IS FOR CAR 4 WINDOW
			if (command.getWord2().equals("gunwindow")
					&& useLocation.equals("Car4a")) {
				containsItem = player1.getFromInventory("gun");

				if (containsItem == true) {
					Car useNextCar = getCar("Car4b");
					Item itemValue = getCar("Car4").getItemValue(item2);
					player1.removeFromInventory(itemValue);
					player1.setLocation(useNextCar);
					System.out
							.println("After shooting the window, your gun jammed!\n"
									+ "You don't know how to fix the problem so you just leave it behind.");

					System.out.println(useNextCar.getDesc());
				}
			}
			if (command.getWord2().equals("inventionmercenaries")
					|| command.getWord2().equals("invention")
					&& useLocation.equals("CarX")) {

				Car useNextCar = getCar("CarXa");
				Item itemValue = getCar("Car9").getItemValue(item2);
				player1.removeFromInventory(itemValue);
				player1.setLocation(useNextCar);
				System.out.println(useNextCar.getDesc());

			}
		}
	}

	private void takeItem(Command command) {
		if (!command.hasWord2() || command.getWord2().equals("take")
				|| command.getWord2().equals("")) {
			System.out.println("Take what?");
			i--;
			return;
		}

		String object = command.getWord2();
		Item carItemValue = player1.getLocation().getItemValue(object);
		String locationItems = player1.getLocation().getItems();

		String car3Photo = getCar("Car3").getItems();
		String toiletKey = getCar("Toilet").getItems();
		String invention = getCar("Car9").getItems();
		// System.out.println(car3Photo+" "+car3PhotoValue);

		if (!object.equals(locationItems)) {
			System.out.println("There is no item called " + object + "!");
		} else if (object.equals(car3Photo)) {
			Item car3PhotoValue = getCar("Car3").getItemValue(car3Photo);
			player1.addToInventory(car3PhotoValue);
			player1.getLocation().removeItem(
					player1.getLocation().getItemValue("photo"));
			System.out.println(textWall.car3a());

		} else if (object.equals(toiletKey)) {

			Item toiletKeyValue = getCar("Toilet").getItemValue(toiletKey);
			player1.addToInventory(toiletKeyValue);
			player1.getLocation().removeItem(
					player1.getLocation().getItemValue("key"));
			player1.setLocation(getCar("ToiletA"));
		} else if (object.equals(invention)) {

			Item inventionValue = getCar("Car9").getItemValue(invention);
			player1.addToInventory(inventionValue);
			player1.getLocation().removeItem(
					player1.getLocation().getItemValue("invention"));
			player1.setLocation(getCar("Car9a"));
		}

		else {
			player1.addToInventory(carItemValue);
			player1.getLocation().removeItem(
					player1.getLocation().getItemValue(object));
		}
	}

	private void enterCar(Command command) {
		boolean containsItem = false;

		if (!command.hasWord2() || command.getWord2().equals("enter")
				|| command.getWord2().equals("")) {
			System.out.println("Enter Where?");
			i--;
			return;
		}
		String direction = command.getWord2();

		Car nextCar = player1.getLocation().getExit(direction);

		if (nextCar == null) {
			System.out.println("I can't " + parser.getEnterWord() + ".");
			i--;
		} else {

			// System.out.println(nextCar+" "+nextCar.getName());
			// System.out.println(getCar("Death")+" "+getCar("Death").getName());

			if (nextCar.getName().equals(getCar("Death").getName())) {
				System.out.println("You died horribly!\n" + "GAME OVER");
				dead = true;
			}
			if (nextCar.getName().equals(getCar("Car3k").getName())
					&& poisonUsed == false) {
				System.out.println("You died terribly!\n" + "GAME OVER");
				dead = true;
			}
			if (nextCar.getName().equals(getCar("RoofDeath").getName())) {
				System.out.println("You fell off the train and DIED!\n\n"
						+ "Loading Checkpoint...\n");
				loadGame();
				System.out.println(player1.getLocation().getDesc());
			} else if (nextCar.equals(getCar("Car3k")) && poisonUsed == true) {
				player1.setLocation(getCar("Car3"));
				System.out.println(player1.getLocation().getDesc());
				Item car3Items = getCar("Car3a").getItemValue("gun");
				System.out.println(getCar("Car3").getItemValue("photo")
						.getItemPlace());
				player1.addToInventoryStation(car3Items);

			}

			else if (nextCar.equals(getCar("Car3"))) {
				player1.setLocation(nextCar);
				System.out.println(player1.getLocation().getDesc());
				Item car3Items = getCar("Car3a").getItemValue("gun");
				System.out.println(getCar("Car3").getItemValue("photo")
						.getItemPlace());
				player1.addToInventoryStation(car3Items);

			} else if (nextCar.equals(getCar("Cabin1"))) {
				player1.setLocation(nextCar);
				System.out.println(player1.getLocation().getDesc());
				player1.setLocation(getCar("Car1"));
				System.out.println(player1.getLocation().getDesc());
			} else if (nextCar.equals(getCar("ToiletB")) && poisonUsed == true
					&& medicineDelivered == false) {
				medicineDelivered = true;
				player1.setLocation(getCar("ToiletB"));
				Item medicineI = getCar("Cabin5a").getItemValue("medicine");
				player1.removeFromInventory(medicineI);
				System.out.println(player1.getLocation().getDesc());
				player1.setLocation(getCar("ToiletE"));

			} else if (nextCar.equals(getCar("ToiletB")) && poisonUsed == true
					&& medicineDelivered == true) {
				player1.setLocation(getCar("ToiletC"));
				System.out.println(player1.getLocation().getDesc());
				player1.setLocation(getCar("ToiletE"));
			} else if (nextCar.equals(getCar("ToiletB"))
					&& medicineDelivered == true) {
				player1.setLocation(getCar("ToiletC"));
				System.out.println(player1.getLocation().getDesc());
			} else if (nextCar.equals(getCar("Cabin5")) && poisonUsed == true) {
				player1.setLocation(getCar("Cabin5b"));
				System.out.println(player1.getLocation().getDesc());
			} else if (nextCar.equals(getCar("Roof"))) {
				player1.setLocation(getCar("Roof"));
				System.out.println(player1.getLocation().getDesc());
				saveGame();
				System.out.println("\nCHECKPOINT REACHED");

			}

			else {

				if (nextCar.equals(getCar("Cabin5"))) {
					boolean containsMedicine = player1
							.getFromInventory("medicine");
					if (containsMedicine == true) {
						containsItem = true;
					}
					if (containsItem == false && medicineDelivered == false) {
						// System.out.println(textWall.cabin5a());
						Item medicineValue = getCar("Cabin5a").getItemValue(
								"medicine");
						player1.addToInventoryStation(medicineValue);
						player1.getLocation().removeItem(
								player1.getLocation().getItemValue("medicine"));
						addedMedicine = true;
					}
				}

				if (nextCar.equals(getCar("ToiletB"))) {
					medicineDelivered = true;
					Item medicineI = getCar("Cabin5a").getItemValue("medicine");
					player1.removeFromInventory(medicineI);

				}

				player1.setLocation(nextCar);
				Car currentLoc = player1.getLocation();
				System.out.println(player1.getLocation().getDesc());
				String carItems = player1.getLocation().getItems();

				if (addedMedicine == true && nextCar.equals(getCar("Cabin5"))
						&& medicineDelivered == false) {
					System.out.println(textWall.cabin5a());
					addedMedicine = false;
				}
				if (carItems != ""
						&& !currentLoc.getName().equals(
								getCar("Toilet").getName())
						&& !currentLoc.equals(getCar("Car4"))
						&& !currentLoc.getName().equals(
								getCar("Car9").getName())) {

					Item itemiValue = player1.getLocation().getItemValue(
							carItems);
					String place = itemiValue.getItemPlace();
					System.out.println(place);
					String nimi = itemiValue.getDescription();
					System.out.println(nimi);
				}
			}
		}
	}

	public void printWelcome() {
		art.train();
		System.out.println("Type START to play the game.");
		System.out.println("Type LOAD to continue your game.");
		System.out.println("Type HELP to learn the commands.");
		System.out.println("Type QUIT to be a coward.");
		System.out.println("Type CREDITS to see the creators.");
		System.out.println("");

	}

	private void printHelp() {
		parser.showCommands();
	}

	private boolean quit(Command command) {
		if (command.hasWord2()) {
			System.out.println("Just type 'quit' if you wish to quit the game");
			i--;
			return false;
		} else {
			return true; // signal that we want to quit
		}
	}

	public Car getCar(String name) {
		return cars.get(name);
	}

	public void addCars(String name, String description) {
		cars.put(name, new Car(name, description));
	}

	public HashMap<String, Car> createCars() {

		Car car;
		// Creating all the rooms
		addCars("Station", textWall.station());
		addCars("Car1", textWall.car1());
		addCars("Car1a", textWall.car1a());
		addCars("Cabin1", textWall.cabin1());
		addCars("Cabin2", textWall.cabin2());
		addCars("Cabin2a", textWall.cabin2a());
		addCars("Closet", textWall.closet());
		addCars("Death", "");
		addCars("Car2", textWall.car2());
		addCars("Car2a", textWall.car2a());
		addCars("Car2b", textWall.car2b());
		addCars("Car2c", textWall.car2c());
		addCars("Cabin3", textWall.cabin3());
		addCars("Cabin4", textWall.cabin4());
		addCars("Cabin5a", "");
		addCars("Cabin5b", textWall.cabin5());
		addCars("Cabin5", textWall.cabin5());
		addCars("Toilet", textWall.toilet());
		addCars("ToiletA", textWall.toiletA());
		addCars("ToiletB", textWall.toiletB());
		addCars("ToiletC", textWall.toiletC());
		addCars("ToiletD", textWall.toiletD());
		addCars("ToiletE", "");
		addCars("Car3k", "");
		addCars("RoofDeath", "");
		addCars("Death", "");
		addCars("Car3", textWall.car3());
		addCars("Car3a", textWall.car3a());
		addCars("Car4", textWall.car4());
		addCars("Car4a", textWall.car4a());
		addCars("Car4b", textWall.car4b());
		addCars("Roof", textWall.roof());
		addCars("Car9", textWall.car9());
		addCars("Car9a", "");
		addCars("CarX", textWall.carX());
		addCars("CarXa", textWall.carXa());

		// INTRO
		car = getCar("Station");
		car.addItem(new Item("ticket", "Ticket says car 1, cabin 2", "-"));
		car.addItemValue(getCar("Station").getItems(), 0);
		car.setExits("car 1", getCar("Car1"));
		car.setExits("car1", getCar("Car1"));
		car.setExits("train", getCar("Car1"));

		// STAGE 1
		car = getCar("Car1");
		car.setExits("cabin 1", getCar("Cabin1"));
		car.setExits("cabin1", getCar("Cabin1"));
		car.setExits("cabin 2", getCar("Cabin2"));
		car.setExits("cabin2", getCar("Cabin2"));

		car = getCar("Car1a");
		car.setExits("car 2", getCar("Car2"));
		car.setExits("car2", getCar("Car2"));

		car = getCar("Cabin1");
		car.setExits("car 1", getCar("Car1"));
		car.setExits("car1", getCar("Car1"));

		car = getCar("Cabin2");
		car.setExits("closet", getCar("Closet"));
		car.setExits("huge closet", getCar("Closet"));
		car.setExits("car 1", getCar("Death"));
		car.setExits("car1", getCar("Death"));

		car = getCar("Closet");
		car.setExits("cabin 2", getCar("Cabin2a"));
		car.setExits("cabin2", getCar("Cabin2a"));

		car = getCar("Cabin2a");
		car.setExits("car 1", getCar("Car1a"));
		car.setExits("car1", getCar("Car1a"));

		// car.setExits("closet", getCar("Closet"));

		// STAGE 2

		car = getCar("Car2");
		car.setExits("toilet", getCar("Toilet"));
		car.setExits("wc", getCar("Toilet"));
		car.setExits("bathroom", getCar("Toilet"));
		car.setExits("car 3", getCar("Car3k"));
		car.setExits("car3", getCar("Car3k"));

		car = getCar("Toilet");
		car.addItem(new Item("key", "It has number 5 on it", ""));
		car.addItemValue(getCar("Toilet").getItems(), 0);

		car = getCar("ToiletA");
		car.setExits("car 2", getCar("Car2a"));
		car.setExits("car2", getCar("Car2a"));

		car = getCar("ToiletB");
		car.setExits("car 2", getCar("Car2b"));
		car.setExits("car2", getCar("Car2b"));

		car = getCar("ToiletC");
		car.setExits("car 2", getCar("Car2b"));
		car.setExits("car2", getCar("Car2b"));

		car = getCar("ToiletD");
		car.setExits("car 2", getCar("Car2a"));
		car.setExits("car2", getCar("Car2a"));

		car = getCar("ToiletE");
		car.setExits("car 2", getCar("Car2c"));
		car.setExits("car2", getCar("Car2c"));

		car = getCar("Car2a");
		car.setExits("cabin 5", getCar("Cabin5"));
		car.setExits("cabin5", getCar("Cabin5"));
		car.setExits("car 3", getCar("Car3k"));
		car.setExits("car3", getCar("Car3k"));
		car.setExits("toilet", getCar("ToiletD"));
		car.setExits("toilet", getCar("ToiletD"));

		car = getCar("Cabin5");
		car.setExits("car 2", getCar("Car2b"));
		car.setExits("car2", getCar("Car2b"));
		car.addItem(new Item("poison",
				"It seems to be POISON(gas) used to kill farm pests.",
				"There's a strange can in the corner of the room."));
		car.addItemValue(getCar("Cabin5").getItems(), 0);

		car = getCar("Cabin5b");
		car.setExits("car 2", getCar("Car2c"));
		car.setExits("car2", getCar("Car2c"));

		car = getCar("Cabin5a");
		car.addItem(new Item("medicine",
				"This heart medicine belongs to the farmer.", ""));
		car.addItemValue(getCar("Cabin5a").getItems(), 0);

		car = getCar("Car2b");
		car.setExits("cabin 5", getCar("Cabin5"));
		car.setExits("cabin5", getCar("Cabin5"));
		car.setExits("car 3", getCar("Car3k"));
		car.setExits("car3", getCar("Car3k"));
		car.setExits("toilet", getCar("ToiletB"));

		car = getCar("Car2c");
		car.setExits("cabin 5", getCar("Cabin5"));
		car.setExits("cabin5", getCar("Cabin5"));
		car.setExits("car 3", getCar("Car3"));
		car.setExits("car3", getCar("Car3"));
		car.setExits("toilet", getCar("ToiletB"));

		// STAGE 3
		car = getCar("Car3");
		car.setExits("car 4", getCar("Car4"));
		car.setExits("car4", getCar("Car4"));
		// car.setExits("car 4", getCar("Car4"));
		car.addItem(new Item("photo", "You're in the photo",
				"It is a PHOTO. You should TAKE it for a better look."));
		car.addItemValue(getCar("Car3").getItems(), 0);

		car = getCar("Car3a");
		car.addItem(new Item("gun", "9mm", "There's a gun on the floor"));
		car.addItemValue(getCar("Car3a").getItems(), 0);

		// STAGE 4
		car = getCar("Car4");
		car.addItem(new Item("chair", "A typical wooden chair with four legs",
				""));
		car.addItemValue(getCar("Car4").getItems(), 0);
		car.setExits("toilet", getCar("Death"));

		car = getCar("Car4a");
		car.setExits("roof", getCar("Roof"));
		car.setExits("window", getCar("Roof"));

		car = getCar("Car4b");
		car.setExits("roof", getCar("Roof"));
		car.setExits("window", getCar("Roof"));

		// STAGE 5
		car = getCar("Roof");
		car.setExits("car 9", getCar("Car9"));
		car.setExits("car9", getCar("Car9"));

		//DEATHS
		car.setExits("car 1", getCar("RoofDeath"));
		car.setExits("car1", getCar("RoofDeath"));
		car.setExits("car 2", getCar("RoofDeath"));
		car.setExits("car2", getCar("RoofDeath"));
		car.setExits("car 3", getCar("RoofDeath"));
		car.setExits("car3", getCar("RoofDeath"));
		car.setExits("car 4", getCar("RoofDeath"));
		car.setExits("car4", getCar("RoofDeath"));
		car.setExits("car 5", getCar("RoofDeath"));
		car.setExits("car5", getCar("RoofDeath"));
		car.setExits("car 6", getCar("RoofDeath"));
		car.setExits("car6", getCar("RoofDeath"));
		car.setExits("car 7", getCar("RoofDeath"));
		car.setExits("car7", getCar("RoofDeath"));
		car.setExits("car 8", getCar("RoofDeath"));
		car.setExits("car8", getCar("RoofDeath"));
		car.setExits("car x", getCar("RoofDeath"));
		car.setExits("carx", getCar("RoofDeath"));
		car.setExits("car 10", getCar("RoofDeath"));
		car.setExits("car10", getCar("RoofDeath"));

		// STAGE 6
		car = getCar("Car9");
		car.setExits("car 10", getCar("Death"));
		car.setExits("car10", getCar("Death"));
		car.setExits("car x", getCar("Death"));
		car.setExits("carx", getCar("Death"));
		car.addItem(new Item("invention",
				"The most magnificent creation mankind has ever made.", ""));
		car.addItemValue(getCar("Car9").getItems(), 0);

		car = getCar("Car9a");
		car.setExits("car 10", getCar("CarX"));
		car.setExits("car10", getCar("CarX"));
		car.setExits("car x", getCar("CarX"));
		car.setExits("carx", getCar("CarX"));

		// FINAL STAGE

		car = getCar("CarX");

		return cars;
	}

	private void printCredits() throws Exception {

		System.out.println("Here are some cool people:");
		art.miika();
		art.oskar();
		art.lauri();
		System.out.println("Thanks for trying out the game!");
	}
}