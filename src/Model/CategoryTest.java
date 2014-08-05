package Model;

import junit.framework.TestCase;
import java.util.ArrayList;


/**
 * Test class for Category
 * @author Marta Smith
 * @author Michael Williams
 * @author William Kofod
 * @version 1.3
 *
 */
public class CategoryTest extends TestCase
{
    private Category cat = new Category("Dairy");
    private Item item = new Item("Milk", 1, "gallon");
    
    public void testSetGetCategoryId()
    {
        cat.setCategoryId(3);
        assertEquals(3, cat.getCategoryId());
    }
    
    public void testSetGetCategoryName()
    {
        cat.setCategoryName("Meat");
        assertEquals("Meat", cat.getCategoryName());
    }
    
    public void testSetGetItems()
    {
        ArrayList<Item> i = new ArrayList<Item>();
        i.add(item);
        cat.setItems(i);
        assertEquals(i, cat.getItems());
    }
    
    public void testAddItemToList()
    {
        ArrayList<Item> i = new ArrayList<Item>();
        i.add(item);
        cat.addItemToList(item);
        assertEquals(i, cat.getItems());
    }
}
