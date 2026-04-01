import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ItemTest {

    @Test
    void testItemCreation() {
        Item item = new Item(10, 3);

        assertEquals(10, item.getValue());
        assertEquals(3, item.getQuantity());
    }

}