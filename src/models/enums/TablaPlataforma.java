package src.models.enums;

public enum TablaPlataforma {
    NOMBRE("nombre");

    private String columnName;

    TablaPlataforma(String columnName) {
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