package src.database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Statement;

/**
 * Clase encargada de gestionar los scripts de la base de datos.
 * Guarda el nombre de los scripts en dos propiedades privadas estáticas.
 * Hay dos métodos para ejecutar cada script, uno para ejecutar el script
 * de creación de esquema de la base de datos y otro método para ejecutar
 * la inserción de los datos en la base de datos.
 * @author José Julio
 * @version 1.0
 */
public class DatabaseSchema {
    private static final String DATABASE_SCHEMA_FILE = "videogames-schema.sql";
    private static final String DATABASE_DATA_FILE = "videogames-data.sql";

    private DatabaseConnection databaseConnection;

    public DatabaseSchema() {
        this.databaseConnection = DatabaseConnection.getInstance();
    }

    /**
     * Utiliza el script de creación de esquema de la base de datos.
     * @return Devuelve true si pudo ejecutar correctamente el script y false si no pudo.
     */
    public boolean executeSchema() {
        return this.readFileExecute(DATABASE_SCHEMA_FILE);
    }

    /**
     * Utiliza el script de inserción de datos de la base de datos.
     * @return Devuelve true si pudo ejecutar correctamente el script y false si no pudo.
     */
    public boolean executeData() {
        return this.readFileExecute(DATABASE_DATA_FILE);
    }

    /**
     * Ejecuta uno de los scripts de la base de datos. Lee un
     * fichero línea a línea y lo va guardando (addBatch), para cuando esté listo,
     * ejecuta el método executeBatch.
     * @param fileName Nombre del archivo a leer y ejecutar
     * @return Devuelve true si se ejecutó correctamente el script y false si no pudo.
     */
    private boolean readFileExecute(String fileName) {
        this.databaseConnection.open();
        try {
            BufferedReader databaseFile = new BufferedReader(new FileReader(fileName));
            Statement statement = this.databaseConnection.getConnection().createStatement();
            String line = databaseFile.readLine();
            while (line != null) {
                while (!line.contains(";")) {
                    line += databaseFile.readLine();
                }
                statement.addBatch(line);
                line = databaseFile.readLine();
            }
            statement.executeBatch();
            if (!this.databaseConnection.isAutoCommitEnabled()) {
                this.databaseConnection.getConnection().commit();
            }
            statement.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.databaseConnection.disconnect();
        }
        return false;
    }
}