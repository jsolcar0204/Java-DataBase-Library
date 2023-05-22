package src.database;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import src.models.enums.Columnable;
import src.models.enums.TablaDesarrollador;
import src.models.enums.TablaEditor;
import src.models.enums.TablaGenero;
import src.models.enums.TablaPlataforma;
import src.models.enums.TablaVideojuego;
import src.models.enums.TablaVideojuegoGenero;
import src.models.enums.TablaVideojuegoPlataforma;
import src.models.enums.Tablas;
import src.models.tables.Agregable;
import src.models.tables.Desarrollador;
import src.models.tables.Editor;
import src.models.tables.Genero;
import src.models.tables.Plataforma;
import src.models.tables.Videojuego;
import src.models.tables.VideojuegoGenero;
import src.models.tables.VideojuegoPlataforma;
import src.models.utils.ColumnFilter;
import src.models.utils.ColumnOrder;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Clase que permite realizar operaciones CRUD sobre la base
 * de datos de videojuegos. Permite realizar consultas personalizadas,
 * inserción de múltiples registros, actualización de múltiples registros,
 * eliminación de múltiples registros, convertir los datos de la base de datos
 * a XML y viceversa. Utiliza la clase {@link DatabaseConnection} para manejar la conexión.
 * @author José Julio
 * @version 1.0
 */
public class DatabaseManager {
    private DatabaseConnection databaseConnection;

    /**
     * Constructor que crea un objeto de tipo DatabaseManager. Permite indicar
     * si se desean utilizar transacciones para realizar el commit manualmente
     * o no. En el caso de que se realicen transacciones, se debe de <b>abrir y cerrar la conexión manualmente</b>,
     * que se hace llamando al método <b>getDatabaseConnection() (.open() para abrir y .disconnect() para cerrar)</b>.
     * Para realizar el commit: <b>getDatabaseConnection().getConnection().commit()</b>.
     * @param isAutoCommitEnabled Booleano para indicar si se desea trabajar con transacciones
     */
    public DatabaseManager(boolean isAutoCommitEnabled) {
        this.databaseConnection = DatabaseConnection.getInstance();
        this.databaseConnection.setAutoCommitEnabled(isAutoCommitEnabled);
    }

    /**
     * Obtiene todos los registros de la base de datos. Se le debe de pasar
     * el nombre de la tabla que se quiere obtener (utilizar enum {@link Tablas})
     * y, si se le pasan columnas, obtiene las columnas dichas de la tabla
     * (usar enums del paquete models.enums).
     * En caso de que no se pasen columnas, se obtendrán todas las columnas.
     * @param tableName Tabla de la que se quieren sacar los registros
     * @param columns Columnas que se desea obtener en la consulta
     * @return Devuelve un ArrayList con todos los registros que ha encontrado según
     * los parámetros especificados
     */
    public ArrayList<Agregable> getData(Tablas tableName, Columnable... columns) {
        ArrayList<Agregable> rows = new ArrayList<Agregable>();
        if (tableName == null || columns == null || this.checkIfTableTypeNotEqualsColumnsType(tableName, columns)) {
            return rows;
        }
        if (this.databaseConnection.isAutoCommitEnabled()) {
            this.databaseConnection.open();
        }
        try {
            PreparedStatement preparedStatement =
                    this.databaseConnection.getConnection()
                            .prepareStatement(this.createBasicQuery(tableName, columns).toString());
            ResultSet rs = preparedStatement.executeQuery();
            rows = this.executeQueryByTable(tableName, rs, columns);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (this.databaseConnection.isAutoCommitEnabled()) {
                this.databaseConnection.disconnect();
            }
        }
        return rows;
    }

    /**
     * Obtiene todos los registros de la base de datos mediante un filtrado.
     * Se le debe de pasar el nombre de la tabla que se quiere obtener (utilizar enum {@link Tablas}),
     * un HashMap en el que la clave sea la columna que se desea utilizar
     * (usar enums del paquete models.enums), y el valor sea el valor del campo que deseamos buscar.
     * Si se le pasan columnas, obtiene las columnas dichas de la tabla
     * (usar enums del paquete models.enums).
     * En caso de que no se pasen columnas, se obtendrán todas las columnas.
     * @param tableName Tabla de la que se quieren sacar los registros
     * @param filters Filtro que se desea realizar, en el campo clave usar el nombre
     *                de la columna que queremos y en el campo valor el campo que queremos buscar
     *                en la base de datos
     * @param columns Columnas que se desea obtener
     * @return Devuelve un ArrayList con todos los registros que ha encontrado según
     * los parámetros especificados
     */
    public ArrayList<Agregable> getData(Tablas tableName, HashMap<Columnable, Object> filters, Columnable... columns) {
        ArrayList<Agregable> rows = new ArrayList<Agregable>();
        if (tableName == null || filters == null || columns == null ||
                this.checkIfTableTypeNotEqualsColumnsType(tableName, filters) ||
                this.checkIfTableTypeNotEqualsColumnsType(tableName, columns) || filters.size() < 2) {
            return rows;
        }
        if (this.databaseConnection.isAutoCommitEnabled()) {
            this.databaseConnection.open();
        }
        try {
            PreparedStatement preparedStatement =
                    this.databaseConnection.getConnection()
                            .prepareStatement(this.createBasicQuery(tableName, columns)
                                    .append(this.createBasicQuery(filters)).toString());
            int dataType = 0, parameterCounter = 0;
            for (Object value : filters.values()) {
                if (value instanceof String) {
                    dataType = Types.VARCHAR;
                } else if (value instanceof Integer) {
                    dataType = Types.INTEGER;
                } else if (value instanceof Double) {
                    dataType = Types.DOUBLE;
                }
                preparedStatement.setObject(++parameterCounter, value, dataType);
            }
            ResultSet rs = preparedStatement.executeQuery();
            rows = this.executeQueryByTable(tableName, rs, columns);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (this.databaseConnection.isAutoCommitEnabled()) {
                this.databaseConnection.disconnect();
            }
        }
        return rows;
    }

