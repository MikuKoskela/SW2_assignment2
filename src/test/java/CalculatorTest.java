import model.Calculator;
import model.Item;
import model.ShoppingCart;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class CalculatorTest {

    @Test
    void testTotalCostForItems() {
        Calculator calc = new Calculator();

        List<Item> items = new ArrayList<>();
        items.add(new Item(10, 2)); // 20
        items.add(new Item(5, 3));  // 15

        double total = calc.totalCost(items);

        assertEquals(35, total);
    }

    @Test
    void testShoppingCartValue() {
        Calculator calc = new Calculator();
        ShoppingCart cart = new ShoppingCart();

        cart.addItem(new Item(10, 2)); // 20
        cart.addItem(new Item(5, 3));  // 15

        double total = calc.shoppingCartValue(cart);

        assertEquals(35, total);
    }

    @Test
    void testEmptyCartValue() {
        Calculator calc = new Calculator();
        ShoppingCart cart = new ShoppingCart();

        assertEquals(0, calc.shoppingCartValue(cart));
    }

    @Test
    void testZeroValues() {
        Calculator calc = new Calculator();

        List<Item> items = new ArrayList<>();
        items.add(new Item(0, 10));
        items.add(new Item(10, 0));

        assertEquals(0, calc.totalCost(items));
    }
}