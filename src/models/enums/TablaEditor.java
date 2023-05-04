package src.models.enums;

public enum TablaEditor {
    NOMBRE("nombre"),
    FECHA_FUNDACION("fecha_fundacion");

    private String columnName;

    TablaEditor(String columnName) {
        this.columnName = columnName;
    }

    @Override
    public String toString() {
        return columnName;
    }

    public String getColumnName() {
        return columnName;
    }
}