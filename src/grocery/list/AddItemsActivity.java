package grocery.list;

import java.util.ArrayList;

import Data.ActiveListsData;
import Data.CategoryData;
import Data.ItemData;
import Model.Category;
import Model.Item;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Toast;

/**
 * Displays the activity for adding predefined items to shopping lists
 * 
 * @author Marta Smith
 * @author Michael Williams
 * @author William Kofod
 * @version 1.3
 * 
 */
public class AddItemsActivity extends Activity {

	private PresetItemsListAdapter expAdapter;
	private ArrayList<Category> expCategories;
	private ExpandableListView expList;
	private Button addButton;
	private Button finishedButton;
	final Context context = this;
	final CategoryData catData = new CategoryData(context);
	final ItemData itemData = new ItemData(context);
	final ActiveListsData listData = new ActiveListsData(context);

	/**
	 * Sets the actions when the activity starts for the first time.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_items);

		catData.open();
		itemData.open();
		listData.open();

		Intent intent = getIntent();
		long listId = intent.getLongExtra("listId", 1);
		Log.d("AddItemsActivity", "Received listId: " + listId);
		final long returnList = listId;

		expList = (ExpandableListView) findViewById(R.id.add_items_explist);
		expCategories = predefinedCategories();
		expAdapter = new PresetItemsListAdapter(AddItemsActivity.this,
				expCategories, listId, listData);
		expList.setAdapter(expAdapter);
		addButton = (Button) findViewById(R.id.addNewItemButton);
		finishedButton = (Button) findViewById(R.id.finishedAddingButton);

		addButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				// get prompts.xml view
				LayoutInflater layoutInflater = LayoutInflater.from(context);
				final View promptView = layoutInflater.inflate(
						R.layout.add_item_prompt, null);
				final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						context);

				// set prompts.xml to be the layout file of the alert dialog
				// builder
				alertDialogBuilder.setView(promptView);

				final EditText nameInput = (EditText) promptView
						.findViewById(R.id.inputAddName);
				final EditText categoryInput = (EditText) promptView
						.findViewById(R.id.inputAddCategory);

				// setup a dialog window
				alertDialogBuilder
						.setCancelable(false)
						.setPositiveButton("Add Item",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int id) {
										StringBuilder text = new StringBuilder();
										text.append("Item added:\n");
										Category newCat = null;
										Item newItem = null;
										String category = null;
										String name = null;
										boolean validData = true;

										if (categoryInput != null) {
											category = categoryInput.getText()
													.toString();
											newCat = catData
													.createCategory(category);
										} else {
											validData = false;
										}

										if (nameInput != null) {
											name = nameInput.getText()
													.toString();
											long catId = newCat.getCategoryId();
											newItem = itemData.createItem(
													catId, name);
										} else {
											validData = false;
										}

										if (validData) {
											expAdapter.addItem(newItem, newCat);

											text.append(name);
											text.append(" added to " + category);
											String toastOutput = text
													.toString();
											Toast.makeText(context,
													toastOutput,
													Toast.LENGTH_LONG).show();
										} else {
											Toast.makeText(
													context,
													"Both fields must be filled/nPlease try again",
													Toast.LENGTH_LONG).show();
										}
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

		finishedButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				Intent i = new Intent(AddItemsActivity.this,
						ViewListActivity.class);
				Log.d("AddItemsActivity", "Returning to list: listId = "
						+ returnList);
				i.putExtra("listId", returnList);
				startActivity(i);
			}
		});

	}

	/**
	 * Retrieves the list of all items, in all categories from the database.
	 * 
	 * @return ArrayList<Category> The list of all items, in their categories
	 */
	public ArrayList<Category> predefinedCategories() {
		ArrayList<Category> catList = new ArrayList<Category>();
		ArrayList<Category> catsFromDB = (ArrayList<Category>) catData
				.getAllCategories();
		for (Category cat : catsFromDB) {
			ArrayList<Item> items = (ArrayList<Item>) itemData
					.getAllItemsByCat(cat.getCategoryId());
			cat.setItems(items);
			catList.add(cat);
		}
		return catList;
	}

	/**
	 * Overrides the onResume method for Activity class. Reopens the data access
	 * objects used in this class.
	 */
	@Override
	public void onResume() {
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
		listData.close();
		catData.close();
		itemData.close();
		super.onDestroy();
	}
}