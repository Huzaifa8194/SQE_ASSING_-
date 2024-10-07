import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PORTest {

    private POR por;
    private final String TEST_TEMP_FILE = "test_temp.txt";
    private final String TEST_DATABASE_FILE = "test_database.txt";

    @Before
    public void setUp() {
        por = new POR(1234567890L);
        por.tempFile = TEST_TEMP_FILE;
    }

    @After
    public void tearDown() {
        new File(TEST_TEMP_FILE).delete();
        new File(TEST_DATABASE_FILE).delete();
    }

    @Test
    public void testDeleteTempItem() throws IOException {
        // Setup
        createTestTempFile();
        por.transactionItem = createTestTransactionItems();

        // Test
        por.deleteTempItem(1);

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
        por.transactionItem = createTestTransactionItems();
        por.totalPrice = 30.0;
        por.tax = 1.1;
        createTestDatabaseFile();

        // Test
        double result = por.endPOS(TEST_DATABASE_FILE);

        // Verify
        assertEquals(33.0, result, 0.001);
        assertTrue(por.transactionItem.isEmpty());
        assertTrue(por.databaseItem.isEmpty());
        assertFalse(new File(TEST_TEMP_FILE).exists());
    }

    @Test
    public void testRetrieveTemp() throws IOException {
        // Setup
        createTestTempFile();

        // Test
        por.retrieveTemp(TEST_DATABASE_FILE);

        // Verify
        assertEquals(2, por.transactionItem.size());
        assertEquals(1, por.transactionItem.get(0).getItemID());
        assertEquals(2, por.transactionItem.get(1).getItemID());
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