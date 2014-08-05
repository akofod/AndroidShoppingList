package Data;

import java.util.ArrayList;

import Model.Category;
import Model.Item;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Data Access Object for the ACTIVELISTS table.
 * 
 * @author Marta Smith
 * @author Michael Williams
 * @author William Kofod
 * @version 1.3
 * 
 */
public class ActiveListsData {
	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;
	final ItemData itemData;
	final CategoryData catData;
	final ShoppingListData slData;

	/**
	 * Constructor for ActiveListsData objects.
	 * 
	 * @param context
	 */
	public ActiveListsData(Context context) {
		dbHelper = new DatabaseHelper(context);
		itemData = new ItemData(context);
		catData = new CategoryData(context);
		slData = new ShoppingListData(context);
	}

	/**
	 * Opens the database.
	 * 
	 * @throws SQLException
	 */
	public void open() throws SQLException {
		db = dbHelper.getWritableDatabase();
	}

	/**
	 * Closes the database.
	 */
	public void close() {
		dbHelper.close();
	}

	/**
	 * Adds an item to the given shopping list.
	 * 
	 * @param listId
	 *            The shopping list to add the item to
	 * @param itemName
	 *            The name of the item to add
	 * @param units
	 *            The unit type for the item
	 * @param quantity
	 *            The quantity for the item
	 * @return boolean True if the item was added, false otherwise
	 */
	public boolean addItemToList(long listId, String itemName, String units,
			int quantity) {

		if (!foundListItem(itemName, listId)) {
			Item newItem = itemData.getItemByName(itemName);

			ContentValues values = new ContentValues();
			values.put(DatabaseHelper.ACTIVELISTS_COLUMN_ITEMID,
					newItem.getItemId());
			values.put(DatabaseHelper.ACTIVELISTS_COLUMN_CATEGORYID,
					newItem.getCatId());
			values.put(DatabaseHelper.ACTIVELISTS_COLUMN_SHOPPINGLISTID, listId);
			values.put(DatabaseHelper.ACTIVELISTS_COLUMN_UNITS, units);
			values.put(DatabaseHelper.ACTIVELISTS_COLUMN_QUANTITY, quantity);

			db.insert(DatabaseHelper.TABLE_ACTIVELISTS, null, values);
			return true;
		} else {
			return false;
		}

	}

	/**
	 * Gets the list of all categories in the given shopping list.
	 * 
	 * @param listId
	 * @return ArrayList<Category> All categories in the shopping list
	 */
	public ArrayList<Category> getListCategories(long listId) {
		boolean added = false;
		ArrayList<Category> catList = new ArrayList<Category>();
		String query = "SELECT * FROM " + DatabaseHelper.TABLE_ACTIVELISTS
				+ " WHERE " + DatabaseHelper.ACTIVELISTS_COLUMN_SHOPPINGLISTID
				+ " = " + listId;
		Cursor cursor = db.rawQuery(query, null);
		if (cursor == null) {
			return catList;
		}
		if (cursor.moveToFirst()) {
			while (!cursor.isAfterLast()) {
				Category cat = cursorToListCategory(cursor);
				Item item = cursorToListItem(cursor);
				for (Category c : catList) {
					if (c.getCategoryName().equalsIgnoreCase(
							cat.getCategoryName())) {
						c.addItemToList(item);
						added = true;
						break;
					} else {
						added = false;
					}
				}
				if (!added) {
					cat.addItemToList(item);
					catList.add(cat);
				}

				cursor.moveToNext();
			}
		}
		cursor.close();
		return catList;
	}

	/**
	 * Checks if an item is in the given list.
	 * 
	 * @param name
	 *            The item name
	 * @param listId
	 *            The current shopping list
	 * @return boolean True if the item is in the shopping list, false otherwise
	 */
	public boolean foundListItem(String name, long listId) {
		String query = "SELECT * FROM " + DatabaseHelper.TABLE_ACTIVELISTS
				+ " WHERE " + DatabaseHelper.ACTIVELISTS_COLUMN_SHOPPINGLISTID
				+ " = " + listId;
		Cursor cursor = db.rawQuery(query, null);
		if (cursor == null) {
			return false;
		}
		cursor.moveToFirst();
		long itemId = itemData.findItemId(name);

		if (itemId < 0) {
			return false;
		}

		while (!cursor.isAfterLast()) {
			long checkId = cursor.getLong(1);
			if (itemId == checkId) {
				cursor.close();
				return true;
			}
			cursor.moveToNext();
		}

		cursor.close();
		return false;

	}

