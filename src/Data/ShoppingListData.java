package Data;

import java.util.ArrayList;

import Model.ShoppingList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Data Access Object for the SHOPPINGLIST table.
 * 
 * @author Marta Smith
 * @author Michael Williams
 * @author William Kofod
 * @version 1.3
 * 
 */
public class ShoppingListData {

	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;
	private String[] allColumns = { DatabaseHelper.SL_COLUMN_SHOPPINGLISTID,
			DatabaseHelper.SL_COLUMN_SHOPPINGLISTNAME };

	/**
	 * Constructor for SoppingListData
	 * 
	 * @param context
	 */
	public ShoppingListData(Context context) {
		dbHelper = new DatabaseHelper(context);
	}

	/**
	 * Opens a writeable connection to the database.
	 * 
	 * @throws SQLException
	 */
	public void open() throws SQLException {
		db = dbHelper.getWritableDatabase();
	}

	/**
	 * Closes the connection to the database.
	 */
	public void close() {
		dbHelper.close();
	}

	/**
	 * Adds a new shopping list
	 * 
	 * @param shoppingListName
	 * @return ShoppingList
	 */
	public ShoppingList addShoppingList(String slName) {
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.SL_COLUMN_SHOPPINGLISTNAME, slName);
		long insertId = db.insert(DatabaseHelper.TABLE_SHOPPINGLIST, null,
				values);
		if (insertId > 5) {
			return null;
		}
		Cursor cursor = db.query(DatabaseHelper.TABLE_SHOPPINGLIST, allColumns,
				DatabaseHelper.SL_COLUMN_SHOPPINGLISTID + " = " + insertId,
				null, null, null, null);
		cursor.moveToFirst();
		ShoppingList newList = cursorToList(cursor);
		cursor.close();
		return newList;
	}

	/**
	 * Retrieves an instance of ShoppingList from the database using the list's
	 * name.
	 * 
	 * @param name
	 *            The name of the list
	 * @return ShoopingList The ShoppingList with the given name
	 */
	public ShoppingList getShoppingListByName(String name) {
		Cursor cursor = db.query(DatabaseHelper.TABLE_SHOPPINGLIST, allColumns,
				DatabaseHelper.SL_COLUMN_SHOPPINGLISTNAME + " = \"" + name
						+ "\"", null, null, null, null);
		cursor.moveToFirst();
		ShoppingList list = cursorToList(cursor);
		return list;
	}

	/**
	 * Retrieves all of the shopping lists in the database.
	 * 
	 * @return ArrayList<ShoppingList> The list of shopping lists
	 */
	public ArrayList<ShoppingList> getAllShoppingLists() {
		ArrayList<ShoppingList> lists = new ArrayList<ShoppingList>();

		Cursor cursor = db.query(DatabaseHelper.TABLE_SHOPPINGLIST, allColumns,
				null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ShoppingList category = cursorToList(cursor);
			lists.add(category);
			cursor.moveToNext();
		}

		cursor.close();
		return lists;
	}

	/**
	 * Removes the shopping list with the given ID from the database.
	 * 
	 * @param listId
	 *            The ID of the list being removed.
	 */
	public void removeShoppingList(long listId) {
		int deleted = db.delete(DatabaseHelper.TABLE_SHOPPINGLIST,
				DatabaseHelper.SL_COLUMN_SHOPPINGLISTID + " = " + listId, null);
		if (deleted > 0) {
			Log.d("ShoppingListData", "Delete successfully deleted " + deleted
					+ " row");
		} else {
			Log.d("ShoppingListData", "Delete was not successful");
		}
	}

	/**
	 * Converts a Cursor returned from a database query into an instance of
	 * ShoppingList.
	 * 
	 * @param cursor
	 *            The Cursor to be converted.
	 * @return ShoppingList The instance of a ShoppingList
	 */
	public ShoppingList cursorToList(Cursor cursor) {
		ShoppingList list = new ShoppingList();
		list.setShoppingListId(cursor.getLong(0));
		list.setShoppingListName(cursor.getString(1));
		return list;
	}

}