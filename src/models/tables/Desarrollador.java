package src.models.tables;

import java.time.LocalDate;

public class Desarrollador implements Agregable {
    private String nombre;
    private LocalDate fechaFundacion;

    public Desarrollador() {
    }

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