    /**
     * Permite realizar una consulta a la base de datos con una ordenación en las columnas.
     * Se le debe de pasar el nombre de la tabla que se quiere obtener (utilizar enum {@link Tablas}),
     * un ArrayList de {@link ColumnOrder} en el que se le deberá de pasar como parámetros la columna
     * que se desea ordenar (usar enums del paquete models.enums), y como segundo parámetro el tipo
     * de ordenación que se quiere (usar enum {@link src.models.enums.other.Orders Orders}).
     * Si se le pasan columnas, obtiene las columnas dichas de la tabla
     * (usar enums del paquete models.enums).
     * En caso de que no se pasen columnas, se obtendrán todas las columnas.
     * @param tableName Tabla de la que se quieren sacar los registros
     * @param orders Lista de campos de la base de datos que se desean ordenar por ASC o DESC
     * @param columns Columnas que se desea obtener
     * @return Devuelve un ArrayList con todos los registros que ha encontrado según
     * los parámetros especificados
     */
    public ArrayList<Agregable> getData(Tablas tableName, ArrayList<ColumnOrder> orders, Columnable... columns) {
        ArrayList<Agregable> rows = new ArrayList<Agregable>();
        if (tableName == null || orders == null || columns == null ||
                this.checkIfTableTypeNotEqualsColumnsTypeWithListColumnOrder(tableName, orders) ||
                this.checkIfTableTypeNotEqualsColumnsType(tableName, columns)) {
            return rows;
        }
        if (this.databaseConnection.isAutoCommitEnabled()) {
            this.databaseConnection.open();
        }
        try {
            PreparedStatement preparedStatement =
                    this.databaseConnection.getConnection()
                            .prepareStatement(this.createBasicQuery(tableName, columns)
                                    .append(this.createBasicQuery(orders)).toString());
            ResultSet rs = preparedStatement.executeQuery();
            rows = this.executeQueryByTable(tableName, rs, columns);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (this.databaseConnection.isAutoCommitEnabled()) {
                this.databaseConnection.disconnect();
            }
        }
        return rows;
    }

    /**
     * Obtiene todos los registros de la base de datos mediante un filtrado y una ordenación en las columnas.
     * Se le debe de pasar el nombre de la tabla que se quiere obtener (utilizar enum {@link Tablas}),
     * un HashMap en el que la clave sea la columna que se desea utilizar
     * (usar enums del paquete models.enums), y el valor sea el valor del campo que deseamos buscar.
     * Para ordenar, se debe pasar un ArrayList de {@link ColumnOrder} en el que se le deberá de pasar
     * como parámetros la columna que se desea ordenar (usar enums del paquete models.enums que implementen
     * la interfaz {@link Columnable}), y como segundo parámetro el tipo de ordenación que se quiere
     * (usar enum {@link src.models.enums.other.Orders Orders}).
     * Si se le pasan columnas, obtiene las columnas dichas de la tabla
     * (usar enums del paquete models.enums).
     * En caso de que no se pasen columnas, se obtendrán todas las columnas.
     * @param tableName Tabla de la que se quieren sacar los registros
     * @param filters Filtro que se desea realizar, en el campo clave usar el nombre
     *                de la columna que queremos y en el campo valor el campo que queremos buscar
     *                en la base de datos
     * @param orders Lista de campos de la base de datos que se desean ordenar por ASC o DESC
     * @param columns Columnas que se desea obtener
     * @return Devuelve un ArrayList con todos los registros que ha encontrado según
     * los parámetros especificados
     */
    public ArrayList<Agregable> getData(Tablas tableName, HashMap<Columnable, Object> filters, ArrayList<ColumnOrder> orders,
                                        Columnable... columns) {
        ArrayList<Agregable> rows = new ArrayList<Agregable>();
        if (tableName == null || filters == null || orders == null ||
                columns == null || this.checkIfTableTypeNotEqualsColumnsType(tableName, filters) ||
                this.checkIfTableTypeNotEqualsColumnsTypeWithListColumnOrder(tableName, orders) ||
                this.checkIfTableTypeNotEqualsColumnsType(tableName, columns) || filters.size() < 2) {
            return rows;
        }
        if (this.databaseConnection.isAutoCommitEnabled()) {
            this.databaseConnection.open();
        }
        try {
            PreparedStatement preparedStatement =
                    this.databaseConnection.getConnection().prepareStatement(this.createBasicQuery(tableName, columns)
                            .append(this.createBasicQuery(filters).append(this.createBasicQuery(orders))).toString());
            int dataType = 0, parameterCounter = 0;
            for (Object value : filters.values()) {
                if (value instanceof String) {
                    dataType = Types.VARCHAR;
                } else if (value instanceof Integer) {
                    dataType = Types.INTEGER;
                } else if (value instanceof Double) {
                    dataType = Types.DOUBLE;
                }
                preparedStatement.setObject(++parameterCounter, value, dataType);
            }
            ResultSet rs = preparedStatement.executeQuery();
            rows = this.executeQueryByTable(tableName, rs, columns);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (this.databaseConnection.isAutoCommitEnabled()) {
                this.databaseConnection.disconnect();
            }
        }
        return rows;
    }

