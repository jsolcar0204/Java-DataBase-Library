package src.models.tables;

/**
 * Clase equivalente a la tabla <b>VideojuegoGenero</b> de la base de datos. Implementa la interfaz {@link Agregable}.
 * Contiene el atributo nombreVideojuego y nombreGenero.
 * @author José Julio
 * @version 1.0
 * @see src.models.enums.TablaVideojuegoGenero
 */
public class VideojuegoGenero implements Agregable {
    private String nombreVideojuego, nombreGenero;

    /**
     * Crea un objeto VideojuegoGenero sin valor en los atributos. Utilizado
     * para realizar consultas con un número de columnas desconocido.
     */
    public VideojuegoGenero() {
    }

    /**
     * Crea un objeto VideojuegoGenero con todos los atributos. Utilizado
     * para realizar consultas con todas las columnas.
     * @param nombreVideojuego Nombre del videojuego
     * @param nombreGenero Nombre del género
     */
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