import org.junit.jupiter.api.Test;
import java.util.Locale;
import static org.junit.jupiter.api.Assertions.*;

public class LanguageViewTest {

    @Test
    void testEnglishLocale() {
        LanguageView view = new LanguageView();
        assertEquals(new Locale("en","US"), view.getLocale("English"));
    }

    @Test
    void testFinnishLocale() {
        LanguageView view = new LanguageView();
        assertEquals(new Locale("fi","FI"), view.getLocale("Finnish"));
    }

    @Test
    void testJapaneseLocale() {
        LanguageView view = new LanguageView();
        assertEquals(new Locale("ja","JP"), view.getLocale("Japanese"));
    }
}