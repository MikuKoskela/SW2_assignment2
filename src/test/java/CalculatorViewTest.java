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
import view.CalculatorView;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CalculatorViewTest extends ApplicationTest {

    private CalculatorView view;

    @Override
    public void start(Stage stage) {
        view = new CalculatorView();

        view.calculateButton = new Button();
        view.itemAmountLabel = new Label();
        view.itemAmountInput = new TextField();
        view.itemPriceInput = new TextField();
        view.totalLabel = new Label();

        Map<String, String> texts = new HashMap<>();
        texts.put("itemAmount", "model.Item amount");
        texts.put("itemPrice", "model.Item price");
        texts.put("calculateButton", "Calculate");
        texts.put("totalCost", "Total");

        view.setLocale(Locale.ENGLISH);
        view.setTexts(texts);
        view.setLanguageCode("en");
        view.initialize();

        VBox root = new VBox(
                view.itemAmountLabel,
                view.itemAmountInput,
                view.itemPriceInput,
                view.calculateButton,
                view.totalLabel
        );

        stage.setScene(new Scene(root, 300, 200));
        stage.show();
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

    // -----------------------------
    // INITIAL STATE TEST
    // -----------------------------
    @Test
    void shouldInitializeCorrectly() {
        assertEquals("", view.totalLabel.getText());
        assertEquals("Calculate", view.calculateButton.getText());
    }

    // -----------------------------
    // FIRST CLICK (SET TOTAL ITEMS)
    // -----------------------------
    @Test
    void shouldSetTotalItemsOnFirstClick() {
        clickOn(view.itemAmountInput).write("2");
        clickOn(view.calculateButton);

        WaitForAsyncUtils.waitForFxEvents();

        assertTrue(view.itemPriceInput.isVisible());
        assertTrue(view.itemPriceInput.isManaged());
        assertTrue(view.itemAmountLabel.getText().contains("(1/2)"));
    }

    // -----------------------------
    // ADD ITEMS AND CALCULATE TOTAL
    // -----------------------------
    @Test
    void shouldCalculateTotalCorrectly() {
        clickOn(view.itemAmountInput).write("2");
        clickOn(view.calculateButton);

        WaitForAsyncUtils.waitForFxEvents();

        // model.Item 1
        clickOn(view.itemAmountInput).write("2");
        clickOn(view.itemPriceInput).write("5.0");
        clickOn(view.calculateButton);

        // model.Item 2
        clickOn(view.itemAmountInput).write("1");
        clickOn(view.itemPriceInput).write("10.0");
        clickOn(view.calculateButton);

        WaitForAsyncUtils.waitForFxEvents();

        String result = view.totalLabel.getText();

        assertTrue(result.contains("Total"));
        assertTrue(result.contains("20.0"));
    }

    // -----------------------------
    // INVALID INPUT HANDLING
    // -----------------------------
    @Test
    void shouldHandleInvalidInput() {
        clickOn(view.itemAmountInput).write("abc");
        clickOn(view.calculateButton);

        WaitForAsyncUtils.waitForFxEvents();

        assertEquals("Invalid input", view.totalLabel.getText());
    }

    // -----------------------------
    // LABEL UPDATES BETWEEN ITEMS
    // -----------------------------
    @Test
    void shouldUpdateItemCounter() {
        clickOn(view.itemAmountInput).write("3");
        clickOn(view.calculateButton);

        WaitForAsyncUtils.waitForFxEvents();

        clickOn(view.itemAmountInput).write("1");
        clickOn(view.itemPriceInput).write("1");
        clickOn(view.calculateButton);

        WaitForAsyncUtils.waitForFxEvents();

        assertTrue(view.itemAmountLabel.getText().contains("(2/3)"));
    }

    // -----------------------------
    // TEXT LOCALIZATION
    // -----------------------------
    @Test
    void shouldApplyLocalizationTexts() {
        assertEquals("model.Item amount", view.itemAmountLabel.getText());
        assertEquals("model.Item amount", view.itemAmountInput.getPromptText());
        assertEquals("model.Item price", view.itemPriceInput.getPromptText());
        assertEquals("Calculate", view.calculateButton.getText());
    }
}