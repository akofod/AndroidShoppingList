package Model;

import java.util.ArrayList;

/**
 * Defines the attributes and methods of a Shopping List.
 * 
 * @author Marta Smith
 * @author Michael Williams
 * @author William Kofod
 * @version 1.3
 * 
 */
public class ShoppingList {
	private long shoppingListId;
	private String shoppingListName;
	private ArrayList<Category> items;

	/**
	 * Default constructor for a shopping list.
	 */
	public ShoppingList() {

	}

	/**
	 * Constructor for a shopping list with the list name as a parameter.
	 * 
	 * @param slName
	 *            The name of the shopping list
	 */
	public ShoppingList(String slName) {
		this.shoppingListId = 0;
		this.shoppingListName = slName;
	}

	/**
	 * Constructor for a shopping list with the listId and name as parameters.
	 * 
	 * @param slId
	 *            The list ID
	 * @param slName
	 *            The name of the shopping list
	 */
	public ShoppingList(long slId, String slName) {
		this.shoppingListId = slId;
		this.shoppingListName = slName;

	}

	/**
	 * Sets the ID of the shoppingList.
	 * 
	 * @param slId
	 *            The ID of the shopping list
	 */
	public void setShoppingListId(long slId) {
		this.shoppingListId = slId;
	}

	/**
	 * Gets the ID of the shoppingList.
	 * 
	 * @return shoppingListId
	 */
	public long getShoppingListId() {
		return this.shoppingListId;
	}

	/**
	 * Sets the name of the category.
	 * 
	 * @param inName
	 *            the new name for the category
	 */
	public void setShoppingListName(String slName) {
		this.shoppingListName = slName;
	}

	/**
	 * Gets the name of the category.
	 * 
	 * @return CategoryName
	 */
	public String getShoppingListName() {
		return this.shoppingListName;
	}

	/**
	 * Gets the list of categories and items in this shopping list.
	 * 
	 * @return The ArrayList of Categories in this list
	 */
	public ArrayList<Category> getItems() {
		return items;
	}

	/**
	 * Sets the list of categories and items in this shopping list.
	 * 
	 * @param items
	 *            The ArrayList of Categories
	 */
	public void setItems(ArrayList<Category> items) {
		this.items = items;
	}

}
