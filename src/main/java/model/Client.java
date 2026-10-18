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
        if (phone == null || !phone.matches("(?:\\+7|[78])[0-9]{10}")) {
            throw new IllegalArgumentException(
                    "Введите телефон в формате +79991234567, 79991234567 или 89991234567"
            );
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
