package grocery.list;

import java.util.ArrayList;

import Data.ActiveListsData;
import Data.CategoryData;
import Model.Category;
import Model.Item;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Adapter used to display the ExpandableListView of items
 * in the ViewListActivity.
 * @author Marta Smith
 * @author Michael Williams
 * @author William Kofod
 * @version 1.3
 *
 */
public class ExpandListAdapter extends BaseExpandableListAdapter {

	private Context context;
	private ArrayList<Category> categories;
	private ActiveListsData listData;
	private CategoryData catData;
	private long listId;
	
	/**
	 * Constructor for the ExpandableListAdapter
	 * @param inContext the context that is creating the adapter
	 * @param inCategories The ArrayList of Categories to be displayed
	 * @param listId The id of the current shopping list to display
	 * @param listData Reference to the ActiveListsData data access object
	 * that was opened in the ViewListActivity.
	 * @param catData Reference to the CategoryData data access object
	 * that was opened in the ViewListActivity.
	 */
	public ExpandListAdapter(Context inContext, ArrayList<Category> inCategories, long listId, ActiveListsData listData, CategoryData catData) {
		this.context = inContext;
		this.categories = inCategories;
		this.listId = listId;
		this.listData = listData;
		this.catData = catData;
	}
	
	/**
	 * Adds an item to the ArrayList being displayed, 
	 * and notifies the adapter to update the view.
	 * @param item The item to be added
	 * @param category The category to add the item to.
	 */
	public void addItem(Item item, Category category) {
		boolean found = false;
		int index = -1;
		for (Category cat : categories) {
			if (cat.getCategoryId() == category.getCategoryId()) {
				index = categories.indexOf(cat);
				found = true;
				break;
			}
		}
		
		if (!found) {
			categories.add(category);
			index = categories.indexOf(category);
		}
		
		ArrayList<Item> its = categories.get(index).getItems();
		its.add(item);
		categories.get(index).setItems(its);
		notifyDataSetChanged();
	}
	
	/**
	 * Removes an item from the ArrayList being displayed,
	 * and notifies the adapter to update the view.
	 * @param item The item to remove
	 * @param category The category to remove the item from
	 */
	public void removeItem(Item item, Category category) {
		Log.d("ExpandListAdapter", "Calling remove");
		Log.d("ExpandListAdapter", "Item passed in: " + item);
		String catName = category.getCategoryName();
		Log.d("ExpandListAdapter", "Category passed in: " + catName);
		int catIndex = -1;
		for (Category cat : categories) {
			if (cat.getCategoryName().equals(catName)) {
				catIndex = categories.indexOf(cat);
				break;
			}
		}
		Log.d("ExpandListAdapter", "Found index of category: " + catIndex);
		categories.get(catIndex).getItems().remove(item);
		Log.d("ExpandListAdapter", "Item removed successfully");
		if (categories.get(catIndex).getItems().isEmpty()) {
			categories.remove(catIndex);
		}
		notifyDataSetChanged();
	}
	
	/**
	 * Retrieves the child at the specified index position.
	 * @param categoryPosition The index of the category
	 * @param itemPosition The index of the item
	 * @return The item being retrieved
	 */
	@Override
	public Object getChild(int categoryPosition, int itemPosition) {
		ArrayList<Item> itemList = categories.get(categoryPosition).getItems();
		return itemList.get(itemPosition);
	}

	/**
	 * Retrieves the id of the item.
	 * @param categoryPosition The index of the category
	 * @param itemPosition The index of the item
	 * @return the id of the item
	 */
	@Override
	public long getChildId(int categoryPosition, int itemPosition) {
		return itemPosition;
	}

