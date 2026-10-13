package console;

import model.Address;
import model.Order;
import model.OrderItem;
import model.delivery.DeliveryMethod;

public class ConsoleOutput {

    public String formatMoney(long kopecks) {
        return String.format("%d,%02d ₽", kopecks / 100, kopecks % 100);
    }

    public void showOrder(Order order) {
        System.out.println("\nЗаказ №" + order.getNumber());
        System.out.println("Клиент: " + order.getClient().getName());
        System.out.println("Телефон: " + order.getClient().getPhone());
        System.out.println("Статус: " + order.getStatus().getStatusName());

        Address address = order.getAddress();

        if (address != null) {
            System.out.println("Адрес: " + address.getCity() + ", " + address.getStreet()
                + ", дом " + address.getHomeNumber());

            if (address.getFlatNumber() != null) {
                System.out.println("Квартира: " + address.getFlatNumber());
            }
        }

        System.out.println("Позиции:");

        for (OrderItem item : order.getOrderItems()) {
            System.out.println(item.getMenuItem().getName() + " × " + item.getCount()
                + " — " + formatMoney(item.calculatePriceKopecks()));
        }

        System.out.println("Стоимость позиций: " + formatMoney(order.calculateItemsPriceKopecks()));
        System.out.println("Чаевые: " + formatMoney(order.getTipKopecks()));

        DeliveryMethod deliveryMethod = order.getDeliveryMethod();

        if (deliveryMethod == null) {
            System.out.println("Способ доставки ещё не выбран.");
        } else {
            System.out.println("Способ: " + deliveryMethod.getName());
            System.out.println("Стоимость доставки: " + formatMoney(deliveryMethod.calculateCostKopecks(order)));
            System.out.println("Ориентировочный срок: " + deliveryMethod.calculateEstimatedMinutes(order) + " минут");
            System.out.println("Итого: " + formatMoney(order.calculateTotalPriceKopecks()));
        }

        if (order.getCourier() != null) {
            System.out.println("Курьер: " + order.getCourier().getName());
        }
    }
}