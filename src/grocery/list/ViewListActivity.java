package grocery.list;

import java.util.ArrayList;

import Data.ActiveListsData;
import Data.CategoryData;
import Model.Category;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;

/**
 * Displays the list of items in a shopping list based on the listId passed
 * through the intent.
 * 
 * @author Marta Smith
 * @author Michael Williams
 * @author William Kofod
 * @version 1.3
 * 
 */
public class ViewListActivity extends Activity {

	private ExpandListAdapter expAdapter;
	private ArrayList<Category> expCategories;
	private ExpandableListView expList;
	private Button addItemsButton;
	private Button returnButton;
	private ActiveListsData listData;
	private long listId;
	private CategoryData catData;

	/**
	 * Creates the view for this activity.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_list);
		Intent intent = getIntent();
		listId = intent.getLongExtra("listId", 1);
		Log.d("ViewListActivity", "listId passed from SelectListActivity: "
				+ listId);
		catData = new CategoryData(this);
		catData.open();
		listData = new ActiveListsData(this);
		listData.open();

		addItemsButton = (Button) findViewById(R.id.add_from_list);
		addItemsButton.setOnClickListener(new View.OnClickListener() {

			/*
			 * (non-Javadoc) Sets the action to perform when the Add Items
			 * button is clicked.
			 * 
			 * @see android.view.View.OnClickListener#onClick(android.view.View)
			 */
			@Override
			public void onClick(View v) {
				Intent i = new Intent(ViewListActivity.this,
						AddItemsActivity.class);
				i.putExtra("listId", listId);
				startActivity(i);
			}
		});

		returnButton = (Button) findViewById(R.id.returnToListButton);
		returnButton.setOnClickListener(new View.OnClickListener() {

			/*
			 * (non-Javadoc) Sets the action to perform when the Return button
			 * is clicked.
			 * 
			 * @see android.view.View.OnClickListener#onClick(android.view.View)
			 */
			@Override
			public void onClick(View v) {
				Intent i = new Intent(ViewListActivity.this, MainActivity.class);
				startActivity(i);

			}
		});

		expList = (ExpandableListView) findViewById(R.id.view_expList);
		expCategories = setCategories();
		expAdapter = new ExpandListAdapter(ViewListActivity.this,
				expCategories, listId, listData, catData);
		expList.setAdapter(expAdapter);
	}

	/**
	 * Retrieves all items to be displayed in the current shopping list from the
	 * database.
	 * 
	 * @return ArrayList of all categories in the list, which each contains an
	 *         ArrayList of items in that category
	 */
	public ArrayList<Category> setCategories() {
		ArrayList<Category> catList = new ArrayList<Category>();
		ArrayList<Category> catsFromDB = listData.getListCategories(listId);
		catList.addAll(catsFromDB);
		return catList;

	}

	/**
	 * Overrides the onResume method for Activity class. Reopens the data access
	 * objects used in this class.
	 */
	@Override
	public void onResume() {
		Log.d("ViewListActivity", "Entering onResume()");
		expAdapter.notifyDataSetChanged();
		listData.open();
		catData.open();
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
		super.onDestroy();
	}

}