    /**
     * Actualiza una o varias filas de una tabla pasada como argumento de la base de datos.
     * Se le debe de pasar el enum de la tabla que se quiere actualizar y uno o varios
     * objetos que tengan implementada la interfaz {@link Agregable}. No permite la actualización
     * de claves primarias ni foráneas.
     * @param tableName Tabla de la que se quieren actualizar registros
     * @param agregables Objetos que tengan implementada la interfaz Agregable
     * @return Devuelve el número de filas que se han actualizado en la tabla de la base de datos.
     * @see Agregable
     */
    public int updateRows(Tablas tableName, Agregable... agregables) {
        if (tableName == null || agregables == null || agregables.length == 0 ||
                this.checkIfTableTypeNotEqualsColumnsType(tableName, agregables)) {
            return 0;
        }
        try {
            PreparedStatement preparedStatement;
            Columnable[] selectedTableEnumFields = this.categorizeTable(tableName);
            String fieldsToUpdate = Arrays.stream(selectedTableEnumFields)
                    .filter(field -> !field.isPrimaryKey() && !field.isForeignKey())
                    .map(field -> field.getColumnName() + " = ?,")
                    .collect(Collectors.joining());
            fieldsToUpdate = fieldsToUpdate.substring(0, fieldsToUpdate.length()-1);
            int parameterCounter, resultUpdate = 0;
            Field[] agregableFields;
            List<Columnable> invalidColumnsToUpdate = Arrays.stream(selectedTableEnumFields)
                    .filter(Columnable::isPrimaryKey)
                    .toList();
            StringBuilder updateSentence;
            if (this.databaseConnection.isAutoCommitEnabled()) {
                this.databaseConnection.open();
            }
            for (Agregable agregable : agregables) {
                parameterCounter = 0;
                updateSentence = new StringBuilder("UPDATE " + tableName + " SET " + fieldsToUpdate + " WHERE nombre = ?");
                preparedStatement = this.databaseConnection.getConnection().prepareStatement(updateSentence.toString());
                agregableFields = agregable.getClass().getDeclaredFields();
                NEXT_AGREGABLE:
                // Comienza en la segunda posición porque se da por hecho que
                // la clave primaria estará siempre en la primera columna
                for (int i = 1; i < agregableFields.length; i++) {
                    for (Columnable invalidColumn : invalidColumnsToUpdate) {
                        if (agregableFields[i].getName().startsWith(invalidColumn.getColumnName())) {
                            continue NEXT_AGREGABLE;
                        }
                    }
                    agregableFields[i].setAccessible(true);
                    preparedStatement.setObject(++parameterCounter, agregableFields[i].get(agregable));
                }
                agregableFields[0].setAccessible(true);
                preparedStatement.setObject(++parameterCounter, agregableFields[0].get(agregable));
                resultUpdate += preparedStatement.executeUpdate();
                preparedStatement.close();
            }
            return resultUpdate;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (this.databaseConnection.isAutoCommitEnabled()) {
                this.databaseConnection.disconnect();
            }
        }
        return 0;
    }

