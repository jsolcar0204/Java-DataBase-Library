package src.models.enums;

/**
 * Interfaz que indica que una clase podrá tener como atributo
 * una columna de una tabla de una base de datos.
 * @author José Julio
 * @version 1.0
 * @see TablaDesarrollador
 * @see TablaEditor
 * @see TablaGenero
 * @see TablaPlataforma
 * @see TablaVideojuego
 * @see TablaVideojuegoGenero
 * @see TablaVideojuegoPlataforma
 */
public interface Columnable {
    /**
     * Método que devuelve el nombre de la columna
     * @return Devuelve el nombre de la columna
     */
    String getColumnName();

    /**
     * Devuelve un booleano, que indicará si la columna es una clave primaria
     * @return Devuelve true si la columna es una clave primaria y false si no lo es
     */
    boolean isPrimaryKey();
    /**
     * Devuelve un booleano, que indicará si la columna es una clave foránea
     * @return Devuelve true si la columna es una clave foránea y false si no lo es
     */
    boolean isForeignKey();
}