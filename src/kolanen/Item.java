package kolanen;

import java.io.Serializable;

public class Item implements Serializable {
	private static final long serialVersionUID = 1L;

	private String name;
	private String description;
	private String itemPlace;

	public Item(String name, String description, String itemPlace) {
		this.name = name;
		this.description = description;
		this.itemPlace = itemPlace;
	}

	public String getDescription() {
		return description;
	}

	public String getName() {
		return name;
	}

	public String getItemPlace() {
		return itemPlace;
	}
}