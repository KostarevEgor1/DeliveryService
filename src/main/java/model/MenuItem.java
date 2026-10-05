package model;

public class MenuItem {
    private final String name;
    private final long priceKopecks;

    public MenuItem(String name, long priceKopecks) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Название товара не должно быть пустым");
        }
        if (priceKopecks < 0) {
            throw new IllegalArgumentException("Цена не должна быть отрицательной");
        }

        this.name = name;
        this.priceKopecks = priceKopecks;
    }

    public String getName() {
        return name;
    }

    public long getPriceKopecks() {
        return priceKopecks;
    }
}
