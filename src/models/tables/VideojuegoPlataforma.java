package src.models.tables;

/**
 * Clase equivalente a la tabla <b>VideojuegoPlataforma</b> de la base de datos. Implementa la interfaz {@link Agregable}.
 * Contiene el atributo nombreVideojuego y nombrePlataforma.
 * @author José Julio
 * @version 1.0
 * @see src.models.enums.TablaVideojuegoPlataforma
 */
public class VideojuegoPlataforma implements Agregable {
    private String nombreVideojuego, nombrePlataforma;

    /**
     * Crea un objeto VideojuegoGenero sin valor en los atributos. Utilizado
     * para realizar consultas con un número de columnas desconocido.
     */
    public VideojuegoPlataforma() {
    }

    /**
     * Crea un objeto VideojuegoGenero con todos los atributos. Utilizado
     * para realizar consultas con todas las columnas.
     * @param nombreVideojuego Nombre del videojuego
     * @param nombrePlataforma Nombre de la plataforma
     */
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