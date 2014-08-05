package Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Creates a helper for creating and managing the SQLite database.
 * 
 * @author Marta Smith
 * @author Michael Williams
 * @author William Kofod
 * @version 1.3
 * 
 */
public class DatabaseHelper extends SQLiteOpenHelper {

	// Logcat tag
	private static final String LOG = "DatabaseHelper";

	// Database Version
	private static final int DATABASE_VERSION = 4;
	// Database Name
	private static final String DATABASE_NAME = "GroceryListManager.db";

	// final variables for tables and columns
	public static final String TABLE_CATEGORY = "CATEGORY";
	public static final String CAT_COLUMN_CATEGORYID = "categoryId";
	public static final String CAT_COLUMN_CATEGORYNAME = "categoryName";

	public static final String TABLE_ITEM = "ITEM";
	public static final String ITEM_COLUMN_ITEMID = "itemId";
	public static final String ITEM_COLUMN_ITEMNAME = "itemName";
	public static final String ITEM_COLUMN_CATEGORYID = "categoryId";

	public static final String TABLE_SHOPPINGLIST = "SHOPPINGLIST";
	public static final String SL_COLUMN_SHOPPINGLISTID = "shoppingListId";
	public static final String SL_COLUMN_SHOPPINGLISTNAME = "shoppingListName";

	public static final String TABLE_ACTIVELISTS = "ACTIVELISTS";
	public static final String ACTIVELISTS_COLUMN_ENTRYID = "entryId";
	public static final String ACTIVELISTS_COLUMN_ITEMID = "itemId";
	public static final String ACTIVELISTS_COLUMN_CATEGORYID = "categoryId";
	public static final String ACTIVELISTS_COLUMN_SHOPPINGLISTID = "shoppingListId";
	public static final String ACTIVELISTS_COLUMN_UNITS = "units";
	public static final String ACTIVELISTS_COLUMN_QUANTITY = "quantity";

	// Table Create Statements
	// ShoppingList table create statement
	private static final String CREATE_TABLE_SHOPPINGLIST = "CREATE TABLE SHOPPINGLIST(shoppingListId INTEGER PRIMARY KEY AUTOINCREMENT, shoppingListName TEXT)";
	// Item table create statement
	private static final String CREATE_TABLE_ITEM = "CREATE TABLE ITEM(itemId INTEGER PRIMARY KEY AUTOINCREMENT, itemName TEXT, categoryId INTEGER)";
	// CategoryList table create statement
	private static final String CREATE_TABLE_CATEGORY = "CREATE TABLE CATEGORY(categoryId INTEGER PRIMARY KEY AUTOINCREMENT, categoryName TEXT)";
	// Active shopping lists table
	private static final String CREATE_TABLE_ACTIVELISTS = "CREATE TABLE ACTIVELISTS(entryId INTEGER PRIMARY KEY AUTOINCREMENT, itemId INTEGER, "
			+ "categoryId INTEGER, shoppingListId INTEGER, units TEXT, quantity INTEGER)";

	// call superclass SQLiteOpenHelper constructor
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * Creates the SQLite database.
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		// creating required tables
		db.execSQL(CREATE_TABLE_SHOPPINGLIST);
		Log.d(LOG, CREATE_TABLE_SHOPPINGLIST);

		db.execSQL(CREATE_TABLE_CATEGORY);
		Log.d(LOG, CREATE_TABLE_CATEGORY);

		db.execSQL(CREATE_TABLE_ITEM);
		Log.d(LOG, CREATE_TABLE_ITEM);

		db.execSQL(CREATE_TABLE_ACTIVELISTS);
		Log.d(LOG, CREATE_TABLE_ACTIVELISTS);
	}

	/**
	 * Removes and recreates the database tables when the project is upgraded.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// on upgrade drop older tables
		db.execSQL("DROP TABLE IF EXISTS SHOPPINGLIST");
		db.execSQL("DROP TABLE IF EXISTS CATEGORY");
		db.execSQL("DROP TABLE IF EXISTS ITEM");
		db.execSQL("DROP TABLE IF EXISTS ACTIVELISTS");
		onCreate(db);
	}

	/**
	 * Close database
	 */
	public void closeDB() {
		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null && db.isOpen())
			db.close();
	}

}
