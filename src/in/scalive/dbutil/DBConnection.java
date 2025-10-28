package in.scalive.dbutil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import in.scalive.config.AppConfig;

public class DBConnection {
    private static final String URL = AppConfig.get("db.url");
    private static final String USER = AppConfig.get("db.user");
    private static final String PASSWORD = AppConfig.get("db.password");

    public static Connection getDBConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
