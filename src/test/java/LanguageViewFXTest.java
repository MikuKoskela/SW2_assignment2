import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.testfx.framework.junit5.ApplicationTest;
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
    public void start(javafx.stage.Stage stage) {
        view = new LanguageView();

        // UI elements
        view.languageLabel = new Label();
        view.languageBox = new ComboBox<>();
        view.ContinueButton = new Button();

        // ✅ MOCK SERVICE
        mockService = Mockito.mock(LocalizationService.class);

        Map<String, String> fakeTexts = new HashMap<>();
        fakeTexts.put("language", "Language");
        fakeTexts.put("calculateButton", "Continue");

        when(mockService.getLocalization(Mockito.anyString()))
                .thenReturn(fakeTexts);

        view.setLocalizationService(mockService);

        view.initialize();
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
        view.languageBox.setValue("Finnish");
        view.languageBox.getOnAction().handle(null);

        assertEquals("fi", view.currentLocale.getLanguage());
    }

    @Test
    void testSwitchLanguagesMultipleTimes() {
        view.languageBox.setValue("Finnish");
        view.languageBox.getOnAction().handle(null);
        assertEquals("fi", view.currentLocale.getLanguage());

        view.languageBox.setValue("Japanese");
        view.languageBox.getOnAction().handle(null);
        assertEquals("ja", view.currentLocale.getLanguage());
    }

    @Test
    void testUnknownLanguageDefaultsToEnglish() {
        view.languageBox.setValue("Unknown");
        view.languageBox.getOnAction().handle(null);

        assertEquals("en", view.currentLocale.getLanguage());
    }

    // -----------------------------
    // UI TEXT UPDATE (NOW WORKS)
    // -----------------------------
    @Test
    void testUpdateLanguageUpdatesUILabels() {
        view.languageBox.setValue("English");
        view.languageBox.getOnAction().handle(null);

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
            view.ContinueButton.getOnAction().handle(null);
        });
    }
}