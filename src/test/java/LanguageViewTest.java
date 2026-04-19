import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.LanguageView;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class LanguageViewTest {

    private LanguageView view;

    @BeforeEach
    void setup() {
        view = new LanguageView();
    }

    @Test
    void testGetLocale_EnglishDefault() {
        Locale locale = view.getLocale("English");
        assertEquals("en", locale.getLanguage());
    }

    @Test
    void testGetLocale_Finnish() {
        Locale locale = view.getLocale("Finnish");
        assertEquals("fi", locale.getLanguage());
        assertEquals("FI", locale.getCountry());
    }

    @Test
    void testGetLocale_Swedish() {
        Locale locale = view.getLocale("Swedish");
        assertEquals("sv", locale.getLanguage());
    }

    @Test
    void testGetLocale_Japanese() {
        Locale locale = view.getLocale("Japanese");
        assertEquals("ja", locale.getLanguage());
    }

    @Test
    void testGetLocale_Arabic() {
        Locale locale = view.getLocale("Arabic");
        assertEquals("ar", locale.getLanguage());
    }

    @Test
    void testGetLocale_UnknownDefaultsToEnglish() {
        Locale locale = view.getLocale("Unknown");
        assertEquals("en", locale.getLanguage());
    }
}
