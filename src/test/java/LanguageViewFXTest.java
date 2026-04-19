import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;
import service.LocalizationService;
import view.LanguageView;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class LanguageViewFXTest extends ApplicationTest {

    private LanguageView view;
    private LocalizationService mockService;

    @Override
    public void start(Stage stage) {
        // ✅ MUST be created on FX thread
        view = new LanguageView();

        // ✅ UI elements
        view.languageLabel = new Label();
        view.languageBox = new ComboBox<>();
        view.ContinueButton = new Button();

        // ✅ Attach everything to a Scene
        VBox root = new VBox(
                view.languageLabel,
                view.languageBox,
                view.ContinueButton
        );
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        // ✅ MOCK SERVICE
        mockService = Mockito.mock(LocalizationService.class);

        Map<String, String> fakeTexts = new HashMap<>();
        fakeTexts.put("language", "Language");
        fakeTexts.put("calculateButton", "Continue");

        when(mockService.getLocalization(Mockito.anyString()))
                .thenReturn(fakeTexts);

        view.setLocalizationService(mockService);

        // ✅ Initialize AFTER scene exists
        Platform.runLater(view::initialize);
        WaitForAsyncUtils.waitForFxEvents();
    }

    // -----------------------------
    // BASIC INIT
    // -----------------------------
    @Test
    void testInitialize_DefaultLanguageIsEnglish() {
        assertEquals("English", view.languageBox.getValue());
    }

    @Test
    void testInitialize_LanguageOptionsLoaded() {
        assertTrue(view.languageBox.getItems().contains("Finnish"));
        assertTrue(view.languageBox.getItems().contains("Japanese"));
    }

    @Test
    void testInitializeCallsUpdateLanguage() {
        assertNotNull(view.currentLocale);
        assertEquals("en", view.currentLocale.getLanguage());
    }

    // -----------------------------
    // LANGUAGE CHANGE
    // -----------------------------
    @Test
    void testLanguageChangeUpdatesLocale() {
        Platform.runLater(() -> view.languageBox.setValue("Finnish"));
        WaitForAsyncUtils.waitForFxEvents();

        assertEquals("fi", view.currentLocale.getLanguage());
    }

    @Test
    void testSwitchLanguagesMultipleTimes() {
        Platform.runLater(() -> view.languageBox.setValue("Finnish"));
        WaitForAsyncUtils.waitForFxEvents();
        assertEquals("fi", view.currentLocale.getLanguage());

        Platform.runLater(() -> view.languageBox.setValue("Japanese"));
        WaitForAsyncUtils.waitForFxEvents();
        assertEquals("ja", view.currentLocale.getLanguage());
    }

    @Test
    void testUnknownLanguageDefaultsToEnglish() {
        Platform.runLater(() -> view.languageBox.setValue("Unknown"));
        WaitForAsyncUtils.waitForFxEvents();

        assertEquals("en", view.currentLocale.getLanguage());
    }

    // -----------------------------
    // UI TEXT UPDATE
    // -----------------------------
    @Test
    void testUpdateLanguageUpdatesUILabels() {
        Platform.runLater(() -> view.languageBox.setValue("English"));
        WaitForAsyncUtils.waitForFxEvents();

        assertEquals("Language", view.languageLabel.getText());
        assertEquals("Continue", view.ContinueButton.getText());
    }

    // -----------------------------
    // BUTTON
    // -----------------------------
    @Test
    void testContinueButtonHasHandler() {
        assertNotNull(view.ContinueButton.getOnAction());
    }

    @Test
    void testContinueButtonClickDoesNotCrash() {
        assertDoesNotThrow(() -> {
            clickOn(view.ContinueButton);
            WaitForAsyncUtils.waitForFxEvents();
        });
    }
}
