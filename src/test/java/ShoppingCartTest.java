import model.Item;
import model.ShoppingCart;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShoppingCartTest {

    @Test
    void newCartIsEmptyAndTotalIsZero() {
        ShoppingCart cart = new ShoppingCart();

        assertTrue(cart.getItems().isEmpty());
        assertEquals(0.0, cart.getTotalValue());
    }

    @Test
    void addItemAddsItemAndUpdatesTotal() {
        ShoppingCart cart = new ShoppingCart();

        Item item = mock(Item.class);
        when(item.getValue()).thenReturn(10.0);
        when(item.getQuantity()).thenReturn(2);

        cart.addItem(item);

        assertEquals(1, cart.getItems().size());
        assertEquals(item, cart.getItems().get(0));
        assertEquals(20.0, cart.getTotalValue());
    }

    @Test
    void multipleItemsAccumulateTotalCorrectly() {
        ShoppingCart cart = new ShoppingCart();

        Item item1 = mock(Item.class);
        when(item1.getValue()).thenReturn(5.0);
        when(item1.getQuantity()).thenReturn(3); // 15

        Item item2 = mock(Item.class);
        when(item2.getValue()).thenReturn(7.5);
        when(item2.getQuantity()).thenReturn(2); // 15

        cart.addItem(item1);
        cart.addItem(item2);

        assertEquals(2, cart.getItems().size());
        assertEquals(30.0, cart.getTotalValue());
    }

    @Test
    void removeItemRemovesItemAndUpdatesTotal() {
        ShoppingCart cart = new ShoppingCart();

        Item item = mock(Item.class);
        when(item.getValue()).thenReturn(12.0);
        when(item.getQuantity()).thenReturn(1);

        cart.addItem(item);
        cart.removeItem(item);

        assertTrue(cart.getItems().isEmpty());
        assertEquals(0.0, cart.getTotalValue());
    }

    @Test
    void getItemsReturnsLiveList() {
        ShoppingCart cart = new ShoppingCart();

        List<Item> items = cart.getItems();
        assertNotNull(items);
        assertTrue(items.isEmpty());
    }
}