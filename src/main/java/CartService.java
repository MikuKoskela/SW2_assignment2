import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class CartService {

    public int saveCartRecord(int totalItems, double totalCost, String language) {
        String sql = """
            INSERT INTO cart_records (total_items, total_cost, language)
            VALUES (?, ?, ?)
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, totalItems);
            ps.setDouble(2, totalCost);
            ps.setString(3, language);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void saveCartItems(int cartRecordId, List<Item> items) {

        String sql = """
            INSERT INTO cart_items
            (cart_record_id, item_number, price, quantity, subtotal)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            int index = 1;
            for (Item item : items) {
                ps.setInt(1, cartRecordId);
                ps.setInt(2, index++);
                ps.setDouble(3, item.getValue());
                ps.setInt(4, item.getQuantity());
                ps.setDouble(5, item.getValue() * item.getQuantity());
                ps.addBatch();
            }

            ps.executeBatch();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}