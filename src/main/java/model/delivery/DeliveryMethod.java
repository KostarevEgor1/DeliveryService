package model.delivery;

import model.Order;

public interface DeliveryMethod {
    String getName();

    long calculateCostKopecks(Order order);

    int calculateEstimatedMinutes(Order order);

    boolean requiresCourier();
}