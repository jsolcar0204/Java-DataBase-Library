package src.models.enums;

import src.models.tables.*;

/**
 * Enum que guarda información de todas las tablas de la base de datos.
 * Se guarda el nombre de la tabla, el número de columnas que tiene la tabla, la clase
 * enum equivalente de la tabla (almacena las columnas de la tabla a detalle) y la clase
 * de mapeado equivalente de la tabla (para crear objetos de la tabla).
 * @author José Julio
 * @version 1.0
 * @see Columnable
 * @see Agregable
 */
public enum Tablas {
    /**
     * Tabla <b>Desarrollador</b> de la base de datos
     */
    DESARROLLADOR("desarrollador", 2, TablaDesarrollador.class, Desarrollador.class),
    /**
     * Tabla <b>Editor</b> de la base de datos
     */
    EDITOR("editor", 2, TablaEditor.class, Editor.class),
    /**
     * Tabla <b>Genero</b> de la base de datos
     */
    GENERO("genero", 2, TablaGenero.class, Genero.class),
    /**
     * Tabla <b>Plataforma</b> de la base de datos
     */
    PLATAFORMA("plataforma", 1, TablaPlataforma.class, Plataforma.class),
    /**
     * Tabla <b>Videojuego</b> de la base de datos
     */
    VIDEOJUEGO("videojuego", 5, TablaVideojuego.class, Videojuego.class),
    /**
     * Tabla <b>VideojuegoGenero</b> de la base de datos
     */
    VIDEOJUEGO_GENERO("videojuego_genero", 2, TablaVideojuegoGenero.class, VideojuegoGenero.class),
    /**
     * Tabla <b>VideojuegoPlataforma</b> de la base de datos
     */
    VIDEOJUEGO_PLATAFORMA("videojuego_plataforma", 2, TablaVideojuegoPlataforma.class, VideojuegoPlataforma.class);

    private String tableName;
    private int numColumns;
    private Class<? extends Columnable> equivalentTableEnum;
    private Class<? extends Agregable> equivalentTableClass;

    Tablas(String tableName, int numColumns, Class<? extends Columnable> equivalentTableEnum, Class<? extends Agregable> equivalentTableClass) {
        this.tableName = tableName;
        this.numColumns = numColumns;
        this.equivalentTableEnum = equivalentTableEnum;
        this.equivalentTableClass = equivalentTableClass;
    }

    @Override
    public String toString() {
        return tableName;
    }

    public String getTableName() {
        return tableName;
    }

    public int getNumColumns() {
        return numColumns;
    }

    public Class<? extends Columnable> getEquivalentTableEnum() {
        return equivalentTableEnum;
    }

    public Class<? extends Agregable> getEquivalentTableClass() {
        return equivalentTableClass;
    }
}