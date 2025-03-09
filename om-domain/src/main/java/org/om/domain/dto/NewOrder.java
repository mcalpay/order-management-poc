package org.om.domain.dto;

import org.om.domain.model.Order;
import org.om.domain.model.OrderSide;
import org.om.domain.model.OrderStatus;

import java.time.LocalDateTime;

//customer, asset, side, size and price
public record NewOrder(Long customerId, String assetName, String orderSide, int size, int price) {

    public Order toEntity() {
        return Order.builder().customerId(customerId).assetName(assetName).orderSide(OrderSide.valueOf(orderSide))
                .size(size).price(price).status(OrderStatus.PENDING).createDate(LocalDateTime.now()).build();
    }

    public boolean isSelling() {
        return OrderSide.SELL.name().equals(orderSide);
    }
}
