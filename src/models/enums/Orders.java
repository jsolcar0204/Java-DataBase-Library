package src.models.enums;

public enum Orders {
    ASC("ASC"),
    DESC("DESC");

    private String orderName;

    Orders(String orderName) {
        this.orderName = orderName;
    }

    @Override
    public String toString() {
        return orderName;
    }

    public String getOrderName() {
        return orderName;
    }
}