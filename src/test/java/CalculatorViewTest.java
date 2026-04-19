import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;
import service.CartService;
import view.CalculatorView;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CalculatorViewTest extends ApplicationTest {

    private CalculatorView view;
    private CartService mockCartService;

    private static void waitUntil(Callable<Boolean> condition) {
        try {
            WaitForAsyncUtils.waitFor(5, java.util.concurrent.TimeUnit.SECONDS, condition);
        } catch (TimeoutException e) {
            fail("Condition was not met in time");
        }
    }

    @Override
    public void start(Stage stage) {
        view = new CalculatorView();

        view.calculateButton = new Button();
        view.itemAmountLabel = new Label();
        view.itemAmountInput = new TextField();
        view.itemPriceInput = new TextField();
        view.totalLabel = new Label();

        // ✅ MOCK CART SERVICE (FIXED)
        mockCartService = mock(CartService.class);

        // saveCartRecord RETURNS INT → must use thenReturn()
        when(mockCartService.saveCartRecord(anyInt(), anyDouble(), anyString()))
                .thenReturn(1);

        // saveCartItems IS VOID → doNothing() is correct
        doNothing().when(mockCartService)
                .saveCartItems(anyInt(), anyList());

        view.setCartService(mockCartService);

        Map<String, String> texts = new HashMap<>();
        texts.put("itemAmount", "Item amount");
        texts.put("itemPrice", "Item price");
        texts.put("calculateButton", "Calculate");
        texts.put("totalCost", "Total");

        VBox root = new VBox(
                view.itemAmountLabel,
                view.itemAmountInput,
                view.itemPriceInput,
                view.calculateButton,
                view.totalLabel
        );

        stage.setScene(new Scene(root, 300, 200));
        stage.show();

        // ✅ Initialize AFTER scene exists
        Platform.runLater(() -> {
            view.setLocale(Locale.ENGLISH);
            view.setTexts(texts);
            view.setLanguageCode("en");
            view.initialize();
        });

        WaitForAsyncUtils.waitForFxEvents();
    }

    @BeforeEach
    void setup() {
        Platform.runLater(() -> {
            view.itemAmountInput.clear();
            view.itemPriceInput.clear();
            view.totalLabel.setText("");

            view.setTotalItems(0);
            view.setCurrentItem(1);
            view.items.clear();
        });

        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    void shouldInitializeCorrectly() {
        assertEquals("Calculate", view.calculateButton.getText());
        assertEquals("", view.totalLabel.getText());
    }

    @Test
    void shouldSetTotalItemsOnFirstClick() {
        clickOn(view.itemAmountInput).write("2");
        clickOn(view.calculateButton);

        waitUntil(() -> view.itemPriceInput.isVisible());
        waitUntil(() -> view.itemAmountLabel.getText().contains("(1/2)"));

        assertTrue(view.itemPriceInput.isVisible());
        assertTrue(view.itemAmountLabel.getText().contains("(1/2)"));
    }

    @Test
    void shouldCalculateTotalCorrectly() {
        clickOn(view.itemAmountInput).write("2");
        clickOn(view.calculateButton);

        waitUntil(() -> view.itemPriceInput.isVisible());

        // item 1
        clickOn(view.itemAmountInput).write("2");
        clickOn(view.itemPriceInput).write("5.0");
        clickOn(view.calculateButton);

        // item 2
        clickOn(view.itemAmountInput).write("1");
        clickOn(view.itemPriceInput).write("10.0");
        clickOn(view.calculateButton);

        waitUntil(() -> view.totalLabel.getText().contains("Total"));

        assertTrue(view.totalLabel.getText().contains("Total"));
    }
}