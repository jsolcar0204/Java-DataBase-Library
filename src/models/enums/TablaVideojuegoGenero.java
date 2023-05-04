package src.models.enums;

public enum TablaVideojuegoGenero {
    NOMBRE_VIDEOJUEGO("nombre_videojuego"),
    NOMBRE_GENERO("nombre_genero");

    private String columnName;

    TablaVideojuegoGenero(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }
}