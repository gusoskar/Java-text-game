package kolanen;

import java.io.Serializable;
import java.util.*;

public class Car implements Serializable {
	private static final long serialVersionUID = 1L;

	private String name;
	private HashMap<String, Car> exits;
	private ArrayList<Item> items;
	private ArrayList<Player> players;
	private String description;
	private HashMap<String, Item> itemKey;

	public Car(String name, String description) {
		this.name = name;
		this.description = description;
		exits = new HashMap<String, Car>();
		items = new ArrayList<Item>();
		players = new ArrayList<Player>();
		itemKey = new HashMap<String, Item>();

	}

	public void addItemValue(String itemName, int position) {

		itemKey.put(itemName, items.get(position));
	}

	public Item getItemValue(String asdf) {
		return itemKey.get(asdf);
	}

	public Car getExit(String direction) {

		return exits.get(direction);

	}

	public void removeItem(Item item) {
		if (item != null) {

			items.remove(item);
		}
	}

	public String getExitString() {
		String s = "Exits:";
		Set<String> keys = exits.keySet();
		for (String key : keys) {
			s += " " + key;
		}
		return s;
	}

	public void setExits(String direction, Car neighbor) {
		exits.put(direction, neighbor);
	}

	public void addItem(Item item) {
		if (item != null) {
			items.add(item);
		}
	}

	public String getItems() {
		String s = "";
		for (Item item : items)
			s += item.getName();
		return s;
	}

	public String getItemPlace() {
		String s = "";
		for (Item item : items)
			s += item.getItemPlace() + " ";
		return s;
	}

	public String getName() {
		return name;
	}

	public String getDesc() {
		return description;
	}
}