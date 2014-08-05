package Model;

import junit.framework.TestCase;


/**
 * Test class for Item
 * @author Marta Smith
 * @author Michael Williams
 * @author William Kofod
 * @version 1.3
 *
 */
public class ItemTest extends TestCase
{
    private Item item = new Item("Milk", 1, "gallon");
    
    public void testSetGetItemId()
    {
        item.setItemId(1);
        assertEquals(1, item.getItemId());
    }
    
    public void testSetGetItemName()
    {
        item.setItemName("Cookies");
        assertEquals("Cookies", item.getItemName());
    }
    
    public void testSetGetQuantity()
    {
        item.setQuantity(2);
        assertEquals(2, item.getQuantity());
    }
    
    public void testSetGetUnitType()
    {
        item.setUnitType("Gallon");
        assertEquals("Gallon", item.getUnitType());
    }
    
    public void testToString()
    {
        String expected = "  1 gallon    Milk";
        assertEquals(expected, item.toString());
    }
    
    public void testSetGetCatId()
    {
        item.setCatId(4);
        assertEquals(4, item.getCatId());
        
    }
    
    public void testSetGetSLId()
    {
        item.setSlId(9);
        assertEquals(9, item.getSlId());
    }
}