package service;

import model.Address;
import model.Client;
import model.Courier;
import model.Order;
import model.OrderItem;
import model.OrderStatus;
import model.delivery.DeliveryMethod;

import java.util.ArrayList;
import java.util.List;

public class OrderService {
    private final List<Order> orders = new ArrayList<>();
    private int nextOrderNumber = 1;

    public Order createOrder(Client client, Address address, List<OrderItem> orderItems, long tipKopecks) {
        Order order = new Order(nextOrderNumber, client, address, orderItems, tipKopecks);

        orders.add(order);
        nextOrderNumber++;

        return order;
    }

    public List<Order> getOrders() {
        return List.copyOf(orders);
    }

    public Order findOrderByNumber(int number) {
        for (Order order : orders) {
            if (order.getNumber() == number) {
                return order;
            }
        }

        throw new IllegalArgumentException("Заказ с номером " + number + " не найден");
    }

    public void addOrderItem(int number, OrderItem item) {
        findOrderByNumber(number).addOrderItem(item);
    }

    public void selectDeliveryMethod(int number, DeliveryMethod deliveryMethod) {
        findOrderByNumber(number).selectDeliveryMethod(deliveryMethod);
    }

    public void assignCourier(int number, Courier courier) {
        findOrderByNumber(number).assignCourier(courier);
    }

    public void changeAddress(int number, Address address) {
        findOrderByNumber(number).changeAddress(address);
    }

    public void changeTipKopecks(int number, long tipKopecks) {
        findOrderByNumber(number).changeTipKopecks(tipKopecks);
    }

    public void changeStatus(int number, OrderStatus newStatus) {
        findOrderByNumber(number).changeStatus(newStatus);
    }
}