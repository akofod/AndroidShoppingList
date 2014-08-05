package grocery.list;

import java.util.ArrayList;

import Data.ActiveListsData;
import Model.Category;
import Model.Item;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Custom adapter for displaying the ExpandableListView of preset items.
 * 
 * @author Marta Smith
 * @author Michael Williams
 * @author William Kofod
 * @version 1.3
 */
public class PresetItemsListAdapter extends BaseExpandableListAdapter {

	private Context context;
	private ArrayList<Category> categories;
	private long listId;
	private ActiveListsData listData;

	/**
	 * Constructor for the PresetItemsListAdapter.
	 * 
	 * @param inContext
	 * @param inCategories
	 * @param listId
	 * @param listData
	 */
	public PresetItemsListAdapter(Context inContext,
			ArrayList<Category> inCategories, long listId,
			ActiveListsData listData) {
		this.context = inContext;
		this.categories = inCategories;
		this.listId = listId;
		this.listData = listData;
	}

	/**
	 * Adds an item to the list being displayed.
	 * 
	 * @param item
	 *            The item to add
	 * @param category
	 *            The category to add to
	 */
	public void addItem(Item item, Category category) {
		int catIndex = -1;
		boolean found = false;
		for (Category cat : categories) {
			if (cat.getCategoryName().equals(category.getCategoryName())) {
				catIndex = categories.indexOf(cat);
				found = true;
				break;
			}
		}
		if (!found) {
			categories.add(category);
			catIndex = categories.indexOf(category);
		}

		ArrayList<Item> its = categories.get(catIndex).getItems();
		its.add(item);
		categories.get(catIndex).setItems(its);
		notifyDataSetChanged();
	}

	/**
	 * Gets the item from the displayed list at the given index.
	 * 
	 * @param categoryPosition
	 *            The index of the category
	 * @param itemPosition
	 *            The index of the item
	 */
	@Override
	public Object getChild(int categoryPosition, int itemPosition) {
		ArrayList<Item> itemList = categories.get(categoryPosition).getItems();
		return itemList.get(itemPosition);
	}

	/**
	 * Gets the ID of the item at the given position.
	 * 
	 * @param categoryPostion
	 *            The index of the category
	 * @param itemPosition
	 *            The index of the item
	 */
	@Override
	public long getChildId(int categoryPosition, int itemPosition) {
		return itemPosition;
	}

	/**
	 * Gets the view for each child element.
	 * 
	 * @param categoryPosition
	 * @param itemPosition
	 * @param isLastItem
	 * @param view
	 * @param parent
	 */
	@Override
	public View getChildView(int categoryPostion, int itemPosition,
			boolean isLastItem, View view, ViewGroup parent) {
		Item item = (Item) getChild(categoryPostion, itemPosition);
		final String childItemName = item.getItemName();
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.expandlist_item, null);
		}
		TextView tv = (TextView) view.findViewById(R.id.tvItem);
		tv.setText(childItemName);

		// Set click listener for children
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				// get prompt view
				LayoutInflater layoutInflater = LayoutInflater.from(context);
				View promptView = layoutInflater.inflate(
						R.layout.add_to_list_prompt, null);
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						context);
				// set add_to_list_prompt.xml to be the layout file of the alert
				// dialog builder
				alertDialogBuilder.setView(promptView);
				// variables for adding data
				final EditText quantityInput = (EditText) promptView
						.findViewById(R.id.inputAddToListQuantity);
				final EditText unitsInput = (EditText) promptView
						.findViewById(R.id.inputAddToListUnits);

				// setup a dialog window
				alertDialogBuilder
						.setCancelable(false)
						.setPositiveButton("Add Item",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// Add an item to shopping list
										StringBuilder text = new StringBuilder();
										int quantity = 0;
										String units;

										text.append("Item added to list\n");
										if (quantityInput != null) {
											try {
												quantity = Integer
														.valueOf(quantityInput
																.getText()
																.toString());
											} catch (Exception e) {
												Toast.makeText(
														context,
														"Quantity must be a number",
														Toast.LENGTH_LONG)
														.show();
												dialog.cancel();
											}
											text.append(quantity + " ");
										} else {
											Toast.makeText(context,
													"Please enter a quantity",
													Toast.LENGTH_LONG).show();
										}
										if (unitsInput != null) {
											units = unitsInput.getText()
													.toString();
											text.append(units + " ");
										} else {
											units = null;
										}
										text.append(childItemName);

										listData.addItemToList(listId,
												childItemName, units, quantity);
										String textOutput = text.toString();
										Toast.makeText(context, textOutput,
												Toast.LENGTH_LONG).show();
									}
								})
						.setNegativeButton("Cancel",
								new DialogInterface.OnClickListener() {
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
		return view;
	}

	/**
	 * Gets the number of child elements to display for a category.
	 * 
	 * @param categoryPosition
	 *            The index of the category
	 * @return The number of items in the category
	 */
	@Override
	public int getChildrenCount(int categoryPosition) {
		ArrayList<Item> itemList = categories.get(categoryPosition).getItems();
		return itemList.size();
	}

	/**
	 * Gets the category at the given position.
	 * 
	 * @param categoryPosition
	 *            The index of the category
	 * @return Object The Category at the index
	 */
	@Override
	public Object getGroup(int categoryPosition) {
		return categories.get(categoryPosition);
	}

	/**
	 * Gets the number of categories being displayed.
	 * 
	 * @return int The number of categories
	 */
	@Override
	public int getGroupCount() {
		return categories.size();
	}

	/**
	 * Gets the ID for the category at the given position.
	 * 
	 * @param categoryPosition
	 *            The index of the category
	 * @return long The ID of the category
	 */
	@Override
	public long getGroupId(int categoryPosition) {

		return categoryPosition;
	}

	/**
	 * Gets the view to display for each Category.
	 * 
	 * @param categoryPosition
	 * @param isLastItem
	 * @param view
	 * @param parent
	 */
	@Override
	public View getGroupView(int categoryPosition, boolean isLastItem,
			View view, ViewGroup parent) {
		Category Category = (Category) getGroup(categoryPosition);
		if (view == null) {
			@SuppressWarnings("static-access")
			LayoutInflater inf = (LayoutInflater) context
					.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			view = inf.inflate(R.layout.expandlist_category, null);
		}
		TextView tv = (TextView) view.findViewById(R.id.tvCategory);
		tv.setText(Category.getCategoryName());
		return view;
	}

	/**
	 * Sets whether or not this adapter has stable IDs.
	 */
	@Override
	public boolean hasStableIds() {
		return true;
	}

	/**
	 * Sets if the child elements are selectable.
	 */
	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		return true;
	}

}
