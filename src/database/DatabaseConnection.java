package src.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author José Julio
 * @version 1.0
 */
public class DatabaseConnection {
    private static Connection connection;

    public static Connection getConnection(String database) {
        if (DatabaseConnection.connection == null) {
            DatabaseConnection.connect("jdbc:mysql://localhost:3306/", database,"root", "");
        }
        return DatabaseConnection.connection;
    }

    private static boolean connect(String connectionString, String database, String user, String password) {
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            DatabaseConnection.connection = DriverManager.getConnection(connectionString + database, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection != null;
    }
}