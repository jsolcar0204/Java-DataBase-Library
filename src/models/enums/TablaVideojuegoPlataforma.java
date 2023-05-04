package src.models.enums;

public enum TablaVideojuegoPlataforma {
    NOMBRE_VIDEOJUEGO("nombre_videojuego"),
    NOMBRE_PLATAFORMA("nombre_plataforma");

    private String columnName;

    TablaVideojuegoPlataforma(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }
}