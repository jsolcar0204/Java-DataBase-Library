package src.database;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Clase que crea y gestiona una conexión a la base de datos. Utiliza un patrón Singleton.
 * A través del método estático getInstance() permite obtener una instancia de un objeto de esta
 * misma clase, con el fin de poder utilizar un objeto de tipo Connection.
 * @author José Julio
 * @version 1.0
 */
public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    private static String connectionString;
    private boolean isAutoCommitEnabled;

    /**
     * Crea un objeto de tipo DatabaseConnection y guarda el String de conexión
     * en una propiedad. Las credenciales para conectarse a la base de datos las
     * coge del fichero database.properties.
     */
    private DatabaseConnection() {
        // Si nunca se instanció anteriormente esta clase, guardar una única vez el String de conexión del JDBC
        if (DatabaseConnection.connectionString == null) {
            DatabaseConnection.connectionString = "jdbc:mysql://%s:%s/%s?user=%s&password=%s";
            Properties properties = new Properties();
            try {
                properties.load(new FileReader("src/database.properties"));
                DatabaseConnection.connectionString = String.format(DatabaseConnection.connectionString,
                        properties.getProperty("IP"),
                        properties.getProperty("PORT"),
                        properties.getProperty("DATABASE_NAME"),
                        properties.getProperty("DATABASE_USER"),
                        properties.getProperty("DATABASE_PASSWORD"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtiene una instancia del tipo DatabaseConnection. En caso de que
     * exista un objeto DatabaseConnection, devolverá el existente
     * @return Devuelve un objeto DatabaseConnection. Si no existe, lo crea.
     */
    public static DatabaseConnection getInstance() {
        if (DatabaseConnection.instance == null) {
            return new DatabaseConnection();
        }
        return DatabaseConnection.instance;
    }

    /**
     * Abre una conexión a la base de datos para poder manejarla.
     * Establece el auto commit, permitiendo así realizar transacciones o no.
     * @return Devuelve true si se pudo abrir la conexión y false si no pudo.
     */
    public boolean open() {
        try {
            this.connection = DriverManager.getConnection(DatabaseConnection.connectionString);
            this.connection.setAutoCommit(this.isAutoCommitEnabled);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cierra la conexión a la base de datos.
     * @return Devuelve true si pudo cerrar la conexión a la base de datos y false si no pudo.
     */
    public boolean disconnect() {
        try {
            this.connection.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean isAutoCommitEnabled() {
        return isAutoCommitEnabled;
    }

    public void setAutoCommitEnabled(boolean autoCommitEnabled) {
        isAutoCommitEnabled = autoCommitEnabled;
    }
}