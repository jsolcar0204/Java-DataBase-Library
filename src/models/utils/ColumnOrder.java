package src.models.utils;

import src.models.enums.Columnable;
import src.models.enums.other.Orders;

/**
 * Clase que hereda de {@link Column}.
 * Almacena una propiedad un valor de la clase enum {@link Orders}, sirviendo
 * para realizar consultas de orden ascendente o descendente. Por ejemplo: nombre DESC o apellido ASC, haciendo
 * referencia a que el campo de la izquierda (clave) es el atributo de la clase {@link Column}
 * (columnName) y el campo de la derecha es el que se guarda en la propiedad de esta clase.
 * @author José Julio
 * @version 1.0
 * @see Column
 */
public class ColumnOrder extends Column {
    private Orders order;

    /**
     * Crea un objeto ColumnFilter que permite crear una consulta con filtrado WHERE.
     * @param columnName Nombre de la columna que se desea filtrar (clave)
     * @param order Tipo de ordenación que se desea aplicar. Por ejemplo, Orders.DESC.
     */
    public ColumnOrder(Columnable columnName, Orders order) {
        super(columnName);
        this.order = order;
    }

    public Orders getOrder() {
        return order;
    }
}