package service;


import util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LocalizationService {
    private static final Logger logger = Logger.getLogger(LocalizationService.class.getName());
    public Map<String, String> getLocalization(String language) {
        Map<String, String> map = new HashMap<>();

        String sql = "SELECT `key`, value FROM localization_strings WHERE language = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, language);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                map.put(rs.getString("key"), rs.getString("value"));
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in localization", e);        }

        return map;
    }

}