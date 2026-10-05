package model.delivery;

import model.Order;

public interface DeliveryMethod {
    long calculateCostKopecks(Order order);
}