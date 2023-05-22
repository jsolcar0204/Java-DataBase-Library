package src.models.tables;

/**
 * Clase equivalente a la tabla <b>Plataforma</b> de la base de datos. Implementa la interfaz {@link Agregable}.
 * Contiene el atributo nombre.
 * @author José Julio
 * @version 1.0
 * @see src.models.enums.TablaPlataforma
 */
public class Plataforma implements Agregable {
    private String nombre;

    /**
     * Crea un objeto Plataforma con todos los atributos. Utilizado
     * para realizar consultas con todas las columnas.
     * @param nombre Nombre de la plataforma
     */
    public Plataforma(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Plataforma{" +
                "nombre='" + nombre + '\'' +
                '}';
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}