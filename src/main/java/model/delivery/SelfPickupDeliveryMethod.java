package model.delivery;

import model.Order;

public class SelfPickupDeliveryMethod implements DeliveryMethod {
    @Override
    public String getName() {
        return "Самовывоз";
    }

    @Override
    public long calculateCostKopecks(Order order) {
        return 0;
    }

    @Override
    public int calculateEstimatedMinutes(Order order) {
        return 25;
    }

    @Override
    public boolean requiresCourier() {
        return false;
    }
}
