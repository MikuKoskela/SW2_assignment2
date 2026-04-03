

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;


public class LanguageView extends Application {
    @FXML
    private Label languageLabel;
    @FXML
    private ComboBox<String> languageBox ;
    @FXML
    private Button ContinueButton;


    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/views/language.fxml")
        );

        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.setTitle("Miku Koskela/Language selection");
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

        ContinueButton.setOnAction(e -> {
            Locale locale = getLocale(languageBox.getValue());
            ResourceBundle r = ResourceBundle.getBundle("bundle", locale);
            openCalculator(locale, r);
        });
    }
    private void openCalculator(Locale locale, ResourceBundle r) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/calculator.fxml"));
            Stage stage = (Stage) ContinueButton.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            CalculatorView controller = loader.getController();
            controller.setLocale(locale);
            controller.setRBundle(r);
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }


    public Locale getLocale(String name) {
        return switch (name) {
            case "Finnish"  -> new Locale("fi", "FI");
            case "Swedish"  -> new Locale("sv", "SE");
            case "Japanese" -> new Locale("ja", "JP");
            case "Arabic" -> new Locale("ar", "AR");
            default         -> new Locale("en", "US");
        };
    }

    private void updateLanguage() {
        Locale locale = getLocale(languageBox.getValue());
        ResourceBundle r = ResourceBundle.getBundle("bundle", locale);
        languageLabel.setText(r.getString("language"));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
