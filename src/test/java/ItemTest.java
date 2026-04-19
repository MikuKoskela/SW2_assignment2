import model.Item;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ItemTest {

    @Test
    void testItemCreation() {
        Item item = new Item(10, 3);

        assertEquals(10, item.getValue());
        assertEquals(3, item.getQuantity());
    }

    @Test
    void testSetPrice() {
        Item item = new Item(5, 1);

        item.setPrice(20.5);

        assertEquals(20.5, item.getValue());
    }

    @Test
    void testSetQuantity() {
        Item item = new Item(5, 1);

        item.setQuantity(10);

        assertEquals(10, item.getQuantity());
    }

    @Test
    void testUpdateBothValues() {
        Item item = new Item(1, 1);

        item.setPrice(99.99);
        item.setQuantity(7);

        assertEquals(99.99, item.getValue());
        assertEquals(7, item.getQuantity());
    }

    @Test
    void testZeroValues() {
        Item item = new Item(0, 0);

        assertEquals(0, item.getValue());
        assertEquals(0, item.getQuantity());
    }

    @Test
    void testNegativeValues() {
        Item item = new Item(-10, -2);

        assertEquals(-10, item.getValue());
        assertEquals(-2, item.getQuantity());
    }

    @Test
    void testDecimalPricePrecision() {
        Item item = new Item(10.12345, 1);

        assertEquals(10.12345, item.getValue());
    }
}