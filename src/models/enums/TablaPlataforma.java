package src.models.enums;

public enum TablaPlataforma {
    NOMBRE("nombre");

    private String nombre;

    TablaPlataforma(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }

    public String getNombre() {
        return nombre;
    }
}