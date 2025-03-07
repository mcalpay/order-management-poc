package org.om.domain.dto;

import org.om.domain.model.Order;
import org.om.domain.model.OrderSide;

//customer, asset, side, size and price
public record NewOrder(String customerId, String assetName, String orderSide, int size, int price) {

    public Order toEntity() {
        return Order.builder().customerId(customerId).assetName(assetName).orderSide(OrderSide.valueOf(orderSide)).size(size).price(price).build();
    }

}
