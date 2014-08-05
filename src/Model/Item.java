package Model;

/**
 * Defines the attributes and methods of an Item.
 * 
 * @author Marta Smith
 * @author Michael Williams
 * @author William Kofod
 * @version 1.3
 * 
 */
public class Item {

	private long itemId;
	private String itemName;
	private long catId;
	private long quantity;
	private String unitType;
	private long slId;

	/**
	 * Default constructor for an item
	 */
	public Item() {

	}

	/**
	 * Constructor for an item.
	 * 
	 * @param inItemName
	 *            The item name
	 * @param inQuantity
	 *            The quantity for this item
	 * @param inUnitType
	 *            The unit type for this item
	 */
	public Item(String inItemName, Integer inQuantity, String inUnitType) {
		this.itemId = 0;
		this.itemName = inItemName;
		this.quantity = inQuantity;
		this.unitType = inUnitType;
	}

	/**
	 * Constructor for an item.
	 * 
	 * @param inItemId
	 *            The item's ID
	 * @param inItemName
	 *            The item name
	 * @param inQuantity
	 *            The quantity for this item
	 * @param inUnitType
	 *            The unit type for this item
	 */
	public Item(long inItemId, String inItemName, Integer inQuantity,
			String inUnitType) {
		this.itemId = inItemId;
		this.itemName = inItemName;
		this.quantity = inQuantity;
		this.unitType = inUnitType;
	}

	/**
	 * Sets the item id. long inItemId The new id value
	 */
	public void setItemId(long inItemId) {
		this.itemId = inItemId;
	}

	/**
	 * Gets the item id. long The item's id
	 */
	public long getItemId() {
		return this.itemId;
	}

	/**
	 * Set item name
	 */
	public void setItemName(String inItemName) {
		this.itemName = inItemName;
	}

	/**
	 * Get item name
	 */
	public String getItemName() {
		return this.itemName;
	}

	/**
	 * Set item quantity
	 */
	public void setQuantity(long inQuantity) {
		this.quantity = inQuantity;
	}

	/**
	 * Get item quantity
	 */
	public long getQuantity() {
		return this.quantity;
	}

	/**
	 * Set item unit type
	 */
	public void setUnitType(String inUnitType) {
		this.unitType = inUnitType;
	}

	/**
	 * Get item unit type
	 */
	public String getUnitType() {
		return this.unitType;
	}

	/**
	 * Returns a String containing the quantity, unitType and itemName.
	 * 
	 * @return String the item string.
	 */
	public String toString() {

		if (quantity == 0) {
			return "  " + itemName;
		} else if (unitType == null) {
			return "  " + Long.toString(quantity) + "    " + itemName;
		} else {
			return "  " + Long.toString(quantity) + " " + unitType + "    "
					+ itemName;
		}
	}

	/**
	 * Gets the category ID for this item.
	 * 
	 * @return long The category ID
	 */
	public long getCatId() {
		return catId;
	}

	/**
	 * Sets the category ID for this item.
	 * 
	 * @param catId
	 *            The category ID
	 */
	public void setCatId(long catId) {
		this.catId = catId;
	}

	/**
	 * Gets the Shopping List ID for this item.
	 * 
	 * @return long The Shopping List ID
	 */
	public long getSlId() {
		return slId;
	}

	/**
	 * Sets the Shopping List ID for this item.
	 * 
	 * @param slId
	 *            The Shopping List ID to be set.
	 */
	public void setSlId(long slId) {
		this.slId = slId;
	}

}
