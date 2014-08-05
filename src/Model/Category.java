package Model;

import java.util.ArrayList;

/**
 * Defines the attributes and methods of a Category.
 * 
 * @author Marta Smith
 * @author Michael Williams
 * @author William Kofod
 * @version 1.3
 * 
 */
public class Category {

	private long categoryId;
	private String categoryName;
	private ArrayList<Item> items;

	/**
	 * Default constructor
	 */
	public Category() {
		// default no-argument constructor
		items = new ArrayList<Item>();
	}

	/**
	 * Constructor for the Category class.
	 * 
	 * @param catName
	 *            The name of the Category
	 */
	public Category(String catName) {
		this.categoryId = 0;
		this.categoryName = catName;
		items = new ArrayList<Item>();
	}

	/**
	 * Constructor for the Category class.
	 * 
	 * @param catId
	 *            The Category ID
	 * @param catName
	 *            The Category name
	 */
	public Category(long catId, String catName) {
		this.categoryId = catId;
		this.categoryName = catName;
		items = new ArrayList<Item>();
	}

	/**
	 * Sets the ID of the category.
	 * 
	 * @param catId
	 *            The Category ID
	 */
	public void setCategoryId(long catId) {
		this.categoryId = catId;
	}

	/**
	 * Gets the ID of the category.
	 * 
	 * @return CategoryId
	 */
	public long getCategoryId() {
		return this.categoryId;
	}

	/**
	 * Sets the name of the category.
	 * 
	 * @param inName
	 *            the new name for the category
	 */
	public void setCategoryName(String catName) {
		this.categoryName = catName;
	}

	/**
	 * Gets the name of the category.
	 * 
	 * @return CategoryName
	 */
	public String getCategoryName() {
		return this.categoryName;
	}

	/**
	 * Gets the list of items in this category.
	 * 
	 * @return ArrayList<Item> The list of items
	 */
	public ArrayList<Item> getItems() {
		return items;
	}

	/**
	 * Sets the list of items in this category.
	 * 
	 * @param items
	 *            The list of items
	 */
	public void setItems(ArrayList<Item> items) {
		this.items = items;
	}

	/**
	 * Adds an item to the list of items for this category.
	 * 
	 * @param item
	 *            The new item to add
	 */
	public void addItemToList(Item item) {
		if (item != null) {
			if (!items.contains(item)) {
				items.add(item);
			}
		}
	}

}
