package src.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author José Julio
 * @version 1.0
 */
public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection(String database, String user, String password) {
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + database, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static DatabaseConnection getInstance(String database, String user, String password) {
        if (DatabaseConnection.instance == null) {
            return new DatabaseConnection(database,user, password);
        }
        return DatabaseConnection.instance;
    }

    public Connection getConnection() {
        return connection;
    }
}