package grocery.list;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Data.ActiveListsData;
import Data.CategoryData;
import Data.ItemData;
import Data.ShoppingListData;
import Model.Category;
import Model.ShoppingList;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Displays the starting screen for this app, and populates the database with
 * preset data if it is not already present on the device.
 * 
 * @author Marta Smith
 * @author Michael Williams
 * @author William Kofod
 * @version 1.3
 * 
 */
public class MainActivity extends Activity {
	final Context context = this;
	final ShoppingListData slData = new ShoppingListData(context);
	final ActiveListsData listData = new ActiveListsData(context);
	final CategoryData catData = new CategoryData(context);
	final ItemData itemData = new ItemData(context);
	private ArrayList<String> list = new ArrayList<String>();
	private Button addListButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_list);

		slData.open();
		listData.open();
		catData.open();
		itemData.open();

		// Determine if preset data has already been added to the database
		try {
			itemData.getItem(0);
			Log.d("MainActivity", "Data already present in database");
		} catch (CursorIndexOutOfBoundsException e) {
			Log.d("MainActivity", "No categories found: Adding preset items");
			// Add preset items to database
			Category cat1 = catData.createCategory("Bakery");
			itemData.createItem(cat1.getCategoryId(), "Bagels");
			itemData.createItem(cat1.getCategoryId(), "Bread - wheat");
			itemData.createItem(cat1.getCategoryId(), "Bread - white");
			itemData.createItem(cat1.getCategoryId(), "Cookies");
			itemData.createItem(cat1.getCategoryId(), "Hamburger Buns");
			itemData.createItem(cat1.getCategoryId(), "Hotdog Buns");

			Category cat2 = catData.createCategory("Canned Goods");
			itemData.createItem(cat2.getCategoryId(), "Corned Beef Hash");
			itemData.createItem(cat2.getCategoryId(), "Corn");
			itemData.createItem(cat2.getCategoryId(), "Green Beans");
			itemData.createItem(cat2.getCategoryId(), "Soup - Tomato");
			itemData.createItem(cat2.getCategoryId(), "Soup - Vegetable Beef");
			itemData.createItem(cat2.getCategoryId(), "Tomatos - diced");
			itemData.createItem(cat2.getCategoryId(), "Tomatos - peeled");

			Category cat3 = catData.createCategory("Dairy");
			itemData.createItem(cat3.getCategoryId(),
					"American Cheese - slices");
			itemData.createItem(cat3.getCategoryId(),
					"Cheddar Cheese - shredded");
			itemData.createItem(cat3.getCategoryId(), "Cheddar Cheese - slices");
			itemData.createItem(cat3.getCategoryId(), "Cottage Cheese");
			itemData.createItem(cat3.getCategoryId(), "Eggs");
			itemData.createItem(cat3.getCategoryId(), "Milk - whole");
			itemData.createItem(cat3.getCategoryId(), "Yogurt");
		}

		final ListView listview = (ListView) findViewById(R.id.select_list);

		list = setActiveLists();

		final StableArrayAdapter adapter = new StableArrayAdapter(this,
				android.R.layout.simple_list_item_1, list);
		listview.setAdapter(adapter);

		addListButton = (Button) findViewById(R.id.addNewListButton);
		addListButton.setOnClickListener(new View.OnClickListener() {

			/*
			 * (non-Javadoc) Set the action for clicking the Add New List button
			 * 
			 * @see android.view.View.OnClickListener#onClick(android.view.View)
			 */
			@Override
			public void onClick(View v) {
				LayoutInflater layoutInflater = LayoutInflater.from(context);
				final View promptView = layoutInflater.inflate(
						R.layout.add_list_prompt, null);
				final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						context);

				// set add_list_prompt.xml to be the layout file of the alert
				// dialog builder
				alertDialogBuilder.setView(promptView);

				final EditText nameInput = (EditText) promptView
						.findViewById(R.id.inputAddListName);

				// setup a dialog window
				alertDialogBuilder
						.setCancelable(false)
						.setPositiveButton("Add List",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int id) {
										StringBuilder text = new StringBuilder();
										String name = null;
										if (nameInput != null) {
											name = nameInput.getText()
													.toString();
											slData.addShoppingList(name);
										}

										text.append(name);
										text.append(" added");
										String toastOutput = text.toString();
										Toast.makeText(context, toastOutput,
												Toast.LENGTH_LONG).show();
										adapter.add(name);
									}
								})
						.setNegativeButton("Cancel",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.cancel();
									}
								});

				// create an alert dialog
				AlertDialog alertD = alertDialogBuilder.create();

				alertD.show();
			}
		});

		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			/*
			 * (non-Javadoc) Set the action for clicking an item
			 * 
			 * @see
			 * android.widget.AdapterView.OnItemClickListener#onItemClick(android
			 * .widget.AdapterView, android.view.View, int, long)
			 */
			@Override
			public void onItemClick(AdapterView<?> parent, final View view,
					int position, long id) {
				final String item = (String) parent.getItemAtPosition(position);
				Intent i = new Intent(MainActivity.this, ViewListActivity.class);
				ShoppingList selectedList = slData.getShoppingListByName(item);
				i.putExtra("listId", selectedList.getShoppingListId());
				startActivity(i);
			};

		});

		listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			/*
			 * (non-Javadoc) Set the activity for a long click on an item
			 * 
			 * @see
			 * android.widget.AdapterView.OnItemLongClickListener#onItemLongClick
			 * (android.widget.AdapterView, android.view.View, int, long)
			 */
			@Override
			public boolean onItemLongClick(AdapterView<?> parent,
					final View view, int position, long id) {
				final String item = (String) parent.getItemAtPosition(position);
				ShoppingList sList = slData.getShoppingListByName(item);
				listData.removeAllListItems(sList.getShoppingListId());
				slData.removeShoppingList(sList.getShoppingListId());
				list.remove(parent.getItemAtPosition(position));
				adapter.notifyDataSetChanged();
				return true;
			}
		});
	}

	/**
	 * Private inner class that creates the adapter for the list view.
	 * 
	 * @author William Kofod
	 * @version 1.3
	 * 
	 */
	private class StableArrayAdapter extends ArrayAdapter<String> {

		HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

		public StableArrayAdapter(Context context, int textViewResourceId,
				List<String> objects) {
			super(context, textViewResourceId, objects);
			for (int i = 0; i < objects.size(); ++i) {
				mIdMap.put(objects.get(i), i);
			}
		}

		/**
		 * Adds a shopping list to the list.
		 * 
		 * @param name
		 *            The name of the list to add
		 */
		@Override
		public void add(String name) {
			if (!list.contains(name)) {
				list.add(name);
			}
			if (!mIdMap.containsKey(name)) {
				mIdMap.put(name, mIdMap.size());
			}
			notifyDataSetChanged();
		}

		/**
		 * Retrieves the id for an item in the list.
		 * 
		 * @param position
		 *            The index of the item
		 * @return The id of the item
		 */
		@Override
		public long getItemId(int position) {
			String item = getItem(position);
			if (!mIdMap.containsKey(item)) {
				mIdMap.put(item, position);
			}
			return mIdMap.get(item);
		}

		/**
		 * Sets if the list has stable ids.
		 * 
		 * @return whether or not the list has stable ids
		 */
		@Override
		public boolean hasStableIds() {
			return true;
		}
	}

	/**
	 * Retrieves the list of shopping lists from the database.
	 * 
	 * @return the ArrayList of the names of the ShoppingList
	 */
	public ArrayList<String> setActiveLists() {
		ArrayList<String> names = new ArrayList<String>();
		ArrayList<ShoppingList> lists = slData.getAllShoppingLists();
		for (ShoppingList list : lists) {
			names.add(list.getShoppingListName());
		}
		return names;
	}

	/**
	 * Overrides the onResume method for Activity class. Reopens the data access
	 * objects used in this class.
	 */
	@Override
	public void onResume() {
		slData.open();
		listData.open();
		catData.open();
		itemData.open();
		super.onResume();
	}

	/**
	 * Overrides the onPause method for Activity class. Closes the data access
	 * objects used in this class.
	 */
	@Override
	public void onPause() {
		slData.close();
		listData.close();
		catData.close();
		itemData.close();
		super.onPause();
	}

	/**
	 * Overrides the onStop method for Activity class. Closes the data access
	 * objects used in this class.
	 */
	@Override
	public void onStop() {
		slData.close();
		listData.close();
		catData.close();
		itemData.close();
		super.onStop();
	}

	/**
	 * Overrides the onDestroy method for Activity class. Closes the data access
	 * objects used in this class.
	 */
	@Override
	public void onDestroy() {
		slData.close();
		listData.close();
		catData.close();
		itemData.close();
		super.onDestroy();
	}
}