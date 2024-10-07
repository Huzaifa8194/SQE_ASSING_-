import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class ManagementTest {

    private Management management;

    @Before
    public void setUp() {
        management = new Management();
    }

    // Test for checkUser method
    @Test
    public void testCheckUser_UserExists() throws IOException {
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("Database/userDatabase.txt")));
        out.println("PhoneNumber OtherDetails");
        out.println("1234567890 SomeOtherDetails");
        out.close();

        Boolean result = management.checkUser(1234567890L);
        assertTrue(result);
    }

    @Test
    public void testCheckUser_UserDoesNotExist() throws IOException {
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("Database/userDatabase.txt")));
        out.println("PhoneNumber OtherDetails");
        out.println("9876543210 SomeOtherDetails");
        out.close();

        Boolean result = management.checkUser(1234567890L);
        assertFalse(result);
    }

    @Test
    public void testCheckUser_FileNotFound() {
        File file = new File("Database/userDatabase.txt");
        if (file.exists()) {
            file.delete();
        }

        Boolean result = management.checkUser(1234567890L);
        assertTrue(result);
    }

    // Test for getLatestReturnDate method
    @Test
    public void testGetLatestReturnDate_UserHasOutstandingReturns() throws IOException {
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("Database/userDatabase.txt")));
        out.println("PhoneNumber ReturnData");
        out.println("1234567890 101,06/30/21,false 102,07/01/21,false");
        out.close();

        List<ReturnItem> result = management.getLatestReturnDate(1234567890L);
        assertEquals(2, result.size());
    }

    @Test
    public void testGetLatestReturnDate_NoOutstandingReturns() throws IOException {
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("Database/userDatabase.txt")));
        out.println("PhoneNumber ReturnData");
        out.println("1234567890 101,06/30/21,true");
        out.close();

        List<ReturnItem> result = management.getLatestReturnDate(1234567890L);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetLatestReturnDate_FileNotFound() {
        File file = new File("Database/userDatabase.txt");
        if (file.exists()) {
            file.delete();
        }

        List<ReturnItem> result = management.getLatestReturnDate(1234567890L);
        assertTrue(result.isEmpty());
    }

    // Test for createUser method
    @Test
    public void testCreateUser_Success() throws IOException {
        File file = new File("Database/userDatabase.txt");
        if (file.exists()) {
            file.delete();
        }

        boolean result = management.createUser(1234567890L);
        assertTrue(result);

        BufferedReader reader = new BufferedReader(new FileReader("Database/userDatabase.txt"));
        String line;
        boolean found = false;
        while ((line = reader.readLine()) != null) {
            if (line.contains("1234567890")) {
                found = true;
            }
        }
        reader.close();
        assertTrue(found);
    }

    @Test
    public void testCreateUser_FileWriteError() {
        File file = new File("Database/userDatabase.txt");
        file.setReadOnly();

        boolean result = management.createUser(1234567890L);
        assertFalse(result);
    }

    // Test for addRental method
    @Test
    public void testAddRental_UserExists() throws IOException {
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("Database/userDatabase.txt")));
        out.println("PhoneNumber ReturnData");
        out.println("1234567890 101,06/30/21,true");
        out.close();

        List<Item> rentalList = new ArrayList<>();
        rentalList.add(new Item(202, "orange", 1.0f, 20));
        rentalList.add(new Item(203, "apple", 1.0f, 10));

        Management.addRental(1234567890L, rentalList);

        BufferedReader reader = new BufferedReader(new FileReader("Database/userDatabase.txt"));
        String line;
        boolean found = false;
        while ((line = reader.readLine()) != null) {
            if (line.contains("202") && line.contains("203")) {
                found = true;
            }
        }
        reader.close();
        assertTrue(found);
    }

    // Test for updateRentalStatus method
    @Test
    public void testUpdateRentalStatus_UserExists() throws IOException {
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("Database/userDatabase.txt")));
        out.println("PhoneNumber ReturnData");
        out.println("1234567890 202,06/30/21,false");
        out.close();

        List<ReturnItem> returnList = new ArrayList<>();
        returnList.add(new ReturnItem(202, 0));

        management.updateRentalStatus(1234567890L, returnList);

        BufferedReader reader = new BufferedReader(new FileReader("Database/userDatabase.txt"));
        String line;
        boolean found = false;
        while ((line = reader.readLine()) != null) {
            if (line.contains("true")) {
                found = true;
            }
        }
        reader.close();
        assertTrue(found);
    }
}
