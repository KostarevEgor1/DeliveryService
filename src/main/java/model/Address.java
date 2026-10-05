package model;

public class Address {
    private final String city;
    private final String street;
    private final String homeNumber;
    private final Integer flatNumber;

    public Address(String city, String street, String homeNumber, Integer flatNumber) {
        if (city == null || city.isBlank()) {
            throw new IllegalArgumentException("Город не должен быть пустым");
        }
        if (street == null || street.isBlank()) {
            throw new IllegalArgumentException("Улица не должна быть пустой");
        }
        if (homeNumber == null || homeNumber.isBlank()) {
            throw new IllegalArgumentException("Номер дома не должен быть пустым");
        }
        if (flatNumber != null && flatNumber <= 0) {
            throw new IllegalArgumentException("Номер квартиры должен быть положительным");
        }

        this.city = city;
        this.street = street;
        this.homeNumber = homeNumber;
        this.flatNumber = flatNumber;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getHomeNumber() {
        return homeNumber;
    }

    public Integer getFlatNumber() {
        return flatNumber;
    }
}
