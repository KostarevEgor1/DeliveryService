package model.delivery;

import model.Order;
import service.DeliveryZoneResolver;

public class BasicDeliveryMethod implements DeliveryMethod {
    private DeliveryZoneResolver zoneResolver;

    public BasicDeliveryMethod(DeliveryZoneResolver zoneResolver) {
        if (zoneResolver == null) {
            throw new IllegalArgumentException(
                    "Компонент определения зоны должен быть указан"
            );
        }

        this.zoneResolver = zoneResolver;
    }

    @Override
    public String getName() {
        return "Стандартная доставка";
    }

    @Override
    public long calculateCostKopecks(Order order) {
        DeliveryZone zone = zoneResolver.resolve(order.getAddress());

        return switch (zone) {
            case NEAR -> 15000L;
            case MEDIUM -> 30000L;
            case FAR -> 45000L;
        };
    }

    @Override
    public int calculateEstimatedMinutes(Order order) {
        DeliveryZone zone = zoneResolver.resolve(order.getAddress());

        return switch (zone) {
            case NEAR -> 40;
            case MEDIUM -> 60;
            case FAR -> 80;
        };
    }

    @Override
    public boolean requiresCourier() {
        return true;
    }
}