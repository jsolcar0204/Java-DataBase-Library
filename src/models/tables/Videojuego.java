package src.models.tables;

import java.time.LocalDate;

/**
 * Clase equivalente a la tabla <b>Videojuego</b> de la base de datos. Implementa la interfaz {@link Agregable}.
 * Contiene el atributo nombre, sinopsis, fechaPublicacion, nombreDesarrollador y nombreEditor.
 * @author José Julio
 * @version 1.0
 * @see src.models.enums.TablaVideojuego
 */
public class Videojuego implements Agregable {
    private String nombre, sinopsis;
    private LocalDate fechaPublicacion;
    private String nombreDesarrollador, nombreEditor;

    /**
     * Crea un objeto Videojuego sin valor en los atributos. Utilizado
     * para realizar consultas con un número de columnas desconocido.
     */
    public Videojuego() {
    }

    /**
     * Crea un objeto Videojuego con todos los atributos. Utilizado
     * para realizar consultas con todas las columnas.
     * @param nombre Nombre del videojuego
     * @param sinopsis Sinospsis del videojuego
     * @param fechaPublicacion Fecha de publicación del videojuego
     * @param nombreDesarrollador Nombre de la entidad desarrolladora del videojuego
     * @param nombreEditor Nombre de la entidad distribuidora del videojuego
     */
    public Videojuego(String nombre, String sinopsis, LocalDate fechaPublicacion, String nombreDesarrollador, String nombreEditor) {
        this.nombre = nombre;
        this.sinopsis = sinopsis;
        this.fechaPublicacion = fechaPublicacion;
        this.nombreDesarrollador = nombreDesarrollador;
        this.nombreEditor = nombreEditor;
    }

    @Override
    public String toString() {
        return "Videojuego{" +
                "nombre='" + nombre + '\'' +
                ", sinopsis='" + sinopsis + '\'' +
                ", fechaPublicacion=" + fechaPublicacion +
                ", nombreDesarrollador='" + nombreDesarrollador + '\'' +
                ", nombreEditor='" + nombreEditor + '\'' +
                '}';
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public LocalDate getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(LocalDate fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public String getNombreDesarrollador() {
        return nombreDesarrollador;
    }

    public void setNombreDesarrollador(String nombreDesarrollador) {
        this.nombreDesarrollador = nombreDesarrollador;
    }

    public String getNombreEditor() {
        return nombreEditor;
    }

    public void setNombreEditor(String nombre_editor) {
        this.nombreEditor = nombre_editor;
    }
}