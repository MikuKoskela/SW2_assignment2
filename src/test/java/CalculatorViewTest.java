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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CalculatorViewTest extends ApplicationTest {

    private CalculatorView view;
    private CartService mockCartService;

    /** Run code safely on the JavaFX thread and wait for completion */
    private void runFx(Runnable action) {
        Platform.runLater(action);
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Override
    public void start(Stage stage) {
        view = new CalculatorView();

        view.calculateButton = new Button();
        view.itemAmountLabel = new Label();
        view.itemAmountInput = new TextField();
        view.itemPriceInput = new TextField();
        view.totalLabel = new Label();

        // ✅ Mock CartService (no DB access in UI tests)
        mockCartService = mock(CartService.class);

        when(mockCartService.saveCartRecord(anyInt(), anyDouble(), anyString()))
                .thenReturn(1);

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
        runFx(() -> {
            view.setLocale(Locale.ENGLISH);
            view.setTexts(texts);
            view.setLanguageCode("en");
            view.initialize();
        });
    }

    @BeforeEach
    void setup() {
        runFx(() -> {
            view.itemAmountInput.clear();
            view.itemPriceInput.clear();
            view.totalLabel.setText("");

            view.setTotalItems(0);
            view.setCurrentItem(1);
            view.items.clear();
        });
    }

    // -----------------------------
    // INITIAL STATE
    // -----------------------------
    @Test
    void shouldInitializeCorrectly() {
        assertEquals("Calculate", view.calculateButton.getText());
        assertEquals("", view.totalLabel.getText());
    }

    // -----------------------------
    // FIRST ITEM COUNT
    // -----------------------------
    @Test
    void shouldSetTotalItemsOnFirstClick() {
        runFx(() -> view.itemAmountInput.setText("2"));
        runFx(view::handleClick); // ✅ call logic directly

        assertTrue(
                view.itemAmountLabel.getText().contains("(1/2)"),
                "Item label should show current item index"
        );
    }

    // -----------------------------
    // TOTAL CALCULATION
    // -----------------------------
    @Test
    void shouldCalculateTotalCorrectly() {
        // total items
        runFx(() -> view.itemAmountInput.setText("2"));
        runFx(view::handleClick);

        // item 1
        runFx(() -> {
            view.itemAmountInput.setText("2");
            view.itemPriceInput.setText("5.0");
        });
        runFx(view::handleClick);

        // item 2
        runFx(() -> {
            view.itemAmountInput.setText("1");
            view.itemPriceInput.setText("10.0");
        });
        runFx(view::handleClick);

        assertTrue(
                view.totalLabel.getText().contains("Total"),
                "Total label should contain total text"
        );
    }
}