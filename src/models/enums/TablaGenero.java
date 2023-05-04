package src.models.enums;

public enum TablaGenero {
    NOMBRE("nombre"),
    DESCRIPCION("descripcion");

    private String columnName;

    TablaGenero(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }
}
