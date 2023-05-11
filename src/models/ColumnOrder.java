package src.models;

import src.models.enums.Orders;

public class ColumnOrder {
    private String columnName;
    private Orders order;

    public ColumnOrder(String columnName, Orders order) {
        this.columnName = columnName;
        this.order = order;
    }

    public String getColumnName() {
        return columnName;
    }

    public Orders getOrder() {
        return order;
    }
}