import console.ConsoleMenu;
import service.OrderService;

public class Main {
    public static void main(String[] args) {
        OrderService orderService = new OrderService();
        ConsoleMenu menu = new ConsoleMenu(orderService);

        menu.run();
    }
}