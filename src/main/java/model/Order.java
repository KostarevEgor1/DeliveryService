package model;

import model.delivery.DeliveryMethod;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private final Integer number;
    private final Client client;
    private Address address;
    private final List<OrderItem> orderItems;
    private Courier courier;
    private OrderStatus status;
    private DeliveryMethod deliveryMethod;
    private long tipKopecks;

    public Order(
            Integer number,
            Client client,
            Address address,
            List<OrderItem> orderItems,
            long tipKopecks
    ) {
        if (number == null || number <= 0) {
            throw new IllegalArgumentException("Номер заказа должен быть положительным");
        }
        if (client == null) {
            throw new IllegalArgumentException("Клиент должен быть указан");
        }
        if (orderItems == null || orderItems.isEmpty()) {
            throw new IllegalArgumentException("Заказ должен содержать хотя бы одну позицию");
        }
        if (orderItems.contains(null)) {
            throw new IllegalArgumentException("Позиции заказа не должны быть null");
        }
        if (tipKopecks < 0) {
            throw new IllegalArgumentException("Чаевые не должны быть отрицательными");
        }

        this.number = number;
        this.client = client;
        this.address = address;
        this.orderItems = new ArrayList<>(orderItems);
        this.tipKopecks = tipKopecks;
        this.status = OrderStatus.CONFIRMED;
    }

    public Integer getNumber() {
        return number;
    }

    public Client getClient() {
        return client;
    }

    public Address getAddress() {
        return address;
    }

    public List<OrderItem> getOrderItems() {
        return List.copyOf(orderItems);
    }

    public Courier getCourier() {
        return courier;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public DeliveryMethod getDeliveryMethod() {
        return deliveryMethod;
    }

    public long getTipKopecks() {
        return tipKopecks;
    }

    public long calculateItemsPriceKopecks() {
        long sum = 0;
        for (OrderItem item : orderItems) {
            sum += item.calculatePriceKopecks();
        }
        return sum;
    }

    public long calculateTotalPriceKopecks() {
        if (deliveryMethod == null) {
            throw new IllegalStateException("Способ доставки ещё не выбран");
        }

        long itemsPrice = calculateItemsPriceKopecks();
        long deliveryPrice = deliveryMethod.calculateCostKopecks(this);

        if (deliveryPrice < 0) {
            throw new IllegalStateException("Стоимость доставки не должна быть отрицательной");
        }

        long priceWithDelivery = itemsPrice + deliveryPrice;
        return priceWithDelivery + tipKopecks;
    }

    public void addOrderItem(OrderItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Позиция заказа не должна быть null");
        }

        orderItems.add(item);
    }
}
