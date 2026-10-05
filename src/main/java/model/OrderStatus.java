package model;

public enum OrderStatus {
    CONFIRMED("Order is confirmed"),
    PREPARING("Order is being prepared"),
    READY("Order is ready"),
    OUT_FOR_DELIVERY("The order is being delivered"),
    COMPLETED("Order is completed"),
    CANCELLED("Order is cancelled");

    OrderStatus(String statusName) {
        this.statusName = statusName;
    }

    private final String statusName;

    public String getStatusName() {
        return statusName;
    }
}
