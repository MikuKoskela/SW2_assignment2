package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL =
            "jdbc:mariadb://localhost:3306/shopping_cart_localization?useUnicode=true&characterEncoding=UTF-8";
    private DatabaseConnection(){}

    public static Connection getConnection() throws SQLException {
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        return DriverManager.getConnection(URL, user, password);
    }
}