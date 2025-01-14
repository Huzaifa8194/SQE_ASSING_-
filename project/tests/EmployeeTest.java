import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import java.util.List;

public class EmployeeTest {
    private EmployeeManagement employeeManagement;
    private static final String TEST_DATABASE = "TestDatabase/testEmployeeDatabase.txt";
    private static final String TEST_TEMP = "TestDatabase/testNewEmployeeDatabase.txt";
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @Before
    public void setUp() throws IOException {
        employeeManagement = new EmployeeManagement();
        EmployeeManagement.employeeDatabase = TEST_DATABASE;
        employeeManagement.temp = TEST_TEMP;
        
        File testDir = new File("TestDatabase");
        testDir.mkdirs();
        try (PrintWriter writer = new PrintWriter(new FileWriter(TEST_DATABASE))) {
            writer.println("1000 Admin John Doe password123");
            writer.println("1001 Cashier Jane Smith pass456");
        }

        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void tearDown() {
        new File(TEST_DATABASE).delete();
        new File(TEST_TEMP).delete();
        new File("TestDatabase").delete();
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void testGetEmployeeList() {
        List<Employee> employees = employeeManagement.getEmployeeList();
        assertEquals(2, employees.size());
        assertEquals("1000", employees.get(0).getUsername());
        assertEquals("1001", employees.get(1).getUsername());
    }

    @Test
    public void testAddEmployee() {
        employeeManagement.add("New Employee", "newpass", true);
        List<Employee> employees = employeeManagement.getEmployeeList();
        assertEquals(3, employees.size());
        Employee newEmployee = employees.get(2);
        assertEquals("1002", newEmployee.getUsername());
        assertEquals("Cashier", newEmployee.getPosition());
        assertEquals("New Employee", newEmployee.getName());
        assertEquals("newpass", newEmployee.getPassword());
    }

    @Test
    public void testAddAdmin() {
        employeeManagement.add("New Admin", "adminpass", false);
        List<Employee> employees = employeeManagement.getEmployeeList();
        assertEquals(3, employees.size());
        Employee newAdmin = employees.get(2);
        assertEquals("1002", newAdmin.getUsername());
        assertEquals("Admin", newAdmin.getPosition());
        assertEquals("New Admin", newAdmin.getName());
        assertEquals("adminpass", newAdmin.getPassword());
    }

    @Test
    public void testDeleteExistingEmployee() {
        boolean result = employeeManagement.delete("1001");
        assertTrue(result);
        List<Employee> employees = employeeManagement.getEmployeeList();
        assertEquals(1, employees.size());
        assertEquals("1000", employees.get(0).getUsername());
    }

    @Test
    public void testDeleteNonExistingEmployee() {
        boolean result = employeeManagement.delete("9999");
        assertFalse(result);
        List<Employee> employees = employeeManagement.getEmployeeList();
        assertEquals(2, employees.size());
    }

    @Test
    public void testUpdateExistingEmployee() {
        int result = employeeManagement.update("1000", "newpass", "Cashier", "Updated Name");
        assertEquals(0, result);
        List<Employee> employees = employeeManagement.getEmployeeList();
        Employee updatedEmployee = employees.get(0);
        assertEquals("1000", updatedEmployee.getUsername());
        assertEquals("Cashier", updatedEmployee.getPosition());
        assertEquals("Updated Name", updatedEmployee.getName());
        assertEquals("newpass", updatedEmployee.getPassword());
    }

    @Test
    public void testUpdateNonExistingEmployee() {
        int result = employeeManagement.update("9999", "newpass", "Cashier", "New Name");
        assertEquals(-1, result);
    }

    @Test
    public void testUpdateWithInvalidPosition() {
        int result = employeeManagement.update("1000", "newpass", "InvalidPosition", "Updated Name");
        assertEquals(-2, result);
    }

    @Test
    public void testUpdateWithEmptyFields() {
        int result = employeeManagement.update("1000", "", "", "");
        assertEquals(0, result);
        List<Employee> employees = employeeManagement.getEmployeeList();
        Employee updatedEmployee = employees.get(0);
        assertEquals("1000", updatedEmployee.getUsername());
        assertEquals("Admin", updatedEmployee.getPosition());
        assertEquals("John Doe", updatedEmployee.getName());
        assertEquals("password123", updatedEmployee.getPassword());
    }

    @Test
    public void testAddWithFileNotFoundException() throws IOException {
        File originalFile = new File(EmployeeManagement.employeeDatabase);
        originalFile.delete();
        
        employeeManagement.add("Test Employee", "testpass", true);
        
        assertTrue(outContent.toString().contains("Unable to open file Log File for logIn"));
    }

    @Test
    public void testAddWithIOException() throws IOException {
        File file = new File(EmployeeManagement.employeeDatabase);
        file.setReadOnly();
        
        employeeManagement.add("Test Employee", "testpass", true);
        
        assertTrue(errContent.toString().contains("java.io.IOException"));
        
        file.setWritable(true);
    }

    @Test
    public void testDeleteWithFileNotFoundException() throws IOException {
        employeeManagement.getEmployeeList(); // Populate the employees list
        File tempFile = new File(employeeManagement.temp);
        tempFile.delete();
        
        employeeManagement.delete("1000");
        
        assertTrue(outContent.toString().contains("Unable to open file 'temp'"));
    }

    @Test
    public void testDeleteWithIOException() throws IOException {
        employeeManagement.getEmployeeList(); // Populate the employees list
        File tempFile = new File(employeeManagement.temp);
        tempFile.createNewFile();
        tempFile.setReadOnly();
        
        employeeManagement.delete("1000");
        
        assertTrue(outContent.toString().contains("Error reading file 'temp'"));
        
        tempFile.setWritable(true);
        tempFile.delete();
    }

    @Test
    public void testUpdateWithFileNotFoundException() throws IOException {
        File tempFile = new File(employeeManagement.temp);
        tempFile.delete();
        
        employeeManagement.update("1000", "newpass", "Cashier", "Updated Name");
        
        assertTrue(outContent.toString().contains("Unable to open file 'temp'"));
    }

    @Test
    public void testUpdateWithIOException() throws IOException {
        File tempFile = new File(employeeManagement.temp);
        tempFile.createNewFile();
        tempFile.setReadOnly();
        
        employeeManagement.update("1000", "newpass", "Cashier", "Updated Name");
        
        assertTrue(outContent.toString().contains("Error reading file 'temp'"));
        
        tempFile.setWritable(true);
        tempFile.delete();
    }

    @Test
    public void testReadFileWithFileNotFoundException() {
        EmployeeManagement.employeeDatabase = "NonExistentFile.txt";
        
        employeeManagement.readFile();
        
        assertTrue(outContent.toString().contains("Unable to open file 'NonExistentFile.txt'"));
    }

    @Test
    public void testReadFileWithIOException() throws IOException {
        File file = new File(EmployeeManagement.employeeDatabase);
        file.setReadOnly();
        
        employeeManagement.readFile();
        
        assertTrue(outContent.toString().contains("Error reading file '" + EmployeeManagement.employeeDatabase + "'"));
        
        file.setWritable(true);
    }

    @Test
    public void testOSCheck() {
        String originalOS = System.getProperty("os.name");
        try {
            System.setProperty("os.name", "Windows 10");
            employeeManagement.readFile();
        } finally {
            System.setProperty("os.name", originalOS);
        }
    }
}