import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class ShoppingCartTest {

    @Test
    void testAddItem() {
        ShoppingCart cart = new ShoppingCart();
        Item item = new Item(10, 2);

        cart.addItem(item);

        List<Item> items = cart.getItems();
        assertEquals(1, items.size());
        assertEquals(item, items.get(0));
    }

    @Test
    void testMultipleItems() {
        ShoppingCart cart = new ShoppingCart();

        cart.addItem(new Item(5, 2));
        cart.addItem(new Item(3, 4));

        assertEquals(2, cart.getItems().size());
    }

    @Test
    void testEmptyCart() {
        ShoppingCart cart = new ShoppingCart();
        assertTrue(cart.getItems().isEmpty());
    }
}