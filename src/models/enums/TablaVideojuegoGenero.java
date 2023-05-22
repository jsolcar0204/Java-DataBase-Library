package src.models.enums;

/**
 * Enum que implementa la interfaz {@link Columnable}.
 * Guarda información respectiva a su tabla correspondiente de la base de datos, así como
 * el nombre de la columna, si es una clave primaria y si es una clave foránea.
 * @author José Julio
 * @version 1.0
 * @see src.models.tables.VideojuegoGenero
 */
public enum TablaVideojuegoGenero implements Columnable {
    /**
     * Columna <b>nombre_videojuego</b> de la tabla {@link src.models.tables.VideojuegoGenero VideojuegoGenero}
     */
    NOMBRE_VIDEOJUEGO("nombre_videojuego", true, true),
    /**
     * Columna <b>nombre_genero</b> de la tabla {@link src.models.tables.VideojuegoGenero VideojuegoGenero}
     */
    NOMBRE_GENERO("nombre_genero", true, true);

    private String columnName;
    private boolean isPrimaryKey, isForeignKey;

    TablaVideojuegoGenero(String columnName, boolean isPrimaryKey, boolean isForeignKey) {
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