	/**
	 * Displays the view for individual items
	 */
	@Override
	public View getChildView(int categoryPostion, int itemPosition, boolean isLastItem, View view,
			ViewGroup parent) {
		Item item = (Item) getChild(categoryPostion, itemPosition);
		final Item itemForAction = item;
		final long childItemId = item.getItemId();
		final String childItemName = item.getItemName();
		final long childQuantity = item.getQuantity();
		final String childUnits = item.getUnitType();
		final long childItemCat = item.getCatId();
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.expandlist_item, null);
		}
		TextView tv = (TextView) view.findViewById(R.id.tvItem);
		tv.setText(item.toString());
		
		//Set click listener for children
		view.setOnClickListener(new OnClickListener() {
			/*
			 * (non-Javadoc)
			 * Sets the action when a child item is clicked.
			 * @see android.view.View.OnClickListener#onClick(android.view.View)
			 */
			@Override
			public void onClick(View view) {
				
				//get prompt view
				LayoutInflater layoutInflater = LayoutInflater.from(context);
				View promptView = layoutInflater.inflate(R.layout.item_picked_prompt, null);
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
				// set item_picked_prompt.xml to be the layout file of the alert dialog builder
				alertDialogBuilder.setView(promptView);
				
				// setup a dialog window
				alertDialogBuilder.setCancelable(false).setNegativeButton("Got it!", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						Log.d("ExpandListAdapter", "Got it! button was clicked");
						StringBuilder text = new StringBuilder();
						text.append("You got ");
						if (childQuantity != 0) {
							text.append(childQuantity + " ");
						}
						if (childUnits != null) {
							text.append(childUnits + " ");
						}
						text.append(childItemName);
						Log.d("ExpandListAdapter", "Output text set");
						String textOutput = text.toString();
						Log.d("ExpandListAdapter", "Getting entry id");
						Log.d("ExpandListAdapter", "Passing childItemId: " + childItemId);
						Log.d("ExpandListAdapter", "Passing listId: " + listId);
						long entryId = listData.findEntryId(childItemId, listId);
						Log.d("ExpandListAdapter", "Found entry id: " + entryId);
						listData.removeEntry(entryId);
						Log.d("ExpandListAdapter", "Execution of removeEntry completed");
						Toast.makeText(context, textOutput,
		                        Toast.LENGTH_SHORT).show();
						removeItem(itemForAction, catData.getCategoryById(childItemCat));
					}
				}).setPositiveButton("Edit Item", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						//open dialog to edit item
						//get prompt view
						LayoutInflater layoutInflater = LayoutInflater.from(context);
						View promptView = layoutInflater.inflate(R.layout.edit_item_prompt, null);
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
						// set prompts.xml to be the layout file of the alert dialog builder
						alertDialogBuilder.setView(promptView);
						final EditText quantityInput = (EditText) promptView.findViewById(R.id.inputItemQuantity);
						final EditText unitsInput = (EditText) promptView.findViewById(R.id.inputItemUnits);
						
						// setup a dialog window
						alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								
								long quantity = childQuantity;
								String unitType = null;
								StringBuilder text = new StringBuilder();
								
								text.append("Item updated:\n");
								text.append(childQuantity + " ");
								if (childUnits != null) {
									text.append(childUnits + " ");
								}
								text.append(childItemName);
								text.append("\nchanged to: \n");
								if (quantityInput != null) {
									try {
										quantity = Long.valueOf(quantityInput.getText().toString());
									} 
									catch (Exception e) {
										Toast.makeText(context, "Quantity must be a number", Toast.LENGTH_LONG);
										dialog.cancel();
									}
									text.append(quantity + " ");
								}
								else {
									text.append(childQuantity + " ");
									quantity = childQuantity;
								}
								if (unitsInput != null) {
									unitType = unitsInput.getText().toString();
									text.append(unitType + " ");
								}
								else {
									text.append(childUnits + " ");
									unitType = childUnits;
								}
								
								
								text.append(childItemName);
								
								listData.editItem(childItemId, listId, Long.valueOf(quantity), unitType);
								itemForAction.setQuantity(Long.valueOf(quantity));
								itemForAction.setUnitType(unitType);
								removeItem(itemForAction, catData.getCategoryById(childItemCat));
								addItem(itemForAction, catData.getCategoryById(childItemCat));
								
								
								String textOutput = text.toString();
								Toast.makeText(context, textOutput,
				                        Toast.LENGTH_LONG).show();
							}
						}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
						
						// create an alert dialog
						AlertDialog alertD = alertDialogBuilder.create();
						alertD.show();
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
	 * Gets the number of items in a category.
	 * @param categoryPosition The index of the category
	 * @return int The number of items
	 */
	@Override
	public int getChildrenCount(int categoryPosition) {
		ArrayList<Item> itemList = categories.get(categoryPosition).getItems();
		return itemList.size();
	}

	/**
	 * Gets the Category at the given index.
	 * @param categoryPosition The index of the category
	 * @return Object The Category
	 */
	@Override
	public Object getGroup(int categoryPosition) {
		return categories.get(categoryPosition);
	}

	/**
	 * Gets the number of Categories to display.
	 * @return int The number of Categories
	 */
	@Override
	public int getGroupCount() {
		return categories.size();
	}

	/**
	 * Gets the ID of the category at the given position.
	 * @param categoryPosition The index of the category
	 * @return long The ID of the Category
	 */
	@Override
	public long getGroupId(int categoryPosition) {
		
		return categoryPosition;
	}

	/**
	 * Gets the view for each Category to be displayed.
	 * @param categoryPosition
	 * @param isLastItem
	 * @param view
	 * @param parent
	 */
	@Override
	public View getGroupView(int categoryPosition, boolean isLastItem, View view, ViewGroup parent) {
		Category category = (Category) getGroup(categoryPosition);
		if (view == null) {
			@SuppressWarnings("static-access")
			LayoutInflater inf = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			view = inf.inflate(R.layout.expandlist_category, null);
		}
		TextView tv = (TextView) view.findViewById(R.id.tvCategory);
		tv.setText(category.getCategoryName());
		return view;
	}

	/**
	 * Sets whether or not the adapter has stable IDs.
	 */
	@Override
	public boolean hasStableIds() {
		return true;
	}

	/**
	 * Sets whether or not each child element is selectable.
	 */
	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		return true;
	}

}