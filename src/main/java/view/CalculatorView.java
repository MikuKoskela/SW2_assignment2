package view;


import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Item;
import repository.DatabaseCartRepository;
import service.CartService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CalculatorView extends Application {

    private Locale locale;
    private Map<String, String> texts;
    private String languageCode;
    private CartService cartService = new CartService(new DatabaseCartRepository());

    private int totalItems = 0;
    private int currentItem = 1;
    public final List<Item> items = new ArrayList<>();

    @FXML
    public Button calculateButton;

    @FXML
    public Label itemAmountLabel;

    @FXML
    public TextField itemAmountInput;

    @FXML
    public TextField itemPriceInput;

    @FXML
    public Label totalLabel;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/calculator.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.setTitle("Miku Koskela / model.Calculator");
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
    public void setCartService(CartService service) {
        this.cartService = service;
    }

    public int getCurrentItem() {
        return currentItem;
    }
    public int getTotalItems() {
        return totalItems;
    }

    public void setCurrentItem(int currentItem) {
        this.currentItem = currentItem;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }
}