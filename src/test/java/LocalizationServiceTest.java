import model.Item;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import repository.DatabaseCartRepository;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LocalizationServiceTest {

    @Test
    void shouldSaveCartAndReturnId() throws Exception {
        DatabaseCartRepository repo = new DatabaseCartRepository();

        Connection conn = mock(Connection.class);
        PreparedStatement stmt = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        try (MockedStatic<DatabaseConnection> mocked =
                     mockStatic(DatabaseConnection.class)) {

            mocked.when(DatabaseConnection::getConnection)
                    .thenReturn(conn);

            when(conn.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS)))
                    .thenReturn(stmt);

            when(stmt.executeUpdate()).thenReturn(1);
            when(stmt.getGeneratedKeys()).thenReturn(rs);
            when(rs.next()).thenReturn(true);
            when(rs.getInt(1)).thenReturn(42);

            int result = repo.saveCart(3, 99.99, "en");

            assertEquals(42, result);

            verify(stmt).setInt(1, 3);
            verify(stmt).setDouble(2, 99.99);
            verify(stmt).setString(3, "en");
            verify(stmt).executeUpdate();
            verify(rs).getInt(1);
        }
    }

    @Test
    void shouldReturnMinusOneOnException() {
        DatabaseCartRepository repo = new DatabaseCartRepository();

        try (MockedStatic<DatabaseConnection> mocked =
                     mockStatic(DatabaseConnection.class)) {

            mocked.when(DatabaseConnection::getConnection)
                    .thenThrow(new RuntimeException("DB down"));

            int result = repo.saveCart(1, 10.0, "en");

            assertEquals(-1, result);
        }
    }

    @Test
    void shouldSaveCartItemsUsingBatch() throws Exception {
        DatabaseCartRepository repo = new DatabaseCartRepository();

        Connection conn = mock(Connection.class);
        PreparedStatement stmt = mock(PreparedStatement.class);

        try (MockedStatic<DatabaseConnection> mocked =
                     mockStatic(DatabaseConnection.class)) {

            mocked.when(DatabaseConnection::getConnection)
                    .thenReturn(conn);

            when(conn.prepareStatement(anyString()))
                    .thenReturn(stmt);

            doNothing().when(stmt).addBatch();
            when(stmt.executeBatch())
                    .thenReturn(new int[]{1, 1});

            Item item1 = mock(Item.class);
            when(item1.getValue()).thenReturn(10.0);
            when(item1.getQuantity()).thenReturn(2);

            Item item2 = mock(Item.class);
            when(item2.getValue()).thenReturn(5.0);
            when(item2.getQuantity()).thenReturn(1);

            repo.saveItems(1, List.of(item1, item2));

            // ✅ cart_id set ONCE
            verify(stmt).setInt(1, 1);

            // ✅ per‑item bindings
            verify(stmt, times(2)).setDouble(eq(2), anyDouble());
            verify(stmt, times(2)).setInt(eq(3), anyInt());

            verify(stmt, times(2)).addBatch();
            verify(stmt).executeBatch();
        }
    }

    @Test
    void shouldHandleExceptionInSaveItems() {
        DatabaseCartRepository repo = new DatabaseCartRepository();

        try (MockedStatic<DatabaseConnection> mocked =
                     mockStatic(DatabaseConnection.class)) {

            mocked.when(DatabaseConnection::getConnection)
                    .thenThrow(new RuntimeException("DB error"));

            assertDoesNotThrow(() ->
                    repo.saveItems(1, List.of())
            );
        }
    }
}
