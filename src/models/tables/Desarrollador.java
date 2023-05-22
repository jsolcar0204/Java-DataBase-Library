package src.models.tables;

import java.time.LocalDate;

/**
 * Clase equivalente a la tabla <b>Desarrollador</b> de la base de datos. Implementa la interfaz {@link Agregable}.
 * Contiene los atributos nombre y fechaFundacion.
 * @author José Julio
 * @version 1.0
 * @see src.models.enums.TablaDesarrollador
 */
public class Desarrollador implements Agregable {
    private String nombre;
    private LocalDate fechaFundacion;

    /**
     * Crea un objeto Desarrollador sin valor en los atributos. Utilizado
     * para realizar consultas con un número de columnas desconocido.
     */
    public Desarrollador() {
    }

    /**
     * Crea un objeto Desarrollador con todos los atributos. Utilizado
     * para realizar consultas con todas las columnas.
     * @param nombre Nombre de la entidad desarrolladora
     * @param fechaFundacion Fecha de fundación de la entidad desarrolladora
     */
    public Desarrollador(String nombre, LocalDate fechaFundacion) {
        this.nombre = nombre;
        this.fechaFundacion = fechaFundacion;
    }

    @Override
    public String toString() {
        return "Desarrollador{" +
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