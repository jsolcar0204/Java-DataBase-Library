package src.models.enums.other;

/**
 * Enum que guarda la sintaxis de las palabras clave <b>ASC</b> y <b>DESC</b>
 * de SQL.
 * @author José Julio
 * @version 1.0
 */
public enum Orders {
    /**
     * Palabra ASC para ordenar ascendentemente un campo
     */
    ASC("ASC"),
    /**
     * Palabra DESC para ordenar descendentemente un campo
     */
    DESC("DESC");

    private String orderName;

    Orders(String orderName) {
        this.orderName = orderName;
    }

    @Override
    public String toString() {
        return orderName;
    }

    public String getOrderName() {
        return orderName;
    }
}