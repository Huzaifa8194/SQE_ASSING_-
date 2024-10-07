import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ItemTest {

    private Item item;

    @Before
    public void setUp() {
        // Initialize an Item object before each test
        item = new Item(1, "TestItem", 19.99f, 10);
    }

    @Test
    public void testConstructor() {
        // Verify that the constructor initializes attributes correctly
        assertEquals(1, item.getItemID());
        assertEquals("TestItem", item.getItemName());
        assertEquals(19.99f, item.getPrice(), 0.001);
        assertEquals(10, item.getAmount());
    }

    @Test
    public void testUpdateAmount() {
        // Update the amount and verify the change
        item.updateAmount(15);
        assertEquals(15, item.getAmount());

        // Update to a lower amount and verify
        item.updateAmount(5);
        assertEquals(5, item.getAmount());

        // Update to zero amount and verify
        item.updateAmount(0);
        assertEquals(0, item.getAmount());
    }

    @Test
    public void testGetters() {
        // Verify that the getter methods return correct values
        assertEquals("TestItem", item.getItemName());
        assertEquals(1, item.getItemID());
        assertEquals(19.99f, item.getPrice(), 0.001);
        assertEquals(10, item.getAmount());
    }
}