    /**
     * Inserta uno o varios registros de una tabla pasada como argumento de la base de datos.
     * Se le debe de pasar el enum de la tabla que se quiere actualizar y uno o varios
     * objetos que tengan implementada la interfaz {@link Agregable}.
     * @param tableName Tabla de la que se quieren insertar registros
     * @param agregables Objetos que tengan implementada la interfaz Agregable
     * @return Devuelve el número de filas que se han agregado en la tabla de la base de datos.
     * @see Agregable
     */
    public int insertRows(Tablas tableName, Agregable... agregables) {
        if (tableName == null || agregables == null || agregables.length == 0 ||
                this.checkIfTableTypeNotEqualsColumnsType(tableName, agregables)) {
            return 0;
        }
        try {
            String fieldsToInsert = Arrays.stream(this.categorizeTable(tableName))
                    .map(Columnable::getColumnName)
                    .collect(Collectors.joining(","));
            StringBuilder insertSentence = new StringBuilder("INSERT INTO " + tableName + " (" + fieldsToInsert + ") VALUES ");
            Field[] agregableFields;
            for (Agregable agregable : agregables) {
                agregableFields = agregable.getClass().getDeclaredFields();
                insertSentence.append("(").append("?,".repeat(agregableFields.length));
                insertSentence = new StringBuilder(insertSentence.substring(0, insertSentence.length()-1));
                insertSentence.append("),");
            }
            insertSentence = new StringBuilder(insertSentence.substring(0, insertSentence.length()-1));
            if (this.databaseConnection.isAutoCommitEnabled()) {
                this.databaseConnection.open();
            }
            PreparedStatement preparedStatement = this.databaseConnection.getConnection().prepareStatement(insertSentence.toString());
            int dataType = 0, parameterCounter = 0;
            for (Agregable agregable : agregables) {
                agregableFields = agregable.getClass().getDeclaredFields();
                for (Field field : agregableFields) {
                    field.setAccessible(true);
                    if (field.get(agregable) == null) {
                        return 0;
                    }
                    if (field.get(agregable) instanceof String) {
                        dataType = Types.VARCHAR;
                    } else if (field.get(agregable) instanceof Integer) {
                        dataType = Types.INTEGER;
                    } else if (field.get(agregable) instanceof Double) {
                        dataType = Types.DOUBLE;
                    }
                    preparedStatement.setObject(++parameterCounter, field.get(agregable), dataType);
                }
            }
            int resultInsert = preparedStatement.executeUpdate();
            preparedStatement.close();
            return resultInsert;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (this.databaseConnection.isAutoCommitEnabled()) {
                this.databaseConnection.disconnect();
            }
        }
        return 0;
    }

    /**
     * Elimina una o varias filas de una tabla pasada como argumento de la base de datos.
     * Se le debe de pasar el enum de la tabla que se quiere actualizar y uno o varios
     * objetos que tengan implementada la interfaz {@link Agregable}. Como segundo parámetro se
     * debe de pasar un ArrayList de {@link ColumnFilter}, que almacenará dos campos, el primero
     * es la columna que se desea filtrar para el borrado (usar enums del paquete models.enums que implementen
     * la interfaz {@link Columnable}) y el segundo campo el valor que se quiere eliminar. No se
     * utiliza un HashMap debido a que si se quieren filtrar por varias columnas de la misma
     * (por ejemplo, TablaDesarrollador.NOMBRE = "Capcom" OR TablaDesarrollador.NOMBRE = "Mojang"),
     * con un HashMap no se podría, ya que la clave no sería única.
     * @param tableName Tabla de la que se quieren eliminar los registros
     * @param columnsFilter Objetos que tengan implementada la interfaz Agregable
     * @return Devuelve el número de filas que se han actualizado en la tabla de la base de datos.
     * @see Agregable
     */
    public int deleteRows(Tablas tableName, ArrayList<ColumnFilter> columnsFilter) {
        if (tableName == null || columnsFilter == null ||
                this.checkIfTableTypeNotEqualsColumnsTypeWithListColumnFilter(tableName, columnsFilter)) {
            return 0;
        }
        try {
            StringBuilder deleteSentence = new StringBuilder("DELETE FROM " + tableName + " WHERE ");
            for (ColumnFilter columnFilter : columnsFilter) {
                deleteSentence.append(columnFilter.getColumnName()).append(" = ? OR ");
            }
            deleteSentence = new StringBuilder(deleteSentence.substring(0, deleteSentence.length()-4));
            if (this.databaseConnection.isAutoCommitEnabled()) {
                this.databaseConnection.open();
            }
            PreparedStatement preparedStatement =
                    this.databaseConnection.getConnection().prepareStatement(deleteSentence.toString());
            int dataType = 0, parameterCounter = 0;
            Object columnValue;
            for (ColumnFilter columnFilter : columnsFilter) {
                columnValue = columnFilter.getColumnValue();
                if (columnValue instanceof String) {
                    dataType = Types.VARCHAR;
                } else if (columnValue instanceof Integer) {
                    dataType = Types.INTEGER;
                } else if (columnValue instanceof Double) {
                    dataType = Types.DOUBLE;
                }
                preparedStatement.setObject(++parameterCounter, columnValue, dataType);
            }
            int resultExecute = preparedStatement.executeUpdate();
            preparedStatement.close();
            return resultExecute;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (this.databaseConnection.isAutoCommitEnabled()) {
                this.databaseConnection.disconnect();
            }
        }
        return 0;
    }

