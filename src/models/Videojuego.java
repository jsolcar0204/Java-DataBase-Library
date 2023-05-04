package src.models;

import java.time.LocalDate;

public class Videojuego implements Agregable {
    private String nombre, sinopsis;
    private LocalDate fechaPublicacion;
    private String nombreDesarrollador, nombre_editor;

    public Videojuego() {
    }

    public Videojuego(String nombre, String sinopsis, LocalDate fechaPublicacion, String nombreDesarrollador, String nombre_editor) {
        this.nombre = nombre;
        this.sinopsis = sinopsis;
        this.fechaPublicacion = fechaPublicacion;
        this.nombreDesarrollador = nombreDesarrollador;
        this.nombre_editor = nombre_editor;
    }

    @Override
    public String toString() {
        return "Videojuego{" +
                "nombre='" + nombre + '\'' +
                ", sinopsis='" + sinopsis + '\'' +
                ", fechaPublicacion=" + fechaPublicacion +
                ", nombreDesarrollador='" + nombreDesarrollador + '\'' +
                ", nombre_editor='" + nombre_editor + '\'' +
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

    public String getNombre_editor() {
        return nombre_editor;
    }

    public void setNombre_editor(String nombre_editor) {
        this.nombre_editor = nombre_editor;
    }
}