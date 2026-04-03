import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class CalculatorViewTest {

    @Test
    void testComputeTotalForItems() {
        CalculatorView view = new CalculatorView();

        List<Item> items = List.of(
                new Item(10, 2),   // 20
                new Item(5, 3)     // 15
        );

        double total = view.computeTotalForItems(items);
        assertEquals(35.0, total);
    }

    @Test
    void testComputeTotalEmptyList() {
        CalculatorView view = new CalculatorView();

        double total = view.computeTotalForItems(List.of());
        assertEquals(0.0, total);
    }

    @Test
    void testComputeTotalWithZeroValues() {
        CalculatorView view = new CalculatorView();

        List<Item> items = List.of(
                new Item(0, 5),  // 0
                new Item(10, 0)  // 0
        );

        double total = view.computeTotalForItems(items);
        assertEquals(0.0, total);
    }
}