    /**
     * Importa de un fichero XML especificado a una cláusula INSERT INTO
     * en la base de datos. Para poder realizar la importación, será necesario
     * que el XML tenga el mismo número de columnas que tenga su tabla equivalente.
     * @param tableName Tabla de la que se quieren insertar los registros
     * @param xmlFileName Ruta del fichero XML a importar (importante, añadir la extensión)
     * @return Devuelve true si se pudo realizar la importación y false si no se pudo.
     */
    public boolean importXmlToTable(Tablas tableName, String xmlFileName) {
        if (tableName == null || xmlFileName == null || !new File(xmlFileName).exists()) {
            return false;
        }
        try {
            Document document = DocumentBuilderFactory.newDefaultInstance().newDocumentBuilder().parse(xmlFileName);
            Element root = document.getDocumentElement();
            NodeList childrenRootNodesElement = root.getChildNodes();
            if (this.databaseConnection.isAutoCommitEnabled()) {
                this.databaseConnection.open();
            }
            if (childrenRootNodesElement.getLength() == 0 ||
                    !childrenRootNodesElement.item(1).getNodeName().equals(tableName.getTableName()) ||
                    childrenRootNodesElement.item(1).getAttributes().item(0) == null) {
                return false;
            }
            int elementCounter = 0;
            for (int i = 0; i < childrenRootNodesElement.getLength(); i++) {
                Node element = childrenRootNodesElement.item(i);
                // Contar solo los elementos
                if (element.getNodeType() == Node.ELEMENT_NODE) {
                    elementCounter++;
                    for (int j = 0; j < element.getChildNodes().getLength(); j++) {
                        Node childElement = element.getChildNodes().item(j);
                        if (childElement.getNodeType() == Node.ELEMENT_NODE) {
                            elementCounter++;
                        }
                    }
                    break;
                }
            }
            // Si el primer elemento (videojuego por ejemplo) del XML no tiene el mismo número de campos
            // que el de su tabla correspondiente, no se puede importar, ya que
            // se debe realizar un INSERT INTO con todos los campos
            if (elementCounter != tableName.getEquivalentTableEnum().getFields().length) {
                return false;
            }
            StringBuilder insertSentence = new StringBuilder("INSERT INTO " + tableName + " VALUES ");
            for (int i = 0; i < childrenRootNodesElement.getLength(); i++) {
                Node childrenNode = childrenRootNodesElement.item(i);
                if (childrenNode.getNodeType() == Node.ELEMENT_NODE) {
                    insertSentence.append("(?,");
                    for (int j = 0; j < childrenNode.getChildNodes().getLength(); j++) {
                        Node childrenTagNode = childrenNode.getChildNodes().item(j);
                        if (childrenTagNode.getNodeType() == Node.ELEMENT_NODE) {
                            insertSentence.append("?,");
                        }
                    }
                    insertSentence = new StringBuilder(insertSentence.substring(0, insertSentence.length()-1));
                    insertSentence.append("),");
                }
            }
            insertSentence = new StringBuilder(insertSentence.substring(0, insertSentence.length()-1));

            int parameterCounter = 0;
            PreparedStatement preparedStatement = this.databaseConnection.getConnection().prepareStatement(insertSentence.toString());
            for (int i = 0; i < childrenRootNodesElement.getLength(); i++) {
                Node childrenNode = childrenRootNodesElement.item(i);
                if (childrenNode.getNodeType() == Node.ELEMENT_NODE) {
                    preparedStatement.setString(++parameterCounter, childrenNode.getAttributes().item(0).getNodeValue());
                    for (int j = 0; j < childrenNode.getChildNodes().getLength(); j++) {
                        Node childrenTagNode = childrenNode.getChildNodes().item(j);
                        if (childrenTagNode.getNodeType() == Node.ELEMENT_NODE) {
                            preparedStatement.setString(++parameterCounter, childrenTagNode.getTextContent());
                        }
                    }
                }
            }
            preparedStatement.executeUpdate();
            preparedStatement.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (this.databaseConnection.isAutoCommitEnabled()) {
                this.databaseConnection.disconnect();
            }
        }
        return false;
    }

