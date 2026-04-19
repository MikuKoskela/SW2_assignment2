import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import service.LocalizationService;
import util.DatabaseConnection;

import java.sql.*;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LocalizationServiceTest {

    @Test
    void shouldReturnLocalizationMap() throws Exception {
        Connection conn = mock(Connection.class);
        PreparedStatement stmt = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(true, false);
        when(rs.getString("key")).thenReturn("language");
        when(rs.getString("value")).thenReturn("Language");

        try (MockedStatic<DatabaseConnection> mocked = mockStatic(DatabaseConnection.class)) {
            mocked.when(DatabaseConnection::getConnection).thenReturn(conn);

            LocalizationService service = new LocalizationService();
            Map<String, String> map = service.getLocalization("en");

            assertEquals("Language", map.get("language"));
        }
    }

    @Test
    void shouldHandleSQLException() {
        try (MockedStatic<DatabaseConnection> mocked = mockStatic(DatabaseConnection.class)) {
            mocked.when(DatabaseConnection::getConnection)
                    .thenThrow(new SQLException());

            LocalizationService service = new LocalizationService();

            assertDoesNotThrow(() -> service.getLocalization("en"));
        }
    }

    @Test
    void shouldHandleEmptyResult() throws Exception {
        Connection conn = mock(Connection.class);
        PreparedStatement stmt = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        try (MockedStatic<DatabaseConnection> mocked = mockStatic(DatabaseConnection.class)) {
            mocked.when(DatabaseConnection::getConnection).thenReturn(conn);

            LocalizationService service = new LocalizationService();
            Map<String, String> map = service.getLocalization("en");

            assertTrue(map.isEmpty());
        }
    }
}