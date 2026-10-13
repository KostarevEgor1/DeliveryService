package console;

import model.Address;
import model.Client;
import model.Courier;
import model.MenuItem;
import model.Order;
import model.OrderItem;
import model.OrderStatus;
import model.delivery.BasicDeliveryMethod;
import model.delivery.DeliveryMethod;
import model.delivery.ExpressDeliveryMethod;
import model.delivery.SelfPickupDeliveryMethod;
import service.DeliveryZoneResolver;
import service.OrderService;

import java.util.ArrayList;
import java.util.List;

public class ConsoleMenu {
    private final OrderService orderService;
    private final ConsoleInput input = new ConsoleInput();
    private final ConsoleOutput output = new ConsoleOutput();

    private int nextClientId = 1;

    private final List<MenuItem> products = List.of(
            new MenuItem("Маргарита", 50000L),
            new MenuItem("Пепперони", 65000L),
            new MenuItem("Четыре сыра", 40000L)
    );

    private final List<Courier> couriers = List.of(
            new Courier(1, "Иван"),
            new Courier(2, "Анна")
    );

    private final DeliveryZoneResolver zoneResolver = new DeliveryZoneResolver();

    private final List<DeliveryMethod> deliveryMethods = List.of(
            new BasicDeliveryMethod(zoneResolver),
            new ExpressDeliveryMethod(zoneResolver),
            new SelfPickupDeliveryMethod()
    );

    public ConsoleMenu(OrderService orderService) {
        this.orderService = orderService;
    }

    public void run() {
        while (true) {
            System.out.println("\n1. Создать заказ");
            System.out.println("2. Показать заказы");
            System.out.println("3. Выбрать доставку");
            System.out.println("4. Назначить курьера");
            System.out.println("5. Изменить статус");
            System.out.println("6. Изменить чаевые");
            System.out.println("0. Выйти");

            int command = input.readInt("Выберите действие: ", 0, 6);

            try {
                switch (command) {
                    case 1 -> createOrder();
                    case 2 -> showOrders();
                    case 3 -> selectDeliveryMethod();
                    case 4 -> assignCourier();
                    case 5 -> changeStatus();
                    case 6 -> changeTips();
                    case 0 -> {
                        System.out.println("Работа программы завершена.");
                        return;
                    }
                }
            } catch (IllegalArgumentException | IllegalStateException exception) {
                System.out.println("Ошибка: " + exception.getMessage());
            }
        }
    }

    private void createOrder() {
        String name = input.readText("Имя клиента: ");
        String phone = input.readText("Телефон: ");

        Client client = new Client(nextClientId, name, phone);

        Address address = null;

        int needsAddress = input.readInt("Указать адрес? 1 — да, 0 — самовывоз: ", 0, 1);

        if (needsAddress == 1) {
            address = readAddress();
        }

        List<OrderItem> items = new ArrayList<>();
        int addMore;

        do {
            items.add(readOrderItem());
            addMore = input.readInt("Добавить ещё товар? 1 — да, 0 — нет: ", 0, 1);
        } while (addMore == 1);

        Order order = orderService.createOrder(client, address, items, 0);
        nextClientId++;

        if (needsAddress == 0) {
            orderService.selectDeliveryMethod(order.getNumber(), deliveryMethods.get(2));
        }

        System.out.println("Создан заказ №" + order.getNumber());
        output.showOrder(order);
    }

    private Address readAddress() {
        while (true) {
            String city = input.readText("Город: ");
            String street = input.readText("Улица без «ул.»: ");
            int house = input.readInt("Номер дома: ", 1, 120);

            Address address = new Address(city, street, String.valueOf(house), null);

            try {
                zoneResolver.resolve(address);
            } catch (IllegalArgumentException exception) {
                System.out.println("Ошибка: " + exception.getMessage());
                System.out.println("Введите адрес ещё раз.");
                continue;
            }

            int flat = input.readInt("Квартира, 0 — нет квартиры: ", 0, 1000);
            Integer flatNumber = null;

            if (flat != 0) {
                flatNumber = flat;
            }

            return new Address(city, street, String.valueOf(house), flatNumber);
        }
    }

