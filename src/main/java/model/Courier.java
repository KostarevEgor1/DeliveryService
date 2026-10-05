package model;

public class Courier {
    private final Integer id;
    private final String name;

    public Courier(Integer id, String name) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("id курьера должен быть положительным");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Имя курьера не должно быть пустым");
        }

        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
