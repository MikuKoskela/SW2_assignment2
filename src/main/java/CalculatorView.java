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
import java.util.ResourceBundle;

public class CalculatorView extends Application {

    private Locale locale;
    private ResourceBundle r;

    // MULTI-ITEM STATE
    private int totalItems = 0;        // how many items user said they will enter
    private int currentItem = 1;       // current item index
    private final List<Item> items = new ArrayList<>();

    @FXML
    private Button calculateButton;

    @FXML
    private Label itemAmountLabel;

    @FXML
    private TextField itemAmountInput;   // REUSED for first input + quantity input

    @FXML
    private TextField itemPriceInput;    // price input only

    @FXML
    private Label totalLabel;


    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/calculator.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.setTitle("Miku Koskela/Calculator");
        stage.show();
    }

    @FXML
    public void initialize() {
        calculateButton.setOnAction(e -> handleClick());
        totalLabel.setText("");  // clear default label
    }

    private void handleClick() {
        try {
            // FIRST STEP: Get number of items
            if (totalItems == 0) {
                totalItems = Integer.parseInt(itemAmountInput.getText().trim());

                itemAmountInput.clear();
                itemPriceInput.clear();

                itemPriceInput.setVisible(true);
                itemPriceInput.setManaged(true);

                // Prepare for next inputs
                itemAmountInput.clear();
                itemPriceInput.clear();

                itemAmountLabel.setText(r.getString("itemAmount") + " (1/" + totalItems + ")");
                calculateButton.setText(r.getString("calculateButton"));

                return;
            }

            // NEXT STEPS: Get price + quantity for each item
            int quantity = Integer.parseInt(itemAmountInput.getText().trim());
            double price = Double.parseDouble(itemPriceInput.getText().trim());

            items.add(new Item(price, quantity));

            itemAmountInput.clear();
            itemPriceInput.clear();
            currentItem++;

            // Continue asking for items
            if (currentItem <= totalItems) {
                itemAmountLabel.setText(r.getString("itemAmount") + " (" + currentItem + "/" + totalItems + ")");
                return;
            }

            double total = 0;
            for (Item item : items) {
                total += item.getQuantity() * item.getValue();
            }

            totalLabel.setText(r.getString("totalCost") + " " + total);

            // OPTIONAL: Disable after finishing
            itemAmountInput.setDisable(true);
            itemPriceInput.setDisable(true);
            calculateButton.setDisable(true);

        } catch (Exception ex) {
            totalLabel.setText("Invalid input");
            ex.printStackTrace();
        }
    }

    // ------------------- Localization ------------------------

    public void setLocale(Locale l) {
        this.locale = l;
    }

    public void setRBundle(ResourceBundle re) {
        this.r = re;

        // FIRST prompt: number of items
        itemAmountLabel.setText(r.getString("itemAmount"));   // "Enter number of items to purchase"
        itemAmountInput.setPromptText(r.getString("itemAmount"));

        // Price field
        itemPriceInput.setPromptText(r.getString("itemPrice"));
        itemPriceInput.setVisible(false);
        // Button text
        calculateButton.setText(r.getString("calculateButton"));

        // Total label title
        totalLabel.setText(r.getString("totalCost"));
    }

    // mirrors final stage so we can test it
    public double computeTotalForItems(List<Item> items) {
        double total = 0;
        for (Item item : items) {
            total += item.getQuantity() * item.getValue();
        }
        return total;
    }

}