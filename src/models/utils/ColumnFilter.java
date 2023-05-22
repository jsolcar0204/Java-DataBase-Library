package src.models.utils;

import src.models.enums.Columnable;

/**
 * Clase que hereda de {@link Column}.
 * Almacena una propiedad un valor de cualquier tipo de dato, sirviendo
 * para realizar consultas de filtrado. Por ejemplo: nombre = "Capcom" o id = 3, haciendo
 * referencia a que el campo de la izquierda (clave) es el atributo de la clase {@link Column}
 * (columnName) y el campo de la derecha (valor) es el que se guarda en la propiedad de esta clase.
 * A diferencia del HashMap, la intención de esta clase es poder tener claves duplicadas.
 * @author José Julio
 * @version 1.0
 * @see Column
 */
public class ColumnFilter extends Column {
    private Object columnValue;

    /**
     * Crea un objeto ColumnFilter que permite crear una consulta con filtrado WHERE.
     * @param columnName Nombre de la columna que se desea filtrar (clave)
     * @param columnValue Nombre del valor que se desea filtrar (valor)
     */
    public ColumnFilter(Columnable columnName, Object columnValue) {
        super(columnName);
        this.columnValue = columnValue;
    }

    public Object getColumnValue() {
        return columnValue;
    }
}