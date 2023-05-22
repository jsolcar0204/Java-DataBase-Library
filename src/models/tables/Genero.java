package src.models.tables;

/**
 * Clase equivalente a la tabla <b>Genero</b> de la base de datos. Implementa la interfaz {@link Agregable}.
 * Contiene los atributos nombre y descripcion.
 * @author José Julio
 * @version 1.0
 * @see src.models.enums.TablaGenero
 */
public class Genero implements Agregable {
    private String nombre, descripcion;

    /**
     * Crea un objeto Genero sin valor en los atributos. Utilizado
     * para realizar consultas con un número de columnas desconocido.
     */
    public Genero() {
    }

    /**
     * Crea un objeto Genero con todos los atributos. Utilizado
     * para realizar consultas con todas las columnas.
     * @param nombre Nombre del género
     * @param descripcion Descripción del género
     */
    public Genero(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "Genero{" +
                "nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}