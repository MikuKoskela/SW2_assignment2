package repository;

import model.Item;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseCartRepository implements CartRepository {

    private static final Logger logger =
            Logger.getLogger(DatabaseCartRepository.class.getName());

    @Override
    public int saveCart(int totalItems, double total, String languageCode) {
        int cartId = -1;

        String sql =
                "INSERT INTO cart (total_items, total_price, language_code) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt =
                     conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, totalItems);
            stmt.setDouble(2, total);
            stmt.setString(3, languageCode);

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    cartId = rs.getInt(1);
                }
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error saving cart", e);
        }

        return cartId;
    }

    @Override
    public void saveItems(int cartId, List<Item> items) {

        String sql =
                "INSERT INTO cart_item (cart_id, price, quantity) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cartId);

            for (Item item : items) {
                stmt.setDouble(2, item.getValue());
                stmt.setInt(3, item.getQuantity());
                stmt.addBatch();
            }

            stmt.executeBatch();

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to save items", e);
        }
    }
}
