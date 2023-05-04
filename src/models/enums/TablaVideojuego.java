package src.models.enums;

public enum TablaVideojuego {
    NOMBRE("nombre"),
    SINOPSIS("sinopsis"),
    FECHA_PUBLICACION("fecha_publicacion"),
    NOMBRE_DESARROLLADOR("nombre_desarrollador"),
    NOMBRE_EDITOR("nombre_editor");

    private String columnName;

    TablaVideojuego(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }
}