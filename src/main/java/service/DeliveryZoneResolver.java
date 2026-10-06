package service;

import model.Address;
import model.delivery.DeliveryZone;

public class DeliveryZoneResolver {

    public DeliveryZone resolve(Address address) {
        if (address == null) {
            throw new IllegalArgumentException("Адрес должен быть указан");
        }

        if (!"Екатеринбург".equalsIgnoreCase(address.getCity())) {
            throw new IllegalArgumentException("Этот город не обслуживается");
        }

        int houseNumber = Integer.parseInt(address.getHomeNumber());

        if (houseNumber < 1 || houseNumber > 120) {
            throw new IllegalArgumentException("Этот номер дома не обслуживается");
        }

        String street = address.getStreet();

        if ("Куйбышева".equalsIgnoreCase(street)) {
            if (houseNumber <= 30) {
                return DeliveryZone.MEDIUM;
            }
            if (houseNumber <= 80) {
                return DeliveryZone.NEAR;
            }
            return DeliveryZone.FAR;
        }

        if ("Малышева".equalsIgnoreCase(street)) {
            return DeliveryZone.NEAR;
        }

        if ("Московская".equalsIgnoreCase(street)
                || "Пушкина".equalsIgnoreCase(street)) {
            return DeliveryZone.MEDIUM;
        }

        if ("Восточная".equalsIgnoreCase(street)) {
            return DeliveryZone.FAR;
        }

        throw new IllegalArgumentException("Эта улица не обслуживается");
    }
}
