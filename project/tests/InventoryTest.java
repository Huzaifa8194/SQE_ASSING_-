import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryTest {

    private Inventory inventory;
    private List<Item> databaseItem; // Assuming Item is a class you've defined
    private List<Item> transactionItem;

    @Before
    public void setUp() {
        inventory = Inventory.getInstance();
        databaseItem = new ArrayList<>();
        transactionItem = new ArrayList<>();
    }

    @Test
    public void testAccessInventorySuccess() throws IOException {
        // Create a test file
        String testFileName = "testDatabase.txt";
        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(testFileName)));
        writer.println("1 ItemA 10.0 5");
        writer.println("2 ItemB 20.0 10");
        writer.close();

        // Test accessInventory
        boolean result = inventory.accessInventory(testFileName, databaseItem);
        assertTrue(result);
        assertEquals(2, databaseItem.size()); // Ensure two items are read

        // Clean up
        new File(testFileName).delete();
    }

    @Test
    public void testAccessInventoryFileNotFound() {
        // Test with a non-existent file
        boolean result = inventory.accessInventory("nonExistentFile.txt", databaseItem);
        assertFalse(result);
        assertEquals(0, databaseItem.size()); // Ensure no items are read
    }

    @Test
    public void testUpdateInventoryRental() {
        // Setup test data
        Item itemA = new Item(1, "ItemA", 10.0f, 5);
        Item itemB = new Item(2, "ItemB", 20.0f, 10);
        databaseItem.add(itemA);
        databaseItem.add(itemB);

        // Adding transaction item to take from inventory
        transactionItem.add(new Item(1, "ItemA", 10.0f, 2)); // Renting 2 of ItemA

        // Update inventory
        inventory.updateInventory("testDatabase.txt", transactionItem, databaseItem, true);

        // Verify the inventory was updated
        assertEquals(3, databaseItem.get(0).getAmount()); // 5 - 2 = 3 for ItemA
        assertEquals(10, databaseItem.get(1).getAmount()); // ItemB should remain unchanged
    }

    @Test
    public void testUpdateInventoryReturn() {
        // Setup test data
        Item itemA = new Item(1, "ItemA", 10.0f, 5);
        Item itemB = new Item(2, "ItemB", 20.0f, 10);
        databaseItem.add(itemA);
        databaseItem.add(itemB);

        // Adding transaction item to return to inventory
        transactionItem.add(new Item(1, "ItemA", 10.0f, 2)); // Returning 2 of ItemA

        // Update inventory
        inventory.updateInventory("testDatabase.txt", transactionItem, databaseItem, false);

        // Verify the inventory was updated
        assertEquals(7, databaseItem.get(0).getAmount()); // 5 + 2 = 7 for ItemA
        assertEquals(10, databaseItem.get(1).getAmount()); // ItemB should remain unchanged
    }
}
