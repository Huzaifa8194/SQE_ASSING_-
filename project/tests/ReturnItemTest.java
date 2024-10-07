import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ReturnItemTest {

    @Test
    public void testConstructorAndGetters() {
        // Create a new instance of ReturnItem
        ReturnItem returnItem = new ReturnItem(12345, 10);

        // Test getItemID method
        assertEquals(12345, returnItem.getItemID());

        // Test getDays method
        assertEquals(10, returnItem.getDays());
    }
}
