package Data;

import java.util.ArrayList;
import java.util.List;

import Model.Category;
import Model.Item;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Data Access Object for the ITEM table.
 * 
 * @author Marta Smith
 * @author Michael Williams
 * @author William Kofod
 * @version 1.3
 * 
 */
public class ItemData {

	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;
	private String[] allColumns = { DatabaseHelper.ITEM_COLUMN_ITEMID,
			DatabaseHelper.ITEM_COLUMN_ITEMNAME,
			DatabaseHelper.ITEM_COLUMN_CATEGORYID };

	// Logcat tag
	private static final String LOG = "ItemData";

	/**
	 * Constructs an instance of ItemData.
	 * 
	 * @param context
	 */
	public ItemData(Context context) {
		dbHelper = new DatabaseHelper(context);
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
	 * Creates an item with the given name.
	 * 
	 * @param itemName
	 * @return The new item
	 */
	public Item createItem(String itemName) {
		if (!foundItem(itemName)) {
			ContentValues values = new ContentValues();
			values.put(DatabaseHelper.ITEM_COLUMN_ITEMNAME, itemName);
			long insertId = db.insert(DatabaseHelper.TABLE_ITEM, null, values);
			Cursor cursor = db.query(DatabaseHelper.TABLE_ITEM, allColumns,
					DatabaseHelper.ITEM_COLUMN_ITEMID + " = " + insertId, null,
					null, null, null);
			cursor.moveToFirst();
			Item newItem = cursorToItem(cursor);
			cursor.close();
			return newItem;
		} else {
			Item oldItem = getItemByName(itemName);
			return oldItem;
		}
	}

	/**
	 * Creates an item with the given name in the given category.
	 * 
	 * @param categoryId
	 * @param itemName
	 * @return The new item.
	 */
	public Item createItem(long categoryId, String itemName) {
		if (!foundItem(itemName)) {
			ContentValues values = new ContentValues();
			values.put(DatabaseHelper.ITEM_COLUMN_ITEMNAME, itemName);
			values.put(DatabaseHelper.ITEM_COLUMN_CATEGORYID, categoryId);
			long insertId = db.insert(DatabaseHelper.TABLE_ITEM, null, values);
			Cursor cursor = db.query(DatabaseHelper.TABLE_ITEM, allColumns,
					DatabaseHelper.ITEM_COLUMN_ITEMID + " = " + insertId, null,
					null, null, null);
			cursor.moveToFirst();
			Item newItem = cursorToItem(cursor);
			cursor.close();
			return newItem;
		} else {
			Item oldItem = getItemByName(itemName);
			return oldItem;
		}
	}

	/**
	 * Finds the id of the given item.
	 * 
	 * @param name
	 * @return long The item's id
	 */
	public long findItemId(String name) {
		long id;
		String query = "SELECT * FROM " + DatabaseHelper.TABLE_ITEM + " WHERE "
				+ DatabaseHelper.ITEM_COLUMN_ITEMNAME + " = \"" + name + "\"";
		SQLiteDatabase readDB = dbHelper.getReadableDatabase();
		Cursor cursor = readDB.rawQuery(query, null);
		if (cursor.moveToFirst()) {
			id = cursor.getLong(0);
		} else {
			id = -1;
		}
		cursor.close();
		return id;
	}

	/**
	 * Delete an item by name
	 * 
	 * @param item
	 *            The item to be deleted
	 */
	public void deleteItem(Item item) {
		long id = item.getItemId();
		db.delete(DatabaseHelper.TABLE_ITEM, DatabaseHelper.ITEM_COLUMN_ITEMID
				+ " = " + id, null);
	}

	/**
	 * Delete an item by id
	 * 
	 * @param id
	 *            The item id to delete
	 */
	public void deleteItem(long id) {
		db.delete(DatabaseHelper.TABLE_ITEM, DatabaseHelper.ITEM_COLUMN_ITEMID
				+ " = " + id, null);
	}

	/**
	 * Get a single item
	 * 
	 * @param item_id
	 * @return Item
	 */
	public Item getItem(long item_id) {
		Log.d(LOG, "Entered getItem()");
		String selectQuery = "SELECT  * FROM " + DatabaseHelper.TABLE_ITEM
				+ " WHERE " + DatabaseHelper.ITEM_COLUMN_ITEMID + " = "
				+ item_id;

		Log.d(LOG, selectQuery);
		SQLiteDatabase readDB = dbHelper.getReadableDatabase();
		Cursor c = readDB.rawQuery(selectQuery, null);

		if (c != null)
			c.moveToFirst();

		Item item = cursorToItem(c);

		c.close();
		return item;
	}

	/**
	 * Get an item by name
	 * 
	 * @param itemName
	 * @return item
	 */
	public Item getItemByName(String itemName) {
		SQLiteDatabase readDB = dbHelper.getReadableDatabase();
		Cursor cursor = readDB
				.query(DatabaseHelper.TABLE_ITEM, allColumns,
						DatabaseHelper.ITEM_COLUMN_ITEMNAME + " = \""
								+ itemName + "\"", null, null, null, null);
		cursor.moveToFirst();
		Item item = cursorToItem(cursor);
		cursor.close();
		return item;
	}

	/**
	 * Get all items
	 */
	public List<Item> getAllItems() {
		List<Item> items = new ArrayList<Item>();

		Cursor cursor = db.query(DatabaseHelper.TABLE_ITEM, allColumns, null,
				null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Item item = cursorToItem(cursor);
			items.add(item);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return items;
	}

	/**
	 * Gets the total number of items in the database.
	 * 
	 * @return int Number of items
	 */
	public int getItemCount() {
		String countQuery = "SELECT  * FROM " + DatabaseHelper.TABLE_ITEM;
		Cursor cursor = db.rawQuery(countQuery, null);

		int count = cursor.getCount();
		cursor.close();

		// return count
		return count;
	}

	/**
	 * Checks if item is already in the table
	 * 
	 * @param itemName
	 *            The name of the item to check for
	 * @return true if the item is in the list, false otherwise
	 */
	public boolean foundItem(String itemName) {
		String selectQuery = "SELECT  * FROM " + DatabaseHelper.TABLE_ITEM;
		Log.d(LOG, selectQuery);
		Cursor c = db.rawQuery(selectQuery, null);
		c.moveToFirst();
		while (!c.isAfterLast()) {
			String checkName = c.getString(1);
			if (checkName.equalsIgnoreCase(itemName)) {
				return true;
			}
			c.moveToNext();
		}
		return false;
	}

	/**
	 * Gets all items from a category and puts into a list
	 * 
	 * @param categoryId
	 *            The category to get item list from
	 * @return List<Item> The list of items in this category
	 */
	public List<Item> getAllItemsByCat(long categoryId) {

		List<Item> items = new ArrayList<Item>();
		Log.d("ItemData", "ArrayList items created");

		Cursor cursor = db.query(DatabaseHelper.TABLE_ITEM, allColumns,
				DatabaseHelper.ITEM_COLUMN_CATEGORYID + " = " + categoryId,
				null, null, null, null);
		Log.d("ItemData", "Cursor returned");

		cursor.moveToFirst();
		Log.d("ItemData", "Cursor moved to first entry");
		while (!cursor.isAfterLast()) {
			Log.d("ItemData", "Entered while loop");
			Item item = cursorToItem(cursor);
			Log.d("ItemData", "New item created");
			if (item != null) {
				Log.d("ItemData", "If statement entered");
				items.add(item);
				Log.d("ItemData", "Item added to list");
			}
			cursor.moveToNext();
			Log.d("ItemData", "Cursor moved to next entry");
		}
		// make sure to close the cursor
		cursor.close();
		Log.d("ItemData", "Cursor closed");
		return items;
	}

	/**
	 * Checks if category item is already in the table
	 */
	public boolean foundCatItem(Category cat, Item i) {
		String selectQuery = "SELECT  * FROM " + DatabaseHelper.TABLE_ITEM;
		Log.d(LOG, selectQuery);
		Cursor c = db.rawQuery(selectQuery, null);

		if (c.moveToFirst()) {
			do {
				if (c.getString(
						c.getColumnIndex(DatabaseHelper.ITEM_COLUMN_CATEGORYID))
						.equals(cat.getCategoryId())
						&& c.getString(
								c.getColumnIndex(DatabaseHelper.ITEM_COLUMN_ITEMID))
								.equals(i.getItemId())) {
					return true;
				}
			} while (c.moveToNext());
		}
		return false;
	}

	/**
	 * Converts a Cursor returned from a database query into an instance of an
	 * Item.
	 * 
	 * @param cursor
	 *            The Cursor to be converted.
	 * @return Item The instance of Item
	 */
	public Item cursorToItem(Cursor cursor) {
		Item item = new Item();
		item.setItemId(cursor.getLong(0));
		item.setItemName(cursor.getString(1));
		item.setCatId(cursor.getLong(2));

		return item;
	}
}
