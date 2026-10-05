package model;

public class OrderItem {
    private final MenuItem menuItem;
    private final int count;

    public OrderItem(MenuItem menuItem, int count) {
        if (menuItem == null) {
            throw new IllegalArgumentException("Товар должен быть указан");
        }
        if (count <= 0) {
            throw new IllegalArgumentException("Количество должно быть положительным");
        }

        this.menuItem = menuItem;
        this.count = count;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public int getCount() {
        return count;
    }

    public long calculatePriceKopecks() {
        return menuItem.getPriceKopecks() * count;
    }
}
