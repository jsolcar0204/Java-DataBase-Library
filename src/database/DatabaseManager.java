package src.database;

import org.w3c.dom.*;
import src.models.ColumnOrder;
import src.models.enums.*;
import src.models.tables.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileWriter;
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

    public ArrayList<Agregable> getData(Tablas tableName, ArrayList<ColumnOrder> orders, String... columns) {
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
            query += tableName + " ORDER BY ";
            for (ColumnOrder columnOrder : orders) {
                query += columnOrder.getColumnName() + " " + columnOrder.getOrder() + ",";
            }
            query = query.substring(0, query.length()-1);

            preparedStatement = this.databaseConnection.getConnection().prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            rows = this.executeQueryByTable(tableName, rs, columns);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

    public ArrayList<Agregable> getData(Tablas tableName, HashMap<String, Object> filters, ArrayList<ColumnOrder> orders,
                                        String... columns) {
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
            query += tableName + " WHERE ";
            for (String key : filters.keySet()) {
                query += key + " = ? AND ";
            }
            query = query.substring(0, query.length()-5);
            query += " ORDER BY ";
            for (ColumnOrder columnOrder : orders) {
                query += columnOrder.getColumnName() + " " + columnOrder.getOrder() + ",";
            }
            query = query.substring(0, query.length()-1);
            System.out.println(query);

            preparedStatement = this.databaseConnection.getConnection().prepareStatement(query);
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

    /*public int updateRows(Tablas tableName, Agregable... agregable) {
        return 0;
    }*/

    public int insertRows(Tablas tableName, Agregable... agregables) {
        try {
            PreparedStatement preparedStatement;
            String insertSentence = "INSERT INTO " + tableName + " (";
            insertSentence += switch (tableName) {
                case DESARROLLADOR -> TablaDesarrollador.NOMBRE.getColumnName() + "," +
                        TablaDesarrollador.FECHA_FUNDACION.getColumnName();
                case EDITOR -> TablaEditor.NOMBRE.getColumnName() + "," +
                        TablaEditor.FECHA_FUNDACION.getColumnName();
                case GENERO -> TablaGenero.NOMBRE.getColumnName() + "," +
                        TablaGenero.DESCRIPCION.getColumnName();
                case PLATAFORMA -> TablaPlataforma.NOMBRE.getColumnName();
                case VIDEOJUEGO -> TablaVideojuego.NOMBRE.getColumnName() + "," +
                        TablaVideojuego.SINOPSIS.getColumnName() + "," +
                        TablaVideojuego.FECHA_PUBLICACION.getColumnName() + "," +
                        TablaVideojuego.NOMBRE_DESARROLLADOR.getColumnName() + "," +
                        TablaVideojuego.NOMBRE_EDITOR.getColumnName();
                case VIDEOJUEGO_GENERO -> TablaVideojuegoGenero.NOMBRE_VIDEOJUEGO.getColumnName() + "," +
                        TablaVideojuegoGenero.NOMBRE_VIDEOJUEGO.getColumnName();
                case VIDEOJUEGO_PLATAFORMA -> TablaVideojuegoPlataforma.NOMBRE_VIDEOJUEGO.getColumnName() + "," +
                        TablaVideojuegoPlataforma.NOMBRE_PLATAFORMA.getColumnName();
            };
            insertSentence += ") VALUES ";
            for (Agregable agregable : agregables) {
                insertSentence += "(";
                if (agregable instanceof Desarrollador) {
                    Desarrollador desarrollador = (Desarrollador) agregable;
                    insertSentence += "'" + desarrollador.getNombre() + "','" + desarrollador.getFechaFundacion() + "'";
                } else if (agregable instanceof Editor) {
                    Editor editor = (Editor) agregable;
                    insertSentence += "'" + editor.getNombre() + "','" + editor.getFechaFundacion() + "'";
                } else if (agregable instanceof Genero) {
                    Genero genero = (Genero) agregable;
                    insertSentence += "'" + genero.getNombre() + "','" + genero.getDescripcion() + "'";
                } else if (agregable instanceof Plataforma) {
                    Plataforma plataforma = (Plataforma) agregable;
                    insertSentence += "'" + plataforma.getNombre() + "'";
                } else if (agregable instanceof Videojuego) {
                    Videojuego videojuego = (Videojuego) agregable;
                    insertSentence += "'" + videojuego.getNombre() + "'," + "'" + videojuego.getSinopsis() + "'," +
                            "'" + videojuego.getFechaPublicacion() + "'," + "'" + videojuego.getNombreDesarrollador() +
                            "'," + "'" + videojuego.getNombreEditor() + "'";
                } else if (agregable instanceof VideojuegoGenero) {
                    VideojuegoGenero videojuegoGenero = (VideojuegoGenero) agregable;
                    insertSentence += "'" + videojuegoGenero.getNombreVideojuego() + "','" +
                            videojuegoGenero.getNombreGenero() + "'";
                } else if (agregable instanceof VideojuegoPlataforma) {
                    VideojuegoPlataforma videojuegoPlataforma = (VideojuegoPlataforma) agregable;
                    insertSentence += "'" + videojuegoPlataforma.getNombreVideojuego() + "','" +
                            videojuegoPlataforma.getNombrePlataforma() + "'";
                }
                insertSentence += "),";
            }
            insertSentence = insertSentence.substring(0, insertSentence.length()-1);
            preparedStatement = this.databaseConnection.getConnection().prepareStatement(insertSentence);
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int deleteRows(Tablas tableName, HashMap<String, Object> columnValue) {
        try {
            PreparedStatement preparedStatement;
            String deleteSentence = "DELETE FROM " + tableName + " WHERE ";
            deleteSentence = deleteSentence.substring(0, deleteSentence.length()-1);
            for (String key : columnValue.keySet()) {
                deleteSentence += " " + key + " = ? OR";
            }
            deleteSentence = deleteSentence.substring(0, deleteSentence.length()-3);
            preparedStatement = this.databaseConnection.getConnection().prepareStatement(deleteSentence);
            int dataType = 0, counter = 0;
            for (Object value : columnValue.values()) {
                if (value instanceof String) {
                    dataType = Types.VARCHAR;
                } else if (value instanceof Integer) {
                    dataType = Types.INTEGER;
                } else if (value instanceof Double) {
                    dataType = Types.DOUBLE;
                }
                preparedStatement.setObject(++counter, value, dataType);
            }
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /*public void importXmlToTable(Tablas tableName, String xmlFileName) {
        try {
            Document document = DocumentBuilderFactory.newDefaultInstance().newDocumentBuilder().parse(xmlFileName);
            Element root = document.getDocumentElement();
            String insertSentence = "INSERT INTO " + root.getChildNodes().item(1).getNodeName() + " (";
            NodeList childrenNodes = root.getChildNodes();
            insertSentence += "nombre,";
            System.out.println(root.getChildNodes().item(1).getChildNodes().item(3).getTextContent());
            System.out.println(childrenNodes.item(1).getChildNodes().getLength());
            for (int i = 1; i <= childrenNodes.item(1).getChildNodes().getLength(); i+=2) {
                Node node = childrenNodes.item(i);
                insertSentence += node.getChildNodes().item(i).getNodeName() + ",";
            }
            insertSentence = insertSentence.substring(0, insertSentence.length()-1).concat(") VALUES (");
            System.out.println(insertSentence);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    public void exportTableToXml(Tablas tableName, String xmlFileName) {
        ArrayList<Agregable> rows = this.getData(tableName);
        try {
            Document document = DocumentBuilderFactory.newDefaultInstance().newDocumentBuilder().newDocument();
            String rootName = switch (tableName) {
                case DESARROLLADOR -> "desarrolladores";
                case EDITOR -> "editores";
                case GENERO -> "generos";
                case PLATAFORMA -> "plataformas";
                case VIDEOJUEGO -> "videojuegos";
                case VIDEOJUEGO_GENERO -> "videojuego-generos";
                case VIDEOJUEGO_PLATAFORMA -> "videojuego-plataformas";
            };
            Element root = document.createElement(rootName);
            Element childElement;
            for (Agregable row : rows) {
                childElement = document.createElement(tableName.getTableName());
                if (row instanceof Desarrollador) {
                    Desarrollador developer = (Desarrollador) row;
                    childElement.setAttribute(TablaDesarrollador.NOMBRE.getColumnName(), developer.getNombre());
                    Element date = document.createElement(TablaDesarrollador.FECHA_FUNDACION.getColumnName());
                    date.appendChild(document.createTextNode(developer.getFechaFundacion().toString()));
                    childElement.appendChild(date);
                } else if (row instanceof Editor) {
                    Editor editor = (Editor) row;
                    childElement.setAttribute(TablaEditor.NOMBRE.getColumnName(), editor.getNombre());
                    Element date = document.createElement(TablaDesarrollador.FECHA_FUNDACION.getColumnName());
                    date.appendChild(document.createTextNode(editor.getFechaFundacion().toString()));
                    childElement.appendChild(date);
                } else if (row instanceof Genero) {
                    Genero gender = (Genero) row;
                    childElement.setAttribute(TablaGenero.NOMBRE.getColumnName(), gender.getNombre());
                    Element description = document.createElement(TablaGenero.DESCRIPCION.getColumnName());
                    description.appendChild(document.createElement(gender.getDescripcion()));
                    childElement.appendChild(description);
                } else if (row instanceof Plataforma) {
                    Plataforma platform = (Plataforma) row;
                    childElement.setAttribute(TablaPlataforma.NOMBRE.getColumnName(), platform.getNombre());
                } else if (row instanceof Videojuego) {
                    Videojuego videogame = (Videojuego) row;
                    childElement.setAttribute(TablaPlataforma.NOMBRE.getColumnName(), videogame.getNombre());
                    Element synopsis = document.createElement(TablaVideojuego.SINOPSIS.getColumnName());
                    synopsis.appendChild(document.createTextNode(videogame.getSinopsis()));
                    Element datePublication = document.createElement(TablaVideojuego.FECHA_PUBLICACION.getColumnName());
                    datePublication.appendChild(document.createTextNode(videogame.getFechaPublicacion().toString()));
                    Element developerName = document.createElement(TablaVideojuego.NOMBRE_DESARROLLADOR.getColumnName());
                    developerName.appendChild(document.createTextNode(videogame.getNombreDesarrollador()));
                    Element editorName = document.createElement(TablaVideojuego.NOMBRE_EDITOR.getColumnName());
                    editorName.appendChild(document.createTextNode(videogame.getNombreEditor()));
                    childElement.appendChild(synopsis);
                    childElement.appendChild(datePublication);
                    childElement.appendChild(developerName);
                    childElement.appendChild(editorName);
                } else if (row instanceof VideojuegoGenero) {
                    VideojuegoGenero videogameGender = (VideojuegoGenero) row;
                    childElement.setAttribute(TablaVideojuegoGenero.NOMBRE_VIDEOJUEGO.getColumnName(),
                            videogameGender.getNombreVideojuego());
                    Element videogameGenderElement = document.createElement(TablaVideojuegoGenero.NOMBRE_GENERO.getColumnName());
                    videogameGenderElement.appendChild(document.createTextNode(videogameGender.getNombreGenero()));
                    childElement.appendChild(videogameGenderElement);
                } else if (row instanceof VideojuegoPlataforma) {
                    VideojuegoPlataforma videogamePlatform = (VideojuegoPlataforma) row;
                    childElement.setAttribute(TablaVideojuegoPlataforma.NOMBRE_VIDEOJUEGO.getColumnName(),
                            videogamePlatform.getNombreVideojuego());
                    Element videogamePlatformElement =
                            document.createElement(TablaVideojuegoPlataforma.NOMBRE_PLATAFORMA.getColumnName());
                    videogamePlatformElement.appendChild(document.createTextNode(videogamePlatform.getNombrePlataforma()));
                    childElement.appendChild(videogamePlatformElement);
                }
                root.appendChild(childElement);
            }
            FileWriter fw = new FileWriter(xmlFileName);
            document.appendChild(root);
            Transformer transformer = TransformerFactory.newDefaultInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(fw);
            transformer.transform(domSource, streamResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
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