    private OrderItem readOrderItem() {
        for (int i = 0; i < products.size(); i++) {
            MenuItem product = products.get(i);

            System.out.println((i + 1) + ". " + product.getName()
                + " — " + output.formatMoney(product.getPriceKopecks()));
        }

        int choice = input.readInt("Товар: ", 1, products.size());
        int count = input.readInt("Количество: ", 1, 100);

        return new OrderItem(products.get(choice - 1), count);
    }

    private void showOrders() {
        List<Order> orders = orderService.getOrders();

        if (orders.isEmpty()) {
            System.out.println("Заказов пока нет.");
            return;
        }

        for (Order order : orders) {
            output.showOrder(order);
        }
    }

    private int readOrderNumber() {
        if (orderService.getOrders().isEmpty()) {
            System.out.println("Заказов пока нет.");
            return 0;
        }

        while (true) {
            int number = input.readInt("Номер заказа, 0 — назад: ", 0, Integer.MAX_VALUE);

            if (number == 0) {
                return 0;
            }

            try {
                orderService.findOrderByNumber(number);
                return number;
            } catch (IllegalArgumentException exception) {
                System.out.println("Ошибка: " + exception.getMessage());
            }
        }
    }

    private void selectDeliveryMethod() {
        int number = readOrderNumber();

        if (number == 0) {
            return;
        }

        Order order = orderService.findOrderByNumber(number);

        if (order.getStatus() != OrderStatus.CONFIRMED) {
            System.out.println("Нельзя менять доставку после начала приготовления или отмены заказа.");
            return;
        }

        while (true) {
            for (int i = 0; i < deliveryMethods.size(); i++) {
                System.out.println((i + 1) + ". " + deliveryMethods.get(i).getName());
            }

            int choice = input.readInt("Способ доставки, 0 — назад: ", 0, deliveryMethods.size());

            if (choice == 0) {
                return;
            }

            try {
                orderService.selectDeliveryMethod(number, deliveryMethods.get(choice - 1));
            } catch (IllegalArgumentException | IllegalStateException exception) {
                System.out.println("Ошибка: " + exception.getMessage());
                continue;
            }

            output.showOrder(order);
            return;
        }
    }

    private void assignCourier() {
        int number = readOrderNumber();

        if (number == 0) {
            return;
        }

        for (int i = 0; i < couriers.size(); i++) {
            System.out.println((i + 1) + ". " + couriers.get(i).getName());
        }

        int choice = input.readInt("Курьер: ", 1, couriers.size());

        orderService.assignCourier(number, couriers.get(choice - 1));
        System.out.println("Курьер назначен.");
    }

    private void changeStatus() {
        int number = readOrderNumber();

        if (number == 0) {
            return;
        }

        Order order = orderService.findOrderByNumber(number);
        List<OrderStatus> statuses = order.getAvailableStatuses();

        if (statuses.isEmpty()) {
            if (order.getStatus() == OrderStatus.READY) {
                System.out.println("Перед отправлением назначьте курьера.");
            } else {
                System.out.println("У завершённого или отменённого заказа нельзя менять статус.");
            }
            return;
        }

        if (order.getDeliveryMethod() == null) {
            System.out.println("Чтобы начать приготовление, сначала выберите доставку.");
        }

        for (int i = 0; i < statuses.size(); i++) {
            System.out.println((i + 1) + ". " + statuses.get(i).getStatusName());
        }

        int choice = input.readInt("Новый статус, 0 — назад: ", 0, statuses.size());

        if (choice == 0) {
            return;
        }

        orderService.changeStatus(number, statuses.get(choice - 1));
        output.showOrder(order);
    }

    private void changeTips() {
        int number = readOrderNumber();

        if (number == 0) {
            return;
        }

        Order order = orderService.findOrderByNumber(number);

        if (order.getStatus() == OrderStatus.CANCELLED) {
            System.out.println("Нельзя менять чаевые отменённого заказа.");
            return;
        }

        while (true) {
            int rubles = input.readInt("Чаевые в целых рублях, 0 — без чаевых: ", 0, 100000);

            try {
                orderService.changeTipKopecks(number, rubles * 100L);
            } catch (IllegalStateException exception) {
                System.out.println("Ошибка: " + exception.getMessage());
                continue;
            }

            System.out.println("Чаевые изменены.");
            return;
        }
    }
}
