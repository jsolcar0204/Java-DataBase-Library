package src.database;

import src.models.Agregable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author José Julio
 * @version 1.0
 */
public class DatabaseManager {
    private Connection connection;

    public DatabaseManager(String databaseName) {
        this.connection = DatabaseConnection.getConnection(databaseName);
    }

    /**
     * Obtiene todos los registros de la base de datos. Se le debe de pasar
     * el nombre de la tabla que se quiere obtener y, si se le pasan columnas,
     * obtiene las columnas dichas de la tabla. En caso de que no se pasen columnas,
     * se obtendrán todas las columnas.
     * @param tableName Nombre de la tabla que se quieren sacar los registros
     * @param columns Nombre de las columnas que se desea obtener
     * @return Devuelve un ArrayList con todos los registros que ha encontrado según
     * los parámetros especificados
     */
    public ArrayList<Object> getData(String tableName, Agregable collectorClass, String... columns) {
        ArrayList<Object> list = new ArrayList<>();
        try {
            PreparedStatement preparedStatement;
            String query = "SELECT ";
            if (columns.length == 0) {
                query += "* FROM " + tableName;
            } else {
                for (String column : columns) {
                    query += column + ",";
                }
                query = query.substring(0, query.length()-1);
                query += " FROM " + tableName;
            }
            preparedStatement = this.connection.prepareStatement(query);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                list.add(collectorClass.add(rs.getString(1), rs.getString(2)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<Object> getData(String tableName, Agregable collectorClass, HashMap<String, String> filters, String... columns) {
        ArrayList<Object> list = new ArrayList<>();
        try {
            PreparedStatement preparedStatement;
            String query = "SELECT ";
            if (columns.length == 0) {
                query += "* FROM " + tableName;
            } else {
                for (String column : columns) {
                    query += column + ",";
                }
                query = query.substring(0, query.length()-1);
                query += " FROM " + tableName;
            }

            if (filters.size() != 0) {
                query += " WHERE ";
                for (String key : filters.keySet()) {
                    for (String value : filters.values()) {
                        query += key + " = " + "'" + value + "'" + " AND ";
                    }
                }
                query = query.substring(0, query.length()-5);
            }
            preparedStatement = this.connection.prepareStatement(query);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                list.add(collectorClass.add());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}