import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import service.LocalizationService;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class LocalizationServiceTest {

    @Test
    void shouldReturnLocalizationMapWhenDataExists() throws Exception {
        LocalizationService service = new LocalizationService();

        Connection conn = mock(Connection.class);
        PreparedStatement stmt = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        try (MockedStatic<DatabaseConnection> mocked =
                     mockStatic(DatabaseConnection.class)) {

            mocked.when(DatabaseConnection::getConnection)
                    .thenReturn(conn);

            when(conn.prepareStatement(anyString()))
                    .thenReturn(stmt);

            when(stmt.executeQuery())
                    .thenReturn(rs);

            // ✅ two rows from DB
            when(rs.next()).thenReturn(true, true, false);
            when(rs.getString("key"))
                    .thenReturn("title", "button.ok");
            when(rs.getString("value"))
                    .thenReturn("My App", "OK");

            Map<String, String> result =
                    service.getLocalization("en");

            assertEquals(2, result.size());
            assertEquals("My App", result.get("title"));
            assertEquals("OK", result.get("button.ok"));

            verify(stmt).setString(1, "en");
            verify(stmt).executeQuery();
        }
    }

    @Test
    void shouldReturnEmptyMapOnException() {
        LocalizationService service = new LocalizationService();

        try (MockedStatic<DatabaseConnection> mocked =
                     mockStatic(DatabaseConnection.class)) {

            mocked.when(DatabaseConnection::getConnection)
                    .thenThrow(new RuntimeException("DB down"));

            Map<String, String> result =
                    service.getLocalization("fi");

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }
}