package src.models;

public class Plataforma implements Agregable {
    private String nombre;

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