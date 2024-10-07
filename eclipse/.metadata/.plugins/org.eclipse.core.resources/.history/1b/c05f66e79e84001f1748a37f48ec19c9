import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.*;
import java.util.*;

public class PointOfSaleTest {

    private TestPointOfSale pos;
    private final String TEST_COUPON_FILE = "test_couponNumber.txt";
    private final String TEST_TEMP_FILE = "test_temp.txt";
    private final String TEST_DATABASE_FILE = "test_database.txt";

    private class TestPointOfSale extends PointOfSale {
        @Override
        public double endPOS(String textFile) {
            return totalPrice;
        }

        @Override
        public void deleteTempItem(int id) {
            // Implementation for testing
        }

        @Override
        public void retrieveTemp(String textFile) {
            // Implementation for testing
        }
    }

    @Before
    public void setUp() {
        pos = new TestPointOfSale();
        pos.tempFile = TEST_TEMP_FILE;
        pos.couponNumber = TEST_COUPON_FILE;
    }

    @After
    public void tearDown() {
        new File(TEST_COUPON_FILE).delete();
        new File(TEST_TEMP_FILE).delete();
        new File(TEST_DATABASE_FILE).delete();
    }

    @Test
    public void testStartNew() throws IOException {
        createTestDatabaseFile();
        assertTrue(pos.startNew(TEST_DATABASE_FILE));
        assertEquals(2, pos.databaseItem.size());
    }

    @Test
    public void testEnterItem() {
        pos.databaseItem.add(new Item(1, "Item1", 10f, 5));
        pos.databaseItem.add(new Item(2, "Item2", 20.0f, 3));

        assertTrue(pos.enterItem(1, 2));
        assertFalse(pos.enterItem(3, 1));
        assertEquals(1, pos.transactionItem.size());
    }

    @Test
    public void testUpdateTotal() {
        pos.transactionItem.add(new Item(1, "Item1", 10.0f, 2));
        pos.transactionItem.add(new Item(2, "Item2", 20.0f, 1));
        assertEquals(40.0, pos.updateTotal(), 0.001);
    }

    @Test
    public void testCoupon() throws IOException {
        createTestCouponFile();
        pos.totalPrice = 100.0;
        assertTrue(pos.coupon("VALID10"));
        assertEquals(90.0, pos.totalPrice, 0.001);
        assertFalse(pos.coupon("INVALID"));
    }

    @Test
    public void testCreateTemp() throws IOException {
        pos.createTemp(1, 2);
        List<String> lines = readFile(TEST_TEMP_FILE);
        assertEquals("1 2", lines.get(0));
    }

    @Test
    public void testRemoveItems() {
        pos.transactionItem.add(new Item(1, "Item1", 10.0f, 2));
        pos.transactionItem.add(new Item(2, "Item2", 20.0f, 1));
        pos.totalPrice = 40.0;

        assertTrue(pos.removeItems(1));
        assertEquals(20.0, pos.totalPrice, 0.001);
        assertEquals(1, pos.transactionItem.size());

        assertFalse(pos.removeItems(3));
    }

    @Test
    public void testGetTotal() {
        pos.totalPrice = 50.0;
        assertEquals(50.0, pos.getTotal(), 0.001);
    }

    @Test
    public void testDetectSystem() {
        pos.detectSystem();
        // This test is system-dependent, so we can't assert much without mocking
        // Just ensure it doesn't throw an exception
    }

    @Test
    public void testCreditCard() {
        assertTrue(pos.creditCard("1234567890123456"));
        assertFalse(pos.creditCard("12345"));
        assertFalse(pos.creditCard("1234567890abcdef"));
    }

    @Test
    public void testLastAddedItem() {
        pos.transactionItem.add(new Item(1, "Item1", 10.0f, 2));
        pos.transactionItem.add(new Item(2, "Item2", 20.0f, 1));
        assertEquals(2, pos.lastAddedItem().getItemID());
    }

    @Test
    public void testGetCart() {
        pos.transactionItem.add(new Item(1, "Item1", 10.0f, 2));
        pos.transactionItem.add(new Item(2, "Item2", 20.0f, 1));
        assertEquals(2, pos.getCart().size());
    }

    @Test
    public void testGetCartSize() {
        pos.transactionItem.add(new Item(1, "Item1", 10.0f, 2));
        pos.transactionItem.add(new Item(2, "Item2", 20.0f, 1));
        assertEquals(2, pos.getCartSize());
    }

    private void createTestDatabaseFile() throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(TEST_DATABASE_FILE))) {
            writer.println("1,Item1,10.0,5");
            writer.println("2,Item2,20.0,3");
        }
    }

    private void createTestCouponFile() throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(TEST_COUPON_FILE))) {
            writer.println("VALID10");
            writer.println("VALID20");
        }
    }

    private List<String> readFile(String filename) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }
}