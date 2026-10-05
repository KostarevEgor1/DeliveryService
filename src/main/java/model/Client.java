package model;

public class Client {
    private final Integer id;
    private final String name;
    private final String phone;

    public Client(Integer id, String name, String phone) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("id клиента должен быть положительным");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Имя клиента не должно быть пустым");
        }
        if (phone == null || phone.isBlank()) {
            throw new IllegalArgumentException("Телефон клиента не должен быть пустым");
        }
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }
}