    /**
     * Exporta una consulta SQL de la base de datos a XML.
     * @param tableName Tabla de la que se quieren insertar los registros
     * @param xmlFileName Ruta del fichero XML a importar (importante, añadir la extensión)
     * @param columns Columnas que se desea obtener en la consulta
     * @return Devuelve true si se exportó correctamente el archivo XML y false si no lo hizo.
     */
    public boolean exportTableToXml(Tablas tableName, String xmlFileName, Columnable... columns) {
        if (tableName == null || xmlFileName == null || columns == null ||
                this.checkIfTableTypeNotEqualsColumnsType(tableName, columns)) {
            return false;
        }
        ArrayList<Agregable> rows = this.getData(tableName, columns);
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
            Columnable[] columnables = this.categorizeTable(tableName);
            Element root = document.createElement(rootName);
            Element childElement, element;
            List<Field> agregableFields;
            Arrays.sort(columns);
            for (Agregable row : rows) {
                childElement = document.createElement(tableName.getTableName());
                agregableFields = Arrays.stream(row.getClass().getDeclaredFields())
                        .filter(field -> {
                            boolean fieldIsNotNull = false;
                            try {
                                field.setAccessible(true);
                                fieldIsNotNull = field.get(row) != null;
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                            return fieldIsNotNull;
                        }).toList();
                for (Field agregableField : agregableFields) {
                    agregableField.setAccessible(true);
                    // Si la primera columna equivale a la clave primaria, ponerlo como atributo
                    if (agregableField.getName().equals(this.convertSnakeCaseToCamelCase(columnables[0].getColumnName()))) {
                        childElement.setAttribute(agregableField.getName(), (String) agregableField.get(row));
                        continue;
                    }
                    element = document.createElement(agregableField.getName());
                    element.appendChild(document.createTextNode(agregableField.get(row).toString()));
                    childElement.appendChild(element);
                }
                root.appendChild(childElement);
            }
            document.appendChild(root);
            Transformer transformer = TransformerFactory.newDefaultInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(new DOMSource(document), new StreamResult(new FileWriter(xmlFileName)));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Almacena los registros de una consulta SQL en un ArrayList de {@link Agregable}
     * a partir del nombre de la tabla especificada en el parámetro, el ResultSet que ha llegado de la consulta
     * y las {@link Columnable columnas} por las que se ha filtrado.
     * @param tableName Tabla de la que se quieren insertar los registros
     * @param rs Registros encontrados en la consulta realizada
     * @param columns Columnas que se desea obtener en la consulta
     * @return Devuelve un ArrayList con todos los objetos mapeados que han llegado de la consulta
     * de la base de datos
     */
    private ArrayList<Agregable> executeQueryByTable(Tablas tableName, ResultSet rs, Columnable[] columns) {
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
                            if (TablaDesarrollador.NOMBRE.getColumnName().equals(columns[i].getColumnName())) {
                                desarrollador.setNombre(rs.getString(i+1));
                            } else if (TablaDesarrollador.FECHA_FUNDACION.getColumnName().equals(columns[i].getColumnName())) {
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
                            if (TablaEditor.NOMBRE.getColumnName().equals(columns[i].getColumnName())) {
                                editor.setNombre(rs.getString(i+1));
                            } else if (TablaEditor.FECHA_FUNDACION.getColumnName().equals(columns[i].getColumnName())) {
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
                            if (TablaGenero.NOMBRE.getColumnName().equals(columns[i].getColumnName())) {
                                genero.setNombre(rs.getString(i+1));
                            } else if (TablaGenero.DESCRIPCION.getColumnName().equals(columns[i].getColumnName())) {
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
                            if (TablaVideojuego.NOMBRE.getColumnName().equals(columns[i].getColumnName())) {
                                videojuego.setNombre(rs.getString(i+1));
                            } else if (TablaVideojuego.SINOPSIS.getColumnName().equals(columns[i].getColumnName())) {
                                videojuego.setSinopsis(rs.getString(i+1));
                            } else if (TablaVideojuego.FECHA_PUBLICACION.getColumnName().equals(columns[i].getColumnName())) {
                                videojuego.setFechaPublicacion(Instant.ofEpochMilli(rs.getDate(i+1).getTime())
                                        .atZone(ZoneId.systemDefault()).toLocalDate());
                            } else if (TablaVideojuego.NOMBRE_DESARROLLADOR.getColumnName().equals(columns[i].getColumnName())) {
                                videojuego.setNombreDesarrollador(rs.getString(i+1));
                            } else if (TablaVideojuego.NOMBRE_EDITOR.getColumnName().equals(columns[i].getColumnName())) {
                                videojuego.setNombreEditor(rs.getString(i+1));
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
                            if (TablaVideojuegoGenero.NOMBRE_VIDEOJUEGO.getColumnName().equals(columns[i].getColumnName())) {
                                videojuegoGenero.setNombreVideojuego(rs.getString(i+1));
                            } else if (TablaVideojuegoGenero.NOMBRE_GENERO.getColumnName().equals(columns[i].getColumnName())) {
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
                            if (TablaVideojuegoPlataforma.NOMBRE_VIDEOJUEGO.getColumnName().equals(columns[i].getColumnName())) {
                                videojuegoPlataforma.setNombreVideojuego(rs.getString(i+1));
                            } else if (TablaVideojuegoPlataforma.NOMBRE_PLATAFORMA.getColumnName().equals(columns[i].getColumnName())) {
                                videojuegoPlataforma.setNombrePlataforma(rs.getString(i+1));
                            }
                        }
                        rows.add(videojuegoPlataforma);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return rows;
    }

    /**
     * Crea una consulta básica con el nombre de la tabla que se consultaron los datos
     * y, si tiene columnas pasadas por parámetro, se muestran las columnas únicamente.
     * La consulta creada es del estilo "SELECT * FROM x" o "SELECT x,x FROM x".
     * @param tableName Tabla de la que se quieren sacar los registros
     * @param columns Columnas que se desea obtener en la consulta
     * @return Devuelve un StringBuilder para que en caso de que se realice una consulta
     * más compleja, se pueda seguir concatenando.
     */
    private StringBuilder createBasicQuery(Tablas tableName, Columnable[] columns) {
        StringBuilder query = new StringBuilder("SELECT ");
        if (columns.length == 0) {
            query.append("*");
        } else {
            for (Columnable column : columns) {
                query.append(column).append(",");
            }
            query = new StringBuilder(query.substring(0, query.length()-1));
        }
        query.append(" FROM ").append(tableName);
        return query;
    }

    /**
     * Crea una consulta básica con filtros (cláusula WHERE). Se le debe pasar un HashMap en el que
     * la clave sea la columna que se desea utilizar (usar enums del paquete models.enums), y el valor
     * sea el valor del campo que deseamos buscar.
     * La consulta creada es del estilo "WHERE x = x" o "WHERE x = x AND y = y".
     * @param filters Filtro que se desea realizar, en el campo clave usar el nombre de la
     *                columna que queremos y en el campo valor el campo que queremos buscar en la base de datos
     * @return Devuelve un StringBuilder para que en caso de que se realice una consulta
     * más compleja, se pueda seguir concatenando.
     */
    private StringBuilder createBasicQuery(HashMap<Columnable, Object> filters) {
        StringBuilder whereQuery = new StringBuilder(" WHERE ");
        for (Columnable key : filters.keySet()) {
            whereQuery.append(key).append(" = ? AND ");
        }
        whereQuery = new StringBuilder(whereQuery.substring(0, whereQuery.length()-5));
        return whereQuery;
    }

    /**
     * Crea una consulta básica con ordenación (cláusula ORDER BY). Se le debe pasar un ArrayList
     * de {@link ColumnOrder} en el que se le deberá de pasar como parámetros la columna
     * que se desea ordenar (usar enums del paquete models.enums), y como segundo parámetro el tipo
     * de ordenación que se quiere (usar enum {@link src.models.enums.other.Orders Orders}).
     * La consulta creada es del estilo "ORDER BY x ASC" o "ORDER BY y DESC" o "ORDER BY i DESC,z ASC".
     * @param orders Lista de campos de la base de datos que se desean ordenar por ASC o DESC
     * @return Devuelve un StringBuilder para que en caso de que se realice una consulta
     * más compleja, se pueda seguir concatenando.
     */
    private StringBuilder createBasicQuery(ArrayList<ColumnOrder> orders) {
        StringBuilder orderQuery = new StringBuilder(" ORDER BY ");
        for (ColumnOrder columnOrder : orders) {
            orderQuery.append(columnOrder.getColumnName()).append(" ").append(columnOrder.getOrder()).append(",");
        }
        orderQuery = new StringBuilder(orderQuery.substring(0, orderQuery.length()-1));
        return orderQuery;
    }

    /**
     * Comprueba si las {@link Columnable columnas} pasadas como argumento, son las equivalentes
     * al nombre de la {@link Tablas tabla} que se ha pasado como primer argumento. Por ejemplo,
     * pasar Tablas.VIDEOJUEGO y TablaDesarrollador.NOMBRE son incompatibles, pero Tablas.VIDEOJUEGO
     * y TablaVideojuego.NOMBRE sí son compatibles.
     * @param tableName Tabla seleccionada
     * @param columns Columnas a comparar si son equivalentes con la tabla seleccionada
     * @return Devuelve true si las columnas son equivalentes a la tabla seleccionada y false si no lo son.
     */
    private boolean checkIfTableTypeNotEqualsColumnsType(Tablas tableName, Columnable[] columns) {
        Class<? extends Columnable> equivalentColumnTable = tableName.getEquivalentTableEnum();
        for (Columnable column : columns) {
            if (!column.getClass().getSimpleName().equals(equivalentColumnTable.getSimpleName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Comprueba si los objetos {@link Agregable agregables} pasadas como argumento, son las equivalentes
     * al nombre de la {@link Tablas tabla} que se ha pasado como primer argumento. Por ejemplo,
     * pasar Tablas.VIDEOJUEGO y new Desarrollador() son incompatibles, pero Tablas.VIDEOJUEGO
     * y new Videojuego() sí son compatibles.
     * @param tableName Tabla seleccionada
     * @param agregables Objetos a comparar si son equivalentes con la tabla seleccionada
     * @return Devuelve true si las columnas son equivalentes a la tabla seleccionada y false si no lo son.
     */
    private boolean checkIfTableTypeNotEqualsColumnsType(Tablas tableName, Agregable[] agregables) {
        Class<? extends Agregable> equivalentColumnTable = tableName.getEquivalentTableClass();
        for (Agregable agregable : agregables) {
            if (!agregable.getClass().getSimpleName().equals(equivalentColumnTable.getSimpleName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Comprueba si el HashMap con {@link Columnable columnas} pasadas como argumento, son las equivalentes
     * al nombre de la {@link Tablas tabla} que se ha pasado como primer argumento. Por ejemplo,
     * pasar Tablas.VIDEOJUEGO y TablaDesarrollador.NOMBRE son incompatibles, pero Tablas.VIDEOJUEGO
     * y TablaVideojuego.NOMBRE sí son compatibles.
     * @param tableName Tabla seleccionada
     * @param columns Clave de columnas a comparar si son equivalentes con la tabla seleccionada
     * @return Devuelve true si las columnas son equivalentes a la tabla seleccionada y false si no lo son.
     */
    private boolean checkIfTableTypeNotEqualsColumnsType(Tablas tableName, HashMap<Columnable, Object> columns) {
        Class<? extends Columnable> equivalentColumnTable = tableName.getEquivalentTableEnum();
        for (Columnable column : columns.keySet()) {
            if (!column.getClass().getSimpleName().equals(equivalentColumnTable.getSimpleName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Comprueba si el ArrayList de {@link ColumnOrder}, la propiedad {@link Columnable columnName},
     * pasadas como argumento, son las equivalentes al nombre de la {@link Tablas tabla} que se ha pasado
     * como primer argumento. Por ejemplo, pasar Tablas.VIDEOJUEGO y TablaDesarrollador.NOMBRE son incompatibles,
     * pero Tablas.VIDEOJUEGO y TablaVideojuego.NOMBRE sí son compatibles.
     * @param tableName Tabla seleccionada
     * @param columns Lista de columnas a comparar si son equivalentes con la tabla seleccionada
     * @return Devuelve true si las columnas son equivalentes a la tabla seleccionada y false si no lo son.
     */
    private boolean checkIfTableTypeNotEqualsColumnsTypeWithListColumnOrder(Tablas tableName, ArrayList<ColumnOrder> columns) {
        Class<? extends Columnable> equivalentColumnTable = tableName.getEquivalentTableEnum();
        for (ColumnOrder column : columns) {
            if (!column.getColumnName().getClass().getSimpleName().equals(equivalentColumnTable.getSimpleName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Comprueba si el ArrayList de {@link ColumnFilter}, la propiedad {@link Columnable columnName},
     * pasadas como argumento, son las equivalentes al nombre de la {@link Tablas tabla} que se ha pasado
     * como primer argumento. Por ejemplo, pasar Tablas.VIDEOJUEGO y TablaDesarrollador.NOMBRE son incompatibles,
     * pero Tablas.VIDEOJUEGO y TablaVideojuego.NOMBRE sí son compatibles.
     * @param tableName Tabla seleccionada
     * @param columns Lista de columnas a comparar si son equivalentes con la tabla seleccionada
     * @return Devuelve true si las columnas son equivalentes a la tabla seleccionada y false si no lo son.
     */
    private boolean checkIfTableTypeNotEqualsColumnsTypeWithListColumnFilter(Tablas tableName, ArrayList<ColumnFilter> columns) {
        Class<? extends Columnable> equivalentColumnTable = tableName.getEquivalentTableEnum();
        for (ColumnFilter column : columns) {
            if (!column.getColumnName().getClass().getSimpleName().equals(equivalentColumnTable.getSimpleName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Devuelve un array de las constantes del enum equivalente a la {@link Tablas tabla}
     * pasada como argumento.
     * @param tableName Tabla seleccionada
     * @return Devuelve un array de las constantes que contiene la clase enum
     * equivalente a la tabla seleccionada como parámetro.
     */
    private Columnable[] categorizeTable(Tablas tableName) {
        return switch (tableName) {
            case DESARROLLADOR -> TablaDesarrollador.values();
            case EDITOR -> TablaEditor.values();
            case GENERO -> TablaGenero.values();
            case PLATAFORMA -> TablaPlataforma.values();
            case VIDEOJUEGO -> TablaVideojuego.values();
            case VIDEOJUEGO_GENERO -> TablaVideojuegoGenero.values();
            case VIDEOJUEGO_PLATAFORMA -> TablaVideojuegoPlataforma.values();
        };
    }

    /**
     * Método de ayuda para la conversión de nombres entre enums y su respectiva clase de mapeado,
     * que los enum utilizan snake case y en las clases utilizan pascal case.
     * @param snakeCaseString String en formato snake_case
     * @return Devuelve un String transformado la palabra en pascalCase
     */
    private String convertSnakeCaseToCamelCase(String snakeCaseString) {
        if (!snakeCaseString.contains("_")) {
            return snakeCaseString;
        }
        return Arrays.stream(snakeCaseString.split("_")).reduce("", (a, b) -> {
            if (a.equals("")) {
                return a.concat(b);
            }
            return a.concat(b.substring(0,1).toUpperCase().concat(b.substring(1)));
        });
    }

    public DatabaseConnection getDatabaseConnection() {
        return databaseConnection;
    }
}