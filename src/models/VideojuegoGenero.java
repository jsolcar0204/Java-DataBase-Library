package src.models;

public class VideojuegoGenero implements Agregable {
    private String nombreVideojuego, nombreGenero;

    public VideojuegoGenero() {
    }

    public VideojuegoGenero(String nombreVideojuego, String nombreGenero) {
        this.nombreVideojuego = nombreVideojuego;
        this.nombreGenero = nombreGenero;
    }

    @Override
    public String toString() {
        return "VideojuegoGenero{" +
                "nombreVideojuego='" + nombreVideojuego + '\'' +
                ", nombreGenero='" + nombreGenero + '\'' +
                '}';
    }

    public String getNombreVideojuego() {
        return nombreVideojuego;
    }

    public void setNombreVideojuego(String nombreVideojuego) {
        this.nombreVideojuego = nombreVideojuego;
    }

    public String getNombreGenero() {
        return nombreGenero;
    }

    public void setNombreGenero(String nombreGenero) {
        this.nombreGenero = nombreGenero;
    }
}