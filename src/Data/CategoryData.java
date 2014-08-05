package Data;

import java.util.ArrayList;
import java.util.List;

import Model.Category;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Data Access Object for the CATEGORY table.
 * 
 * @author Marta Smith
 * @author Michael Williams
 * @author William Kofod
 * @version 1.3
 * 
 */
public class CategoryData {

	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;
	private String[] allColumns = { DatabaseHelper.CAT_COLUMN_CATEGORYID,
			DatabaseHelper.CAT_COLUMN_CATEGORYNAME };
	final ItemData itemData;

	// tables used in this class
	public final static String SHOPPINGLIST_TABLE = "ShoppingList";
	public final static String CATEGORY_TABLE = "Category";
	public final static String ITEMS_TABLE = "Items";

	// Category table key constants
	public final static String KEY_CATEGORY_ID = "categoryId";
	public final static String KEY_CATEGORY_NAME = "categoryName";

	// Logcat tag
	private static final String LOG = "CategoryData";

	/**
	 * Constructor for CategoryData objects
	 * 
	 * @param context
	 */
	public CategoryData(Context context) {
		dbHelper = new DatabaseHelper(context);
		itemData = new ItemData(context);
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
	 * Creates a category.
	 * 
	 * @param categoryName
	 *            The name of the category to create
	 * @return Category
	 */
	public Category createCategory(String categoryName) {

		if (!foundCat(categoryName)) {
			ContentValues values = new ContentValues();
			values.put(DatabaseHelper.CAT_COLUMN_CATEGORYNAME, categoryName);
			long insertId = db.insert(DatabaseHelper.TABLE_CATEGORY, null,
					values);
			Cursor cursor = db.query(DatabaseHelper.TABLE_CATEGORY, allColumns,
					DatabaseHelper.CAT_COLUMN_CATEGORYID + " = " + insertId,
					null, null, null, null);
			cursor.moveToFirst();
			Category newCat = cursorToCategory(cursor);
			cursor.close();
			return newCat;
		} else {
			Category oldCat = getCategoryByName(categoryName);
			return oldCat;
		}
	}

	/**
	 * Deletes a category
	 * 
	 * @param category
	 *            The category to delete
	 */
	public void deleteCategory(Category category) {
		long id = category.getCategoryId();
		db.delete(DatabaseHelper.TABLE_CATEGORY,
				DatabaseHelper.CAT_COLUMN_CATEGORYID + " = " + id, null);
	}

	/**
	 * Get all categories.
	 * 
	 * @return List<Category> All categories in the database
	 */
	public List<Category> getAllCategories() {

		List<Category> categories = new ArrayList<Category>();

		Cursor cursor = db.query(DatabaseHelper.TABLE_CATEGORY, allColumns,
				null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Category category = cursorToCategory(cursor);
			categories.add(category);
			cursor.moveToNext();
		}

		cursor.close();
		return categories;
	}

	/**
	 * Get a single category
	 * 
	 * @param cat_id
	 * @return Category
	 */
	public Category getCategoryById(long cat_id) {
		String selectQuery = "SELECT * FROM " + CATEGORY_TABLE + " WHERE "
				+ KEY_CATEGORY_ID + " = " + cat_id;
		Log.d(LOG, "Query in getCategoryById()");
		Log.d(LOG, selectQuery);
		SQLiteDatabase readDB = dbHelper.getReadableDatabase();

		Cursor c = readDB.rawQuery(selectQuery, null);

		if (c != null)
			c.moveToFirst();

		Category cat = new Category();
		cat.setCategoryId(c.getInt((c.getColumnIndex(KEY_CATEGORY_ID))));
		cat.setCategoryName(c.getString(c.getColumnIndex(KEY_CATEGORY_NAME)));

		return cat;
	}

	/**
	 * Get a category by name
	 * 
	 * @param catName
	 * @return Category
	 */
	public Category getCategoryByName(String catName) {

		Log.d(LOG, "Query in getCategoryByName()");
		Cursor cursor = db.query(DatabaseHelper.TABLE_CATEGORY, allColumns,
				DatabaseHelper.CAT_COLUMN_CATEGORYNAME + " = \"" + catName
						+ "\"", null, null, null, null);
		cursor.moveToFirst();
		Category category = cursorToCategory(cursor);
		return category;
	}

	/**
	 * Gets the total number of categories in the database.
	 * 
	 * @return int Total categories
	 */
	public int getCategoryCount() {
		Log.d(LOG, "Query in getCategoryCount()");
		String countQuery = "SELECT  * FROM " + CATEGORY_TABLE;
		Cursor cursor = db.rawQuery(countQuery, null);

		int count = cursor.getCount();
		cursor.close();

		// return count
		return count;
	}

	/**
	 * Checks if category is already in the table.
	 * 
	 * @param catName
	 * @return boolean True if category is in database, false otherwise
	 */
	public boolean foundCat(String catName) {
		Log.d(LOG, "Entered foundCat()");
		String selectQuery = "SELECT  * FROM " + CATEGORY_TABLE;
		Log.d(LOG, selectQuery);
		Cursor c = db.rawQuery(selectQuery, null);

		if (c.moveToFirst()) {
			do {
				if (c.getString(c.getColumnIndex(KEY_CATEGORY_NAME)).equals(
						catName)) {
					return true;
				}
			} while (c.moveToNext());
		}
		return false;
	}

	/**
	 * Converts a cursor into a Category.
	 * 
	 * @param cursor
	 * @return Category
	 */
	public Category cursorToCategory(Cursor cursor) {
		Log.d(LOG, "Entered cursorToCategory()");
		Category category = new Category();
		category.setCategoryId(cursor.getLong(0));
		category.setCategoryName(cursor.getString(1));
		return category;
	}

}
