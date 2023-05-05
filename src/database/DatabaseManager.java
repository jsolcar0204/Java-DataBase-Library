package src.database;

import src.models.enums.*;
import src.models.tables.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author José Julio
 * @version 1.0
 */
public class DatabaseManager {
    private DatabaseConnection databaseConnection;

    public DatabaseManager(String databaseName) {
        this.databaseConnection = DatabaseConnection.getInstance(databaseName, "root", "");
    }

    /**
     * Obtiene todos los registros de la base de datos. Se le debe de pasar
     * el nombre de la tabla que se quiere obtener (utilizar enum {@link Tablas})
     * y, si se le pasan columnas, obtiene las columnas dichas de la tabla
     * (se recomienda utilizar enums correspondientes en el paquete models.enums).
     * En caso de que no se pasen columnas, se obtendrán todas las columnas.
     * @param tableName Nombre de la tabla que se quieren sacar los registros
     * @param columns Nombre de las columnas que se desea obtener
     * @return Devuelve un ArrayList con todos los registros que ha encontrado según
     * los parámetros especificados
     */
    public ArrayList<Agregable> getData(Tablas tableName, String... columns) {
        ArrayList<Agregable> rows = new ArrayList<Agregable>();
        try {
            PreparedStatement preparedStatement;
            String query = "SELECT ";
            if (columns.length == 0) {
                query += "* FROM ";
            } else {
                for (String column : columns) {
                    query += column + ",";
                }
                query = query.substring(0, query.length()-1);
                query += " FROM ";
            }
            query += tableName;
            preparedStatement = this.databaseConnection.getConnection().prepareStatement(query);

            ResultSet rs = preparedStatement.executeQuery();
            rows = this.executeQueryByTable(tableName, rs, columns);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

    /**
     * Obtiene todos los registros de la base de datos mediante un filtrado.
     * Se le debe de pasar el nombre de la tabla que se quiere obtener (utilizar enum {@link Tablas}),
     * un HashMap en el que la clave sea el nombre de la columna que se desea utilizar
     * (se recomienda utilizar enums correspondientes en el paquete models.enums), y el valor sea el valor
     * del campo que deseamos buscar. Si se le pasan columnas, obtiene las columnas dichas de la tabla
     * (se recomienda utilizar enums correspondientes en el paquete models.enums).
     * En caso de que no se pasen columnas, se obtendrán todas las columnas.
     * @param tableName Nombre de la tabla que se quieren sacar los registros
     * @param filters Filtro que se desea realizar, en el campo clave usar el nombre
     *                de la columna que queremos y en el campo valor el campo que queremos buscar
     *                en la base de datos
     * @param columns Nombre de las columnas que se desea obtener
     * @return Devuelve un ArrayList con todos los registros que ha encontrado según
     * los parámetros especificados
     */
    public ArrayList<Agregable> getData(Tablas tableName, HashMap<String, Object> filters, String... columns) {
        ArrayList<Agregable> rows = new ArrayList<Agregable>();
        if (filters.size() < 2) {
            return rows;
        }
        try {
            PreparedStatement preparedStatement;
            String query = "SELECT ";
            if (columns.length == 0) {
                query += "* FROM ";
            } else {
                for (String column : columns) {
                    query += column + ",";
                }
                query = query.substring(0, query.length()-1);
                query += " FROM ";
            }
            String whereQuery = " WHERE ";
            for (String key : filters.keySet()) {
                whereQuery += key + " = ? AND ";
            }
            whereQuery = whereQuery.substring(0, whereQuery.length()-5);
            query += tableName;

            preparedStatement = this.databaseConnection.getConnection().prepareStatement(query + whereQuery);
            int dataType = 0, counter = 0;
            for (Object value : filters.values()) {
                if (value instanceof String) {
                    dataType = Types.VARCHAR;
                } else if (value instanceof Integer) {
                    dataType = Types.INTEGER;
                } else if (value instanceof Double) {
                    dataType = Types.DOUBLE;
                }
                preparedStatement.setObject(++counter, value, dataType);
            }
            ResultSet rs = preparedStatement.executeQuery();
            rows = this.executeQueryByTable(tableName, rs, columns);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

    private ArrayList<Agregable> executeQueryByTable(Tablas tableName, ResultSet rs, String[] columns) {
        ArrayList<Agregable> rows = new ArrayList<Agregable>();
        try {
            switch (tableName) {
                case DESARROLLADOR -> {
                    Desarrollador desarrollador;
                    while (rs.next()) {
                        if (columns.length == 0) {
                            rows.add(new Desarrollador(rs.getString(1), Instant.ofEpochMilli(
                                            rs.getDate(2).getTime())
                                    .atZone(ZoneId.systemDefault()).toLocalDate()));
                            continue;
                        }
                        desarrollador = new Desarrollador();
                        for (int i = 0; i < columns.length; i++) {
                            if (TablaDesarrollador.NOMBRE.getColumnName().equals(columns[i])) {
                                desarrollador.setNombre(rs.getString(i+1));
                            } else if (TablaDesarrollador.FECHA_FUNDACION.getColumnName().equals(columns[i])) {
                                desarrollador.setFechaFundacion(Instant.ofEpochMilli(rs.getDate(i+1).getTime())
                                        .atZone(ZoneId.systemDefault()).toLocalDate());
                            }
                        }
                        rows.add(desarrollador);
                    }
                }
                case EDITOR -> {
                    Editor editor;
                    while (rs.next()) {
                        if (columns.length == 0) {
                            rows.add(new Editor(rs.getString(1), Instant.ofEpochMilli(
                                            rs.getDate(2).getTime())
                                    .atZone(ZoneId.systemDefault()).toLocalDate()));
                            continue;
                        }
                        editor = new Editor();
                        for (int i = 0; i < columns.length; i++) {
                            if (TablaEditor.NOMBRE.getColumnName().equals(columns[i])) {
                                editor.setNombre(rs.getString(i+1));
                            } else if (TablaEditor.FECHA_FUNDACION.getColumnName().equals(columns[i])) {
                                editor.setFechaFundacion(Instant.ofEpochMilli(rs.getDate(i+1).getTime())
                                        .atZone(ZoneId.systemDefault()).toLocalDate());
                            }
                        }
                        rows.add(editor);
                    }
                }
                case GENERO -> {
                    Genero genero;
                    while (rs.next()) {
                        if (columns.length == 0) {
                            rows.add(new Genero(rs.getString(1), rs.getString(2)));
                            continue;
                        }
                        genero = new Genero();
                        for (int i = 0; i < columns.length; i++) {
                            if (TablaGenero.NOMBRE.getColumnName().equals(columns[i])) {
                                genero.setNombre(rs.getString(i+1));
                            } else if (TablaGenero.DESCRIPCION.getColumnName().equals(columns[i])) {
                                genero.setDescripcion(rs.getString(i+1));
                            }
                        }
                        rows.add(genero);
                    }
                }
                case PLATAFORMA -> {
                    while (rs.next()) {
                        rows.add(new Plataforma(rs.getString(1)));
                    }
                }
                case VIDEOJUEGO -> {
                    Videojuego videojuego;
                    while (rs.next()) {
                        if (columns.length == 0) {
                            rows.add(new Videojuego(rs.getString(1), rs.getString(2),
                                    Instant.ofEpochMilli(rs.getDate(3).getTime())
                                            .atZone(ZoneId.systemDefault()).toLocalDate(), rs.getString(4),
                                    rs.getString(5)));
                            continue;
                        }
                        videojuego = new Videojuego();
                        for (int i = 0; i < columns.length; i++) {
                            if (TablaVideojuego.NOMBRE.getColumnName().equals(columns[i])) {
                                videojuego.setNombre(rs.getString(i+1));
                            } else if (TablaVideojuego.SINOPSIS.getColumnName().equals(columns[i])) {
                                videojuego.setSinopsis(rs.getString(i+1));
                            } else if (TablaVideojuego.FECHA_PUBLICACION.getColumnName().equals(columns[i])) {
                                videojuego.setFechaPublicacion(Instant.ofEpochMilli(rs.getDate(i+1).getTime())
                                        .atZone(ZoneId.systemDefault()).toLocalDate());
                            }
                        }
                        rows.add(videojuego);
                    }
                }
                case VIDEOJUEGO_GENERO -> {
                    VideojuegoGenero videojuegoGenero;
                    while (rs.next()) {
                        if (columns.length == 0) {
                            rows.add(new VideojuegoGenero(rs.getString(1), rs.getString(2)));
                            continue;
                        }
                        videojuegoGenero = new VideojuegoGenero();
                        for (int i = 0; i < columns.length; i++) {
                            if (TablaVideojuegoGenero.NOMBRE_VIDEOJUEGO.getColumnName().equals(columns[i])) {
                                videojuegoGenero.setNombreVideojuego(rs.getString(i+1));
                            } else if (TablaVideojuegoGenero.NOMBRE_GENERO.getColumnName().equals(columns[i])) {
                                videojuegoGenero.setNombreGenero(rs.getString(i+1));
                            }
                        }
                        rows.add(videojuegoGenero);
                    }
                }
                case VIDEOJUEGO_PLATAFORMA -> {
                    VideojuegoPlataforma videojuegoPlataforma;
                    while (rs.next()) {
                        if (columns.length == 0) {
                            rows.add(new VideojuegoPlataforma(rs.getString(1), rs.getString(2)));
                            continue;
                        }
                        videojuegoPlataforma = new VideojuegoPlataforma();
                        for (int i = 0; i < columns.length; i++) {
                            if (TablaVideojuegoPlataforma.NOMBRE_VIDEOJUEGO.getColumnName().equals(columns[i])) {
                                videojuegoPlataforma.setNombreVideojuego(rs.getString(i+1));
                            } else if (TablaVideojuegoPlataforma.NOMBRE_PLATAFORMA.getColumnName().equals(columns[i])) {
                                videojuegoPlataforma.setNombrePlataforma(rs.getString(i+1));
                            }
                        }
                        rows.add(videojuegoPlataforma);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }
}