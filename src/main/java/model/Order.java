package model;

import model.delivery.DeliveryMethod;
import service.DeliveryZoneResolver;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private final Integer number;
    private final Client client;
    private final Address address;
    private final List<OrderItem> orderItems;
    private Courier courier;
    private OrderStatus status;
    private DeliveryMethod deliveryMethod;
    private long tipKopecks;
    private final DeliveryZoneResolver zoneResolver = new DeliveryZoneResolver();

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

    private void checkEditingAllowed() {
        if (status != OrderStatus.CONFIRMED) {
            throw new IllegalStateException("Невозможно изменить заказ после начала приготовления");
        }
    }

    public void selectDeliveryMethod(DeliveryMethod deliveryMethod) {
        checkEditingAllowed();

        if (deliveryMethod == null) {
            throw new IllegalArgumentException("Способ доставки должен быть указан");
        }

        if (deliveryMethod.requiresCourier()) {
            zoneResolver.resolve(address);
        } else if (tipKopecks != 0) {
            throw new IllegalStateException("Для самовывоза не предусмотрены чаевые курьеру");
        }

        this.deliveryMethod = deliveryMethod;

        if (!deliveryMethod.requiresCourier()) {
            this.courier = null;
        }
    }

    public void changeTipKopecks(long tipKopecks) {
        if (status == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Нельзя менять чаевые отменённого заказа");
        }

        if (tipKopecks < 0) {
            throw new IllegalArgumentException("Чаевые не должны быть отрицательными");
        }

        if (deliveryMethod != null && !deliveryMethod.requiresCourier() && tipKopecks != 0) {
            throw new IllegalStateException("Для самовывоза не предусмотрены чаевые курьеру");
        }

        this.tipKopecks = tipKopecks;
    }

    public void assignCourier(Courier courier) {
        if (courier == null) {
            throw new IllegalArgumentException("Курьер должен быть указан");
        }

        if (status != OrderStatus.CONFIRMED && status != OrderStatus.PREPARING && status != OrderStatus.READY) {
            throw new IllegalStateException("На этом этапе нельзя назначать или менять курьера");
        }

        if (deliveryMethod == null) {
            throw new IllegalStateException("Сначала выберите способ доставки");
        }

        if (!deliveryMethod.requiresCourier()) {
            throw new IllegalStateException("Для самовывоза курьер не требуется");
        }

        this.courier = courier;
    }

    public List<OrderStatus> getAvailableStatuses() {
        switch (status) {
            case CONFIRMED -> {
                if (deliveryMethod == null) {
                    return List.of(OrderStatus.CANCELLED);
                }
                return List.of(OrderStatus.PREPARING, OrderStatus.CANCELLED);
            }
            case PREPARING -> {
                return List.of(OrderStatus.READY);
            }
            case READY -> {
                if (!deliveryMethod.requiresCourier()) {
                    return List.of(OrderStatus.COMPLETED);
                }
                if (courier != null) {
                    return List.of(OrderStatus.OUT_FOR_DELIVERY);
                }
                return List.of();
            }
            case OUT_FOR_DELIVERY -> {
                return List.of(OrderStatus.COMPLETED);
            }
            default -> {
                return List.of();
            }
        }
    }

    public void changeStatus(OrderStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("Новый статус должен быть указан");
        }

        if (!getAvailableStatuses().contains(newStatus)) {
            throw new IllegalStateException("Недопустимый переход: " + status + " → " + newStatus);
        }

        this.status = newStatus;
    }
}
