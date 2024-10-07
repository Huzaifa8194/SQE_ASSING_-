import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;

public class POSSystemTest {

    private POSSystem posSystem;
    private final String TEST_EMPLOYEE_DB = "test_employeeDatabase.txt";
    private final String TEST_RENTAL_DB = "test_rentalDatabase.txt";
    private final String TEST_ITEM_DB = "test_itemDatabase.txt";
    private final String TEST_TEMP_FILE = "test_temp.txt";
    private final String TEST_LOG_FILE = "test_employeeLogfile.txt";

    @Before
    public void setUp() throws Exception {
        posSystem = new POSSystem();
        posSystem.employeeDatabase = TEST_EMPLOYEE_DB;
        posSystem.rentalDatabaseFile = TEST_RENTAL_DB;
        posSystem.itemDatabaseFile = TEST_ITEM_DB;
        posSystem.tempFile = TEST_TEMP_FILE;
        createTestEmployeeDB();
    }

    @After
    public void tearDown() {
        new File(TEST_EMPLOYEE_DB).delete();
        new File(TEST_RENTAL_DB).delete();
        new File(TEST_ITEM_DB).delete();
        new File(TEST_TEMP_FILE).delete();
        new File(TEST_LOG_FILE).delete();
    }

    @Test
    public void testReadFile() {
        posSystem.readFile();
        assertEquals(2, posSystem.employees.size());
        assertEquals("John", posSystem.employees.get(0).getName());
        assertEquals("Admin", posSystem.employees.get(1).getPosition());
    }

    @Test
    public void testLogInToFile() throws Exception {
        posSystem.logInToFile("user1", "John Doe", "Cashier", Calendar.getInstance());
        List<String> logLines = readFile(TEST_LOG_FILE);
        assertTrue(logLines.get(0).contains("John Doe (user1 Cashier) logs into POS System"));
    }

    @Test
    public void testCheckTemp() throws IOException {
        assertFalse(posSystem.checkTemp());
        new FileWriter(TEST_TEMP_FILE).close();
        assertTrue(posSystem.checkTemp());
    }

    @Test
    public void testContinueFromTemp() throws IOException {
        createTestTempFile("Sale");
        assertEquals("Sale", posSystem.continueFromTemp(1234567890L));

        createTestTempFile("Rental");
        assertEquals("Rental", posSystem.continueFromTemp(1234567890L));

        createTestTempFile("Return");
        assertEquals("Return", posSystem.continueFromTemp(1234567890L));

        new File(TEST_TEMP_FILE).delete();
        assertEquals("", posSystem.continueFromTemp(1234567890L));
    }

    @Test
    public void testLogOutToFile() throws Exception {
        posSystem.logOutToFile("user1", "John Doe", "Cashier", Calendar.getInstance());
        List<String> logLines = readFile(TEST_LOG_FILE);
        assertTrue(logLines.get(0).contains("John Doe (user1 Cashier) logs out of POS System"));
    }

    @Test
    public void testLogOut() {
        posSystem.username = "user1";
        posSystem.name = "John Doe";
        posSystem.logOut("Cashier");
        List<String> logLines = readFile(TEST_LOG_FILE);
        assertTrue(logLines.get(0).contains("John Doe (user1 Cashier) logs out of POS System"));
    }

    @Test
    public void testLogIn() {
        assertEquals(1, posSystem.logIn("user1", "pass1"));
        assertEquals(2, posSystem.logIn("user2", "pass2"));
        assertEquals(0, posSystem.logIn("user3", "pass3"));
        assertEquals(0, posSystem.logIn("user1", "wrongpass"));
    }

    private void createTestEmployeeDB() throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(TEST_EMPLOYEE_DB))) {
            writer.println("user1 pass1 John Doe Cashier");
            writer.println("user2 pass2 Jane Smith Admin");
        }
    }

    private void createTestTempFile(String type) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(TEST_TEMP_FILE))) {
            writer.println(type);
            writer.println("1234567890");
            writer.println("1 2");
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