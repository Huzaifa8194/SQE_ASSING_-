import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class POHTest {

    private POH poh;
    private final String TEST_TEMP_FILE = "test_temp.txt";
    private final String TEST_DATABASE_FILE = "test_database.txt";
    private final String TEST_RETURN_SALE_FILE = "test_returnSale.txt";

    @Before
    public void setUp() {
        poh = new POH(1234567890L);
        poh.tempFile = TEST_TEMP_FILE;
    }

    @After
    public void tearDown() {
        new File(TEST_TEMP_FILE).delete();
        new File(TEST_DATABASE_FILE).delete();
        new File(TEST_RETURN_SALE_FILE).delete();
    }

    @Test
    public void testDeleteTempItem() throws IOException {
        // Setup
        createTestTempFile();
        poh.transactionItem = createTestTransactionItems();

        // Test
        poh.deleteTempItem(1);

        // Verify
        List<String> remainingLines = readFile(TEST_TEMP_FILE);
        assertEquals(3, remainingLines.size());
        assertEquals("Type", remainingLines.get(0));
        assertEquals("1234567890", remainingLines.get(1));
        assertEquals("2 1", remainingLines.get(2));
    }

    @Test
    public void testEndPOS() throws IOException {
        // Setup
        poh.transactionItem = createTestTransactionItems();
        poh.totalPrice = 30.0;
        poh.tax = 1.1;
        poh.returnSale = true;
        createTestDatabaseFile();

        // Test
        double result = poh.endPOS(TEST_DATABASE_FILE);

        // Verify
        assertEquals(33.0, result, 0.001);
        assertTrue(poh.transactionItem.isEmpty());
        assertTrue(poh.databaseItem.isEmpty());
        assertFalse(new File(TEST_TEMP_FILE).exists());
        assertTrue(new File(TEST_RETURN_SALE_FILE).exists());
    }

    @Test
    public void testRetrieveTemp() throws IOException {
        // Setup
        createTestTempFile();

        // Test
        poh.retrieveTemp(TEST_DATABASE_FILE);

        // Verify
        assertEquals(2, poh.transactionItem.size());
        assertEquals(1, poh.transactionItem.get(0).getItemID());
        assertEquals(2, poh.transactionItem.get(1).getItemID());
    }

    private void createTestTempFile() throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(TEST_TEMP_FILE))) {
            writer.println("Type");
            writer.println("1234567890");
            writer.println("1 2");
            writer.println("2 1");
        }
    }

    private void createTestDatabaseFile() throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(TEST_DATABASE_FILE))) {
            writer.println("1,Item1,10.0,5");
            writer.println("2,Item2,20.0,3");
        }
    }

    private List<Item> createTestTransactionItems() {
        List<Item> items = new ArrayList<>();
        items.add(new Item(1, "Item1", 10f, 2));
        items.add(new Item(2, "Item2", 20f, 1));
        return items;
    }

    private List<String> readFile(String filename) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }
}