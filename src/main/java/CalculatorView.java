import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CalculatorView extends Application {

    private Locale locale;
    private Map<String, String> texts;
    private String languageCode;
    private final CartService cartService = new CartService();

    private int totalItems = 0;
    private int currentItem = 1;
    private final List<Item> items = new ArrayList<>();

    @FXML
    private Button calculateButton;

    @FXML
    private Label itemAmountLabel;

    @FXML
    private TextField itemAmountInput;

    @FXML
    private TextField itemPriceInput;

    @FXML
    private Label totalLabel;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/calculator.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.setTitle("Miku Koskela / Calculator");
        stage.show();
    }

    @FXML
    public void initialize() {
        calculateButton.setOnAction(e -> handleClick());
        totalLabel.setText("");
    }

    private void handleClick() {
        try {
            if (totalItems == 0) {
                totalItems = Integer.parseInt(itemAmountInput.getText().trim());

                itemAmountInput.clear();
                itemPriceInput.clear();

                itemPriceInput.setVisible(true);
                itemPriceInput.setManaged(true);

                itemAmountLabel.setText(
                        texts.get("itemAmount") + " (1/" + totalItems + ")"
                );

                return;
            }

            int quantity = Integer.parseInt(itemAmountInput.getText().trim());
            double price = Double.parseDouble(itemPriceInput.getText().trim());

            items.add(new Item(price, quantity));

            itemAmountInput.clear();
            itemPriceInput.clear();
            currentItem++;

            if (currentItem <= totalItems) {
                itemAmountLabel.setText(
                        texts.get("itemAmount") + " (" + currentItem + "/" + totalItems + ")"
                );
                return;
            }


            double total = 0;
            for (Item item : items) {
                total += item.getValue() * item.getQuantity();
            }

            totalLabel.setText(texts.get("totalCost") + " " + total);

            int cartId = cartService.saveCartRecord(
                    totalItems,
                    total,
                    languageCode
            );

            cartService.saveCartItems(cartId,items);


        } catch (Exception ex) {
            totalLabel.setText("Invalid input");
            ex.printStackTrace();
        }
    }

    // ---------------- DB localization / state ----------------

    public void setLocale(Locale l) {
        this.locale = l;
    }

    public void setTexts(Map<String, String> texts) {
        this.texts = texts;

        itemAmountLabel.setText(texts.get("itemAmount"));
        itemAmountInput.setPromptText(texts.get("itemAmount"));
        itemPriceInput.setPromptText(texts.get("itemPrice"));
        calculateButton.setText(texts.get("calculateButton"));
        totalLabel.setText(texts.get("totalCost"));
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }
}