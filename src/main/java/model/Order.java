package model;

import model.delivery.DeliveryMethod;

import java.util.List;

public class Order {
    Integer number;
    Client client;
    Address address;
    List<OrderItem> orderItems;
    Courier courier;
    long totalPriceKopecks;
    OrderStatus status;
    DeliveryMethod deliveryMethod;
}
