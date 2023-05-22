package src.models.tables;

import java.time.LocalDate;

/**
 * Clase equivalente a la tabla <b>Editor</b> de la base de datos. Implementa la interfaz {@link Agregable}.
 * Contiene los atributos nombre y fechaFundacion.
 * @author José Julio
 * @version 1.0
 * @see src.models.enums.TablaEditor
 */
public class Editor implements Agregable {
    private String nombre;
    private LocalDate fechaFundacion;

    /**
     * Crea un objeto Editor sin valor en los atributos. Utilizado
     * para realizar consultas con un número de columnas desconocido.
     */
    public Editor() {
    }

    /**
     * Crea un objeto Editor con todos los atributos. Utilizado
     * para realizar consultas con todas las columnas.
     * @param nombre Nombre de la entidad editora
     * @param fechaFundacion Fecha de fundación de la entidad editora
     */
    public Editor(String nombre, LocalDate fechaFundacion) {
        this.nombre = nombre;
        this.fechaFundacion = fechaFundacion;
    }

    @Override
    public String toString() {
        return "Editor{" +
                "nombre='" + nombre + '\'' +
                ", fechaFundacion=" + fechaFundacion +
                '}';
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFechaFundacion() {
        return fechaFundacion;
    }

    public void setFechaFundacion(LocalDate fechaFundacion) {
        this.fechaFundacion = fechaFundacion;
    }
}