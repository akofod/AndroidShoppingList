package Model;

import junit.framework.TestCase;


/**
 * Test class for ShoppingList
 * @author Marta Smith
 * @author Michael Williams
 * @author William Kofod
 * @version 1.3
 *
 */
public class ShoppingListTest extends TestCase
{
    private ShoppingList sl = new ShoppingList("Wal-Mart");
    
    public void testSetGetShoppingListId()
    {
        sl.setShoppingListId(2);
        assertEquals(2, sl.getShoppingListId());
    }
    
    public void testSetGetShoppingListName()
    {
        sl.setShoppingListName("Costco");
        assertEquals("Costco", sl.getShoppingListName());
    }
}