package src.models.utils;

import src.models.enums.Columnable;

/**
 * Clase abstracta que guarda en una propiedad un objeto que tenga implementada la interfaz {@link Columnable},
 * que solo lo tendrán las clases enum que sean relativas con las tablas de la base de datos. De esta clase,
 * se especializan dos clases más, {@link ColumnFilter} y {@link ColumnOrder}.
 * @author José Julio
 * @version 1.0
 * @see Columnable
 */
public abstract class Column {
    private Columnable columnName;

    /**
     * Crea un objeto Column que almacena en una propiedad una tabla enum que implementa
     * la interfaz Columnable.
     * @param columnName Nombre de la columna
     */
    protected Column(Columnable columnName) {
        this.columnName = columnName;
    }

    public Columnable getColumnName() {
        return columnName;
    }
}