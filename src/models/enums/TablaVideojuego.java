package src.models.enums;

/**
 * Enum que implementa la interfaz {@link Columnable}.
 * Guarda información respectiva a su tabla correspondiente de la base de datos, así como
 * el nombre de la columna, si es una clave primaria y si es una clave foránea.
 * @author José Julio
 * @version 1.0
 * @see src.models.tables.Videojuego
 */
public enum TablaVideojuego implements Columnable {
    /**
     * Columna <b>nombre</b> de la tabla {@link src.models.tables.Videojuego Videojuego}
     */
    NOMBRE("nombre", true, false),
    /**
     * Columna <b>sinopsis</b> de la tabla {@link src.models.tables.Videojuego Videojuego}
     */
    SINOPSIS("sinopsis", false, false),
    /**
     * Columna <b>fecha_publicacion</b> de la tabla {@link src.models.tables.Videojuego Videojuego}
     */
    FECHA_PUBLICACION("fecha_publicacion", false, false),
    /**
     * Columna <b>nombre_desarrollador</b> de la tabla {@link src.models.tables.Videojuego Videojuego}
     */
    NOMBRE_DESARROLLADOR("nombre_desarrollador", false, true),
    /**
     * Columna <b>nombre_editor</b> de la tabla {@link src.models.tables.Videojuego Videojuego}
     */
    NOMBRE_EDITOR("nombre_editor", false, true);

    private String columnName;
    private boolean isPrimaryKey, isForeignKey;

    TablaVideojuego(String columnName, boolean isPrimaryKey, boolean isForeignKey) {
        this.columnName = columnName;
        this.isPrimaryKey = isPrimaryKey;
        this.isForeignKey = isForeignKey;
    }

    @Override
    public String toString() {
        return columnName;
    }

    @Override
    public String getColumnName() {
        return columnName;
    }

    @Override
    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    @Override
    public boolean isForeignKey() {
        return isForeignKey;
    }
}