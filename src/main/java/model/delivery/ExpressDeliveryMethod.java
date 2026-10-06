package model.delivery;

import model.Order;
import service.DeliveryZoneResolver;

public class ExpressDeliveryMethod implements DeliveryMethod {
    private DeliveryZoneResolver zoneResolver;

    public ExpressDeliveryMethod(DeliveryZoneResolver zoneResolver) {
        if (zoneResolver == null) {
            throw new IllegalArgumentException(
                    "Компонент определения зоны должен быть указан"
            );
        }

        this.zoneResolver = zoneResolver;
    }

    @Override
    public String getName() {
        return "Экспресс-доставка";
    }

    @Override
    public long calculateCostKopecks(Order order) {
        DeliveryZone zone = zoneResolver.resolve(order.getAddress());

        return switch (zone) {
            case NEAR -> 30000L;
            case MEDIUM -> 50000L;
            case FAR -> 70000L;
        };
    }

    @Override
    public int calculateEstimatedMinutes(Order order) {
        DeliveryZone zone = zoneResolver.resolve(order.getAddress());

        return switch (zone) {
            case NEAR -> 25;
            case MEDIUM -> 35;
            case FAR -> 50;
        };
    }

    @Override
    public boolean requiresCourier() {
        return true;
    }
}