	/**
	 * Finds the entry id for the given item in the given list.
	 * 
	 * @param itemId
	 * @param listId
	 * @return long The entry id
	 */
	public long findEntryId(long itemId, long listId) {
		String query = "SELECT * FROM " + DatabaseHelper.TABLE_ACTIVELISTS
				+ " WHERE " + DatabaseHelper.ACTIVELISTS_COLUMN_SHOPPINGLISTID
				+ " = " + listId;
		Log.d("ActiveListData", "Calling query: " + query);
		Cursor cursor = db.rawQuery(query, null);
		Log.d("ActiveListData", "Query called " + query);
		if (cursor != null) {
			cursor.moveToFirst();
		}
		long id = -1;
		while (!cursor.isAfterLast()) {
			Item i = cursorToListItem(cursor);
			Log.d("ActiveListData", "Checking item: " + i.getItemName() + " "
					+ i.getUnitType() + " " + i.getQuantity());
			if (i.getItemId() == itemId) {
				id = cursor.getLong(0);
				Log.d("ActiveListData", "Found entry id: " + id);
				break;
			}
			cursor.moveToNext();
		}
		cursor.close();
		return id;

	}

	/**
	 * Removes an entry from the ACTIVELISTSTABLE.
	 * 
	 * @param entryId
	 */
	public void removeEntry(long entryId) {
		int deleted = db.delete(DatabaseHelper.TABLE_ACTIVELISTS,
				DatabaseHelper.ACTIVELISTS_COLUMN_ENTRYID + " = " + entryId,
				null);

		if (deleted > 0) {
			Log.d("ActiveListData", "Delete successfully deleted " + deleted
					+ " row");
		} else {
			Log.d("ActiveListData", "Delete was not successful");
		}
	}

	/**
	 * Removes all items from the given shopping list.
	 * 
	 * @param listId
	 */
	public void removeAllListItems(long listId) {
		int deleted = db.delete(DatabaseHelper.TABLE_ACTIVELISTS,
				DatabaseHelper.ACTIVELISTS_COLUMN_SHOPPINGLISTID + " = "
						+ listId, null);
		if (deleted > 0) {
			Log.d("ActiveListData", "Delete successfully deleted " + deleted
					+ " row");
		} else {
			Log.d("ActiveListData", "Delete was not successful");
		}
	}

	/**
	 * Edits an item in the list
	 * 
	 * @param
	 * @return
	 */
	public void editItem(long itemId, long listId, long quantity, String units) {
		ContentValues values = new ContentValues();
		long entryId = findEntryId(itemId, listId);
		values.put(DatabaseHelper.ACTIVELISTS_COLUMN_QUANTITY, quantity);
		values.put(DatabaseHelper.ACTIVELISTS_COLUMN_UNITS, units);
		db.update(DatabaseHelper.TABLE_ACTIVELISTS, values,
				DatabaseHelper.ACTIVELISTS_COLUMN_ENTRYID + " = " + entryId,
				null);

	}

	/**
	 * Converts a Cursor object into an item in a shopping list.
	 * 
	 * @param cursor
	 * @return Item
	 */
	public Item cursorToListItem(Cursor cursor) {
		Item item = new Item();
		item.setItemId(cursor.getLong(1));
		item.setCatId(cursor.getLong(2));
		item.setSlId(cursor.getLong(3));
		item.setUnitType(cursor.getString(4));
		item.setQuantity(cursor.getLong(5));
		item.setItemName(itemData.getItem(item.getItemId()).getItemName());

		return item;
	}

	/**
	 * Converts a Cursor object into a category in a shopping list.
	 * 
	 * @param cursor
	 * @return
	 */
	public Category cursorToListCategory(Cursor cursor) {
		Category cat = new Category();
		cat.setCategoryId(cursor.getLong(2));
		cat.setCategoryName(catData.getCategoryById(cat.getCategoryId())
				.getCategoryName());
		return cat;
	}
}
