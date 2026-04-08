import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.Map;

public class LanguageView extends Application {

    @FXML
    private Label languageLabel;

    @FXML
    private ComboBox<String> languageBox;

    @FXML
    private Button ContinueButton;

    private final LocalizationService localizationService = new LocalizationService();
    private Map<String, String> texts;
    public Locale currentLocale;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/views/language.fxml")
        );

        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.setTitle("Miku Koskela / Language selection");
        stage.show();
    }

    @FXML
    public void initialize() {

        languageBox.getItems().addAll(
                "English",
                "Finnish",
                "Swedish",
                "Japanese",
                "Arabic"
        );

        languageBox.setValue("English");

        updateLanguage();

        languageBox.setOnAction(e -> updateLanguage());

        ContinueButton.setOnAction(e -> openCalculator());
    }

    private void updateLanguage() {
        currentLocale = getLocale(languageBox.getValue());

        // ✅ Language code stored in DB (en, fi, sv, ja, ar)
        String languageCode = currentLocale.getLanguage();

        texts = localizationService.getLocalization(languageCode);

        // ✅ UI text from database
        languageLabel.setText(texts.get("language"));
        ContinueButton.setText(texts.get("calculateButton"));
    }

    private void openCalculator() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/calculator.fxml"));
            Stage stage = (Stage) ContinueButton.getScene().getWindow();

            stage.setScene(new Scene(loader.load()));

            CalculatorView controller = loader.getController();

            controller.setLocale(currentLocale);
            controller.setTexts(texts);   // ✅ pass DB localization
            controller.setLanguageCode(currentLocale.getLanguage()); // for DB save

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Locale getLocale(String name) {
        return switch (name) {
            case "Finnish"  -> new Locale("fi", "FI");
            case "Swedish"  -> new Locale("sv", "SE");
            case "Japanese" -> new Locale("ja", "JP");
            case "Arabic"   -> new Locale("ar", "AR");
            default         -> new Locale("en", "US");
        };
    }

    public static void main(String[] args) {
        launch(args);
    }
}