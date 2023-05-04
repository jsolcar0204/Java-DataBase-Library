package src.models;

public class VideojuegoPlataforma implements Agregable {
    private String nombreVideojuego, nombrePlataforma;

    public VideojuegoPlataforma() {
    }

    public VideojuegoPlataforma(String nombreVideojuego, String nombrePlataforma) {
        this.nombreVideojuego = nombreVideojuego;
        this.nombrePlataforma = nombrePlataforma;
    }

    @Override
    public String toString() {
        return "VideojuegoPlataforma{" +
                "nombreVideojuego='" + nombreVideojuego + '\'' +
                ", nombrePlataforma='" + nombrePlataforma + '\'' +
                '}';
    }

    public String getNombreVideojuego() {
        return nombreVideojuego;
    }

    public void setNombreVideojuego(String nombreVideojuego) {
        this.nombreVideojuego = nombreVideojuego;
    }

    public String getNombrePlataforma() {
        return nombrePlataforma;
    }

    public void setNombrePlataforma(String nombrePlataforma) {
        this.nombrePlataforma = nombrePlataforma;
    }
}